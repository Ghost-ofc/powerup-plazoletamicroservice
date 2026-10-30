package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        RestaurantEntity entity = restaurantEntityMapper.toEntity(restaurantModel);
        restaurantRepository.save(entity);
    }

    @Override
    public RestaurantModel findByNit(String nit) {
        return restaurantRepository.findByNit(nit)
                .map(restaurantEntityMapper::toModel)
                .orElse(null);
    }

    @Override
    public RestaurantModel findById(Long id) {
        return restaurantRepository.findById(id)
                .map(restaurantEntityMapper::toModel)
                .orElse(null);
    }

    @Override
    public boolean existsByNit(String nit) {
        return Boolean.TRUE.equals(restaurantRepository.existsByNit(nit));
    }
}
