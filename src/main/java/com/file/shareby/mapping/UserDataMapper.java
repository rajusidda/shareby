package com.file.shareby.mapping;

import com.file.shareby.domain.UploadData;
import com.file.shareby.domain.User;
import com.file.shareby.payload.UploadDataDTO;
import com.file.shareby.payload.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDataMapper {
    public abstract UserDTO mapDomainToDto(User user);
    User mapDtoToDomain(UserDTO userDTO);
}
