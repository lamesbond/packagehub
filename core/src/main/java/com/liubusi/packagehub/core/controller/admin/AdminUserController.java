package com.liubusi.packagehub.core.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liubusi.packagehub.common.result.Result;
import com.liubusi.packagehub.common.pojo.entity.User;
import com.liubusi.packagehub.core.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
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

    @ApiOperation("获取用户分页列表")
    @GetMapping("/getPage/{current}/{size}")
    public Result getPage(
            @ApiParam(value = "当前页", required = true)
            @PathVariable Integer current,
            @ApiParam(value = "每页数量", required = true)
            @PathVariable Integer size) {
        Page<User> userPage = userService.login(current, size);
        return Result.ok().data("User", userPage);
    }


}

