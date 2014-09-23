package com.keran.marinov.itunezfeed.listener;

import com.keran.marinov.itunezfeed.list.FeedEntry;


public interface FragmentListener {
    public void onLaunchNoNetworkConnectionDialog();
    public void onLaunchTopListOptionsDialog(int position,FeedEntry feedEntry);
    public void onLaunchFavouritesListOptionsDialog(String songTitle);
}
