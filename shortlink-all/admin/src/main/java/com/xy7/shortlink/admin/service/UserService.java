package com.xy7.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy7.shortlink.admin.dao.entity.UserDO;
import com.xy7.shortlink.admin.dto.req.UserLoginReqDTO;
import com.xy7.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.xy7.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.xy7.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.xy7.shortlink.admin.dto.resp.UserRespDTO;

public interface UserService extends IService<UserDO> {

    UserRespDTO getUserByUsername(String username);

    Boolean hasUsername(String username);

    void register(UserRegisterReqDTO requestParam);

    void update(UserUpdateReqDTO requestParam);

    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    Boolean checkLogin(String username, String token);

    void logout(String username, String token);
}
