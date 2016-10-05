package ch.yama.flickr;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

public class UpdateTitle {

	public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
		new UpdateTitle().run();
	}

	void run() throws IOException, URISyntaxException, InterruptedException {
		final OAuth10aService oAuth = new ServiceBuilder().apiKey("ccafdafce413356a9c50ed4b55fdc9dc")
				.apiSecret("2adf86e8bfbd3030").build(FlickrApi.instance());
		final OAuth1RequestToken requestToken = oAuth.getRequestToken();
		final String authUrl = oAuth.getAuthorizationUrl(requestToken);
		System.out.println(
				"Adresse d'authentification à utiliser si le navigateur ne s'ouvre pas tout seul : " + authUrl);
		Desktop.getDesktop().browse(new URI(authUrl));
		final Scanner scanner = new Scanner(System.in);
		final String oAuthVerifier = scanner.nextLine();
		scanner.close();
		final OAuth1AccessToken accessToken = oAuth.getAccessToken(requestToken, oAuthVerifier);

		int page = 1;
		int pages = 99999;
		while (page <= pages) {
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
				final String title = photo.getString("title");
				final String dateTaken = photo.getString("datetaken");
				final long id = photo.getLong("id");
				if (title == null || "".equals(title.trim())) {
					if (dateTaken != null && !"".equals(dateTaken.trim())) {
						System.out.println("Updating photo #" + id + ", title => " + dateTaken);
						final OAuthRequest updateRequest = new OAuthRequest(Verb.POST,
								"https://api.flickr.com/services/rest/?format=json&nojsoncallback=1&method=flickr.photos.setMeta&photo_id="
										+ id + "&title=" + URLEncoder.encode(dateTaken, "UTF-8"),
								oAuth);
						oAuth.signRequest(accessToken, updateRequest);
						final Response updateResponse = updateRequest.send();
						System.out.println(updateResponse.getBody());
						Thread.sleep(1000);
					}
				}
			}
			page = page + 1;
			Thread.sleep(1000);
			//page = 99999;
		}
	}

}
