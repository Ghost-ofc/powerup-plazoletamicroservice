package com.pragma.powerup.infrastructure.input.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.domain.exception.InvalidRestaurantNameException;
import com.pragma.powerup.domain.exception.RestaurantAlreadyExistsException;
import com.pragma.powerup.domain.exception.UserNotOwnerException;
import com.pragma.powerup.infrastructure.exceptionhandler.ControllerAdvisor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class RestaurantRestControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private IRestaurantHandler restaurantHandler;

    @InjectMocks
    private RestaurantRestController restaurantRestController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(restaurantRestController)
                .setControllerAdvice(new ControllerAdvisor())
                .build();
    }

    @Test
    @DisplayName("Debe responder 201 Created al registrar un restaurante válido")
    void saveRestaurant_whenValidPayload_shouldReturn201() throws Exception {
        RestaurantRequestDto requestDto = new RestaurantRequestDto(
                "Restaurante El Gourmet",
                "900123456",
                "Calle 100 # 15-20",
                "+573005698325",
                "https://imagenes.com/logo.png",
                2L
        );

        doNothing().when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        mockMvc.perform(post("/api/v1/restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Debe responder 400 Bad Request cuando el usuario no es propietario")
    void saveRestaurant_whenUserNotOwner_shouldReturn400() throws Exception {
        RestaurantRequestDto requestDto = new RestaurantRequestDto(
                "Restaurante El Gourmet",
                "900123456",
                "Calle 100 # 15-20",
                "+573005698325",
                "https://imagenes.com/logo.png",
                99L
        );

        doThrow(new UserNotOwnerException("The user is not an owner"))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        mockMvc.perform(post("/api/v1/restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("The user is not an owner"));
    }

    @Test
    @DisplayName("Debe responder 400 Bad Request cuando el nombre es solo numérico")
    void saveRestaurant_whenNameOnlyNumbers_shouldReturn400() throws Exception {
        RestaurantRequestDto requestDto = new RestaurantRequestDto(
                "123456",
                "900123456",
                "Calle 100 # 15-20",
                "+573005698325",
                "https://imagenes.com/logo.png",
                2L
        );

        mockMvc.perform(post("/api/v1/restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Debe responder 409 Conflict cuando el NIT ya existe")
    void saveRestaurant_whenNitAlreadyExists_shouldReturn409() throws Exception {
        RestaurantRequestDto requestDto = new RestaurantRequestDto(
                "Restaurante El Gourmet",
                "900123456",
                "Calle 100 # 15-20",
                "+573005698325",
                "https://imagenes.com/logo.png",
                2L
        );

        doThrow(new RestaurantAlreadyExistsException("A restaurant with this NIT already exists"))
                .when(restaurantHandler).saveRestaurant(any(RestaurantRequestDto.class));

        mockMvc.perform(post("/api/v1/restaurant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("A restaurant with this NIT already exists"));
    }
}
