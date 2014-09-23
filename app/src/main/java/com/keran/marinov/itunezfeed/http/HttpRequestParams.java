package com.keran.marinov.itunezfeed.http;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.List;



public class HttpRequestParams
{
    private static final String LOG_TAG = "HttpRequestParams";
    private static final boolean LOGGING_ENABLED = true;
	private String httpUrl;
	private HttpConnectionManager.HttpMethod httpMethod;
	private List<NameValuePair> postParams; 
	private HttpConnectionManager.HttpMethodBody httpMethodBody;
	private JSONObject jsonObject;

	public HttpRequestParams(
			String httpUrl, 
			HttpConnectionManager.HttpMethod httpMethod,
			List<NameValuePair> postParams, 
			HttpConnectionManager.HttpMethodBody httpMethodBody,
			JSONObject jsonObject
			) 
	{
		this.httpUrl = httpUrl;
		this.httpMethod = httpMethod;
		this.postParams = postParams;
		this.httpMethodBody = httpMethodBody;
		this.jsonObject = jsonObject;
		
		
	}

	public String getHttpUrl()
	{
		return httpUrl;
	}

	public HttpConnectionManager.HttpMethod getHttpMethod()
	{
		return httpMethod;
	}

	public List<NameValuePair> getHttpPostParams()
	{
		return postParams;
	}

	public HttpConnectionManager.HttpMethodBody getHttpMethodBody()
	{
		return httpMethodBody;
	}

	public JSONObject getJsonObject()
	{
		return jsonObject;
	}

	public void setHttpUrl(String httpUrl)
	{
		this.httpUrl = httpUrl;
	}

	public void setHttpMethod(HttpConnectionManager.HttpMethod httpMethod)
	{
		this.httpMethod = httpMethod;
	}

	public void setPostParams(List<NameValuePair> postParams)
	{
		this.postParams = postParams;
	}

	public void setHttpMethodBody(HttpConnectionManager.HttpMethodBody httpMethodBody)
	{
		this.httpMethodBody = httpMethodBody;
	}

	public void setJsonObject(JSONObject jsonObject)
	{
		this.jsonObject = jsonObject;
	}
}
