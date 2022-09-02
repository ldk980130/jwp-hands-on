package com.example.etag;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration
public class EtagFilterConfiguration {

	private static final String ETAG_URL_PATH = "/etag";

	@Bean
	public FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
		FilterRegistrationBean<ShallowEtagHeaderFilter> filterRegistrationBean =
			new FilterRegistrationBean<>(new ShallowEtagHeaderFilter());
		filterRegistrationBean.addUrlPatterns(ETAG_URL_PATH);
		return filterRegistrationBean;
	}
}
