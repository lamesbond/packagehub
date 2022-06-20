package com.liubusi.packagehub.sms.service;

import com.liubusi.packagehub.common.pojo.vo.EmailVO;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
public interface EmailService {

    void send(EmailVO emailVO);

    void send(String email);
}
