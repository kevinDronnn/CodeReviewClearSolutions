package com.example.codereviewclearsolutions.Mapper;

import com.example.codereviewclearsolutions.Dto.UserDto;
import com.example.codereviewclearsolutions.Entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    UserEntity userDtoToUserEntity(UserDto userDto);


    @Mapping(target = "email", source = "email")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "birthDate", source = "birthDate")
    @Mapping(target = "address", source = "address")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    List<UserDto> userEntityToUserDto(List<UserEntity> userEntities);
}
