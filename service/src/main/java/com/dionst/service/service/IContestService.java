package com.dionst.service.service;

import com.dionst.service.common.PageResult;
import com.dionst.service.model.dto.contest.ContestAddRequest;
import com.dionst.service.model.dto.contest.ContestPageRequest;
import com.dionst.service.model.entity.Contest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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

    /**
     * 获取榜单
     *
     * @param contestId
     * @return
     */
    List<String> getRanking(Long contestId);

    /**
     * 更新榜单
     * @param contestId
     * @param userId
     */
    void updateRanking(Long contestId, Long userId);
}
