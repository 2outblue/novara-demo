package com.novara.novara_demo.web;


import com.novara.novara_demo.model.dto.NewUserDTO;
import com.novara.novara_demo.model.dto.ShowUserDTO;
import com.novara.novara_demo.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ShowUserDTO>> getAllUsers() {
        List<ShowUserDTO> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{email}")
    public ResponseEntity<ShowUserDTO> getUserByEmail(@PathVariable String email) {
        ShowUserDTO userDto = userService.getUserInfo(email);

        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/search/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ShowUserDTO>> searchUserByEmail(@PathVariable String email) {
        List<ShowUserDTO> dtos = userService.searchUserByEmail(email);
        return ResponseEntity.ok(dtos);
    }

    @PostMapping
    public ResponseEntity<ShowUserDTO> createUser(@RequestBody NewUserDTO userDto, UriComponentsBuilder uriBuilder) {
        ShowUserDTO newUser = userService.createUser(userDto);
        return ResponseEntity.created(
                uriBuilder.path("/users/{email}").build(newUser.getEmail())
        ).body(newUser);
    }
}
