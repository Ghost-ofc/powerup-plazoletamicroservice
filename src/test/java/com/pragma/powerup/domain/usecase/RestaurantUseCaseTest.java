package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.InvalidNitException;
import com.pragma.powerup.domain.exception.InvalidPhoneException;
import com.pragma.powerup.domain.exception.InvalidRestaurantNameException;
import com.pragma.powerup.domain.exception.RequiredFieldException;
import com.pragma.powerup.domain.exception.RestaurantAlreadyExistsException;
import com.pragma.powerup.domain.exception.UserNotOwnerException;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserClientPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantUseCaseTest {

    @Mock
    private IRestaurantPersistencePort restaurantPersistencePort;

    @Mock
    private IUserClientPort userClientPort;

    @InjectMocks
    private RestaurantUseCase restaurantUseCase;

    private RestaurantModel validRestaurant;

    @BeforeEach
    void setUp() {
        validRestaurant = new RestaurantModel(
                null,
                "Restaurante El Gourmet 10",
                "900123456",
                "Calle 100 # 15-20",
                "+573005698325",
                "https://images.com/logo.png",
                2L
        );
    }

    @Test
    @DisplayName("Debe guardar el restaurante exitosamente cuando todos los datos son válidos")
    void saveRestaurant_whenValidData_shouldSaveSuccessfully() {
        when(restaurantPersistencePort.existsByNit(validRestaurant.getNit())).thenReturn(false);
        when(userClientPort.isUserOwner(validRestaurant.getIdPropietario())).thenReturn(true);

        restaurantUseCase.saveRestaurant(validRestaurant);

        verify(restaurantPersistencePort).saveRestaurant(validRestaurant);
    }

    @ParameterizedTest
    @ValueSource(strings = {"12345", "0000", "987654321", " 123456 "})
    @DisplayName("Debe lanzar InvalidRestaurantNameException si el nombre contiene únicamente números")
    void saveRestaurant_whenNameIsOnlyNumbers_shouldThrowInvalidRestaurantNameException(String numericName) {
        validRestaurant.setNombre(numericName);

        assertThrows(InvalidRestaurantNameException.class, () -> restaurantUseCase.saveRestaurant(validRestaurant));
        verify(restaurantPersistencePort, never()).saveRestaurant(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Restaurante 123", "123 Burger", "KFC 2.0", "Plaza 50"})
    @DisplayName("Debe guardar exitosamente si el nombre tiene números pero también letras")
    void saveRestaurant_whenNameHasLettersAndNumbers_shouldSaveSuccessfully(String name) {
        validRestaurant.setNombre(name);
        when(restaurantPersistencePort.existsByNit(anyString())).thenReturn(false);
        when(userClientPort.isUserOwner(any())).thenReturn(true);

        restaurantUseCase.saveRestaurant(validRestaurant);

        verify(restaurantPersistencePort).saveRestaurant(validRestaurant);
    }

    @ParameterizedTest
    @ValueSource(strings = {"900123ABC", "NIT-1234", "900.123", "ABC900"})
    @DisplayName("Debe lanzar InvalidNitException si el NIT no es únicamente numérico")
    void saveRestaurant_whenNitNotNumeric_shouldThrowInvalidNitException(String invalidNit) {
        validRestaurant.setNit(invalidNit);

        assertThrows(InvalidNitException.class, () -> restaurantUseCase.saveRestaurant(validRestaurant));
        verify(restaurantPersistencePort, never()).saveRestaurant(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"+57300569832599", "+12345678901234", "12345678901234"})
    @DisplayName("Debe lanzar InvalidPhoneException si el teléfono supera los 13 caracteres")
    void saveRestaurant_whenPhoneExceeds13Digits_shouldThrowInvalidPhoneException(String longPhone) {
        validRestaurant.setTelefono(longPhone);

        assertThrows(InvalidPhoneException.class, () -> restaurantUseCase.saveRestaurant(validRestaurant));
        verify(restaurantPersistencePort, never()).saveRestaurant(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"telefono123", "+57 300 569", "++57300", "+57-300"})
    @DisplayName("Debe lanzar InvalidPhoneException si el teléfono no es válido")
    void saveRestaurant_whenPhoneFormatInvalid_shouldThrowInvalidPhoneException(String invalidPhone) {
        validRestaurant.setTelefono(invalidPhone);

        assertThrows(InvalidPhoneException.class, () -> restaurantUseCase.saveRestaurant(validRestaurant));
        verify(restaurantPersistencePort, never()).saveRestaurant(any());
    }

    @Test
    @DisplayName("Debe lanzar UserNotOwnerException cuando el usuario no es propietario o no existe")
    void saveRestaurant_whenUserIsNotOwner_shouldThrowUserNotOwnerException() {
        when(restaurantPersistencePort.existsByNit(validRestaurant.getNit())).thenReturn(false);
        when(userClientPort.isUserOwner(validRestaurant.getIdPropietario())).thenReturn(false);

        assertThrows(UserNotOwnerException.class, () -> restaurantUseCase.saveRestaurant(validRestaurant));
        verify(restaurantPersistencePort, never()).saveRestaurant(any());
    }

    @Test
    @DisplayName("Debe lanzar RestaurantAlreadyExistsException cuando el NIT ya existe")
    void saveRestaurant_whenNitAlreadyExists_shouldThrowRestaurantAlreadyExistsException() {
        when(restaurantPersistencePort.existsByNit(validRestaurant.getNit())).thenReturn(true);

        assertThrows(RestaurantAlreadyExistsException.class, () -> restaurantUseCase.saveRestaurant(validRestaurant));
        verify(restaurantPersistencePort, never()).saveRestaurant(any());
    }

    @Test
    @DisplayName("Debe lanzar RequiredFieldException cuando falta algún campo obligatorio")
    void saveRestaurant_whenRequiredFieldMissing_shouldThrowRequiredFieldException() {
        RestaurantModel noName = new RestaurantModel(null, null, "123", "Dir", "+57300", "url", 2L);
        assertThrows(RequiredFieldException.class, () -> restaurantUseCase.saveRestaurant(noName));

        RestaurantModel blankName = new RestaurantModel(null, "  ", "123", "Dir", "+57300", "url", 2L);
        assertThrows(RequiredFieldException.class, () -> restaurantUseCase.saveRestaurant(blankName));

        RestaurantModel noNit = new RestaurantModel(null, "Restaurante", null, "Dir", "+57300", "url", 2L);
        assertThrows(RequiredFieldException.class, () -> restaurantUseCase.saveRestaurant(noNit));

        RestaurantModel noDir = new RestaurantModel(null, "Restaurante", "123", "", "+57300", "url", 2L);
        assertThrows(RequiredFieldException.class, () -> restaurantUseCase.saveRestaurant(noDir));

        RestaurantModel noPhone = new RestaurantModel(null, "Restaurante", "123", "Dir", null, "url", 2L);
        assertThrows(RequiredFieldException.class, () -> restaurantUseCase.saveRestaurant(noPhone));

        RestaurantModel noLogo = new RestaurantModel(null, "Restaurante", "123", "Dir", "+57300", "", 2L);
        assertThrows(RequiredFieldException.class, () -> restaurantUseCase.saveRestaurant(noLogo));

        RestaurantModel noOwner = new RestaurantModel(null, "Restaurante", "123", "Dir", "+57300", "url", null);
        assertThrows(RequiredFieldException.class, () -> restaurantUseCase.saveRestaurant(noOwner));

        assertThrows(RequiredFieldException.class, () -> restaurantUseCase.saveRestaurant(null));
    }

    @Test
    @DisplayName("Debe consultar restaurante por ID exitosamente")
    void findById_whenValidId_shouldReturnRestaurant() {
        when(restaurantPersistencePort.findById(1L)).thenReturn(validRestaurant);

        RestaurantModel result = restaurantUseCase.findById(1L);

        assertNotNull(result);
        assertEquals("900123456", result.getNit());
        verify(restaurantPersistencePort).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar RequiredFieldException si el id consultado es nulo")
    void findById_whenIdNull_shouldThrowRequiredFieldException() {
        assertThrows(RequiredFieldException.class, () -> restaurantUseCase.findById(null));
    }
}
