package com.liubusi.packagehub.sms.controller.admin;

import com.liubusi.packagehub.common.pojo.vo.EmailVO;
import com.liubusi.packagehub.common.result.Result;
import com.liubusi.packagehub.sms.service.EmailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Api(tags = "邮件发送管理")
@RestController
@RequestMapping("/admin/sms/email")
@Slf4j
public class AdminEmailController {
    @Resource
    private EmailService emailService;

    @ApiOperation("发送邮件")
    @PostMapping("/send")
    public Result send(
            @ApiParam(value = "邮件内容")
            @RequestBody EmailVO emailVO) {
        emailService.send(emailVO);
        return Result.ok().message("发送成功");
    }
}
