package com.pragma.powerup.infrastructure.out.client;

import com.pragma.powerup.infrastructure.out.client.dto.UserClientDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "${services.users.url}")
public interface IUserFeignClient {

    @GetMapping("/{id}")
    UserClientDto getUserById(@PathVariable("id") Long id);
}
