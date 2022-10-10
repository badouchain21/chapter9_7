package com.bdc.api.rest.interceptor;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 提取 response 的 body
 * @author MSI-PC
 *
 */
@ControllerAdvice
public class InterceptResponse implements ResponseBodyAdvice<Object> {
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}
	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		ServletServerHttpRequest req=(ServletServerHttpRequest)request;
		HttpServletRequest servletRequest = req.getServletRequest();
		servletRequest.setAttribute("responseBody", body);
		return body;
	}
}
