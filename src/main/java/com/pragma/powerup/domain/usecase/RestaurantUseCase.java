package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.InvalidNitException;
import com.pragma.powerup.domain.exception.InvalidPhoneException;
import com.pragma.powerup.domain.exception.InvalidRestaurantNameException;
import com.pragma.powerup.domain.exception.RequiredFieldException;
import com.pragma.powerup.domain.exception.RestaurantAlreadyExistsException;
import com.pragma.powerup.domain.exception.UserNotOwnerException;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserClientPort;

import java.util.regex.Pattern;

public class RestaurantUseCase implements IRestaurantServicePort {

    private static final String NUMERIC_ONLY_REGEX = "^[0-9]+$";
    private static final String PHONE_REGEX = "^\\+?[0-9]{1,13}$";

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserClientPort userClientPort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort,
                             IUserClientPort userClientPort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userClientPort = userClientPort;
    }

    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        validateRequiredFields(restaurantModel);
        validateRestaurantName(restaurantModel.getNombre());
        validateNitFormat(restaurantModel.getNit());
        validatePhoneFormat(restaurantModel.getTelefono());
        validateUniqueness(restaurantModel.getNit());
        validateOwnerRole(restaurantModel.getIdPropietario());

        restaurantPersistencePort.saveRestaurant(restaurantModel);
    }

    @Override
    public RestaurantModel findById(Long id) {
        if (id == null) {
            throw new RequiredFieldException("id");
        }
        return restaurantPersistencePort.findById(id);
    }

    private void validateRequiredFields(RestaurantModel restaurantModel) {
        if (restaurantModel == null) {
            throw new RequiredFieldException("restaurant");
        }
        if (isNullOrBlank(restaurantModel.getNombre())) {
            throw new RequiredFieldException("nombre");
        }
        if (isNullOrBlank(restaurantModel.getNit())) {
            throw new RequiredFieldException("nit");
        }
        if (isNullOrBlank(restaurantModel.getDireccion())) {
            throw new RequiredFieldException("direccion");
        }
        if (isNullOrBlank(restaurantModel.getTelefono())) {
            throw new RequiredFieldException("telefono");
        }
        if (isNullOrBlank(restaurantModel.getUrlLogo())) {
            throw new RequiredFieldException("urlLogo");
        }
        if (restaurantModel.getIdPropietario() == null) {
            throw new RequiredFieldException("idPropietario");
        }
    }

    private void validateRestaurantName(String name) {
        // Name may contain numbers, but cannot be composed ONLY of numbers
        if (Pattern.matches(NUMERIC_ONLY_REGEX, name.trim())) {
            throw new InvalidRestaurantNameException();
        }
    }

    private void validateNitFormat(String nit) {
        if (!Pattern.matches(NUMERIC_ONLY_REGEX, nit)) {
            throw new InvalidNitException();
        }
    }

    private void validatePhoneFormat(String phone) {
        if (!Pattern.matches(PHONE_REGEX, phone) || phone.length() > 13) {
            throw new InvalidPhoneException();
        }
    }

    private void validateUniqueness(String nit) {
        if (restaurantPersistencePort.existsByNit(nit)) {
            throw new RestaurantAlreadyExistsException("A restaurant with NIT '" + nit + "' already exists.");
        }
    }

    private void validateOwnerRole(Long ownerId) {
        if (!userClientPort.isUserOwner(ownerId)) {
            throw new UserNotOwnerException("The user with ID '" + ownerId + "' does not exist or does not have the OWNER role.");
        }
    }

    private boolean isNullOrBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
