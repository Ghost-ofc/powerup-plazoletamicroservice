package com.pragma.powerup.infrastructure.out.client.adapter;

import com.pragma.powerup.infrastructure.out.client.IUserFeignClient;
import com.pragma.powerup.infrastructure.out.client.dto.UserClientDto;
import feign.FeignException;
import feign.Request;
import feign.RequestTemplate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserClientAdapterTest {

    @Mock
    private IUserFeignClient userFeignClient;

    @InjectMocks
    private UserClientAdapter userClientAdapter;

    @Test
    @DisplayName("Debe retornar true si el usuario tiene rol ROLE_PROPIETARIO")
    void isUserOwner_whenRoleIsRolePropietario_shouldReturnTrue() {
        UserClientDto user = new UserClientDto(2L, "Carlos", "Gomez", "carlos@correo.com", "ROLE_PROPIETARIO");
        when(userFeignClient.getUserById(2L)).thenReturn(user);

        boolean result = userClientAdapter.isUserOwner(2L);

        assertTrue(result);
    }

    @Test
    @DisplayName("Debe retornar true si el usuario tiene rol PROPIETARIO")
    void isUserOwner_whenRoleIsPropietario_shouldReturnTrue() {
        UserClientDto user = new UserClientDto(2L, "Carlos", "Gomez", "carlos@correo.com", "PROPIETARIO");
        when(userFeignClient.getUserById(2L)).thenReturn(user);

        boolean result = userClientAdapter.isUserOwner(2L);

        assertTrue(result);
    }

    @Test
    @DisplayName("Debe retornar false si el usuario tiene otro rol")
    void isUserOwner_whenRoleNotOwner_shouldReturnFalse() {
        UserClientDto user = new UserClientDto(3L, "Juan", "Perez", "juan@correo.com", "ROLE_CLIENTE");
        when(userFeignClient.getUserById(3L)).thenReturn(user);

        boolean result = userClientAdapter.isUserOwner(3L);

        assertFalse(result);
    }

    @Test
    @DisplayName("Debe retornar false si userId es nulo")
    void isUserOwner_whenUserIdNull_shouldReturnFalse() {
        assertFalse(userClientAdapter.isUserOwner(null));
    }

    @Test
    @DisplayName("Debe retornar false si FeignClient lanza FeignException (usuario no encontrado)")
    void isUserOwner_whenFeignThrowsException_shouldReturnFalse() {
        Request request = Request.create(Request.HttpMethod.GET, "/api/v1/user/99", Collections.emptyMap(), null, new RequestTemplate());
        when(userFeignClient.getUserById(99L)).thenThrow(new FeignException.NotFound("Not Found", request, null, null));

        boolean result = userClientAdapter.isUserOwner(99L);

        assertFalse(result);
    }
}
