package com.blossom.ggkt.live.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blossom.ggkt.client.course.CourseFeignClient;
import com.blossom.ggkt.client.user.UserInfoFeignClient;
import com.blossom.ggkt.live.mtcloud.CommonResult;
import com.blossom.ggkt.live.mtcloud.MTCloud;
import com.blossom.ggkt.live.service.*;
import com.blossom.ggkt.model.live.*;
import com.blossom.ggkt.live.mapper.LiveCourseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blossom.ggkt.model.user.UserInfo;
import com.blossom.ggkt.model.vod.Teacher;
import com.blossom.ggkt.utils.DateUtil;
import com.blossom.ggkt.vo.live.LiveCourseConfigVo;
import com.blossom.ggkt.vo.live.LiveCourseFormVo;
import com.blossom.ggkt.vo.live.LiveCourseGoodsView;
import com.blossom.ggkt.vo.live.LiveCourseVo;
import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.rmi.activation.ActivationException;
import java.util.*;

/**
 * <p>
 * 直播课程表 服务实现类
 * </p>
 *
 * @author blossom
 * @since 2023-07-27
 */
@Service
public class LiveCourseServiceImpl extends ServiceImpl<LiveCourseMapper, LiveCourse> implements LiveCourseService {

    //远程调用
    @Resource
    private CourseFeignClient courseFeignClient;

    @Resource
    private MTCloud mtCloudClient;

    @Resource
    private LiveCourseDescriptionService liveCourseDescriptionService;

    @Resource
    private LiveCourseAccountService liveCourseAccountService;

    @Resource
    private LiveCourseConfigService liveCourseConfigService;

    @Resource
    private LiveCourseGoodsService liveCourseGoodsService;

    //远程调用
    @Resource
    private UserInfoFeignClient userInfoFeignClient;

    //分页
    @Override
    public IPage<LiveCourse> selectPage(Page<LiveCourse> pageParam) {
        IPage<LiveCourse> page = baseMapper.selectPage(pageParam, null);
        //获取讲师信息
        List<LiveCourse> liveCourseList = page.getRecords();
        for (LiveCourse liveCourse : liveCourseList) {
            Teacher teacherLive = courseFeignClient.getTeacherLive(liveCourse.getTeacherId());
            if (teacherLive != null) {
                liveCourse.getParam().put("teacherName", teacherLive.getName());
                liveCourse.getParam().put("teacherLevel", teacherLive.getLevel());
            }
        }

        return page;
    }

