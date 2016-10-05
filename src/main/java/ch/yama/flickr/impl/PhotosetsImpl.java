package ch.yama.flickr.impl;

import java.io.IOException;
import java.util.List;
import java.util.stream.StreamSupport;

import org.json.JSONObject;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import ch.yama.flickr.api.PhotosetsApi;
import ch.yama.flickr.model.Photoset;
import io.mikael.urlbuilder.UrlBuilder;

public class PhotosetsImpl extends FlickrImpl implements PhotosetsApi {

	PhotosetsImpl(Flickr flickr) {
		super(flickr);
	}

	@Override
	public List<Photoset> getList() throws IOException {
		final Builder<Photoset> builder = ImmutableList.<Photoset>builder();

		final UrlBuilder urlBuilder = getUrlBuilder()
			.addParameter("method", "flickr.photosets.getList")
			;

		requestPages(urlBuilder, "photosets", photosets -> {
			System.out.println(photosets);
			StreamSupport
				.stream(photosets.getJSONArray("photoset").spliterator(), false)
				.map(JSONObject.class::cast)
				.map(Photoset::new)
				.forEach(builder::add);
			return true;
		});

		return builder.build();

		/*

		final OAuthRequest request = new OAuthRequest(Verb.GET,
				"https://api.flickr.com/services/rest/?format=json&nojsoncallback=1&method=flickr.photos.search&user_id=142925583@N05&page="
						+ page + "&extras=date_upload,date_taken",
				oAuth);
		oAuth.signRequest(accessToken, request);
		final Response response = request.send();
		//System.out.println(response.getBody());
		final JSONObject json = new JSONObject(response.getBody());
		final JSONObject photos = json.getJSONObject("photos");
		pages = photos.getInt("pages");
		System.out.println("Processing page " + page + " of " + pages);
		final JSONArray photoArray = photos.getJSONArray("photo");
		for (int i = 0; i < photoArray.length(); i++) {
			final JSONObject photo = photoArray.getJSONObject(i);
			*/
	}

}
