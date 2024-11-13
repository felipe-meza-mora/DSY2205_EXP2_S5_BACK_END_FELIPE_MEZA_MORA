package com.tienda.tienda.service;

import com.tienda.tienda.model.User;
import com.tienda.tienda.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    
    // Método para guardar un usuario
    @Override
public User saveUser(User user) {
    return userRepository.save(user);
}
    
    // Método para obtener un usuario por su username
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    // Método para obtener todos los usuarios
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // Método para obtener un usuario por su id
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    // Método para eliminar un usuario
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Método para actualizar un usuario
    @Override
    public Optional<User> updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            user.setNombre(updatedUser.getNombre());
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword());
            user.setPermisos(updatedUser.getPermisos());
            user.setRut(updatedUser.getRut());
            user.setCorreo(updatedUser.getCorreo());
            user.setTelefono(updatedUser.getTelefono());
            user.setDireccionEnvio(updatedUser.getDireccionEnvio());
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }
    
    // Método para el login
    @Override
    public Optional<User> login(String username, String password) {

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }
}