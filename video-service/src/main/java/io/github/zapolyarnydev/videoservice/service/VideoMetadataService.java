package io.github.zapolyarnydev.videoservice.service;

import io.github.zapolyarnydev.videoservice.dto.CreateVideoMetadataDTO;
import io.github.zapolyarnydev.videoservice.dto.UpdateVideoMetadataDTO;
import io.github.zapolyarnydev.videoservice.entity.VideoMetadataEntity;
import io.github.zapolyarnydev.videoservice.exception.VideoMetadataNotFoundByShortIdException;
import io.github.zapolyarnydev.videoservice.exception.VideoMetadataNotFoundException;
import io.github.zapolyarnydev.videoservice.generator.ShortIdGenerator;
import io.github.zapolyarnydev.videoservice.mapper.VideoMetadataMapper;
import io.github.zapolyarnydev.videoservice.repository.VideoMetadataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class VideoMetadataService {

    private final VideoMetadataRepository metadataRepository;

    private final ShortIdGenerator shortIdGenerator;

    private final VideoMetadataMapper metadataMapper;

    @Transactional
    public VideoMetadataEntity createVideoMetadata(CreateVideoMetadataDTO metadataDTO) {
        var entity = VideoMetadataEntity.builder()
                .title(metadataDTO.title())
                .description(metadataDTO.description())
                .uploadedAt(metadataDTO.uploadedAt())
                .shortIdGenerator(shortIdGenerator)
                .build();

        log.info("New video metadata has been uploaded:\nTitle: {}\nShort id: {}", metadataDTO.title(), entity.getShortId());
        return metadataRepository.save(entity);
    }

    public VideoMetadataEntity findById(UUID id) {
        return metadataRepository.findById(id)
                .orElseThrow(() -> new VideoMetadataNotFoundException(id));
    }

    public VideoMetadataEntity findByShortId(String shortId) {
        return metadataRepository.findByShortId(shortId)
                .orElseThrow(() -> new VideoMetadataNotFoundByShortIdException(shortId));
    }

    public List<VideoMetadataEntity> getVideosByAuthor(UUID authorId, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("uploadedAt"));
        return metadataRepository.findByAuthorId(authorId, pageable).getContent();
    }

    @Transactional
    public void updateVideoMetadata(UpdateVideoMetadataDTO updateDTO) {
        var entity = findById(updateDTO.id());
        metadataMapper.updateVideoMetadata(updateDTO, entity);
        metadataRepository.save(entity);
        log.info("Video with id {} has been updated", updateDTO.id());
    }


    @Transactional
    public void removeVideoMetadata(UUID id) {
        var entity = findById(id);
        metadataRepository.delete(entity);
        log.info("Video with id {} has been deleted", id);
    }
}
