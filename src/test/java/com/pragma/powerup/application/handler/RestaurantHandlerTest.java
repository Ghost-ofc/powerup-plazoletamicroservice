package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.impl.RestaurantHandler;
import com.pragma.powerup.application.mapper.IRestaurantRequestMapper;
import com.pragma.powerup.application.mapper.IRestaurantResponseMapper;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.RestaurantModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantHandlerTest {

    @Mock
    private IRestaurantServicePort restaurantServicePort;

    @Mock
    private IRestaurantRequestMapper restaurantRequestMapper;

    @Mock
    private IRestaurantResponseMapper restaurantResponseMapper;

    @InjectMocks
    private RestaurantHandler restaurantHandler;

    @Test
    @DisplayName("Debe mapear DTO y llamar a saveRestaurant en el servicio de dominio")
    void saveRestaurant_shouldMapAndCallDomainService() {
        RestaurantRequestDto requestDto = new RestaurantRequestDto(
                "Restaurante El Gourmet", "900123456", "Calle 100", "+573005698325", "http://logo.png", 2L
        );
        RestaurantModel model = new RestaurantModel();

        when(restaurantRequestMapper.toModel(requestDto)).thenReturn(model);

        restaurantHandler.saveRestaurant(requestDto);

        verify(restaurantRequestMapper).toModel(requestDto);
        verify(restaurantServicePort).saveRestaurant(model);
    }

    @Test
    @DisplayName("Debe consultar por ID y mapear a response DTO")
    void getById_shouldFindAndMapToResponseDto() {
        RestaurantModel model = new RestaurantModel(
                1L, "Restaurante El Gourmet", "900123456", "Calle 100", "+573005698325", "http://logo.png", 2L
        );
        RestaurantResponseDto responseDto = new RestaurantResponseDto(
                1L, "Restaurante El Gourmet", "900123456", "Calle 100", "+573005698325", "http://logo.png", 2L
        );

        when(restaurantServicePort.findById(1L)).thenReturn(model);
        when(restaurantResponseMapper.toResponse(model)).thenReturn(responseDto);

        RestaurantResponseDto result = restaurantHandler.getById(1L);

        assertNotNull(result);
        assertEquals("900123456", result.getNit());
        verify(restaurantServicePort).findById(1L);
        verify(restaurantResponseMapper).toResponse(model);
    }
}
