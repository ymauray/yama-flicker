package ch.yama.flickr;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class UpdateUploadDate {

	private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder().append(DateTimeFormatter.ISO_DATE)
			.appendLiteral(' ').append(DateTimeFormatter.ISO_TIME).toFormatter();

	public static void main(final String[] args) throws IOException, URISyntaxException, InterruptedException {

		final Pattern pattern = Pattern
				.compile("^([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2})\\.([0-9]{2})\\.([0-9]{2}).*$");

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
			System.out.println(response.getBody());
			final JSONObject json = new JSONObject(response.getBody());
			final JSONObject photos = json.getJSONObject("photos");
			pages = photos.getInt("pages");
			final long total = photos.getLong("perpage");
			final JSONArray photoArray = photos.getJSONArray("photo");
			for (int i = 0; i < photoArray.length(); i++) {
				final JSONObject photo = photoArray.getJSONObject(i);
				LocalDateTime.parse(photo.getString("datetaken"), UpdateUploadDate.formatter);
				LocalDateTime.ofInstant(Instant.ofEpochSecond(photo.getLong("dateupload")), ZoneId.systemDefault());
				final long id = photo.getLong("id");
				if (photo.getInt("datetakenunknown") == 1) {
					final String title = photo.getString("title");
					final Matcher matcher = pattern.matcher(title);
					if (matcher.matches()) {
						final String dateTaken = "" + matcher.group(1) + "-" + matcher.group(2) + "-" + matcher.group(3)
								+ "%20" + matcher.group(4) + ":" + matcher.group(5) + ":" + matcher.group(6);
						System.out.println("Page " + page + " of " + pages + ", photo " + photo.getLong("id") + " ("
								+ (i + 1) + "/" + total + ") : mise à jour de la date de prise de vue : " + dateTaken);

						final OAuthRequest updateRequest = new OAuthRequest(Verb.POST,
								"https://api.flickr.com/services/rest/?format=json&nojsoncallback=1&method=flickr.photos.setDates&photo_id="
										+ id + "&date_taken=" + dateTaken + "&date_taken_granularity=0",
								oAuth);
						oAuth.signRequest(accessToken, updateRequest);
						final Response updateResponse = updateRequest.send();
						System.out.println(updateResponse.getBody());
						Thread.sleep(1000);
					}
				}
				/*
				 * if (!dateUpload.equals(dateTaken)) {
				 * System.out.println("Page " + page + " of " + pages +
				 * ", photo " + photo.getLong("id") + " (" + (i + 1) + "/" +
				 * total + ") : mise à jour de la date de téléchargement, " +
				 * dateUpload + " -> " + dateTaken); final long newDateUpload =
				 * dateTaken.atZone(ZoneId.systemDefault()).toEpochSecond();
				 * System.out.println("Date de publication : " + newDateUpload);
				 * final OAuthRequest updateRequest = new
				 * OAuthRequest(Verb.POST,
				 * "https://api.flickr.com/services/rest/?format=json&nojsoncallback=1&method=flickr.photos.setDates&photo_id="
				 * + id + "&date_posted=" + newDateUpload, oAuth);
				 * oAuth.signRequest(accessToken, updateRequest); final Response
				 * updateResponse = updateRequest.send();
				 * System.out.println(updateResponse.getBody());
				 * Thread.sleep(1000); }
				 */
			}
			page = page + 1;
		}
	}

}
