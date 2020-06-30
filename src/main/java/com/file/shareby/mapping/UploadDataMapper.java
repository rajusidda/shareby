package com.file.shareby.mapping;

import com.file.shareby.DTO.UploadDataDTO;
import com.file.shareby.domain.UploadData;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UploadDataMapper {
    public abstract UploadDataDTO mapDomainToDto(UploadData uploadData);

    UploadData mapDtoToDomain(UploadDataDTO uploadDataDTO);
}
