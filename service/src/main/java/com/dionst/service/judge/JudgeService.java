package com.dionst.service.judge;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dionst.service.common.ErrorCode;
import com.dionst.service.exception.BusinessException;
import com.dionst.service.judge.run.JavaRunner;
import com.dionst.service.judge.run.Runner;
import com.dionst.service.model.entity.JudgeData;
import com.dionst.service.model.entity.Program;
import com.dionst.service.model.entity.Question;
import com.dionst.service.model.entity.Submission;
import com.dionst.service.model.enums.VerdictEnum;
import com.dionst.service.service.*;
import com.dionst.service.utils.FileUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Random;

@Data
@Component
@AllArgsConstructor
public class JudgeService {

    private final ISubmissionService submissionService;
    private final IProgramService programService;
    private final IQuestionService questionService;
    private final IJudgeDataService judgeDataService;
    private final IUpdateRankingRequestService updateRankingRequestService;
    private final IContestService contestService;

    public void judge(Long submitId) throws IOException, InterruptedException {
        Submission submission = submissionService.getById(submitId);
        //查询用户程序
        Long codeId = submission.getCodeId();
        Program userProgram = programService.getById(codeId);
        //查询判题程序
        Long questionIndex = submission.getQuestionIndex();
        Long contestId = submission.getContestId();
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.lambda()
                .eq(Question::getContestId, contestId)
                .eq(Question::getQuestionIndex, questionIndex);


        VerdictEnum result = VerdictEnum.Accepted;
        //todo 测试
        Random random = new Random();
        if (random.nextBoolean()) {
            result = VerdictEnum.WrongAnswer;
        } else if (random.nextBoolean()) {
            result = VerdictEnum.CompileError;
        } else if (random.nextBoolean()) {
            result = VerdictEnum.OutOfMemoryError;
        }


//        Question question = questionService.getOne(questionQueryWrapper);
//        Long judgeProgramId = question.getJudgeProgramId();
//        Program judgeProgram = programService.getById(judgeProgramId);
//        Runner judgeRunner = getRunnerStrategy(judgeProgram);
//        Runner userRunner = getRunnerStrategy(userProgram);
//
//        //判题限制
//        Long timeLimit = question.getTimeLimit();
//        Long memoryLimit = question.getMemoryLimit();
//
//        //查询所有测试用例
//        Long questionId = question.getId();
//        QueryWrapper<JudgeData> judgeDataQueryWrapper = new QueryWrapper<>();
//        judgeDataQueryWrapper.lambda()
//                .eq(JudgeData::getQuestionId, questionId);
//        long count = judgeDataService.count(judgeDataQueryWrapper);
//        while (count > 0) {
//            JudgeData judgeData = judgeDataService.page(new Page<>(count, 0), judgeDataQueryWrapper).getRecords().get(0);
//            VerdictEnum tmpResult = doJudge(judgeData,
//                    userRunner, judgeRunner,
//                    timeLimit, memoryLimit);
//            if (!VerdictEnum.Accepted.equals(tmpResult)) {
//                result = tmpResult;
//            }
//            count--;
//        }

        UpdateWrapper<Submission> submissionUpdateWrapper = new UpdateWrapper<>();
        submissionUpdateWrapper.lambda()
                .set(Submission::getVerdict, result.getValue())
                .eq(Submission::getId, submission.getId())
                .ne(Submission::getVerdict, VerdictEnum.Accepted.getValue());
        submissionService.update(submissionUpdateWrapper);

        //更新榜单
        contestService.updateRanking(contestId, submission.getUserId());
    }

    VerdictEnum doJudge(JudgeData judgeData,
                        Runner userRunner, Runner judgeRunner,
                        Long timeLimit, Long memoryLimit) throws IOException, InterruptedException {
        //将测试数据写入输入文件
        FileUtil.writeToFile(judgeData.getInput(), userRunner.getInputFile());
        VerdictEnum verdict = userRunner.run(timeLimit, memoryLimit);
        if (!VerdictEnum.Accepted.equals(verdict)) {
            return verdict;
        }
        //读取输出
        String userOutput = FileUtil.readAllFile(userRunner.getOutputFile());
        //将输入数据和输出数据，交给判题程序运行
        FileUtil.writeToFile(userOutput, judgeRunner.getUserOutputFile());
        FileUtil.writeToFile(judgeData.getInput(), judgeRunner.getInputFile());
        FileUtil.writeToFile(judgeData.getOutput(), judgeRunner.getOutputFile());
        verdict = userRunner.run(timeLimit, memoryLimit);
        return verdict;
    }

    Runner getRunnerStrategy(Program program) throws IOException, InterruptedException {
        Runner runner;
        switch (program.getLanguage()) {
            case 1:
                runner = new JavaRunner(program.getCode());
                break;
            case 2:
                runner = null;//new CppRunner(program.getCode());
                break;
            default:
                throw new BusinessException(ErrorCode.NO_LANGUAGE);
        }
        return runner;
    }

}
