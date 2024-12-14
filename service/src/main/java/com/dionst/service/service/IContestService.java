package com.dionst.service.service;

import com.dionst.service.common.PageResult;
import com.dionst.service.model.dto.contest.ContestAddRequest;
import com.dionst.service.model.dto.contest.ContestPageRequest;
import com.dionst.service.model.entity.Contest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 比赛 服务类
 * </p>
 *
 * @author 张树程
 * @since 2024-12-13
 */
public interface IContestService extends IService<Contest> {

    /**
     * 添加比赛
     *
     * @param contestAddRequest
     * @return
     */
    long addContest(ContestAddRequest contestAddRequest);

    /**
     * 分页查看
     * @param contestPageRequest
     * @return
     */
    PageResult pageContest(ContestPageRequest contestPageRequest);

    /**
     * 参加比赛
     * @param contestId
     */
    void participate(Long contestId);
}
