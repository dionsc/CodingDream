package com.dionst.service.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.dionst.service.constant.MQConstant;
import com.dionst.service.model.entity.JudgeRequest;
import com.dionst.service.mapper.JudgeRequestMapper;
import com.dionst.service.service.IJudgeRequestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.util.concurrent.FutureCallback;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;

/**
 * <p>
 * 判题请求 服务实现类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Service
public class JudgeRequestServiceImpl extends ServiceImpl<JudgeRequestMapper, JudgeRequest> implements IJudgeRequestService {


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void sendJudgeRequest(Long submissionId) {
        JudgeRequest judgeRequest = new JudgeRequest().setSubmissionId(submissionId);
        save(judgeRequest);
        new Thread(() -> {
            sendToMQ(judgeRequest.getId());
        }).start();
    }


    private void sendToMQ(Long id) {
        JudgeRequest judgeRequest = getById(id);
        UpdateWrapper<JudgeRequest> judgeRequestUpdateWrapper = new UpdateWrapper<>();
        judgeRequestUpdateWrapper.lambda()
                .set(JudgeRequest::getUpdated, true)
                .eq(JudgeRequest::getId, id);
        CorrelationData cd = new CorrelationData();
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                if (result.isAck())
                    update(judgeRequestUpdateWrapper);
            }

            @Override
            public void onFailure(Throwable ex) {

            }
        });
        rabbitTemplate.convertAndSend(MQConstant.JUDGE_EXCHANGE, "", judgeRequest.getSubmissionId(), cd);
    }

    /**
     * 定时任务检查没有发送成功的，重新发送
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void checkSendFail() {
        QueryWrapper<JudgeRequest> judgeRequestQueryWrapper = new QueryWrapper<>();
        judgeRequestQueryWrapper.lambda()
                .select(JudgeRequest::getId)
                .eq(JudgeRequest::getUpdated, false);
        //重新发送
        List<JudgeRequest> list = list(judgeRequestQueryWrapper);
        for (JudgeRequest judgeRequest : list) {
            sendToMQ(judgeRequest.getId());
        }
    }
}
