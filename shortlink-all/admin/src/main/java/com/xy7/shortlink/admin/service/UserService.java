package com.xy7.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy7.shortlink.admin.dao.entity.UserDO;
import com.xy7.shortlink.admin.dto.resp.UserRespDTO;

public interface UserService extends IService<UserDO> {

    UserRespDTO getUserByUsername(String username);
}
