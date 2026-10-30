package com.pragma.powerup.infrastructure.configuration;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserClientPort;
import com.pragma.powerup.domain.usecase.RestaurantUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public IRestaurantServicePort restaurantServicePort(IRestaurantPersistencePort restaurantPersistencePort,
                                                        IUserClientPort userClientPort) {
        return new RestaurantUseCase(restaurantPersistencePort, userClientPort);
    }
}
