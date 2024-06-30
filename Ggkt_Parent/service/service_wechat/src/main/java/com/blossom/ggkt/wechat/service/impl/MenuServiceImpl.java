package com.blossom.ggkt.wechat.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blossom.ggkt.model.wechat.Menu;
import com.blossom.ggkt.vo.wechat.MenuVo;
import com.blossom.ggkt.wechat.mapper.MenuMapper;
import com.blossom.ggkt.wechat.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 微信订单明细 订单明细 服务实现类
 * </p>
 *
 * @author blossom
 * @since 2023-07-20
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Resource
    private WxMpService wxMpService;

    //获取所有菜单按照一级二级封装
    @Override
    public List<MenuVo> findMenuInfo() {
        //创建集合用于封装最终数据
        List<MenuVo> finalMenuList = new ArrayList<>();
        //查询所有菜单数据
        List<Menu> menuList = baseMapper.selectList(null);
        //从所有数据中获取一级菜单
        //使用stream流的方式遍历，filter过滤menu中id为0的数据，最后collect(Collectors.toList())把过滤的数据封装到一个新的集合里
        List<Menu> oneMenuList = menuList.stream().filter(menu -> menu.getParentId() == 0).collect(Collectors.toList());
        //遍历一级菜单，并把Menu转换成MenuVo
        for (Menu oneMenu : oneMenuList) {
            MenuVo oneMenuVo = new MenuVo();//存一级菜单
            BeanUtils.copyProperties(oneMenu, oneMenuVo);
            //遍历处理二级菜单,判断二级菜单的ParentId与一级菜单的id是否相同,来确定该二级菜单属于哪个一级菜单
            List<Menu> twoMenuList = menuList.stream()
                    .filter(menu -> Objects.equals(menu.getParentId(), oneMenu.getId()))
                    .collect(Collectors.toList());
            List<MenuVo> twoList = new ArrayList<>();
            for (Menu twoMenu : twoMenuList) {
                MenuVo twoMenuVo = new MenuVo();//存二级菜单
                BeanUtils.copyProperties(twoMenu, twoMenuVo);
                twoList.add(twoMenuVo);
            }
            //把二级菜单放入一级菜单
            oneMenuVo.setChildren(twoList);
            //存入最终集合里
            finalMenuList.add(oneMenuVo);
        }

        return finalMenuList;
    }

    //获取一级菜单--id为0时就是一级菜单
    @Override
    public List<Menu> findMenuOneInfo() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, 0);
        return baseMapper.selectList(wrapper);
    }

    //同步菜单方法
    @Override
    public void syncMenu() {
        //获取所有数据
        List<MenuVo> menuVoList = this.findMenuInfo();
        //封装结构以数组格式
        JSONArray jsonList = new JSONArray();
        for (MenuVo oneMenuVo : menuVoList) {
            //json对象，一级菜单
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            //json数组，二级菜单
            JSONArray two = new JSONArray();
            //遍历得到二级菜单
            for (MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                JSONObject view = new JSONObject();
                //判断数据库中的type是否为view
                view.put("type", twoMenuVo.getType());
                if (twoMenuVo.getType().equals("view")) {
                    view.put("name", twoMenuVo.getName());
//                    view.put("url", "http://ggkt2.vipgz1.91tunnel.com/#" + twoMenuVo.getUrl());
                    view.put("url", "http://ggktflfo.v5.idcfengye.com/#" + twoMenuVo.getUrl()); //访问菜单
                } else {
                    view.put("name", twoMenuVo.getName());
                    view.put("key", twoMenuVo.getMeunKey());
                }
                two.add(view);
            }
            one.put("sub_button", two);//二级菜单放入一级菜单
            jsonList.add(one);
        }
        //封装最外层的菜单
        JSONObject button = new JSONObject();
        button.put("button", jsonList);
        try {
            this.wxMpService.getMenuService().menuCreate(button.toJSONString());
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    //公众号菜单删除
    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }
}
