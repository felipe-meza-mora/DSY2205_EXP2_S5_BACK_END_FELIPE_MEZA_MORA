package com.tienda.tienda.controller;

import com.tienda.tienda.model.User;
import com.tienda.tienda.service.UserService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    // Endpoint para registrar un usuario
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {
        User savedUser = userService.saveUser(user);
        return ResponseEntity.ok("Usuario añadido correctamente: " + savedUser.getUsername());
    }

    // Endpoint para obtener el usuario por id
    @GetMapping("/{username}")
    public ResponseEntity<Map<String, Object>> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.findByUsername(username);
        Map<String, Object> response = new HashMap<>();
        
        if (user.isPresent()) {
            response.put("message", "Usuario encontrado: " + user.get().getUsername());
            response.put("user", user.get());
            return ResponseEntity.ok(response);
        } else {
            response.put("message", "Usuario no encontrado");
            return ResponseEntity.status(404).body(response);
        }
    }

    // Endpoint para obtener todos los usuarios
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Se han encontrado " + users.size() + " usuarios.");
        response.put("users", users);
        
        return ResponseEntity.ok(response);
    }

    // Endpoint para actualizar un usuario
    @PutMapping("/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @Valid @RequestBody User updatedUser) {
        Optional<User> user = userService.updateUser(id, updatedUser);
        return user.map(u -> ResponseEntity.ok("Usuario actualizado correctamente: " + u.getUsername()))
                   .orElseGet(() -> ResponseEntity.status(404).body("Usuario no encontrado para actualización"));
    }

    // Endpoint para eliminar un usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        Optional<User> user = userService.findById(id);
        if (user.isPresent()) {
            userService.deleteUser(id);
            return ResponseEntity.ok("Usuario eliminado correctamente: " + user.get().getUsername());
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado para eliminar");
        }
    }

    // Endpoint para el login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        Optional<User> user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        if (user.isPresent()) {
            return ResponseEntity.ok("Login exitoso para el usuario: " + user.get().getUsername());
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }

    // Endpoint para verificar si el RUT está registrado
    @PostMapping("/check-rut")
    public ResponseEntity<Boolean> isRutRegistered(@RequestBody Map<String, String> requestBody) {
        String rut = requestBody.get("rut");
        boolean exists = userService.isRutRegistered(rut);
        return ResponseEntity.ok(exists);
    }

    // Endpoint para verificar si el correo electrónico está registrado
    @PostMapping("/check-email")
    public ResponseEntity<Boolean> isEmailRegistered(@RequestBody Map<String, String> requestBody) {
        String email = requestBody.get("email");
        boolean exists = userService.isEmailRegistered(email);
        return ResponseEntity.ok(exists);
    }

    // Endpoint para verificar si un nombre de usuario ya está registrado
    @PostMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestBody Map<String, String> requestBody) {
        String username = requestBody.get("username");
        boolean exists = userService.findByUsername(username).isPresent();
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        
        return ResponseEntity.ok(response);
    }

    // Endpoint para actualizar la contraseña de un usuario
    @PutMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");
        
        boolean updated = userService.updatePassword(email, newPassword);
        if (updated) {
            return ResponseEntity.ok("Contraseña actualizada correctamente.");
        } else {
            return ResponseEntity.status(404).body("Usuario no encontrado o error al actualizar la contraseña.");
        }
    }
}