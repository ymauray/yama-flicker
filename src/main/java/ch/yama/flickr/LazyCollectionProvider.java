package ch.yama.flickr;

import java.io.IOException;
import java.util.function.Function;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth10aService;

import io.mikael.urlbuilder.UrlBuilder;

public class LazyCollectionProvider<T> {

	Logger logger = LoggerFactory.getLogger(LazyCollectionProvider.class);

	private final OAuth10aService oAuth;
	private final OAuth1AccessToken accessToken;

	private UrlBuilder urlBuilder;
	private String rootObjectName;
	private String dataObjectName;

	private int page;
	private int total;
	private int perPage;
	private int pageIndex;
	private int totalIndex;
	private int pauseBetweenPages;
	private JSONArray data;
	private Function<JSONObject, T> factory;


	public LazyCollectionProvider(final OAuth10aService oAuth, OAuth1AccessToken accessToken) {
		this.oAuth = oAuth;
		this.accessToken = accessToken;

		page = 0;
		pageIndex = 0;
		perPage = 0;
		totalIndex = 0;
		pauseBetweenPages = 0;
	}

	public void setUrlBuilder(final UrlBuilder urlBuilder) {
		this.urlBuilder = urlBuilder;
	}

	public void setRootObjectName(String rootObjectName) {
		this.rootObjectName = rootObjectName;
	}

	public void setDataObjectName(String dataObjectName) {
		this.dataObjectName = dataObjectName;
	}

	public void setObjectFactory(Function<JSONObject, T> factory) {
		this.factory = factory;
	}

	public void setPauseBetweenPages(int pauseBetweenPages) {
		this.pauseBetweenPages = pauseBetweenPages;
	}

	void fetch() {
		final UrlBuilder builder = this.urlBuilder.addParameter("page", Integer.toString(++this.page));

		final String url = builder.toUrl().toString();
		final OAuthRequest request = new OAuthRequest(Verb.GET, url, oAuth);
		oAuth.signRequest(accessToken, request);
		final Response response = request.send();
		JSONObject json = null;
		try {
			logger.debug(response.getBody());
			json = new JSONObject(response.getBody());
		} catch (JSONException | IOException e) {
			logger.error(e.getMessage(), e);
		}

		final String stat = json.getString("stat");
		if (!"ok".equals(stat)) {
			logger.error(json.toString(2));
			throw new RuntimeException(new IllegalStateException("Unexpected stat : " + stat));
		}
		final JSONObject rootObject = json.getJSONObject(rootObjectName);
		this.page = rootObject.getInt("page");
		this.perPage = rootObject.getInt("perpage");
		this.total = rootObject.getInt("total");
		this.pageIndex = 0;
		this.data = rootObject.getJSONArray(dataObjectName);
	}

	public int getTotal() {
		return this.total;
	}

	public boolean hasNext() {
		return this.totalIndex < this.total;
	}

	public T next() {
		if (this.totalIndex < this.total) {
			if (this.pageIndex >= this.perPage) {
				if (this.pauseBetweenPages > 0) {
					try {
						logger.info("Pausing " + pauseBetweenPages + " milliseconds");
						Thread.sleep(pauseBetweenPages);
					} catch (final InterruptedException e) {
						logger.error(e.getMessage(), e);
					}
				}
				fetch();
			}
			this.totalIndex += 1;
			return factory.apply(this.data.getJSONObject(this.pageIndex++));
		} else {
			return null;
		}
	}

}
