package ch.yama.flickr;

import java.util.Collection;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mikael.urlbuilder.UrlBuilder;

public abstract class AbstractCollectionServiceBuilder<T> extends AbstractServiceBuilder {

	Logger logger = LoggerFactory.getLogger(AbstractCollectionServiceBuilder.class);

	private int pauseBetweenPages;

	protected void setPauseBetweePages(final int pauseBetweenPages) {
		this.pauseBetweenPages = pauseBetweenPages;
	}

	public Collection<T> get() {
		checkMandatoryParameters();

		final LazyCollectionProvider<T> lazyCollectionProvider = new LazyCollectionProvider<T>(oAuth, accessToken);

		UrlBuilder builder = UrlBuilder.fromString("https://api.flickr.com/services/rest/")
				.addParameter("format", "json").addParameter("nojsoncallback", "1")
				.addParameter("method", getRestMethodName());

		builder = completeUrlParameters(builder);
		lazyCollectionProvider.setUrlBuilder(builder);

		lazyCollectionProvider.setRootObjectName(getRootObjectName());
		lazyCollectionProvider.setDataObjectName(getDataObjectName());
		lazyCollectionProvider.setPauseBetweenPages(pauseBetweenPages);
		lazyCollectionProvider.setObjectFactory(this::getObject);

		return new LazyCollection<T>(lazyCollectionProvider);
	}

	abstract String getRootObjectName();

	abstract String getDataObjectName();

	abstract T getObject(final JSONObject object);

}
