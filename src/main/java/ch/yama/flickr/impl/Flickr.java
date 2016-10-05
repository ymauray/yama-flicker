package ch.yama.flickr.impl;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuth1RequestToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import ch.yama.flickr.api.PhotosetsApi;
import io.mikael.urlbuilder.UrlBuilder;

public class Flickr {

	private final OAuth10aService m_oAuth;
	private final OAuth1AccessToken m_accessToken;

	private PhotosetsApi m_photosetsApi;

	private Flickr(OAuth10aService oAuth, OAuth1AccessToken accessToken) {
		m_oAuth = oAuth;
		m_accessToken = accessToken;
	}

	public static Flickr authenticate() throws IOException, URISyntaxException {
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

		return new Flickr(oAuth, accessToken);
	}

	public PhotosetsApi photosets() {
		if (m_photosetsApi == null) {
			m_photosetsApi = new PhotosetsImpl(this);
		}
		return m_photosetsApi;
	}

	public OAuthRequest getRequest(UrlBuilder builder) {
		final OAuthRequest request = new OAuthRequest(Verb.GET, builder.toString(), m_oAuth);
		m_oAuth.signRequest(m_accessToken, request);
		return request;
	}

}
