package ch.yama.flickr;

import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;

import io.mikael.urlbuilder.UrlBuilder;

public abstract class AbstractUpdateService extends AbstractServiceBuilder {

	Logger logger = LoggerFactory.getLogger(AbstractUpdateService.class);

	public JSONObject post() {
		checkMandatoryParameters();

		UrlBuilder builder = UrlBuilder.fromString("https://api.flickr.com/services/rest/")
				.addParameter("format", "json").addParameter("nojsoncallback", "1")
				.addParameter("method", getRestMethodName());

		builder = completeUrlParameters(builder);

		final String url = builder.toUrl().toString();
		final OAuthRequest updateRequest = new OAuthRequest(Verb.POST, url, oAuth);
		oAuth.signRequest(accessToken, updateRequest);
		final Response updateResponse = updateRequest.send();
		try {
			return new JSONObject(updateResponse.getBody());
		} catch (final IOException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}
}
