package pt.amane.infrastructure.video;

import java.util.Optional;
import pt.amane.domain.video.AudioVideoMedia;
import pt.amane.domain.video.ImageMedia;
import pt.amane.domain.video.MediaResourceGateway;
import pt.amane.domain.video.Resource;
import pt.amane.domain.video.VideoID;
import pt.amane.domain.video.VideoMediaType;
import pt.amane.domain.video.VideoResource;
import pt.amane.infrastructure.configuration.proprieties.storage.StorageProperties;
import pt.amane.infrastructure.services.StorageService;

public class MediaResourceGatewayImpl implements MediaResourceGateway {

  private final String filenamePattern;
  private final String locationPattern;
  private final StorageService storageService;

  public MediaResourceGatewayImpl(final StorageProperties props, final StorageService storageService) {
    this.filenamePattern = props.getFileNamePattern();
    this.locationPattern = props.getLocationPattern();
    this.storageService = storageService;
  }

  @Override
  public AudioVideoMedia storeAudioVideo(final VideoID anId, final VideoResource videoResource) {
    final var filepath = filepath(anId, videoResource.type());
    final var aResource = videoResource.resource();
    store(filepath, aResource);
    return AudioVideoMedia.with(aResource.checksum(), aResource.name(), filepath);
  }

  @Override
  public ImageMedia storeImage(final VideoID anId, final VideoResource videoResource) {
    final var filepath = filepath(anId, videoResource.type());
    final var aResource = videoResource.resource();
    store(filepath, aResource);
    return ImageMedia.with(aResource.checksum(), aResource.name(), filepath);
  }

  @Override
  public Optional<Resource> getResource(final VideoID anId, final VideoMediaType type) {
    return this.storageService.get(filepath(anId, type));
  }

  @Override
  public void clearResources(final VideoID anId) {
    final var ids = this.storageService.list(folder(anId));
    this.storageService.deleteAll(ids);
  }

  private String filename(final VideoMediaType aType) {
    return filenamePattern.replace("{type}", aType.name());
  }

  private String folder(final VideoID anId) {
    return locationPattern.replace("{videoId}", anId.getValue());
  }

  private String filepath(final VideoID anId, final VideoMediaType aType) {
    return folder(anId)
        .concat("/")
        .concat(filename(aType));
  }

  private void store(final String filepath, final Resource aResource) {
    this.storageService.store(filepath, aResource);
  }

}