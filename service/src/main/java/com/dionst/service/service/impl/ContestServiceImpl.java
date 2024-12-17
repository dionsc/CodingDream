package com.dionst.service.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dionst.service.common.ErrorCode;
import com.dionst.service.common.PageResult;
import com.dionst.service.constant.ContestConstant;
import com.dionst.service.constant.RedisConstant;
import com.dionst.service.exception.BusinessException;
import com.dionst.service.mapper.QuestionMapper;
import com.dionst.service.mapper.SubmissionMapper;
import com.dionst.service.mapper.UserRatingMapper;
import com.dionst.service.model.dto.ranking.RankingRow;
import com.dionst.service.model.dto.contest.ContestAddRequest;
import com.dionst.service.model.dto.contest.ContestPageRequest;
import com.dionst.service.model.dto.ranking.SubmissionDetails;
import com.dionst.service.model.entity.*;
import com.dionst.service.mapper.ContestMapper;
import com.dionst.service.model.enums.VerdictEnum;
import com.dionst.service.service.IContestService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dionst.service.service.IQuestionService;
import com.dionst.service.service.ISubmissionService;
import com.dionst.service.service.IUserRatingService;
import com.dionst.service.utils.UserHolder;
import com.google.gson.Gson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 比赛 服务实现类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
@Service
public class ContestServiceImpl extends ServiceImpl<ContestMapper, Contest> implements IContestService {

    @Autowired
    private UserRatingMapper userRatingMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private Gson gson;

    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public long addContest(ContestAddRequest contestAddRequest) {
        String description = contestAddRequest.getDescription();
        String title = contestAddRequest.getTitle();
        LocalDateTime startTime = contestAddRequest.getStartTime();
        Long duration = contestAddRequest.getDuration();

        //检查时间是否距离比赛开始时间过短
        if (startTime.isBefore(LocalDateTime.now().plusDays(ContestConstant.CREATE_BEFORE_START))) {
            throw new BusinessException(ErrorCode.CREATE_CLOSE_TO_START_TIME);
        }
        //检查比赛时长是否过短
        if (duration < ContestConstant.MIN_CONTEST_DURATION) {
            throw new BusinessException(ErrorCode.CONTEST_DURATION_TOO_SHORT);
        }
        Contest contest = new Contest().setDescription(description).setTitle(title).setStartTime(startTime).setDuration(duration);

        User user = UserHolder.getUser();
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        contest.setCreateId(user.getId());
        //保存
        save(contest);
        return contest.getCreateId();
    }

    @Override
    public PageResult pageContest(ContestPageRequest contestPageRequest) {
        int current = contestPageRequest.getCurrent();
        int pageSize = contestPageRequest.getPageSize();
        String title = contestPageRequest.getTitle();
        boolean asc = contestPageRequest.isAsc();

        QueryWrapper<Contest> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().select(Contest::getId, Contest::getTitle, Contest::getDescription, Contest::getDuration);
        if (!StrUtil.isBlank(title)) {
            queryWrapper.lambda().like(Contest::getTitle, title);
        }
        if (asc) queryWrapper.lambda().orderByAsc(Contest::getStartTime);
        else queryWrapper.lambda().orderByDesc(Contest::getStartTime);

        //分页查询
        Page<Contest> page = page(new Page<>(current, pageSize), queryWrapper);

        return new PageResult(page.getTotal(), page.getRecords());
    }

    @Override
    public void participate(Long contestId) {
        //查看比赛是否已经结束
        Contest contest = getById(contestId);
        LocalDateTime startTime = contest.getStartTime();
        Long duration = contest.getDuration();
        if (startTime.plusMinutes(duration).isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.CONTEST_FINISH);
        }
        //检查是否已经参赛
        QueryWrapper<UserRating> userRatingQueryWrapper = new QueryWrapper<>();
        userRatingQueryWrapper.lambda().eq(UserRating::getContestId, contestId).eq(UserRating::getUserId, UserHolder.getUser().getId());

        long c = userRatingMapper.selectCount(userRatingQueryWrapper);
        //如果已经参赛
        if (c > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //保存
        UserRating userRating = new UserRating();
        userRating.setContestId(contestId);
        userRating.setUserId(UserHolder.getUser().getId());
        userRating.setRatingChange(Integer.MIN_VALUE);
        userRatingMapper.insert(userRating);
    }

