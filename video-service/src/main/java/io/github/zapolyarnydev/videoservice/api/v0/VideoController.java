package io.github.zapolyarnydev.videoservice.api.v0;

import io.github.zapolyarnydev.commons.api.ApiResponse;
import io.github.zapolyarnydev.videoservice.dto.CreateVideoMetadataDTO;
import io.github.zapolyarnydev.videoservice.dto.UpdateVideoMetadataDTO;
import io.github.zapolyarnydev.videoservice.dto.VideoMetadataInfo;
import io.github.zapolyarnydev.videoservice.entity.VideoMetadataEntity;
import io.github.zapolyarnydev.videoservice.mapper.VideoMetadataMapper;
import io.github.zapolyarnydev.videoservice.service.VideoMetadataService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v0/videos")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VideoController {

    VideoMetadataService metadataService;
    VideoMetadataMapper metadataMapper;


    @PostMapping
    public ResponseEntity<ApiResponse<String>> addVideo(@Valid @RequestBody CreateVideoMetadataDTO videoMetadataDTO,
                                                        @RequestHeader("Calmify-User-Email") String email) {
        //TODO add gRPC for communication between services
        VideoMetadataEntity entity = metadataService.createVideoMetadata(videoMetadataDTO, UUID.randomUUID());
        return ResponseEntity.ok(
                ApiResponse.success("Video successfully uploaded!", entity.getShortId())
        );
    }

    @PatchMapping("/{shortId}")
    public ResponseEntity<ApiResponse<String>> updateVideo(@PathVariable String shortId,
                                                           @Valid @RequestBody UpdateVideoMetadataDTO videoMetadataDTO,
                                                           @RequestHeader("Calmify-User-Email") String email) {
        metadataService.updateVideoMetadata(shortId, videoMetadataDTO);
        //TODO add gRPC for communication between services
        VideoMetadataEntity entity = metadataService.findByShortId(shortId);
        return ResponseEntity.ok(
                ApiResponse.success("Video successfully updated!", shortId)
        );
    }

    @GetMapping("/{shortId}")
    public ResponseEntity<ApiResponse<VideoMetadataInfo>> getVideoMetadata(@PathVariable String shortId) {
        VideoMetadataEntity entity = metadataService.findByShortId(shortId);
        VideoMetadataInfo info = metadataMapper.toInfo(entity);
        return ResponseEntity.ok(
                ApiResponse.success("meta-data of the video has been sent", info)
        );
    }

    @DeleteMapping("/{shortId}")
    public ResponseEntity<ApiResponse<String>> deleteVideo(@PathVariable String shortId,
                                                           @RequestHeader("Calmify-User-Email") String email) {
        //TODO add gRPC for communication between services
        metadataService.removeVideoMetadata(shortId);
        return ResponseEntity.ok(
                ApiResponse.success("Video successfully deleted!", shortId)
        );
    }
}
