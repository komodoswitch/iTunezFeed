package com.keran.marinov.itunezfeed.listener;

import com.keran.marinov.itunezfeed.list.FeedEntry;

import java.util.ArrayList;


public interface DownloadTaskFeedListener {

    public void onUpdateLatestList(ArrayList<FeedEntry> latestList);

    public void onDownloadTaskCompleted();
}
