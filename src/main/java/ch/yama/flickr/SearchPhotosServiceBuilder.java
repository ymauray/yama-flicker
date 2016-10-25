package ch.yama.flickr;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import io.mikael.urlbuilder.UrlBuilder;

@Service
@Lazy
public class SearchPhotosServiceBuilder extends AbstractCollectionServiceBuilder<Photo> {

	Logger logger = LoggerFactory.getLogger(SearchPhotosServiceBuilder.class);

	private String userId;
	private String extras;

	public SearchPhotosServiceBuilder forUser(final String userId) {
		this.userId = userId;
		return this;
	}

	public SearchPhotosServiceBuilder withExtras(final String extras) {
		this.extras = extras;
		return this;
	}

	public SearchPhotosServiceBuilder withPauseBetweenPages(int pagePause) {
		super.setPauseBetweePages(pagePause);
		return this;
	}

	@Override
	protected void checkMandatoryParameters() {
		if (this.userId == null || "".equals(this.userId.trim())) {
			throw new IllegalStateException("user id not set. Call ::forUser first");
		}
	}

	@Override
	protected String getRestMethodName() {
		return "flickr.photos.search";
	}

	@Override
	protected UrlBuilder completeUrlParameters(final UrlBuilder builder) {
		UrlBuilder completedBuilder = builder.addParameter("user_id", this.userId);
		if (this.extras != null && !"".equals(this.extras.trim())) {
			completedBuilder = completedBuilder.addParameter("extras", this.extras);
		}

		return completedBuilder;
	}

	@Override
	String getRootObjectName() {
		return "photos";
	}

	@Override
	String getDataObjectName() {
		return "photo";
	}

	@Override
	Photo getObject(JSONObject object) {
		return new Photo(object);
	}
}
