package com.otterdev.sea_salt.config.r2dbc;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.mapping.event.BeforeConvertCallback;

import com.otterdev.sea_salt.entity.User;

import reactor.core.publisher.Mono;

@Configuration
public class R2dbcConfig {

    @Bean
    public BeforeConvertCallback<User> beforeConvertCallback() {
        return (entity, table) -> {
            if (entity.getId() == null) {
                entity.setId(UUID.randomUUID()); // Set UUID directly
            }
            if (entity.getCreatedAt() == null) {
                entity.setCreatedAt(LocalDateTime.now());
            }
            entity.setUpdatedAt(LocalDateTime.now());
            return Mono.just(entity);
        };
    }
}
