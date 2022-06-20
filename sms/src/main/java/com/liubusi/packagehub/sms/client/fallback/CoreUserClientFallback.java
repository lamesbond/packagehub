package com.liubusi.packagehub.sms.client.fallback;

import com.liubusi.packagehub.sms.client.CoreUserClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CoreUserClientFallback implements CoreUserClient {
    @Override
    public boolean checkEmail(String email) {
        log.error("远程调用失败，服务熔断");
        return false;
    }
}
