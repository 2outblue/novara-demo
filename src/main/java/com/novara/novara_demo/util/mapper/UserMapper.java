package com.novara.novara_demo.util.mapper;

import com.novara.novara_demo.model.dto.NewUserDTO;
import com.novara.novara_demo.model.dto.ShowUserDTO;
import com.novara.novara_demo.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    ShowUserDTO toShowUserDTO(User user);
    User toUser(NewUserDTO dto);

}
