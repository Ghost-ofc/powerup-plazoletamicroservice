package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.RestaurantModel;

public interface IRestaurantPersistencePort {
    void saveRestaurant(RestaurantModel restaurantModel);
    RestaurantModel findByNit(String nit);
    RestaurantModel findById(Long id);
    boolean existsByNit(String nit);
}
