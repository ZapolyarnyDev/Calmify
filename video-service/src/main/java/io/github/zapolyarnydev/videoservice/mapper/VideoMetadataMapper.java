package io.github.zapolyarnydev.videoservice.mapper;

import io.github.zapolyarnydev.videoservice.dto.UpdateVideoMetadataDTO;
import io.github.zapolyarnydev.videoservice.entity.VideoMetadataEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface VideoMetadataMapper {

    @Mappings({
            @Mapping(source = "title", target = "title"),
            @Mapping(source = "description", target = "description")
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVideoMetadata(UpdateVideoMetadataDTO updateDTO, @MappingTarget VideoMetadataEntity entity);
}
