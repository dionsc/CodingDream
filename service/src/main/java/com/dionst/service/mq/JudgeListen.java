package com.dionst.service.mq;

import com.dionst.service.constant.MQConstant;
import com.dionst.service.judge.JudgeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Component
@Slf4j
public class JudgeListen {

    @Autowired
    private JudgeService judgeService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = MQConstant.JUDGE_QUEUE),
            exchange = @Exchange(name = MQConstant.JUDGE_EXCHANGE, type = ExchangeTypes.FANOUT)
    ))
    public void listenContestSubmitQueue(Long submitId) throws IOException, InterruptedException {
        log.info("开始判题：{}", submitId);
        judgeService.judge(submitId);
    }
}
