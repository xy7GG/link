package com.xy7.shortlink.project.net;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import com.xy7.shortlink.framework.starter.user.core.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @author xy7
 * @Description
 * @date 2024/7/16
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class MessageEventHandler {

    private final SocketInstance socketInstance;

    /**
     * 客户端发起连接时触发
     *
     * @param client 客户端Socket对象信息
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        UUID sessionId = client.getSessionId();
        String userId = UserContext.getUserId();
        log.info("客户端{}建立链接连接", sessionId);
        socketInstance.insertSocketClient(userId,client);
    }

    /**
     * 客户端断开连接时触发
     *
     * @param client 客户端Socket对象信息
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        String userId = UserContext.getUserId();
        UUID sessionId = client.getSessionId();
        log.info("客户端{}断开连接", sessionId);
        socketInstance.removeClient(userId);
    }

}
