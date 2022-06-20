package com.liubusi.packagehub.sms.controller.api;

import com.liubusi.packagehub.common.pojo.vo.EmailVO;
import com.liubusi.packagehub.common.result.Result;
import com.liubusi.packagehub.sms.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "发送邮件验证码")
@RestController
@RequestMapping("/api/sms/email")
@Slf4j
public class ApiEmailController {
    @Resource
    private EmailService emailService;

    @ApiOperation("发送邮件")
    @GetMapping("/send/{email}")
    public Result send(
            @ApiParam(value = "邮件内容")
            @PathVariable String email) {
        emailService.send(email);
        return Result.ok().message("发送成功");
    }
}
