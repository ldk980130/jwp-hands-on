package com.example.cachecontrol;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.google.common.net.HttpHeaders;

@Component
public class CacheInterceptor implements HandlerInterceptor {

	private static final String CACHE_CONTROL_VALUE = "no-cache, private";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		response.setHeader(HttpHeaders.CACHE_CONTROL, CACHE_CONTROL_VALUE);
		return true;
	}
}
