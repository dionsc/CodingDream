package com.dionst.service.utils;

import com.dionst.service.constant.MQConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public abstract class CommonUtils {

    public static String camelToSnake(String camel) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < camel.length(); i++) {
            if ('A' <= camel.charAt(i) && camel.charAt(i) <= 'Z') {
                res.append((char) (camel.charAt(i) - ('A' - 'a')));
            } else
                res.append(camel.charAt(i));
        }
        return res.toString();
    }

    public static void sendMessageToFrontend(String messageRequestJson, RabbitTemplate rabbitTemplate) {
        rabbitTemplate.convertAndSend(MQConstant.BROADCAST_EXCHANGE, "", messageRequestJson);

    }
}
