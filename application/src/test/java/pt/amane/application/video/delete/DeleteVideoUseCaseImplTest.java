package pt.amane.application.video.delete;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.exception.InternalErrorException;
import pt.amane.domain.video.MediaResourceGateway;
import pt.amane.domain.video.VideoGateway;
import pt.amane.domain.video.VideoID;

class DeleteVideoUseCaseImplTest extends UseCaseTest {

  @InjectMocks
  private DeleteVideoUseCaseImpl useCase;

  @Mock
  private VideoGateway videoGateway;

  @Mock
  private MediaResourceGateway mediaResourceGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(videoGateway, mediaResourceGateway);
  }

  @Test
  void givenAValidId_whenCallsDeleteVideo_shouldDeleteIt() {

    // given
    final var aVideoId = VideoID.unique();

    Mockito.doNothing()
        .when(videoGateway).deleteById(aVideoId);

    Mockito.doNothing()
        .when(mediaResourceGateway).clearResources(aVideoId);

    // when
    Assertions.assertDoesNotThrow(() -> useCase.execute(aVideoId.getValue()));

    // then
    Mockito.verify(videoGateway).deleteById(aVideoId);
    Mockito.verify(mediaResourceGateway).clearResources(aVideoId);
  }

  @Test
  void givenAnInvalidId_whenCallsDeleteVideo_shouldBeOk() {

    // given
    final var aVideoId = VideoID.from("123");

    Mockito.doNothing()
        .when(videoGateway).deleteById(aVideoId);

    // when
    Assertions.assertDoesNotThrow(() -> useCase.execute(aVideoId.getValue()));

    // then
    Mockito.verify(videoGateway).deleteById(aVideoId);
  }

  @Test
  void givenAValidId_whenCallsDeleteVideoAndGatewayThrowsException_shouldReceiveException() {

    // given
    final var aVideoId = VideoID.from("1231");

    Mockito.doThrow(InternalErrorException.with("Error on delete video", new RuntimeException()))
        .when(videoGateway).deleteById(aVideoId);

    // when
    Assertions.assertThrows(InternalErrorException.class, () -> useCase.execute(aVideoId.getValue()));

    // then
    Mockito.verify(videoGateway).deleteById(aVideoId);

  }
}