package com.dionst.service.constant;

public interface RedisConstant {
   String LOGIN_CODE_KEY = "login_code:";
   String LOGIN_TOKEN_KEY = "login_token:";
   long LOGIN_TOKEN_TTL = 30;
   long LOGIN_CODE_TTL = 5;
}
