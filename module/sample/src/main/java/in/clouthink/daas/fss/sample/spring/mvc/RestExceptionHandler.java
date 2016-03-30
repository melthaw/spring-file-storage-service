package in.clouthink.daas.fss.sample.spring.mvc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Log logger = LogFactory.getLog(RestExceptionHandler.class);

	@ExceptionHandler({RuntimeException.class})
	protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e,
	                                                      WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		logger.error(e, e);
		return handleExceptionInternal(e,
		                               ResultWrapper.failure(e),
		                               headers,
		                               HttpStatus.OK,
		                               request);
	}

}
