package com.blossom.ggkt.activity.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blossom.ggkt.activity.service.CouponInfoService;
import com.blossom.ggkt.model.activity.CouponInfo;
import com.blossom.ggkt.model.activity.CouponUse;
import com.blossom.ggkt.result.Result;
import com.blossom.ggkt.vo.activity.CouponUseQueryVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 优惠券信息 前端控制器
 * </p>
 *
 * @author blossom
 * @since 2023-07-19
 */
@RestController
@RequestMapping("/admin/activity/couponInfo")
public class CouponInfoController {
    @Resource
    private CouponInfoService couponInfoService;

    //优惠卷分页
    @GetMapping("{page}/{limit}")
    public Result<IPage<CouponInfo>> index(
                        @PathVariable Long page,
                        @PathVariable Long limit){
        Page<CouponInfo> pageParam = new Page<>(page, limit);
        IPage<CouponInfo> pageModel = couponInfoService.page(pageParam);
        return Result.ok(pageModel);
    }

    //优惠卷的增删改查
    @GetMapping("get/{id}")
    public Result<CouponInfo> get(@PathVariable String id) {
        CouponInfo couponInfo = couponInfoService.getById(id);
        return Result.ok(couponInfo);
    }

    @PostMapping("save")
    public Result<Object> save(@RequestBody CouponInfo couponInfo) {
        couponInfoService.save(couponInfo);
        return Result.ok(null);
    }

    @PutMapping("update")
    public Result<Object> updateById(@RequestBody CouponInfo couponInfo) {
        couponInfoService.updateById(couponInfo);
        return Result.ok(null);
    }

    @DeleteMapping("remove/{id}")
    public Result<Object>  remove(@PathVariable String id) {
        couponInfoService.removeById(id);
        return Result.ok(null);
    }

    @DeleteMapping("batchRemove")
    public Result<Object> batchRemove(@RequestBody List<String> idList){
        couponInfoService.removeByIds(idList);
        return Result.ok(null);
    }

    //获取已使用的优惠卷分页----每页数，总数，查询对象
    @GetMapping("couponUse/{page}/{limit}")
    public Result<IPage<CouponUse>> index(
            @PathVariable Long page,
            @PathVariable Long limit,
            CouponUseQueryVo couponUseQueryVo){
        Page<CouponUse> pageParam = new Page<>(page, limit);
        IPage<CouponUse> pageModel = couponInfoService.selectCouponUsePage(pageParam,couponUseQueryVo);
        return Result.ok(pageModel);
    }
}

