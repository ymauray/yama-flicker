package ch.yama.flickr.api;

import java.io.IOException;
import java.util.List;

import ch.yama.flickr.model.Photoset;

public interface PhotosetsApi {

	List<Photoset> getList() throws IOException;

}
