package com.xy7.shortlink.project.net;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author xy7
 * @Description
 * @date 2024/7/12
 */
@Slf4j
@Component
public class MyNamespaceHandler {
    //测试使用
    @OnEvent("message")
    public void testHandler(SocketIOClient client, String data, AckRequest ackRequest) throws JsonProcessingException {

        log.info("SplSearch:{}", data);

        if (ackRequest.isAckRequested()) {
            //返回给客户端，说我接收到了
            ackRequest.sendAckData("SplSearch", data);
        }

    }
}
