package com.example.version;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CacheBustingWebConfig implements WebMvcConfigurer {

	public static final String PREFIX_STATIC_RESOURCES = "/resources";

	private final VersionHandlebarsHelper handlebarsHelper;

	@Autowired
	public CacheBustingWebConfig(VersionHandlebarsHelper handlebarsHelper) {
		this.handlebarsHelper = handlebarsHelper;
	}

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler(handlebarsHelper.staticUrls("/**"))
			.addResourceLocations("classpath:/static/")
			.setCacheControl(CacheControl
				.maxAge(Duration.ofDays(365))
				.cachePublic()
			);
	}
}
