package com.novara.novara_demo.util.mapper;


import com.novara.novara_demo.model.dto.NewUserDTO;
import com.novara.novara_demo.model.dto.ShowUserDTO;
import com.novara.novara_demo.model.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperCustom {

    public User toEntity(NewUserDTO dto) {
        User newUser = new User();
        newUser.setEmail(dto.getEmail());
        newUser.setFirstName(dto.getFirstName());
        newUser.setLastName(dto.getLastName());
        newUser.setPassword(dto.getPassword());
        return newUser;
    }

    public ShowUserDTO toShowUserDTO(User user){
        ShowUserDTO dto = new ShowUserDTO();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        return dto;
    }
}
