package com.testNisum.controller;

import com.testNisum.model.User;
import com.testNisum.model.UserResponseDto;
import com.testNisum.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Validated
@Tag(name = "Gestión de usuarios", description = "Permite  buscar, crear, actualizar, eliminar y listar usuarios")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation( summary = "Creación de usuarios", description = "Permite crear un usuario, valida nombre, email y formato de password")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Operación exitosa", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserController.class)) }),
                    @ApiResponse(responseCode = "400", description = "Error, no pudo ser creada el usuario" ,content = { @Content( mediaType = "none", schema = @Schema(implementation = HashMap.class))  } )
            })
    @PostMapping
    @Valid
    public ResponseEntity<UserResponseDto > createUser(@RequestBody User user) {
        UserResponseDto  createdUser = userService.createUser(user);
        return ResponseEntity.ok(createdUser);
    }

    @Operation( summary = "Listado de usuarios", description = "Lista todos los usuarios")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Operación exitosa", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserController.class)) }),
            })
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation( summary = "Busqueda de usuarios", description = "Busca un usuario por id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Operación exitosa", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserController.class)) }),
                    @ApiResponse(responseCode = "404", description = "No encontrado", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserController.class)) }),
            })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @Operation( summary = "Actualización de usuarios", description = "Actualiza un usuario por id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Operación exitosa", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserController.class)) }),
                    @ApiResponse(responseCode = "500", description = "Usuario no encontrado", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserController.class)) }),
            })
    @Valid
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable UUID id, @RequestBody User userDetails) {
        UserResponseDto  updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation( summary = "Eliminación de usuarios", description = "Elimina un usuario por id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Operación exitosa", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserController.class)) }),
                    @ApiResponse(responseCode = "500", description = "Usuario no encontrado", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = UserController.class)) }),
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();

    }
}
