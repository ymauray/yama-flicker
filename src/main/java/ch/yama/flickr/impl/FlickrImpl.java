package ch.yama.flickr.impl;

import java.io.IOException;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;

import io.mikael.urlbuilder.UrlBuilder;

public abstract class FlickrImpl {

	Logger LOGGER = LoggerFactory.getLogger(FlickrImpl.class);

	private final Flickr m_flickr;

	public FlickrImpl(Flickr flickr) {
		m_flickr = flickr;
	}

	protected UrlBuilder getUrlBuilder() {
		return UrlBuilder
				.fromString("https://api.flickr.com/services/rest")
				.addParameter("format", "json")
				.addParameter("nojsoncallback", "1")
				.addParameter("user_id", "142925583@N05");
	}

	protected void requestPages(UrlBuilder builder, String root, RequestPaginator paginator) throws IOException {
		int page = 1;
		int pages = 99999;
		while (page <= pages) {
			builder.setParameter("page", Integer.toString(page));
			final OAuthRequest request = m_flickr.getRequest(builder);
			final Response response = request.send();
			LOGGER.trace(response.getBody());
			final JSONObject json = new JSONObject(response.getBody());
			final JSONObject payload = json.getJSONObject(root);
			pages = payload.getInt("pages");
			if (!paginator.apply(payload)) {
				break;
			}
			page += 1;
		}
	}
}
