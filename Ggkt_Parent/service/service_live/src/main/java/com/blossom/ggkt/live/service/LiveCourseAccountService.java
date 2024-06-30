package com.blossom.ggkt.live.service;

import com.blossom.ggkt.model.live.LiveCourseAccount;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 直播课程账号表（受保护信息） 服务类
 * </p>
 *
 * @author blossom
 * @since 2023-07-27
 */
public interface LiveCourseAccountService extends IService<LiveCourseAccount> {

    //查看账号信息
    LiveCourseAccount getByLiveCourseId(Long id);
}
