package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos para la creación de un nuevo restaurante")
public class RestaurantRequestDto {

    @NotBlank(message = "El nombre del restaurante es obligatorio")
    @Pattern(regexp = ".*[a-zA-ZáéíóúÁÉÍÓÚñÑ].*", message = "El nombre del restaurante no puede contener únicamente números")
    @Schema(description = "Nombre del restaurante (puede incluir números pero no solo números)", example = "Restaurante Gourmet 10")
    private String nombre;

    @NotBlank(message = "El NIT es obligatorio")
    @Pattern(regexp = "^[0-9]+$", message = "El NIT debe ser únicamente numérico")
    @Schema(description = "NIT del restaurante (únicamente números)", example = "900123456")
    private String nit;

    @NotBlank(message = "La dirección es obligatoria")
    @Schema(description = "Dirección física del restaurante", example = "Calle 100 # 15-20")
    private String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+?[0-9]{1,13}$", message = "El teléfono debe contener máximo 13 caracteres numéricos y puede iniciar con '+'")
    @Schema(description = "Teléfono de contacto (máx 13 caracteres)", example = "+573005698325")
    private String telefono;

    @NotBlank(message = "La URL del logo es obligatoria")
    @Schema(description = "URL del logo del restaurante", example = "https://imagenes.com/logos/restaurante.png")
    private String urlLogo;

    @NotNull(message = "El ID del propietario es obligatorio")
    @Schema(description = "ID del usuario propietario a cargo del restaurante", example = "2")
    private Long idPropietario;
}
