package pt.amane.infrastructure.ampq;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import pt.amane.application.video.media.update.UpdateMediaStatusCommand;
import pt.amane.application.video.media.update.UpdateMediaStatusUseCase;
import pt.amane.domain.video.MediaStatus;
import pt.amane.infrastructure.configuration.json.Json;
import pt.amane.infrastructure.video.model.VideoEncoderCompleted;
import pt.amane.infrastructure.video.model.VideoEncoderError;
import pt.amane.infrastructure.video.model.VideoEncoderResult;

@Component
public class VideoEncoderListener {

  private static final Logger log = LoggerFactory.getLogger(VideoEncoderListener.class);

  static final String LISTENER_ID = "videoEncodedListener";

  private final UpdateMediaStatusUseCase updateMediaStatusUseCase;

  public VideoEncoderListener(final UpdateMediaStatusUseCase updateMediaStatusUseCase) {
    this.updateMediaStatusUseCase = Objects.requireNonNull(updateMediaStatusUseCase);
  }

  @RabbitListener(id = LISTENER_ID, queues = "${amqp.queues.video-encoded.queue}")
  public void onVideoEncodedMessage(@Payload final String message) {
    final var aResult = Json.readValue(message, VideoEncoderResult.class);

    if (aResult instanceof VideoEncoderCompleted dto) {
      log.error("[message:video.listener.income] [status:completed] [payload:{}]", message);
      final var aCmd = new UpdateMediaStatusCommand(
          MediaStatus.COMPLETED,
          dto.id(),
          dto.video().resourceId(),
          dto.video().encodedVideoFolder(),
          dto.video().filePath()
      );

      this.updateMediaStatusUseCase.execute(aCmd);
    } else if (aResult instanceof VideoEncoderError) {
      log.error("[message:video.listener.income] [status:error] [payload:{}]", message);
    } else {
      log.error("[message:video.listener.income] [status:unknown] [payload:{}]", message);
    }
  }
}