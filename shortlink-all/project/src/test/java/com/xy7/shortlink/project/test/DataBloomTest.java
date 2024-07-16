/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xy7.shortlink.project.test;


import cn.hutool.core.date.DateUtil;
import io.socket.client.IO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.Socket;
import java.util.ArrayList;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class DataBloomTest {

    @Autowired
    private RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;

    @Test
    public void testRBloomFilter() {

//        String fullShortUrl_1 = "43.139.204.197:8001/2i1Uqb";
//        shortUriCreateCachePenetrationBloomFilter.add(fullShortUrl_1);
//        boolean contains = shortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl);
//        if(contains){
//            log.info("存在");
//        }
    }

    @Test
    public void test() {
        String username = "exampleUser";
        int shardIndex = Math.abs(username.hashCode() % 1024);
        System.out.println("Shard index: " + shardIndex);
    }

    @Test
    public void test2(){
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
            io.socket.client.Socket socket = IO.socket(url + "?userId=1", options);

            socket.on("Socket.EVENT_CONNECT", args1 -> socket.send("hello..."));

            // 自定义事件`connected` -> 接收服务端成功连接消息
            socket.on("connected", objects -> log.debug("服务端:" + objects[0].toString()));

            // 自定义事件`push_data_event` -> 接收服务端消息
            socket.on("push_data_event", objects -> log.debug("服务端:" + objects[0].toString()));

            // 自定义事件`myBroadcast` -> 接收服务端广播消息
            socket.on("myBroadcast", objects -> log.debug("服务端：" + objects[0].toString()));

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