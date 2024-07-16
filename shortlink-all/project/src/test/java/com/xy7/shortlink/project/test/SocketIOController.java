package com.xy7.shortlink.project.test;

import com.xy7.shortlink.framework.starter.convention.result.Result;
import com.xy7.shortlink.framework.starter.web.Results;
import com.xy7.shortlink.project.net.ISocketIOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xy7
 * @Description
 * @date 2024/7/12
 */
@RestController
@RequestMapping("/api/socket.io")
public class SocketIOController {

    @Autowired
    private ISocketIOService socketIOService;

    @PostMapping(value = "/pushMessageToUser")
    public Result<Void> pushMessageToUser(@RequestParam String userId, @RequestParam String msgContent) {
        socketIOService.pushMessageToUser(userId, msgContent);
        return Results.success();
    }

}