    //添加直播
    @Override
    public void save(LiveCourseFormVo liveCourseVo) {

        final LiveCourse liveCourse = new LiveCourse();
        BeanUtils.copyProperties(liveCourseVo, liveCourse);
        //获取讲师信息
        final Teacher teacher = courseFeignClient.getTeacherLive(liveCourseVo.getTeacherId());
        //调用方法添加直播课程
        //创建map集合，封装其他参数
        HashMap<Object, Object> options = new HashMap<>();
        options.put("scenes", 2);//直播类型。1: 教育直播，2: 生活直播。默认 1，说明：根据平台开通的直播类型填写
        options.put("password", liveCourseVo.getPassword());

//      course_name 课程名称
//      account 发起直播课程的主播账号
//      start_time 课程开始时间,格式: 2015-01-10 12:00:00
//      end_time 课程结束时间,格式: 2015-01-10 13:00:00
//      nickname  主播昵称
//      accountIntro  主播介绍
        try {
            String res = mtCloudClient.courseAdd(
                    liveCourse.getCourseName(),
                    teacher.getId().toString(),
                    new DateTime(liveCourse.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    new DateTime(liveCourse.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    teacher.getName(),
                    teacher.getIntro(),
                    options);
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) {//判断是否创建成功
                //添加直播基本信息
                JSONObject object = commonResult.getData();
                liveCourse.setCourseId(object.getLong("course_id"));//直播课程id（每次不一样）
                baseMapper.insert(liveCourse);
                //添加直播详细信息
                LiveCourseDescription liveCourseDescription = new LiveCourseDescription();
                liveCourseDescription.setDescription(liveCourseVo.getDescription());
                liveCourseDescription.setLiveCourseId(liveCourse.getId());
                liveCourseDescriptionService.save(liveCourseDescription);
                //添加直播账号信息
                LiveCourseAccount liveCourseAccount = new LiveCourseAccount();
                liveCourseAccount.setLiveCourseId(liveCourse.getId());
                liveCourseAccount.setZhuboAccount(object.getString("bid"));
                liveCourseAccount.setZhuboPassword(liveCourseVo.getPassword());
                liveCourseAccount.setAdminKey(object.getString("admin_key"));
                liveCourseAccount.setUserKey(object.getString("user_key"));
                liveCourseAccount.setZhuboKey(object.getString("zhubo_key"));
                liveCourseAccountService.save(liveCourseAccount);
            }else {
                System.out.println(commonResult.getmsg());
                throw new Exception("创建失败");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //删除直播
    @Override
    public void removeLive(Long id) {
        //根据id查询直播课程信息
        LiveCourse liveCourse = baseMapper.selectById(id);
        if(liveCourse != null) {
            //获取直播courseid
            Long courseId = liveCourse.getCourseId();
            try {
                //调用方法删除平台直播课程
                mtCloudClient.courseDelete(courseId.toString());
                //删除表数据
                baseMapper.deleteById(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //查询和更新
    @Override
    public LiveCourseFormVo getLiveCourseFormVo(Long id) {
        //获取课程基本信息
        final LiveCourse liveCourse = baseMapper.selectById(id);
        //获取描述
        final LiveCourseDescription liveCourseDescription = liveCourseDescriptionService.getLiveCourseById(id);
        //封装
        final LiveCourseFormVo liveCourseFormVo = new LiveCourseFormVo();
        BeanUtils.copyProperties(liveCourse,liveCourseFormVo);
        liveCourseFormVo.setDescription(liveCourseDescription.getDescription());
        return liveCourseFormVo;
    }

    @Override
    public void updateLiveById(LiveCourseFormVo liveCourseVo) {
        //根据id获取直播课程基本信息
        LiveCourse liveCourse = baseMapper.selectById(liveCourseVo.getId());
        BeanUtils.copyProperties(liveCourseVo,liveCourse);
        //讲师
        Teacher teacher =
                courseFeignClient.getTeacherLive(liveCourseVo.getTeacherId());
        HashMap<Object, Object> options = new HashMap<>();
        try {
            String res = mtCloudClient.courseUpdate(liveCourse.getCourseId().toString(),
                    teacher.getId().toString(),
                    liveCourse.getCourseName(),
                    new DateTime(liveCourse.getStartTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    new DateTime(liveCourse.getEndTime()).toString("yyyy-MM-dd HH:mm:ss"),
                    teacher.getName(),
                    teacher.getIntro(),
                    options);
            //返回结果转换，判断是否成功
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) == MTCloud.CODE_SUCCESS) {
                JSONObject object = commonResult.getData();
                //更新直播课程基本信息
                liveCourse.setCourseId(object.getLong("course_id"));
                baseMapper.updateById(liveCourse);
                //直播课程描述信息更新
                LiveCourseDescription liveCourseDescription =
                        liveCourseDescriptionService.getLiveCourseById(liveCourse.getId());
                liveCourseDescription.setDescription(liveCourseVo.getDescription());
                liveCourseDescriptionService.updateById(liveCourseDescription);
            } else {
                throw new Exception("修改直播课程失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取直播配置信息
    @Override
    public LiveCourseConfigVo getCourseConfig(Long id) {
        //根据id查询配置信息
        LiveCourseConfig liveCourseConfig = liveCourseConfigService.getCourseConfigCourseId(id);
        //封装
        final LiveCourseConfigVo liveCourseConfigVo = new LiveCourseConfigVo();
        if (liveCourseConfig != null){
            //查询商品列表
            List<LiveCourseGoods> liveCourseGoodsList = liveCourseGoodsService.getGoodsListCourseId(id);
            //封装
            BeanUtils.copyProperties(liveCourseConfig,liveCourseConfigVo);
            liveCourseConfigVo.setLiveCourseGoodsList(liveCourseGoodsList);
        }
        return liveCourseConfigVo;
    }

    //修改直播配置
    @Override
    public void updateConfig(LiveCourseConfigVo liveCourseConfigVo) {
        //修改配置
        LiveCourseConfig liveCourseConfigUpt = new LiveCourseConfig();
        BeanUtils.copyProperties(liveCourseConfigVo, liveCourseConfigUpt);
        if(null == liveCourseConfigVo.getId()) {
            liveCourseConfigService.save(liveCourseConfigUpt);
        } else {
            liveCourseConfigService.updateById(liveCourseConfigUpt);
        }
        //修改商品
        liveCourseGoodsService.remove(new LambdaQueryWrapper<LiveCourseGoods>().eq(LiveCourseGoods::getLiveCourseId, liveCourseConfigVo.getLiveCourseId()));
        if(!CollectionUtils.isEmpty(liveCourseConfigVo.getLiveCourseGoodsList())) {
            liveCourseGoodsService.saveBatch(liveCourseConfigVo.getLiveCourseGoodsList());
        }
        //修改直播平台
        this.updateLifeConfig(liveCourseConfigVo);
    }

    private void updateLifeConfig(LiveCourseConfigVo liveCourseConfigVo) {
        //获取直播课程基本信息
        final LiveCourse liveCourse = baseMapper.selectById(liveCourseConfigVo.getLiveCourseId());
        //封装平台需要的参数
        //参数设置
        HashMap<Object,Object> options = new HashMap<Object, Object>();
        //界面模式
        options.put("pageViewMode", liveCourseConfigVo.getPageViewMode());
        //观看人数开关
        JSONObject number = new JSONObject();
        number.put("enable", liveCourseConfigVo.getNumberEnable());
        options.put("number", number.toJSONString());
        //观看人数开关
        JSONObject store = new JSONObject();
        number.put("enable", liveCourseConfigVo.getStoreEnable());
        number.put("type", liveCourseConfigVo.getStoreType());
        options.put("store", number.toJSONString());
        //商城列表
        List<LiveCourseGoods> liveCourseGoodsList = liveCourseConfigVo.getLiveCourseGoodsList();
        if(!CollectionUtils.isEmpty(liveCourseGoodsList)) {
            List<LiveCourseGoodsView> liveCourseGoodsViewList = new ArrayList<>();
            for(LiveCourseGoods liveCourseGoods : liveCourseGoodsList) {
                LiveCourseGoodsView liveCourseGoodsView = new LiveCourseGoodsView();
                BeanUtils.copyProperties(liveCourseGoods, liveCourseGoodsView);
                liveCourseGoodsViewList.add(liveCourseGoodsView);
            }
            JSONObject goodsListEdit = new JSONObject();
            goodsListEdit.put("status", "0");
            options.put("goodsListEdit ", goodsListEdit.toJSONString());
            options.put("goodsList", JSON.toJSONString(liveCourseGoodsViewList));
        }

        try {
            final String res = mtCloudClient.courseUpdateLifeConfig(
                    liveCourse.getCourseId().toString(),
                    options);
            CommonResult<JSONObject> commonResult = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(commonResult.getCode()) != MTCloud.CODE_SUCCESS) {
                throw new Exception("修改配置信息失败");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //获取最近5个直播信息
    @Override
    public List<LiveCourseVo> getLatelyList() {
        List<LiveCourseVo> liveCourseVoList = baseMapper.getLatelyList();

        for(LiveCourseVo liveCourseVo : liveCourseVoList) {
            //封装开始和结束时间
            liveCourseVo.setStartTimeString(new DateTime(liveCourseVo.getStartTime()).toString("yyyy年MM月dd HH:mm"));
            liveCourseVo.setEndTimeString(new DateTime(liveCourseVo.getEndTime()).toString("HH:mm"));
            //封装讲师
            final Long teacherId = liveCourseVo.getTeacherId();
            liveCourseVo.setTeacher(courseFeignClient.getTeacherLive(teacherId));
            //封装直播状态
            liveCourseVo.setLiveStatus(this.getLiveStatus(liveCourseVo));
        }
        return liveCourseVoList;
    }

    private Integer getLiveStatus(LiveCourseVo liveCourseVo) {
        // 直播状态 0：未开始 1：直播中 2：直播结束
        int liveStatus = 0;
        Date curTime = new Date();
        if(DateUtil.dateCompare(curTime, liveCourseVo.getStartTime())) {
            liveStatus = 0;
        } else if(DateUtil.dateCompare(curTime, liveCourseVo.getEndTime())) {
            liveStatus = 1;
        } else {
            liveStatus = 2;
        }
        return liveStatus;
    }

    private Integer getLiveStatus(LiveCourse liveCourse) {
        // 直播状态 0：未开始 1：直播中 2：直播结束
        int liveStatus = 0;
        Date curTime = new Date();
        if(DateUtil.dateCompare(curTime, liveCourse.getStartTime())) {
            liveStatus = 0;
        } else if(DateUtil.dateCompare(curTime, liveCourse.getEndTime())) {
            liveStatus = 1;
        } else {
            liveStatus = 2;
        }
        return liveStatus;
    }

    //获取用户access_token
    //为测试可能userId为空的情况，参照orderInfoServiceImpl的写法
    @Override
    public JSONObject getAccessToken(Long id, Long userId) {
        //获得课程信息
        final LiveCourse liveCourse = baseMapper.selectById(id);
        //获取用户信息
        final UserInfo userInfo = userInfoFeignClient.getById(userId);
        try {
            //封装平台需要的参数
            /**
             * 课程id
             * 用户id
             * 用户昵称
             * 用户角色
             * 有效期
             * 其他
             */
            HashMap<Object, Object> options = new HashMap<Object, Object>();
            final String res = mtCloudClient.courseAccess(
                    liveCourse.getCourseId().toString(),
                    userId.toString(),
                    userInfo.getNickName(),
                    MTCloud.ROLE_USER,
                    80 * 80 * 80,
                    options);
            final CommonResult<JSONObject> result = JSON.parseObject(res, CommonResult.class);
            if(Integer.parseInt(result.getCode()) == MTCloud.CODE_SUCCESS) {
                JSONObject object = result.getData();
                System.out.println("access::"+object.getString("access_token"));
                return object;
            } else {
                throw new Exception("获取失败");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //根据id得到直播详情信息
    @Override
    public Map<String, Object> getInfoById(Long courseId) {
        LiveCourse liveCourse = this.getById(courseId);
        liveCourse.getParam().put("startTimeString", new DateTime(liveCourse.getStartTime()).toString("yyyy年MM月dd HH:mm"));
        liveCourse.getParam().put("endTimeString", new DateTime(liveCourse.getEndTime()).toString("yyyy年MM月dd HH:mm"));
        Teacher teacher = courseFeignClient.getTeacherLive(liveCourse.getTeacherId());
        LiveCourseDescription liveCourseDescription = liveCourseDescriptionService.getLiveCourseById(courseId);

        Map<String, Object> map = new HashMap<>();
        map.put("liveCourse", liveCourse);
        map.put("liveStatus", this.getLiveStatus(liveCourse));
        map.put("teacher", teacher);
        if(null != liveCourseDescription) {
            map.put("description", liveCourseDescription.getDescription());
        } else {
            map.put("description", "");
        }
        return map;
    }
}
