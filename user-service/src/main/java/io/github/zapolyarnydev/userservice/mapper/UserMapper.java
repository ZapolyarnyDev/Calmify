package io.github.zapolyarnydev.userservice.mapper;

import io.github.zapolyarnydev.userservice.dto.SelfInfoResponseDTO;
import io.github.zapolyarnydev.userservice.dto.UpdateUserRequestDTO;
import io.github.zapolyarnydev.userservice.entity.UserEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mappings({
            @Mapping(source = "displayName", target = "displayName"),
            @Mapping(source = "handle", target = "handle"),
            @Mapping(source = "description", target = "description"),
            @Mapping(target = "showActivity", ignore = true),
            @Mapping(target = "lastSeenAt", ignore = true)
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequest(UpdateUserRequestDTO updateUserRequestDTO, @MappingTarget UserEntity userEntity);

    @Mappings({
            @Mapping(source = "displayName", target = "displayName"),
            @Mapping(source = "handle", target = "handle"),
            @Mapping(source = "description", target = "description"),
    })
    SelfInfoResponseDTO fromEntityToSelfInfo(UserEntity entity);
}
