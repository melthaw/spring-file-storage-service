package in.clouthink.daas.fss.zimg.impl;

import com.google.api.client.http.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import in.clouthink.daas.fss.zimg.exception.ZimgDeleteException;
import in.clouthink.daas.fss.zimg.exception.ZimgDownloadException;
import in.clouthink.daas.fss.zimg.exception.ZimgHttpException;
import in.clouthink.daas.fss.zimg.exception.ZimgUploadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.MalformedInputException;

/**
 * @author dz
 * @since 3
 */
public class HttpClient {

    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    public GenericJson upload(String uploadEndpoint,
                              String contentType,
                              long size,
                              InputStream inputStream) throws IOException {
        try {
            final JsonObjectParser jsonObjectParser = new JsonObjectParser(JSON_FACTORY);
            HttpRequestFactory requestFactory =
                    HTTP_TRANSPORT.createRequestFactory(request -> request.setParser(jsonObjectParser));

            InputStreamContent inputStreamContent = new InputStreamContent(contentType, inputStream);
            inputStreamContent.setLength(size);

            HttpRequest request = requestFactory.buildPostRequest(new GenericUrl(uploadEndpoint),
                                                                  inputStreamContent);

            HttpHeaders headers = request.getHeaders();
            headers.setContentType(contentType);
            headers.setAccept("application/json");

            HttpResponse httpResponse = request.execute();
            httpResponse.getStatusCode();

            return httpResponse.parseAs(GenericJson.class);
        } catch (MalformedInputException e) {
            throw new ZimgHttpException(e);
        } catch (HttpResponseException e) {
            throw new ZimgUploadException(e.getStatusMessage());
        }
    }

    public GenericJson info(String url) throws IOException {
        final JsonObjectParser jsonObjectParser = new JsonObjectParser(JSON_FACTORY);
        HttpRequestFactory requestFactory =
                HTTP_TRANSPORT.createRequestFactory(request -> request.setParser(jsonObjectParser));
        return requestFactory.buildGetRequest(new GenericUrl(url)).execute().parseAs(GenericJson.class);
    }

    public void delete(String url) throws IOException {
        try {
            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();
            requestFactory.buildGetRequest(new GenericUrl(url)).execute();
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 403 || e.getStatusCode() == 404) {
                throw new ZimgDeleteException(e.getStatusMessage());
            }
            throw new ZimgHttpException(e);
        }
    }

    public void download(String url, OutputStream outputStream) throws IOException {
        try {
            HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory();
            requestFactory.buildGetRequest(new GenericUrl(url)).execute().download(outputStream);
        } catch (HttpResponseException e) {
            if (e.getStatusCode() == 404) {
                throw new ZimgDownloadException(e.getStatusMessage());
            }
            throw new ZimgHttpException(e);
        }
    }

}