    @Override
    public List<String> getRanking(Long contestId) {
        String key = RedisConstant.RANKING + contestId;
        Boolean has = stringRedisTemplate.hasKey(key);
        if (!has) {
            buildRanking(contestId);
            return null;
        }
        List<String> result = new ArrayList<>();
        Set<String> userIdSet = stringRedisTemplate.opsForZSet().reverseRange(key, 0, -1);
        if (userIdSet == null || userIdSet.isEmpty()) {
            return null;
        }
        for (String userIdString : userIdSet) {
            long userId = Long.parseLong(userIdString);
            String rankingRowKey = RedisConstant.RANKING + contestId + ":" + userId;
            if (!stringRedisTemplate.hasKey(rankingRowKey)) {
                updateRanking(contestId, userId);
            }
            String json = stringRedisTemplate.opsForValue().get(rankingRowKey);
            result.add(json);
        }
        return result;
    }

    void buildRanking(Long contestId) {
        String rankingKey = RedisConstant.RANKING + contestId;
        //获取互斥锁，防止重复更新
        String lockKey = RedisConstant.RANKING_LOCK + contestId;
        RLock lock = redissonClient.getLock(lockKey);
        if (!lock.tryLock()) {
            return;
        }
        //查询所有比赛用户
        QueryWrapper<UserRating> userRatingQueryWrapper = new QueryWrapper<>();
        userRatingQueryWrapper.lambda().select(UserRating::getUserId).eq(UserRating::getContestId, contestId);
        List<UserRating> userlist = userRatingMapper.selectList(userRatingQueryWrapper);

        //查询比赛的所有题目
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.lambda().select(Question::getQuestionIndex, Question::getId).eq(Question::getContestId, contestId);
        List<Question> questionList = questionMapper.selectList(questionQueryWrapper);

        //查询比赛开始时间
        QueryWrapper<Contest> contestQueryWrapper = new QueryWrapper<>();
        contestQueryWrapper.lambda().select(Contest::getStartTime).eq(Contest::getId, contestId);
        Contest contest = getOne(contestQueryWrapper);
        LocalDateTime startTime = contest.getStartTime();

        for (UserRating userRating : userlist) {
            RankingRow rankingRow = new RankingRow();
            Long userId = userRating.getUserId();
            long penalty = 0;
            long accepted = 0;
            long acceptedPenalty;
            List<SubmissionDetails> submissionDetailsList = new ArrayList<>();
            for (Question question : questionList) {

                QueryWrapper<Submission> submissionQueryWrapper = new QueryWrapper<>();
                submissionQueryWrapper.lambda().eq(Submission::getContestId, contestId).eq(Submission::getQuestionIndex, question.getQuestionIndex()).eq(Submission::getVerdict, VerdictEnum.Accepted.getValue()).orderByAsc(Submission::getSubmitTime);
                List<Submission> list = submissionMapper.selectList(submissionQueryWrapper);
                //如果有正确提交
                if (!list.isEmpty()) {
                    SubmissionDetails submissionDetails = new SubmissionDetails();
                    submissionDetails.setQuestionIndex(question.getQuestionIndex());

                    accepted++;
                    Submission submission = list.get(0);

                    //计算通过罚时
                    acceptedPenalty = Duration.between(startTime, submission.getSubmitTime()).toMinutes();
                    penalty += acceptedPenalty;
                    submissionDetails.setAcceptedPenalty(acceptedPenalty);
                    //计算错误罚时
                    submissionQueryWrapper = new QueryWrapper<>();
                    submissionQueryWrapper.lambda().eq(Submission::getContestId, contestId).eq(Submission::getQuestionIndex, question.getQuestionIndex()).ne(Submission::getVerdict, VerdictEnum.Accepted.getValue()).ne(Submission::getVerdict, VerdictEnum.InQueue.getValue()).ne(Submission::getVerdict, VerdictEnum.Pending.getValue()).ne(Submission::getVerdict, VerdictEnum.Running.getValue()).le(Submission::getSubmitTime, submission.getSubmitTime());
                    long c = submissionMapper.selectCount(submissionQueryWrapper);
                    penalty += c * ContestConstant.WRONG_PENALTY;

                    //查询所有提交
                    submissionQueryWrapper = new QueryWrapper<>();
                    submissionQueryWrapper.lambda().eq(Submission::getContestId, contestId).eq(Submission::getQuestionIndex, question.getQuestionIndex()).eq(Submission::getUserId, userId);
                    list = submissionMapper.selectList(submissionQueryWrapper);
                    submissionDetails.setSubmissions(list);
                    submissionDetailsList.add(submissionDetails);
                }
            }
            rankingRow.setAccepted(accepted);
            rankingRow.setPenalty(penalty);
            rankingRow.setSubmissionDetails(submissionDetailsList);
            double score = accepted + 1.0 / (penalty + 1);
            String rankingRowKey = RedisConstant.RANKING_ROW + contestId + ":" + userId;
            stringRedisTemplate.opsForValue().set(rankingRowKey, gson.toJson(rankingRow));
            stringRedisTemplate.opsForZSet().add(rankingKey, userId.toString(), score);
        }
        lock.unlock();
    }


