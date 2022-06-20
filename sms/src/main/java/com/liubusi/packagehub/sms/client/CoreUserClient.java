package com.liubusi.packagehub.sms.client;

import com.liubusi.packagehub.sms.client.fallback.CoreUserClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "core", fallback = CoreUserClientFallback.class)
public interface CoreUserClient {

    @GetMapping("/api/core/userInfo/checkEmail/{email}")
    boolean checkEmail(@PathVariable String email);
}