package com.projects.planner.todo.feign;

import com.projects.planner.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "planner-users", fallback = UserFeignClientFallback.class)
public interface UserFeignClient {

    @PostMapping("/user/id")
    ResponseEntity<User> findUserById(@RequestBody Long id);

}

@Component
class UserFeignClientFallback implements UserFeignClient {

    // Вызывается при недоступности микросервиса planner-users
    @Override
    public ResponseEntity<User> findUserById(Long id) {
        return null;
    }
}