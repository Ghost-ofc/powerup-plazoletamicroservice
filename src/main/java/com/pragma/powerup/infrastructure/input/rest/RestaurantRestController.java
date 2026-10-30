package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/restaurant")
@RequiredArgsConstructor
@Tag(name = "Restaurantes", description = "Endpoints para la gestión de restaurantes en la plazoleta")
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;

    @Operation(summary = "Crear nuevo restaurante",
            description = "Permite a un Administrador registrar un nuevo restaurante asociando a un usuario con rol Propietario.",
            security = {@SecurityRequirement(name = "bearer-jwt")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante creado exitosamente", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (NIT/teléfono no numéricos, nombre solo números, propietario inválido)", content = @Content),
            @ApiResponse(responseCode = "401", description = "No autenticado o token JWT inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acceso denegado: solo el Administrador puede crear restaurantes", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto: ya existe un restaurante con el NIT ingresado", content = @Content)
    })
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ADMIN')")
    public ResponseEntity<Void> saveRestaurant(@Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
        restaurantHandler.saveRestaurant(restaurantRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Consultar restaurante por ID",
            description = "Consulta la información detallada de un restaurante por su identificador.",
            security = {@SecurityRequirement(name = "bearer-jwt")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurante encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RestaurantResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Restaurante no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantHandler.getById(id));
    }
}
