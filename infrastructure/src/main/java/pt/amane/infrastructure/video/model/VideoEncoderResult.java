package pt.amane.infrastructure.video.model;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXISTING_PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = NAME, include = EXISTING_PROPERTY, property = "status")
@VideoResponseTypes
public sealed interface VideoEncoderResult
    permits VideoEncoderCompleted, VideoEncoderError {

  String getStatus();
}