    public void updateRanking(Long contestId, Long userId) {


        String rankingKey = RedisConstant.RANKING + contestId;
        String rankingRowKey = RedisConstant.RANKING_ROW + contestId + ":" + userId;

        //如果还没构建榜单则直接构建
        if (!stringRedisTemplate.hasKey(rankingKey)) {
            buildRanking(contestId);
        }

        //查询比赛的所有题目
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.lambda().select(Question::getQuestionIndex, Question::getId).eq(Question::getContestId, contestId);
        List<Question> questionList = questionMapper.selectList(questionQueryWrapper);

        //查询比赛开始时间
        QueryWrapper<Contest> contestQueryWrapper = new QueryWrapper<>();
        contestQueryWrapper.lambda().select(Contest::getStartTime).eq(Contest::getId, contestId);
        Contest contest = getOne(contestQueryWrapper);
        LocalDateTime startTime = contest.getStartTime();

        RankingRow rankingRow = new RankingRow();
        long penalty = 0;
        long accepted = 0;
        long acceptedPenalty;
        List<SubmissionDetails> submissionDetailsList = new ArrayList<>();
        for (Question question : questionList) {

            QueryWrapper<Submission> submissionQueryWrapper = new QueryWrapper<>();
            submissionQueryWrapper.lambda().eq(Submission::getContestId, contestId).eq(Submission::getQuestionIndex, question.getQuestionIndex()).eq(Submission::getVerdict, VerdictEnum.Accepted.getValue()).orderByAsc(Submission::getSubmitTime);
            List<Submission> list = submissionMapper.selectList(submissionQueryWrapper);
            //如果有正确提交
            if (!list.isEmpty()) {
                SubmissionDetails submissionDetails = new SubmissionDetails();
                submissionDetails.setQuestionIndex(question.getQuestionIndex());

                accepted++;
                Submission submission = list.get(0);

                //计算通过罚时
                acceptedPenalty = Duration.between(startTime, submission.getSubmitTime()).toMinutes();
                penalty += acceptedPenalty;
                submissionDetails.setAcceptedPenalty(acceptedPenalty);
                //计算错误罚时
                submissionQueryWrapper = new QueryWrapper<>();
                submissionQueryWrapper.lambda().eq(Submission::getContestId, contestId).eq(Submission::getQuestionIndex, question.getQuestionIndex()).ne(Submission::getVerdict, VerdictEnum.Accepted.getValue()).ne(Submission::getVerdict, VerdictEnum.InQueue.getValue()).ne(Submission::getVerdict, VerdictEnum.Pending.getValue()).ne(Submission::getVerdict, VerdictEnum.Running.getValue()).le(Submission::getSubmitTime, submission.getSubmitTime());
                long c = submissionMapper.selectCount(submissionQueryWrapper);
                penalty += c * ContestConstant.WRONG_PENALTY;
                //查询所有提交
                submissionQueryWrapper = new QueryWrapper<>();
                submissionQueryWrapper.lambda().eq(Submission::getContestId, contestId).eq(Submission::getQuestionIndex, question.getQuestionIndex()).eq(Submission::getUserId, userId);
                list = submissionMapper.selectList(submissionQueryWrapper);
                submissionDetails.setSubmissions(list);
                submissionDetailsList.add(submissionDetails);
            }
        }
        rankingRow.setAccepted(accepted);
        rankingRow.setPenalty(penalty);
        rankingRow.setSubmissionDetails(submissionDetailsList);
        double score = accepted + 1.0 / (penalty + 1);
        stringRedisTemplate.opsForValue().set(rankingRowKey, gson.toJson(rankingRow));
        //更新榜单行信息
        stringRedisTemplate.opsForZSet().remove(rankingKey, userId.toString());
        //更新榜单
        stringRedisTemplate.opsForZSet().add(rankingKey, userId.toString(), score);
    }
}
