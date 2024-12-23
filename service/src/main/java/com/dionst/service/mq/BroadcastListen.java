package com.dionst.service.mq;

import com.dionst.service.constant.MQConstant;
import com.dionst.service.judge.JudgeService;
import com.dionst.service.model.dto.contest.SendMessageRequest;
import com.dionst.service.websocket.WebSocketServer;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class BroadcastListen {

    @Autowired
    private WebSocketServer webSocketServer;

    @Autowired
    private Gson gson;

    @RabbitListener(queues = "#{anonymousQueue.name}")
    public void listenBroadcastQueue(String messageRequestJson) {
        try {
            SendMessageRequest sendMessageRequest = gson.fromJson(messageRequestJson, SendMessageRequest.class);
            webSocketServer.sendToAllClient(sendMessageRequest.getContestId(), sendMessageRequest.getMessage());
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
