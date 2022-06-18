package com.liubusi.packagehub.sms.service.impl;

import com.liubusi.packagehub.common.pojo.vo.EmailVO;
import com.liubusi.packagehub.sms.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


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
}
