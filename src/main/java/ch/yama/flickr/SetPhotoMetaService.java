package ch.yama.flickr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import io.mikael.urlbuilder.UrlBuilder;

@Service
@Lazy
public class SetPhotoMetaService extends AbstractUpdateService {

	Logger logger = LoggerFactory.getLogger(SetPhotoMetaService.class);

	private String photoId;
	private String title;

	public SetPhotoMetaService forPhoto(String photoId) {
		this.photoId = photoId;
		return this;
	}

	public SetPhotoMetaService withTitle(String title) {
		this.title = title;
		return this;
	}

	@Override
	protected void checkMandatoryParameters() {
		if (this.photoId == null || "".equals(this.photoId.trim())) {
			throw new IllegalStateException("photo id is mandatory");
		}
	}

	@Override
	protected String getRestMethodName() {
		return "flickr.photos.setMeta";
	}

	@Override
	protected UrlBuilder completeUrlParameters(final UrlBuilder builder) {
		UrlBuilder completedBuilder = builder.addParameter("photo_id", photoId);
		if (title != null && !"".equals(title.trim())) {
			completedBuilder = completedBuilder.addParameter("title", title);
		}
		return completedBuilder;
	}
}
