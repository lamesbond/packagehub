package com.liubusi.packagehub.sms.service.impl;

import com.liubusi.packagehub.common.exception.Assert;
import com.liubusi.packagehub.common.pojo.vo.EmailVO;
import com.liubusi.packagehub.common.result.ResponseEnum;
import com.liubusi.packagehub.common.result.Result;
import com.liubusi.packagehub.common.util.RandomUtils;
import com.liubusi.packagehub.common.util.RegexValidateUtils;
import com.liubusi.packagehub.sms.client.CoreUserClient;
import com.liubusi.packagehub.sms.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Resource
    private MailSender mailSender;

    @Resource
    private CoreUserClient coreUserClient;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void send(EmailVO emailVO) {
// new 一个简单邮件消息对象
        SimpleMailMessage message = new SimpleMailMessage();
        // 和配置文件中的的username相同，相当于发送方
        message.setFrom("lamesbond@163.com");
        // 收件人邮箱
        message.setTo(emailVO.getAddress());
        // 标题
        message.setSubject(emailVO.getTitle());
        // 正文
        message.setText(emailVO.getContent());
        // 发送
        mailSender.send(message);
    }

    @Override
    public void send(String email) {
        Assert.notEmpty(email, ResponseEnum.EMAIL_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkEmail(email), ResponseEnum.EMAIL_ERROR);

        //邮箱是否注册
        boolean result = coreUserClient.checkEmail(email);
        System.out.println("result = " + result);
        Assert.isTrue(result == true, ResponseEnum.EMAIL_EXIST_ERROR);

        String code = (String)redisTemplate.opsForValue().get("packagehub:sms:code:" + email);
        if(!StringUtils.isEmpty(code)) {
            return ;
        }
        //如果从redis获取不到，
        // 生成验证码，
        code = RandomUtils.getSixBitRandom();
        System.out.println("生成的验证码是：" + code);
        //调用send方法，通过整合短信服务进行发送
        EmailVO emailVO = new EmailVO();
        emailVO.setAddress(email);
        emailVO.setContent(code);
        emailVO.setTitle("用户注册验证码发送");
        this.send(emailVO);
        //生成验证码放到redis里面，设置有效时间

        redisTemplate.opsForValue().set("packagehub:sms:code:" + email, code,15, TimeUnit.MINUTES);
    }
}
