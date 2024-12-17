package com.dionst.service.constant;

public interface RedisConstant {
    String LOGIN_CODE_KEY = "login_code:";
    String LOGIN_TOKEN_KEY = "login_token:";
    long LOGIN_TOKEN_TTL = 30;
    long LOGIN_CODE_TTL = 5;
    String RANKING = "ranking:";
    String RANKING_LOCK = "ranking_lock:";
    String RANKING_ROW = "ranking_row:";
}
