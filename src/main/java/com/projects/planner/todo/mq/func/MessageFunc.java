package com.projects.planner.todo.mq.func;

import com.projects.planner.todo.service.NewUserDefaultDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
public class MessageFunc {

    private final NewUserDefaultDataService dataService;

    @Bean
    public Consumer<Message<Long>> setDefaultUserDataConsume() {
        return message -> dataService.setDefaultUserData(message.getPayload());
    }

}
