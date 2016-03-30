package in.clouthink.daas.fss.sample.spring.mvc;

import in.clouthink.daas.fss.rest.ResultResponseBody;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Resolves method arguments annotated with {@code @ResultResponseBody} and handles return values from methods annotated
 * with {@code @ResultResponseBody} by reading and writing to the body of the request or response with an {@link
 * HttpMessageConverter}.
 */
public class ResultResponseHandlerMethodProcessor extends RequestResponseBodyMethodProcessor {

	public ResultResponseHandlerMethodProcessor(final List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
	}

	public ResultResponseHandlerMethodProcessor(final List<HttpMessageConverter<?>> messageConverters,
												final ContentNegotiationManager contentNegotiationManager) {
		super(messageConverters, contentNegotiationManager);
	}

	@Override
	public boolean supportsReturnType(final MethodParameter returnType) {
		return returnType.getMethodAnnotation(ResultResponseBody.class) != null;
	}

	@Override
	public void handleReturnValue(Object returnValue,
								  MethodParameter returnType,
								  ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest) throws IOException, HttpMediaTypeNotAcceptableException {
		if (returnValue == null) {
			super.handleReturnValue(ResultWrapper.succeed(), returnType, mavContainer, webRequest);
		} else if (returnValue instanceof ResultWrapper) {
			super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
		} else if (returnValue instanceof HashMap && ((HashMap) returnValue).containsKey("succeed")) {
			super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
		} else {
			super.handleReturnValue(ResultWrapper.succeed(returnValue), returnType, mavContainer, webRequest);
		}
	}

}
