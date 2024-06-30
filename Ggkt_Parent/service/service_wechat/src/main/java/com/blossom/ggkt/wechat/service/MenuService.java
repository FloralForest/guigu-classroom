package com.blossom.ggkt.wechat.service;

import com.blossom.ggkt.model.wechat.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blossom.ggkt.vo.wechat.MenuVo;

import java.util.List;

/**
 * <p>
 * 微信订单明细 订单明细 服务类
 * </p>
 *
 * @author blossom
 * @since 2023-07-20
 */
public interface MenuService extends IService<Menu> {

    //获取所有菜单按照一级二级封装
    List<MenuVo> findMenuInfo();

    //获取一级菜单
    List<Menu> findMenuOneInfo();

    //同步菜单
    void syncMenu();

    //公众号菜单删除
    void removeMenu();
}
