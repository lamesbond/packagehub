package com.liubusi.packagehub.core.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liubusi.packagehub.common.result.Result;
import com.liubusi.packagehub.core.pojo.entity.User;
import com.liubusi.packagehub.core.pojo.vo.UserVO;
import com.liubusi.packagehub.core.service.UserAuthService;
import com.liubusi.packagehub.core.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 字典管理表 前端控制器
 * </p>
 *
 * @author liubusi
 * @since 2022-05-22
 */
@Api(tags = "用户授权")
@RestController
@RequestMapping("/admin/core/userAuth")
@Slf4j
public class AdminUserAuthController {

    @Resource
    private UserAuthService userAuthService;

    @ApiOperation("给用户授权项目")
    @PostMapping("/save")
    public Result save(
            @ApiParam(value = "用户数据")
            @RequestBody UserVO userVO) {
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        userService.save(user);
        return Result.ok().message("新增成功");
    }

    @ApiOperation("删除用户")
    @DeleteMapping("/remove/{id}")
    public Result remove(
            @ApiParam(value = "用户id", required = true)
            @PathVariable Long id) {
        userService.removeById(id);
        return Result.ok().message("删除成功");
    }

    @ApiOperation("根据用户ID修改数据等")
    @PostMapping("/update")
    public Result update(
            @ApiParam(value = "用户id", required = true)
            @RequestBody UserVO userVO) {
        User user = new User();
        BeanUtils.copyProperties(userVO, user);
        userService.updateById(user);
        return Result.ok().message("修改成功");
    }

}

