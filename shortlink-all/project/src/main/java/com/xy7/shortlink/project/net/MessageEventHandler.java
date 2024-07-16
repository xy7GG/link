package com.xy7.shortlink.project.net;

import cn.hutool.core.date.DateUtil;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author xy7
 * @Description
 * @date 2024/7/12
 */
@Component
public class MessageEventHandler {

    private static final Logger logger = LoggerFactory.getLogger(MessageEventHandler.class);

    /**
     * 服务器socket对象
     */
    public SocketIOServer socketIoServer;

    /**
     * 客户端集合
     */
    public List<String> listClient = new CopyOnWriteArrayList<>();

    public SocketInstance socketInstance = SocketInstance.getSocketInstance();

    /**
     * 超时时间
     */
    static final int limitSeconds = 60;

    /**
     * 初始化消息事件处理器
     *
     * @param server 服务器socket对象
     */
    @Autowired
    public MessageEventHandler(SocketIOServer server) {
        logger.info("初始化SOCKET消息事件处理器");
        this.socketIoServer = server;
    }

    /**
     * 客户端发起连接时触发
     *
     * @param client 客户端Socket对象信息
     */
    @OnConnect
    public void onConnect(SocketIOClient client) {
        logger.info("客户端{}已连接", client.getSessionId());
        String sessionId = getSessionId(client);
        listClient.add(sessionId);
        socketInstance.insertSocketClient(sessionId, client);
        //向前端发送接收数据成功标识
        client.sendEvent("connect_success", "已经成功连接");

    }

    /**
     * 客户端断开连接时触发
     *
     * @param client 客户端Socket对象信息
     */
    @OnDisconnect
    public void onDisconnect(SocketIOClient client) {
        logger.info("客户端{}断开连接", client.getSessionId());
        String sessionId = getSessionId(client);

        listClient.remove(sessionId);
        socketInstance.remoteClient(sessionId);

    }


    /**
     * 客户端发送消息时触发
     *
     * @param client  客户端Socket对象信息
     * @param request AckRequest 回调对象
     * @param data    消息信息实体
     */
    @OnEvent(value = SocketConstants.SocketEvent.MESSAGE)
    public void onEvent(SocketIOClient client, AckRequest request, String data) {
        System.out.println("发来消息：" + data);
        request.sendAckData("服务端已收到");
        client.sendEvent("messageevent", "back data");
        //socketIoServer.getClient(client.getSessionId()).sendEvent("messageevent", "back data");
    }

    @OnEvent(value = SocketConstants.SocketEvent.BROADCAST)
    public void onEventByBroadcast(SocketIOClient client, AckRequest request, String data) {
        System.out.println("发来消息：" + data);
        request.sendAckData("服务端-广播事件已收到");
        client.sendEvent(SocketConstants.SocketEvent.BROADCAST, "广播事件 " + DateUtil.now());
        //socketIoServer.getClient(client.getSessionId()).sendEvent("messageevent", "back data");
    }


    /**
     * 广播消息 函数可在其他类中调用
     */
    public void sendBroadcast(byte[] data) {
        //向已连接的所有客户端发送数据,map实现客户端的存储
        for (SocketIOClient client : socketInstance.getClientSocketAll().values()) {
            if (client.isChannelOpen()) {
                client.sendEvent("message_event", data);
            }
        }
    }

    /**
     * 获取客户端的session Id
     *
     * @param client: 客户端
     */
    private String getSessionId(SocketIOClient client) {
        return client.getSessionId().toString();

    }

    /**
     * 获取连接的客户端ip地址
     *
     * @param client: 客户端
     * @return 获取连接的客户端ip地址
     */
    private String getIpByClient(SocketIOClient client) {
        String sa = client.getRemoteAddress().toString();
        return sa.substring(1, sa.indexOf(":"));
    }

}
