package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantJpaAdapterTest {

    @Mock
    private IRestaurantRepository restaurantRepository;

    @Mock
    private IRestaurantEntityMapper restaurantEntityMapper;

    @InjectMocks
    private RestaurantJpaAdapter restaurantJpaAdapter;

    @Test
    @DisplayName("Debe guardar un restaurante a través del repositorio")
    void saveRestaurant_shouldMapAndSave() {
        RestaurantModel model = new RestaurantModel();
        RestaurantEntity entity = new RestaurantEntity();

        when(restaurantEntityMapper.toEntity(model)).thenReturn(entity);

        restaurantJpaAdapter.saveRestaurant(model);

        verify(restaurantEntityMapper).toEntity(model);
        verify(restaurantRepository).save(entity);
    }

    @Test
    @DisplayName("Debe buscar restaurante por NIT")
    void findByNit_shouldReturnRestaurant() {
        RestaurantEntity entity = new RestaurantEntity();
        RestaurantModel model = new RestaurantModel();

        when(restaurantRepository.findByNit("900123456")).thenReturn(Optional.of(entity));
        when(restaurantEntityMapper.toModel(entity)).thenReturn(model);

        RestaurantModel result = restaurantJpaAdapter.findByNit("900123456");

        assertNotNull(result);
        verify(restaurantRepository).findByNit("900123456");
    }

    @Test
    @DisplayName("Debe buscar restaurante por ID")
    void findById_shouldReturnRestaurant() {
        RestaurantEntity entity = new RestaurantEntity();
        RestaurantModel model = new RestaurantModel();

        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(restaurantEntityMapper.toModel(entity)).thenReturn(model);

        RestaurantModel result = restaurantJpaAdapter.findById(1L);

        assertNotNull(result);
        verify(restaurantRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe verificar existencia por NIT")
    void existsByNit_shouldReturnBoolean() {
        when(restaurantRepository.existsByNit("900123456")).thenReturn(true);

        assertTrue(restaurantJpaAdapter.existsByNit("900123456"));
    }
}
