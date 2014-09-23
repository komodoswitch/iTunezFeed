package com.keran.marinov.itunezfeed.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpConnectionManager {


    private final String LOG_TAG = "HttpConnectionManager";
    private static final boolean LOG_ENABLED = false;
    private HttpConnectionResponseResult httpConnectionResponseResult = HttpConnectionResponseResult.HTTP_CONNECTION_RESPONSE_DEFAULT;

    public enum HttpConnectionResponseResult {
        HTTP_CONNECTION_RESPONSE_OK,
        HTTP_CONNECTION_RESPONSE_BODY_ERROR,
        HTTP_CONNECTION_RESPONSE_IO_EXCEPTION,
        HTTP_CONNECTION_RESPONSE_CLIENT_PROTOCOL_EXCEPTION,
        HTTP_CONNECTION_RESPONSE_GENERAL_EXCEPTION,
        HTTP_CONNECTION_RESPONSE_DEFAULT
    };

    public enum HttpMethod {
        HTTP_METHOD_GET,
        HTTP_METHOD_POST
    };

    public enum HttpMethodBody {
        HTTP_METHOD_HAS_JSON_BODY,
        HTTP_METHOD_HAS_NO_JSON_BODY
    };

    private HttpConnectionResponseResult getHttpConnectionResponseResult() {
        return this.httpConnectionResponseResult;
    }

    private void setHttpConnectionResponseResult(HttpConnectionResponseResult httpConnectionResponseResult) {
        this.httpConnectionResponseResult = httpConnectionResponseResult;
    }

    public HttpConnectionResponse getHttpConnectionResponse(HttpRequestParams httpRequestParams) {

        String httpResponseBody = "";
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
        HttpConnectionParams.setSoTimeout(httpParams, 20000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpPost httpPost = new HttpPost(httpRequestParams.getHttpUrl());
        HttpGet httpGet = new HttpGet(httpRequestParams.getHttpUrl());

        int httpStatusCode = -1;
        try {
            HttpResponse response = null;

            if (httpRequestParams.getHttpMethod() == HttpMethod.HTTP_METHOD_POST) {
                if (httpRequestParams.getHttpMethodBody() == HttpMethodBody.HTTP_METHOD_HAS_JSON_BODY && httpRequestParams.getJsonObject() != null) {
                    StringEntity stringEntity = new StringEntity(httpRequestParams.getJsonObject().toString());
                    stringEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    httpPost.setEntity(stringEntity);
                }

                if (httpRequestParams.getHttpPostParams() != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(httpRequestParams.getHttpPostParams()));
                }

                response = httpClient.execute(httpPost);
            } else if (httpRequestParams.getHttpMethod() == HttpMethod.HTTP_METHOD_GET) {
                response = httpClient.execute(httpGet);
            }

            if (response != null) {
                StatusLine statusLine = response.getStatusLine();
                if (statusLine != null) {
                    httpStatusCode = statusLine.getStatusCode();
                    HttpEntity responseEntity = response.getEntity();
                    if (responseEntity != null) {
                        String responseBody = EntityUtils.toString(responseEntity);
                        if (responseBody != null) {
                            httpResponseBody = responseBody;
                            setHttpConnectionResponseResult(HttpConnectionResponseResult.HTTP_CONNECTION_RESPONSE_OK);
                        } else
                            setHttpConnectionResponseResult(HttpConnectionResponseResult.HTTP_CONNECTION_RESPONSE_BODY_ERROR);
                    } else
                        setHttpConnectionResponseResult(HttpConnectionResponseResult.HTTP_CONNECTION_RESPONSE_BODY_ERROR);
                } else
                    setHttpConnectionResponseResult(HttpConnectionResponseResult.HTTP_CONNECTION_RESPONSE_BODY_ERROR);
            } else
                setHttpConnectionResponseResult(HttpConnectionResponseResult.HTTP_CONNECTION_RESPONSE_BODY_ERROR);
        } //catch end
        catch (ClientProtocolException e) {
            e.printStackTrace();
            setHttpConnectionResponseResult(HttpConnectionResponseResult.HTTP_CONNECTION_RESPONSE_CLIENT_PROTOCOL_EXCEPTION);
        } catch (IOException e) {
            e.printStackTrace();
            setHttpConnectionResponseResult(HttpConnectionResponseResult.HTTP_CONNECTION_RESPONSE_IO_EXCEPTION);
        } catch (Exception e) {
            e.printStackTrace();
            setHttpConnectionResponseResult(HttpConnectionResponseResult.HTTP_CONNECTION_RESPONSE_GENERAL_EXCEPTION);
        }
        return new HttpConnectionResponse(httpResponseBody, getHttpConnectionResponseResult());
    }

}
