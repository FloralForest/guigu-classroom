package com.blossom.ggkt.user.service;

import com.blossom.ggkt.model.user.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author blossom
 * @since 2023-07-19
 */
public interface UserInfoService extends IService<UserInfo> {

    //判断是否第一次授权--查询数据库
    UserInfo getUserInfoOpenid(String openId);
}
