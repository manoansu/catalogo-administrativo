package pt.amane.application.video.create;

import java.util.Optional;
import pt.amane.domain.video.AudioVideoMedia;
import pt.amane.domain.video.ImageMedia;
import pt.amane.domain.video.Resource;
import pt.amane.domain.video.VideoID;
import pt.amane.domain.video.VideoMediaType;
import pt.amane.domain.video.VideoResource;

public interface MediaResourceGateway {

  AudioVideoMedia storeAudioVideo(VideoID anId, VideoResource aResource);

  ImageMedia storeImage(VideoID anId, VideoResource aResource);

  Optional<Resource> getResource(VideoID anId, VideoMediaType type);

  void clearResources(VideoID anId);

}
