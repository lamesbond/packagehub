package com.liubusi.packagehub.core.controller.api;

import com.liubusi.packagehub.common.exception.Assert;
import com.liubusi.packagehub.common.pojo.vo.UserVO;
import com.liubusi.packagehub.common.result.ResponseEnum;
import com.liubusi.packagehub.common.result.Result;
import com.liubusi.packagehub.common.util.JwtUtils;
import com.liubusi.packagehub.common.util.RegexValidateUtils;
import com.liubusi.packagehub.core.service.ApiUserService;
import com.liubusi.packagehub.core.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
@RequestMapping("/api/core/user")
@Slf4j
public class ApiUserController {

    @Resource
    private UserService userService;

    @Resource
    private ApiUserService apiUserService;

    @ApiOperation("会员注册")
    @PostMapping("/register")
    public Result register(@RequestBody UserVO userVO){
        String email = userVO.getEmail();
        String password = userVO.getPassword();

        //MOBILE_NULL_ERROR(-202, "邮箱不能为空"),
        Assert.notEmpty(email, ResponseEnum.EMAIL_NULL_ERROR);
        //MOBILE_ERROR(-203, "手机号不正确"),
        Assert.isTrue(RegexValidateUtils.checkEmail(email), ResponseEnum.EMAIL_ERROR);
        //PASSWORD_NULL_ERROR(-204, "密码不能为空"),
        Assert.notEmpty(password, ResponseEnum.PASSWORD_NULL_ERROR);

        //注册
        apiUserService.register(userVO);
        return Result.ok().message("注册成功");
    }

    @ApiOperation("网站用户登录")
    @PostMapping("/login")
    public Result login(@RequestBody UserVO userVO, HttpServletRequest request){
        String email = userVO.getEmail();
        String password = userVO.getPassword();

        Assert.notEmpty(email, ResponseEnum.EMAIL_NULL_ERROR);
        Assert.notEmpty(password, ResponseEnum.PASSWORD_NULL_ERROR);

        String ip = request.getRemoteAddr();
        UserVO userVO1 = apiUserService.login(userVO, ip);

        return Result.ok().data("userInfo", userVO1);
    }

    @ApiOperation("校验令牌")
    @GetMapping("/checkToken")
    public Result checkToken(HttpServletRequest request) {

        String token = request.getHeader("token");
        boolean result = JwtUtils.checkToken(token);

        if(result){
            return Result.ok();
        }else{
            //LOGIN_AUTH_ERROR(-211, "未登录"),
            return Result.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
        }
    }

    @ApiOperation("校验邮箱是否注册")
    @GetMapping("/checkEmail/{email}")
    public boolean checkEmail(@PathVariable String email) {
        return apiUserService.checkEmail(email);
    }
}

