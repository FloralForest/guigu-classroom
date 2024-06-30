package com.blossom.ggkt.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blossom.ggkt.model.user.UserInfo;
import com.blossom.ggkt.user.mapper.UserInfoMapper;
import com.blossom.ggkt.user.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author blossom
 * @since 2023-07-19
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    //判断是否第一次授权--查询数据库
    @Override
    public UserInfo getUserInfoOpenid(String openId) {
        LambdaQueryWrapper<UserInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserInfo::getOpenId,openId);
        return baseMapper.selectOne(wrapper);
    }
}
