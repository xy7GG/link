package com.xy7.shortlink.project.net;

import com.corundumstudio.socketio.SocketIOClient;
import com.xy7.shortlink.framework.starter.convention.exception.ClientException;
import com.xy7.shortlink.framework.starter.user.core.UserContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SocketInstance {
    /**
     * 客户端Socket连接对象容器
     */
    private static Map<String, SocketIOClient> socketClients = new ConcurrentHashMap<>();

    /**
     * 私有构造
     */
    private SocketInstance() {
        //从缓存中获取socketClients
        socketClients = new HashMap<>();
    }

    /**
     * 定义一个私有的内部类，在第一次用这个嵌套类时，会创建一个实例。而类型为SocketInstanceHolder的类，
     * 只有在SocketInstance.getSocketInstance()中调用，
     * 由于私有的属性，他人无法使用SocketInstanceHolder，不调用SocketInstance.getSocketInstance()就不会创建实例。
     * 优点：达到了lazy loading的效果，即按需创建实例。
     * 无法适用于分布式集群部署
     */
    private static class SocketInstanceHolder {
        /**
         * 创建全局唯一实例
         */
        private final static SocketInstance instance = new SocketInstance();
    }

    /**
     * 获取全局唯一实例
     *
     * @return SocketInstance对象
     */
    public static SocketInstance getSocketInstance() {
        return SocketInstanceHolder.instance;
    }


    public void pushMessage(String topic, String message) {
        SocketIOClient clientSocket = getClientSocket();
        if (ObjectUtils.isEmpty(clientSocket)){
            return;
        }
        clientSocket.sendEvent(topic, message);
    }

    /**
     * 新增客户端连接到容器
     *
     * @param userId
     * @param socketIOClient 客户端socket对象
     */
    public void insertSocketClient(String userId, SocketIOClient socketIOClient) {
        socketClients.put(userId, socketIOClient);
    }

    /**
     * 获取客户端Socket对象
     *
     * @return 客户端Socket对象
     */
    public SocketIOClient getClientSocket() {
        String userId = UserContext.getUserId();
        return socketClients.get(userId);
    }

    /**
     * 获取所有客户端Socket对象
     *
     * @return 客户端Socket对象
     */
    public Map<String, SocketIOClient> getClientSocketAll() {
        return socketClients;
    }


    /**
     * 删除客户端
     * @param userId 用户id
     */
    public void removeClient(String userId) {
        SocketIOClient oldSocketIOClient = socketClients.get(userId);
        if (oldSocketIOClient != null) {
            try {
                //关闭客户端连接
                oldSocketIOClient.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        socketClients.remove(userId);
    }
}