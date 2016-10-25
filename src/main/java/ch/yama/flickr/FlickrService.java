package ch.yama.flickr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlickrService {

	@Autowired
	SearchPhotosServiceBuilder searchPhotosServiceBuilder;

	@Autowired
	SetPhotoMetaService setPhotoMetaService;

	public SearchPhotosServiceBuilder searchPhotos() {
		return searchPhotosServiceBuilder;
	}

	public SetPhotoMetaService setPhotoMeta() {
		return setPhotoMetaService;
	}
}
