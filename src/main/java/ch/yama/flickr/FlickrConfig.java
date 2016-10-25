package ch.yama.flickr;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.scribejava.apis.FlickrApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth1AccessToken;
import com.github.scribejava.core.oauth.OAuth10aService;

@Configuration
public class FlickrConfig {

	@Bean
	public OAuth10aService getOAuthService() {
		return new ServiceBuilder().apiKey("ccafdafce413356a9c50ed4b55fdc9dc").apiSecret("2adf86e8bfbd3030")
				.build(FlickrApi.instance());
	}

	@Bean
	public OAuth1AccessToken getAccessToken() {
		return new OAuth1AccessToken("72157672885869996-5722ab2d98cf3680", "221ed6dac14abe89");
	}
}
