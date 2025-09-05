package pt.amane.infrastructure.video.model;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonSubTypes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Inform jackson the possible type that it can use like: VideoEncoderCompleted.class and VideoEncoderError.class
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSubTypes({
        @JsonSubTypes.Type(value = VideoEncoderCompleted.class),
        @JsonSubTypes.Type(value = VideoEncoderError.class),
})
public @interface VideoResponseTypes {
}
