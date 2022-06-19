package com.liubusi.packagehub.core.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liubusi.packagehub.common.pojo.entity.User;
import com.liubusi.packagehub.common.pojo.vo.UserAuthVO;
import com.liubusi.packagehub.common.pojo.vo.UserInfoVO;
import com.liubusi.packagehub.common.pojo.vo.UserVO;
import com.liubusi.packagehub.common.result.Result;
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
@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/core/user")
@Slf4j
public class AdminUserController {

    @Resource
    private UserService userService;

    @ApiOperation("登录")
    @PostMapping("/login")
    public Result login(
            @ApiParam(value = "用户登录表单")
            @RequestBody UserVO userVO) {
        String token = userService.login(userVO);
        return Result.ok().message("登录成功").data("token", token);
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/info")
    public Result getInfo(
            @ApiParam(value = "登录token", required = true)
            @RequestParam String token) {
        log.info("获取userinfo使用的token：" + token);
        UserInfoVO userInfo = userService.getInfo(token);
        return Result.ok().data("userInfo", userInfo);
    }

    @ApiOperation("登出")
    @PostMapping("/logout")
    public Result logout(
            @ApiParam(value = "用户登出")
            @RequestBody UserInfoVO userInfoVO) {
        log.info("用户登出了：" + userInfoVO.getToken());
        return Result.ok().message("已登出");
    }

    @ApiOperation("获取用户分页列表")
    @GetMapping("/selectPage/{current}/{size}")
    public Result selectPage(
            @ApiParam(value = "当前页", required = true)
            @PathVariable Integer current,
            @ApiParam(value = "每页数量", required = true)
            @PathVariable Integer size) {
        Page<User> userPage = userService.selectPage(current, size);
        return Result.ok().data("User", userPage);
    }

    @ApiOperation("获取单个用户")
    @GetMapping("/getOne/{id}")
    public Result getOne(
            @ApiParam(value = "用户id", required = true)
            @PathVariable Long id) {
        User user = userService.getById(id);
        return Result.ok().data("User", user);
    }

    @ApiOperation("新增用户")
    @PostMapping("/save")
    public Result save(
            @ApiParam(value = "用户数据")
            @RequestBody UserVO userVO) {
        User user = new User();
        log.info("新增用户信息" + userVO.toString());
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

    @ApiOperation("给用户授权项目")
    @PostMapping("/auth")
    public Result auth(
            @ApiParam(value = "用户授权列表")
            @RequestBody UserAuthVO userAuthVO) {
        userService.auth(userAuthVO);
        return Result.ok().message("授权成功");
    }

}

