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

package com.xy7.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xy7.shortlink.admin.dao.entity.UserDO;
import com.xy7.shortlink.admin.dto.req.UserDeletionReqDTO;
import com.xy7.shortlink.admin.dto.req.UserLoginReqDTO;
import com.xy7.shortlink.admin.dto.req.UserRegisterReqDTO;
import com.xy7.shortlink.admin.dto.req.UserUpdateReqDTO;
import com.xy7.shortlink.admin.dto.resp.UserLoginRespDTO;
import com.xy7.shortlink.admin.dto.resp.UserRespDTO;
import jakarta.validation.constraints.NotEmpty;

public interface UserService extends IService<UserDO> {
    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户返回实体
     */
    UserRespDTO getUserByUsername(String username);


    /**
     * 查询用户名是否存在
     *
     * @param username 用户名
     * @return 用户名存在返回 True，不存在返回 False
     */
    Boolean hasUsername(String username);

    /**
     * 注册用户
     *
     * @param requestParam 注册用户请求参数
     */
    void register(UserRegisterReqDTO requestParam);

    /**
     * 根据用户名修改用户
     *
     * @param requestParam 修改用户请求参数
     */
    void update(UserUpdateReqDTO requestParam);

    /**
     * 用户登录
     *
     * @param requestParam 用户登录请求参数
     * @return 用户登录返回参数 Token
     */
    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    /**
     * 检查用户是否登录
     *
     * @param token    用户登录 Token
     * @return 用户是否登录标识
     */
    UserLoginRespDTO checkLogin(String token);

    /**
     * 用户退出登录
     *
     * @param accessToken 用户登录 Token 凭证
     */
    void logout(String accessToken);

    /**
     * 注销用户
     *
     * @param requestParam 注销用户入参
     */
    void deletion(UserDeletionReqDTO requestParam);

    /**
     * 根据证件类型和证件号查询注销次数
     *
     * @param phone 手机号码
     * @return 注销次数
     */
    Integer queryUserDeletionNum(String phone);
}
