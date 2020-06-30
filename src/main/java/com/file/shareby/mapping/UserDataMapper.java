package com.file.shareby.mapping;

import com.file.shareby.DTO.UserDTO;
import com.file.shareby.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDataMapper {
    public abstract UserDTO mapDomainToDto(User user);

    User mapDtoToDomain(UserDTO userDTO);
}
