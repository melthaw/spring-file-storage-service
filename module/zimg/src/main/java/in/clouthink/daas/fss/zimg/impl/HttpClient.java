package in.clouthink.daas.fss.zimg.impl;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import in.clouthink.daas.fss.zimg.exception.ZimgHttpException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.MalformedInputException;

public class HttpClient {

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();


    public static GenericJson upload(String uploadEndpoint,
                                     String contentType,
                                     long size,
                                     InputStream inputStream) {
        try {
            final JsonObjectParser jsonObjectParser = new JsonObjectParser(JSON_FACTORY);
            HttpRequestFactory requestFactory =
                    HTTP_TRANSPORT.createRequestFactory(request -> request.setParser(jsonObjectParser));

            InputStreamContent inputStreamContent = new InputStreamContent(contentType, inputStream);
            inputStreamContent.setLength(size);

            HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(uploadEndpoint),
                                                                  inputStreamContent);

            request.setLoggingEnabled(true);
            request.setCurlLoggingEnabled(true);

            HttpHeaders headers = request.getHeaders();
            headers.setContentType(contentType);
            headers.setAccept("application/json");

            HttpResponse httpResponse = request.execute();
            httpResponse.getStatusCode();

            return httpResponse.parseAs(GenericJson.class);
        } catch (MalformedInputException e) {
            throw new ZimgHttpException(e);
        } catch (IOException e) {
            throw new ZimgHttpException(e);
        }
    }

    public static void delete(String url) {
        try {
            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();
            requestFactory.buildGetRequest(new GenericUrl(url)).execute();
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 403 || e.getStatusCode() == 404) {
                throw new ZimgHttpException(e.getStatusMessage());
            }
            throw new ZimgHttpException(e);
        } catch (IOException e) {
            throw new ZimgHttpException(e);
        }
    }
}
