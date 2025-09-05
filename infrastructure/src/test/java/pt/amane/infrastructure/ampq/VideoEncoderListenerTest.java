package pt.amane.infrastructure.ampq;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.amqp.rabbit.test.TestRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pt.amane.AmqpTest;
import pt.amane.application.video.media.update.UpdateMediaStatusCommand;
import pt.amane.application.video.media.update.UpdateMediaStatusUseCase;
import pt.amane.domain.utils.IdUtils;
import pt.amane.domain.video.MediaStatus;
import pt.amane.infrastructure.configuration.annotations.VideoEncodedQueue;
import pt.amane.infrastructure.configuration.json.Json;
import pt.amane.infrastructure.configuration.proprieties.amqp.QueueProperties;
import pt.amane.infrastructure.video.model.VideoEncoderCompleted;
import pt.amane.infrastructure.video.model.VideoEncoderError;
import pt.amane.infrastructure.video.model.VideoMessage;
import pt.amane.infrastructure.video.model.VideoMetadata;

@AmqpTest
class VideoEncoderListenerTest {

  @Autowired
  private TestRabbitTemplate rabbitTemplate;

  @Autowired
  private RabbitListenerTestHarness harness;

  @MockitoBean
  private UpdateMediaStatusUseCase updateMediaStatusUseCase;

  @Autowired
  @VideoEncodedQueue
  private QueueProperties queueProperties;

  @Test
  void givenErrorResult_whenCallsListener_shouldProcess() throws InterruptedException {
    // given
    final var expectedError = new VideoEncoderError(
        new VideoMessage("123", "abc"),
        "Video not found"
    );

    final var expectedMessage = Json.writeValueAsString(expectedError);

    // when
    this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

    // then
    final var invocationData =
        harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

    Assertions.assertNotNull(invocationData);
    Assertions.assertNotNull(invocationData.getArguments());

    final var actualMessage = (String) invocationData.getArguments()[0];
    Assertions.assertEquals(expectedMessage, actualMessage);
  }

  @Test
  void givenCompletedResult_whenCallsListener_shouldCallUseCase() throws InterruptedException {
    // given
    final var expectedId = IdUtils.uuid();
    final var expectedOutputBucket = "codeeducationtest";
    final var expectedStatus = MediaStatus.COMPLETED;
    final var expectedEncoderVideoFolder = "anyfolder";
    final var expectedResourceId = IdUtils.uuid();
    final var expectedFilePath = "any.mp4";
    final var expectedMetadata =
        new VideoMetadata(expectedEncoderVideoFolder, expectedResourceId, expectedFilePath);

    final var aResult = new VideoEncoderCompleted(expectedId, expectedOutputBucket, expectedMetadata);

    final var expectedMessage = Json.writeValueAsString(aResult);

    doNothing().when(updateMediaStatusUseCase).execute(any());

    // when
    this.rabbitTemplate.convertAndSend(queueProperties.getQueue(), expectedMessage);

    // then
    final var invocationData =
        harness.getNextInvocationDataFor(VideoEncoderListener.LISTENER_ID, 1, TimeUnit.SECONDS);

    Assertions.assertNotNull(invocationData);
    Assertions.assertNotNull(invocationData.getArguments());

    final var actualMessage = (String) invocationData.getArguments()[0];
    Assertions.assertEquals(expectedMessage, actualMessage);

    final var cmdCaptor = ArgumentCaptor.forClass(UpdateMediaStatusCommand.class);
    verify(updateMediaStatusUseCase).execute(cmdCaptor.capture());

    final var actualCommand = cmdCaptor.getValue();
    Assertions.assertEquals(expectedStatus, actualCommand.status());
    Assertions.assertEquals(expectedId, actualCommand.videoId());
    Assertions.assertEquals(expectedResourceId, actualCommand.resourceId());
    Assertions.assertEquals(expectedEncoderVideoFolder, actualCommand.folder());
    Assertions.assertEquals(expectedFilePath, actualCommand.filename());
  }
}
