package ch.yama.flickr;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.oauth.OAuth10aService;

import io.mikael.urlbuilder.UrlBuilder;

public abstract class AbstractServiceBuilder {

	@Autowired
	OAuth1AccessToken accessToken;

	@Autowired
	OAuth10aService oAuth;

	protected abstract void checkMandatoryParameters();

	protected abstract String getRestMethodName();

	protected abstract UrlBuilder completeUrlParameters(final UrlBuilder builder);

}
