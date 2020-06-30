package com.file.shareby.mapping;

import com.file.shareby.DTO.SharedDataDTO;
import com.file.shareby.domain.SharedData;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SharedDataMapper {

    public abstract SharedDataDTO mapDomainToDto(SharedData sharedData);

    SharedData mapDtoToDomain(SharedDataDTO sharedDataDTO);
}
