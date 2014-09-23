package com.keran.marinov.itunezfeed.http;

import android.os.AsyncTask;

import com.keran.marinov.itunezfeed.list.FeedEntry;
import com.keran.marinov.itunezfeed.listener.DownloadTaskFeedListener;

import java.util.ArrayList;

public class DownloadFeedTask extends AsyncTask<String, Void, Void> {

    private DownloadTaskFeedListener downloadTaskFeedListener;
    private static final String LOG_TAG = "DownloadFeedTask";
    private static final boolean LOGGING_ENABLED = true;

    public void setDownloadTasFeedListener(DownloadTaskFeedListener downloadTaskFeedListener) {
        this.downloadTaskFeedListener = downloadTaskFeedListener;
    }

    @Override
    protected Void doInBackground(String... urls) {
        ArrayList<FeedEntry> latestFeedList = null;
        HttpConnectionManager httpConnectionManager = new HttpConnectionManager();
        HttpConnectionResponse httpConnectionResponse = httpConnectionManager.getHttpConnectionResponse(new HttpRequestParams(urls[0], HttpConnectionManager.HttpMethod.HTTP_METHOD_GET, null, null, null));

        if (httpConnectionResponse.getHttpConnectionResponseResult() == HttpConnectionManager.HttpConnectionResponseResult.HTTP_CONNECTION_RESPONSE_OK) {
            JsonParserManager jsonParserManager = new JsonParserManager();
            latestFeedList = jsonParserManager.getFeedList(httpConnectionResponse.getHttpResponseBody());
            //L.log(TAG,LOGGING_ENABLED,"OUTPUT :"+httpConnectionResponse.getHttpResponseBody());
        }
        synchronized (this) {
            if (latestFeedList != null && downloadTaskFeedListener != null)
                downloadTaskFeedListener.onUpdateLatestList(latestFeedList);
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        if (downloadTaskFeedListener != null)
            downloadTaskFeedListener.onDownloadTaskCompleted();
            }


}
