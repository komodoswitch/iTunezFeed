package com.keran.marinov.itunezfeed.ui.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.keran.marinov.itunezfeed.R;
import com.keran.marinov.itunezfeed.http.DownloadFeedTask;
import com.keran.marinov.itunezfeed.http.NetworkStatusManager;
import com.keran.marinov.itunezfeed.list.FeedEntry;
import com.keran.marinov.itunezfeed.list.FeedListAdapter;
import com.keran.marinov.itunezfeed.list.SavedListParcelable;
import com.keran.marinov.itunezfeed.listener.DownloadTaskFeedListener;
import com.keran.marinov.itunezfeed.listener.FragmentListener;

import java.util.ArrayList;
import java.util.concurrent.RejectedExecutionException;


public class TopListFragment extends Fragment implements Button.OnClickListener, DownloadTaskFeedListener {

    private final String LOG_TAG = "TopListFragment";
    private final String BUNDLE_SAVED_LIST = "BUNDLE_SAVED_LIST";
    private static final String FEED_URL = "https://itunes.apple.com/us/rss/topsongs/limit=200/json";
    private ArrayList<FeedEntry> feedList = new ArrayList<FeedEntry>();
    private ListView feedListView = null;
    private Button refreshFeedButton = null;
    private FeedListAdapter feedListAdapter = null;
    private AlertDialog networkStatusDialog = null;
    private boolean wasNetworkStatusDialogShowing = false;
    private FragmentListener mListener = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View rootView = inflater.inflate(R.layout.top_list_fragment_layout, container, false);
        feedListView = (ListView) rootView.findViewById(R.id.feed_list_view);
        refreshFeedButton = (Button) rootView.findViewById(R.id.refresh_feed_button);
        refreshFeedButton.setOnClickListener(this);
        feedListAdapter = new FeedListAdapter(this.getActivity(), feedList);
        feedListView.setAdapter(feedListAdapter);
        restoreStateFromBundle(bundle);
        feedListView.setOnItemClickListener
                (
                        new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                if(feedList != null && feedList.size() >0) {
                                    FeedEntry feedEntry = feedList.get(position);
                                    if(mListener != null)
                                        mListener.onLaunchTopListOptionsDialog(position,feedEntry);

                                }
                            }
                        }
                );

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (FragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == refreshFeedButton.getId())
            loadFeed();
    }

    private void updateFeedList(ArrayList<FeedEntry> list) {
        if (feedList != null) {
            feedList.clear();
            feedList.addAll(list);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        saveStateToBundle(outState);
        super.onSaveInstanceState(outState);
    }

    private void restoreStateFromBundle(Bundle bundle) {
        if (bundle == null) {
            loadFeed();
            Log.d(LOG_TAG, "LOAD FROM NETWORK BUNDLE IS NULL");
        } else {
            SavedListParcelable savedListParcelable = (SavedListParcelable) bundle.getParcelable(BUNDLE_SAVED_LIST);
            if (savedListParcelable != null)
            {
                ArrayList<FeedEntry> saveList = savedListParcelable.getSavedList();
                if (saveList != null && saveList.size() > 0) {
                    updateFeedList(saveList);
                    if (feedListAdapter != null)
                        feedListAdapter.notifyDataSetChanged();
                    Log.d(LOG_TAG, "restoreStateFromBundle - LOAD FROM SAVED LIST SIZE :" + saveList.size());
                }
            }
        }
    }

    private void saveStateToBundle(Bundle outState) {
        if (outState != null) {
            if (feedList != null && feedList.size() > 0) {
                SavedListParcelable savedListParcelable = new SavedListParcelable();
                savedListParcelable.setSavedList(feedList);
                outState.putParcelable(BUNDLE_SAVED_LIST, savedListParcelable);
                Log.d(LOG_TAG, "saveStateToBundle :" + feedList.size());
            }
        }
    }

    private void loadFeed() {
        if (NetworkStatusManager.getInstance(this.getActivity()).isConnectionAvailable()) {
            try {
                DownloadFeedTask downloadFeedTask = new DownloadFeedTask();
                downloadFeedTask.setDownloadTasFeedListener(this);
                downloadFeedTask.execute(FEED_URL);
            } catch (RejectedExecutionException e) {
                e.printStackTrace();
            }
        } else {
            if (mListener != null)
                mListener.onLaunchNoNetworkConnectionDialog();
        }

    }

    @Override
    public void onUpdateLatestList(ArrayList<FeedEntry> latestList) {
        updateFeedList(latestList);
    }

    @Override
    public void onDownloadTaskCompleted() {
        if (feedListAdapter != null)
            feedListAdapter.notifyDataSetChanged();
    }



}
