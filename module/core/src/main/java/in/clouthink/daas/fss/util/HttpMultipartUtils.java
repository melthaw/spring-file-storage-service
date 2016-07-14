package in.clouthink.daas.fss.util;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;

/**
* @author dz
 */
public abstract class HttpMultipartUtils {

	public static MultipartFile resolveMultipartFile(HttpServletRequest request) {
		if (!(request instanceof MultipartHttpServletRequest)) {
			return null;
		}

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Iterator<String> it = multipartRequest.getFileNames();
		if (it == null || !it.hasNext()) {
			return null;
		}

		return multipartRequest.getFile(it.next());
	}

}
