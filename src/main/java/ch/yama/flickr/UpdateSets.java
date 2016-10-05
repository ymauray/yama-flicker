package ch.yama.flickr;

import java.io.IOException;
import java.net.URISyntaxException;

import ch.yama.flickr.impl.Flickr;

public class UpdateSets {

	public static void main(String[] args) throws IOException, URISyntaxException {
		new UpdateSets().run();
	}

	void run() throws IOException, URISyntaxException {
		final Flickr flickr = Flickr.authenticate();
		flickr
			.photosets()
			.getList()
			.stream()
			.filter(photoset -> photoset.getTitle().equals("Auto Upload"))
			.findFirst()
			.ifPresent(autoUpload -> {
				System.out.println(autoUpload);
			});
		/*
		final int page = 1;
		final OAuthRequest photosetsGetListRequest = new OAuthRequest(Verb.GET,
				"https://api.flickr.com/services/rest/?format=json&nojsoncallback=1&method=flickr.photosets.getList&user_id=142925583@N05&page="
						+ page,
				oAuth);
		oAuth.signRequest(accessToken, photosetsGetListRequest);
		final Response response = photosetsGetListRequest.send();
		*/
	}

}
