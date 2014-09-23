package com.keran.marinov.itunezfeed.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.keran.marinov.itunezfeed.R;
import com.keran.marinov.itunezfeed.common.L;
import com.keran.marinov.itunezfeed.database.FavouritesTable;
import com.keran.marinov.itunezfeed.database.ItunezFeedContentProviderManager;
import com.keran.marinov.itunezfeed.list.FeedEntry;
import com.keran.marinov.itunezfeed.listener.FragmentListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class FavouritesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private final String LOG_TAG = "FavouritesListFragment";
    private final boolean LOG_ENABLED = true;
    private final String BUNDLE_FAVOURITES_SAVED_LIST = "BUNDLE_FAVOURITES_SAVED_LIST";
    private ArrayList<FeedEntry> feedList = new ArrayList<FeedEntry>();
    private ListView feedListView = null;
    private FragmentListener mListener = null;
    private SimpleCursorAdapter simpleCursorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View rootView = inflater.inflate(R.layout.favourites_fragment_layout, container, false);
        feedListView = (ListView) rootView.findViewById(R.id.feed_list_view);
        simpleCursorAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.feed_list_item, null,
                new String[] {FavouritesTable.FAVOURITES_SONG_TITLE,FavouritesTable.FAVOURITES_SONG_IMAGE_URL},
                new int[] {  R.id.title_text_view, R.id.album_image_view });
        feedListView.setAdapter(simpleCursorAdapter);
        simpleCursorAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            /** Binds the Cursor column defined by the specified index to the specified view */
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (view.getId() == R.id.album_image_view) {
                    if (cursor != null) {
                        String imageUrl = cursor.getString(cursor.getColumnIndex(FavouritesTable.FAVOURITES_SONG_IMAGE_URL));
                        if (imageUrl != null)
                            Picasso.with(FavouritesFragment.this.getActivity()).load(imageUrl).into(((ImageView) view));
                    }
                    return true;
                }
                return false;
            }
        });
        getLoaderManager().initLoader(0, null, this);
        feedListView.setOnItemClickListener
                (
                        new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                TextView text = (TextView) view.findViewById(R.id.title_text_view);
                                String songTitle = text.getText().toString();

                               if(mListener != null)
                                   mListener.onLaunchFavouritesListOptionsDialog(songTitle);

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
    public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        ItunezFeedContentProviderManager itunezFeedContentProviderManager = new ItunezFeedContentProviderManager(this.getActivity());
        CursorLoader cursorLoader = itunezFeedContentProviderManager.getFavouritesTableCursorLoader();

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if(cursor == null)
        {
            L.log(LOG_TAG,LOG_ENABLED,"CURSOR IS NULL");
        }
        else
        {
            if(cursor.moveToFirst())
            {
                Log.i("FAVOURITES TABLE SONG ID :", cursor.getString(cursor.getColumnIndex(FavouritesTable.FAVOURITES_SONG_ID)) + "=================");
                Log.i("FAVOURITES TABLE SONG TITLE :",cursor.getString(cursor.getColumnIndex(FavouritesTable.FAVOURITES_SONG_TITLE)) + "=================");
                Log.i("FAVOURITES TABLE SONG IMAGE URL :",cursor.getString(cursor.getColumnIndex(FavouritesTable.FAVOURITES_SONG_IMAGE_URL)) + "=================");
            }
            else
            {
                L.log(LOG_TAG,LOG_ENABLED,"CURSOR DID NOT MOVE TO FIRST");
            }


        }

        simpleCursorAdapter.swapCursor(cursor);
        simpleCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        simpleCursorAdapter.swapCursor(null);
    }
}