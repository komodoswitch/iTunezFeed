package com.keran.marinov.itunezfeed.ui.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.keran.marinov.itunezfeed.list.FeedEntry;
import com.keran.marinov.itunezfeed.listener.FragmentListener;
import com.keran.marinov.itunezfeed.ui.fragment.FavouritesFragment;
import com.keran.marinov.itunezfeed.R;
import com.keran.marinov.itunezfeed.listener.TabListener;
import com.keran.marinov.itunezfeed.ui.fragment.TopListFragment;
import com.keran.marinov.itunezfeed.dialog.DialogManager;

public class ItunezFeedActivity extends Activity implements FragmentListener {

    private static final String LOG_TAG = "ItunezFeedActivity";
    private static final boolean LOGGING_ENABLED = true;
    private final String BUNDLE_SAVED_TAB = "BUNDLE_SAVED_TAB";
    private final String NETWORK_STATUS_DIALOG_STATE = "NETWORK_STATUS_DIALOG_STATE";
    private final String TOP_LIST_OPTIONS_DIALOG_STATE = "TOP_LIST_OPTIONS_DIALOG_STATE";
    private final String FAVOURITES_LIST_DIALOG_STATE = "FAVOURITES_LIST_DIALOG_STATE";
    private final String LAST_TOP_LIST_FEED_ENTRY_SELECTION = "LAST_TOP_LIST_FEED_ENTRY_SELECTION";
    private final String LAST_TOP_LIST_OPTIONS_POSITION = "LAST_TOP_LIST_OPTIONS_POSITION";
    private final String LAST_FAVOURITES_LIST_OPTIONS_SONG_TITLE = "LAST_FAVOURITES_LIST_OPTIONS_SONG_TITLE";
    private DialogManager dialogManager;
    private FeedEntry lastTopListFeedEntrySelection ;
    private int lastTopListOptionPosition = 0;
    private String lastFavouritesListOptionsSongTitle = "";

    @Override
    protected void onDestroy()
    {
        super.onDestroy();;

        if(dialogManager != null)
            dialogManager.hideAllDialogs();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialogManager = new DialogManager(this);
        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        bar.addTab(bar.newTab().setText(this.getString(R.string.top_list_fragment_indicator_string))
                .setTabListener(new TabListener(this, this.getString(R.string.top_list_fragment_indicator_string), TopListFragment.class)));
        bar.addTab(bar.newTab().setText(this.getResources().getString(R.string.favourites_fragment_indicator_string))
                .setTabListener(new TabListener(this, this.getResources().getString(R.string.favourites_fragment_indicator_string), FavouritesFragment.class)));

        if (savedInstanceState != null) {
            restoreStateFromBundle(bar,savedInstanceState);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateToBundle(outState);
    }

    private void saveStateToBundle(Bundle outState)
    {
        outState.putInt(BUNDLE_SAVED_TAB, getActionBar().getSelectedNavigationIndex());
        outState.putBoolean(NETWORK_STATUS_DIALOG_STATE,this.dialogManager.isNetworkStatusDialogShowing());
        outState.putBoolean(TOP_LIST_OPTIONS_DIALOG_STATE,this.dialogManager.isTopListOptionsDialogShowing());
        outState.putBoolean(FAVOURITES_LIST_DIALOG_STATE,this.dialogManager.isFavouritesListOptionsShowing());

        outState.putParcelable(this.LAST_TOP_LIST_FEED_ENTRY_SELECTION,this.getLastTopListFeedEntrySelection());
        outState.putInt(this.LAST_TOP_LIST_OPTIONS_POSITION,this.getLastTopListOptionPosition());
        outState.putString(this.LAST_FAVOURITES_LIST_OPTIONS_SONG_TITLE,this.getLastFavouritesListOptionsSongTitle());
    }

    private void restoreStateFromBundle(ActionBar bar, Bundle bundle)
    {
        bar.setSelectedNavigationItem(bundle.getInt(BUNDLE_SAVED_TAB, 0));

        boolean wasNetworkDialogShowing = bundle.getBoolean(NETWORK_STATUS_DIALOG_STATE);
        boolean wasTopListOptionsDialogShowing = bundle.getBoolean(TOP_LIST_OPTIONS_DIALOG_STATE);
        boolean wasFavouritesListOptionsDialogShow = bundle.getBoolean(FAVOURITES_LIST_DIALOG_STATE);
        this.setLastTopListFeedEntrySelection((FeedEntry)bundle.getParcelable(LAST_TOP_LIST_FEED_ENTRY_SELECTION));
        this.setLastTopListOptionPosition(bundle.getInt(this.LAST_TOP_LIST_OPTIONS_POSITION));
        this.setLastFavouritesListOptionsSongTitle(bundle.getString(this.LAST_FAVOURITES_LIST_OPTIONS_SONG_TITLE));

        if(dialogManager != null)
        {
            if (wasNetworkDialogShowing)
                dialogManager.launchNoNetworkConnectionDialog();
            if(wasTopListOptionsDialogShowing)
                dialogManager.launchTopListOptions(getLastTopListOptionPosition(),this.getLastTopListFeedEntrySelection());
            if(wasFavouritesListOptionsDialogShow)
                dialogManager.launchFavouritesListOptions(this.getLastFavouritesListOptionsSongTitle());
        }




    }

    public FeedEntry getLastTopListFeedEntrySelection() {
        return lastTopListFeedEntrySelection;
    }


    public void setLastTopListFeedEntrySelection(FeedEntry lastTopListFeedEntrySelection) {
        this.lastTopListFeedEntrySelection = lastTopListFeedEntrySelection;
    }



    public void setLastTopListOptionPosition(int lastTopListOptionPosition) {
        this.lastTopListOptionPosition = lastTopListOptionPosition;
    }

    public void setLastFavouritesListOptionsSongTitle(String lastFavouritesListOptionsSongTitle) {
        this.lastFavouritesListOptionsSongTitle = lastFavouritesListOptionsSongTitle;
    }
    public int getLastTopListOptionPosition() {
        return lastTopListOptionPosition;
    }

    public String getLastFavouritesListOptionsSongTitle() {
        return lastFavouritesListOptionsSongTitle;
    }
    public void onFragmentInteraction(Uri uri)
    {


    }

    @Override
    public void onLaunchNoNetworkConnectionDialog()
    {
        if (dialogManager != null)
            dialogManager.launchNoNetworkConnectionDialog();
    }

    @Override
    public void onLaunchTopListOptionsDialog(int position, FeedEntry feedEntry) {

       this.setLastTopListOptionPosition(position);
       this.setLastTopListFeedEntrySelection(feedEntry);
        if(dialogManager != null)
            dialogManager.launchTopListOptions(position,feedEntry);
    }

    @Override
    public void onLaunchFavouritesListOptionsDialog(String songTitle) {
        this.setLastFavouritesListOptionsSongTitle(songTitle);
        if(dialogManager != null)
            dialogManager.launchFavouritesListOptions(songTitle);
    }



}

