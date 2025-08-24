package com.novara.novara_demo.service;

import com.novara.novara_demo.model.dto.NewUserDTO;
import com.novara.novara_demo.model.dto.ShowUserDTO;
import com.novara.novara_demo.model.entity.User;
import com.novara.novara_demo.repository.UserRepository;
import com.novara.novara_demo.repository.UserSearchRepository;
import com.novara.novara_demo.util.mapper.UserMapperCustom;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapperCustom userMapper;
    private final UserSearchRepository userSearchRepository;

    public UserService(UserRepository userRepository, UserMapperCustom userMapper, UserSearchRepository userSearchRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;

        this.userSearchRepository = userSearchRepository;
    }

    public ShowUserDTO createUser(NewUserDTO dto) {
        User newUser = this.userMapper.toEntity(dto);
        newUser.setId(UUID.randomUUID());
        User savedUser = this.userRepository.save(newUser);
        return userMapper.toShowUserDTO(savedUser);
    }

    public ShowUserDTO getUserInfo(String email) {
        User entity = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return userMapper.toShowUserDTO(entity);
    }

    public List<ShowUserDTO> getAllUsers() {
        List<User> entities = userRepository.findAll();
        return entities.stream().map(userMapper::toShowUserDTO).toList();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(this::entityToUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + email + " not found"));
    }

    public List<ShowUserDTO> searchUserByEmail(String email) {
        List<User> users = userSearchRepository.searchByEmail(email, 5);
        return users.stream().map(userMapper::toShowUserDTO).toList();
    }


    private UserDetails entityToUserDetails(User entity) {
        return new org.springframework.security.core.userdetails.User(
                entity.getEmail(),
                entity.getPassword(),
                entity.getRoles().stream()
                        .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                        .collect(Collectors.toList())
        );
    }



}
