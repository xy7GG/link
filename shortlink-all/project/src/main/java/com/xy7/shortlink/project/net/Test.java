package com.xy7.shortlink.project.net;

import cn.hutool.core.date.DateUtil;
import com.corundumstudio.socketio.SocketIOClient;


import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xy7
 * @Description
 * @date 2024/7/16
 */
@Slf4j
public class Test {
    public static void main(String []args){
        // 服务端socket.io连接通信地址
        String url = "http://127.0.0.1:8888";

        try {
            IO.Options options = new IO.Options();
            options.transports = new String[]{"websocket"};
            options.reconnectionAttempts = 2;
            // 失败重连的时间间隔
            options.reconnectionDelay = 1000;
            // 连接超时时间(ms)
            options.timeout = 500;
            // userId: 唯一标识 传给服务端存储
            Socket socket = IO.socket(url, options);
            socket.connect();
            // 自定义事件`push_data_event` -> 向服务端发送消息
            socket.emit("push_data_event", "发送数据 " + DateUtil.now());
            while (true) {
                Thread.sleep(3000);
                // 自定义事件`push_data_event` -> 向服务端发送消息
                socket.emit("push_data_event", "发送数据 " + DateUtil.now());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
