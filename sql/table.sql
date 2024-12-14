create database if not exists coding_dream_db;

use coding_dream_db;

-- 比赛表
create table if not exists contest
(
    id          bigint auto_increment comment 'id' primary key,
    create_id   bigint                             not null comment '创建者',
    description varchar(256)                       not null comment '比赛描述',
    title       varchar(32)                        not null comment '比赛标题',

    start_time  datetime                           not null comment '比赛开始时间',
    duration    bigint                             not null comment '比赛时长(单位:分钟)',

    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 not null comment '是否删除'
) comment '比赛' collate = utf8mb4_unicode_ci;


-- 比赛题目表
create table if not exists question
(
    id               bigint auto_increment comment 'id' primary key,
    create_id        bigint                             not null comment '创建者',

    contest_id       bigint                             not null comment '题目所属比赛id',
    question_index   bigint                             not null comment '题目在本场比赛的编号(用户自己设置)',

    title            varchar(256)                       not null comment '题目标题',
    description      text                               not null comment '题目描述',

    time_limit       bigint                             not null comment '判题时间限制(ms)',
    memory_limit     bigint                             not null comment '判题内存限制(MB)',

    try_number       bigint   default 0                 not null comment '题目提交数量',
    accepted_number  bigint   default 0                 not null comment '题目通过数量',

    judge_program_id bigint                             not null comment '判题程序',

    create_time      datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time      datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete        tinyint  default 0                 not null comment '是否删除',
    unique key unique_contest_question_index (contest_id, question_index)
) comment '比赛题目' collate = utf8mb4_unicode_ci;

create table if not exists program
(
    id          bigint auto_increment comment 'id' primary key,
    language    int                                not null comment '编程语言 枚举：1->java 2->java',
    code        text                               not null comment '代码',

    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 not null comment '是否删除'
) comment '程序' collate = utf8mb4_unicode_ci;


-- 赛时提交
create table if not exists submission
(
    id             bigint auto_increment comment 'id' primary key,
    user_id        bigint                             not null comment '用户编号',
    contest_id     bigint                             not null comment '参加的比赛的编号',
    question_index bigint                             not null comment '题目在本场比赛的编号(用户自己设置)',

    code_id        bigint                             not null comment '提交的代码',
    verdict        int                                not null comment '系统判定结果',
    submit_time    datetime                           not null comment '代码提交时间',

    create_time    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time    datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete      tinyint  default 0                 not null comment '是否删除'
) comment '赛时提交' collate = utf8mb4_unicode_ci;


-- 判题数据表
create table if not exists judge_data
(
    id          bigint auto_increment comment 'id' primary key,
    name        varchar(256)                       not null comment '数据名称',
    question_id bigint                             not null comment '数据对应题目',
    data        mediumtext                         not null comment '数据部分',

    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 not null comment '是否删除'
) comment '判题数据' collate = utf8mb4_unicode_ci;


-- 榜单更新请求表
create table if not exists update_ranking_request
(
    id          bigint auto_increment comment 'id' primary key,
    contest_id  bigint                             not null comment '提交的比赛',
    user_id     bigint                             not null comment '用户编号',
    updated     boolean  default false             not null comment '是否已经发送到队列',

    create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete   tinyint  default 0                 not null comment '是否删除'
) comment '榜单更新请求' collate = utf8mb4_unicode_ci;


-- 判题请求表
create table if not exists judge_request
(
    id            bigint auto_increment comment 'id' primary key,
    submission_id bigint                             not null comment '对应提交编号',
    updated       boolean  default false             not null comment '是否已经发送到队列',

    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint  default 0                 not null comment '是否删除'
) comment '判题请求' collate = utf8mb4_unicode_ci;


-- 用户表
create table if not exists user
(
    id            bigint auto_increment comment 'id' primary key,
    phone         varchar(256)                       not null comment '手机号码',
    follow_number bigint   default 0                 not null comment '关注数',
    fans_number   bigint   default 0                 not null comment '粉丝数',
    user_role     int                                not null comment '用户角色 枚举：0->普通 1->管理员 2->封号',
    nickname      varchar(256)                       not null comment '用户昵称',

    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint  default 0                 not null comment '是否删除',
    UNIQUE KEY idx_nickname (nickname)
) comment '用户' collate = utf8mb4_unicode_ci;


-- 用户rating积分表
create table if not exists user_rating
(
    id            bigint auto_increment comment 'id' primary key,
    user_id       bigint                             not null comment '用户编号',
    contest_id    bigint                             not null comment '比赛编号',
    rating_change int                                not null comment '在本场比赛获得的积分',

    create_time   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    is_delete     tinyint  default 0                 not null comment '是否删除'
) comment '用户rating积分' collate = utf8mb4_unicode_ci;

