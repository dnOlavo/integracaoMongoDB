package com.iftm.start_example.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.iftm.start_example.models.User;
import com.iftm.start_example.models.dto.UserDTO;
import com.iftm.start_example.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public ResponseEntity<List<UserDTO>> findAll() {
        var dbUsers = repository.findAll();
        if (dbUsers.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var userDtos = dbUsers.stream()
                .map(user -> new UserDTO(user))
                .collect(Collectors.toList());

        return ResponseEntity.ok(userDtos);
    }

    public ResponseEntity<UserDTO> findById(ObjectId id) {
        if (id == null) {
            return ResponseEntity.badRequest().build();
        }

        var dbUser = repository.findById(id);
        if (dbUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new UserDTO(dbUser.get()));
    }

    public ResponseEntity<UserDTO> save(User user) {
        // Validação de dados do usuário
        if (user.getName().isBlank() || user.getAge() <= 0) {
            return ResponseEntity.badRequest().build();
        }

        user.setId(ObjectId.get());
        return ResponseEntity.ok(new UserDTO(repository.save(user)));
    }

    public ResponseEntity<UserDTO> update(UserDTO user) {
        // Validação do ID do usuário
        if (user.getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        var objectId = new ObjectId(user.getId());
        var dbUser = repository.findById(objectId);
        if (dbUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Atualização dos dados
        var dbUserObj = dbUser.get();
        dbUserObj.setName(user.getName());
        dbUserObj.setAge(user.getAge());
        return ResponseEntity.ok(new UserDTO(repository.save(dbUserObj)));
    }

    public ResponseEntity<?> delete(ObjectId id) {
        if (id == null) {
            return ResponseEntity.badRequest().build(); // Retorna 400 se o ID for nulo
        }

        Optional<User> user = repository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build(); // Retorna 404 se o usuário não existir
        }

        repository.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content em caso de sucesso
    }
}

