package com.keran.marinov.itunezfeed.dialog;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.keran.marinov.itunezfeed.R;
import com.keran.marinov.itunezfeed.database.ItunezFeedContentProviderManager;
import com.keran.marinov.itunezfeed.list.FeedEntry;

public class DialogManager {

    private static final String LOG_TAG = "DialogManager";
    private static final boolean LOGGING_ENABLED = true;
    private Context context;
    private AlertDialog networkStatusDialog;
    private AlertDialog topListOptionsDialog;
    private AlertDialog favouritesListOptionsDialog;
    private boolean isTopListOptionsDialogShowing = false;
    private boolean isFavouritesListOptionsShowing = false;
    private boolean isNetworkStatusDialogShowing = false;
    private ItunezFeedContentProviderManager itunezFeedContentProviderManager;

    public DialogManager(Context context)
    {
        this.context = context;
        itunezFeedContentProviderManager = new ItunezFeedContentProviderManager(this.context);
    }

    public void launchTopListOptions(final int position,final FeedEntry feedEntry)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(feedEntry.getTitle())
                .setCancelable(true)
                .setTitle(context.getResources().getString(R.string.options_string))
                .setPositiveButton(context.getString(R.string.dialog_download_image_button_string), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        downloadImage(feedEntry.getImageUrl(),position);
                        dialog.dismiss();
                        setTopListOptionsDialogShowing(false);
                    }
                })
                .setNegativeButton(context.getString(R.string.dialog_save_favourite_button_string), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        addNewFavourite(feedEntry);
                        dialog.dismiss();
                        setTopListOptionsDialogShowing(false);
                    }
                })
                .setNeutralButton(context.getResources().getString(R.string.dialog_close_button_string), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        setTopListOptionsDialogShowing(false);
                    }
                });
        topListOptionsDialog = builder.create();
        if(!topListOptionsDialog.isShowing())
        {
            topListOptionsDialog.show();
            setTopListOptionsDialogShowing(true);
        }
    }

    public void launchFavouritesListOptions(final String songTitle)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(songTitle)
                .setCancelable(true)
                .setTitle(context.getString(R.string.options_string))
                .setPositiveButton(context.getString(R.string.dialog_delete_favourite_button_string), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteFavouriteSong(songTitle);
                        dialog.dismiss();
                        setFavouritesListOptionsDialogShowing(false);
                    }
                })
                .setNegativeButton(context.getString(R.string.dialog_close_button_string), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        setFavouritesListOptionsDialogShowing(false);
                    }
                });
        favouritesListOptionsDialog = builder.create();
        if(!favouritesListOptionsDialog.isShowing())
        {
            favouritesListOptionsDialog.show();
            setFavouritesListOptionsDialogShowing(true);
        }
    }


    public void launchNoNetworkConnectionDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getResources().getString(R.string.dialog_network_connection_error_string))
                .setCancelable(true)
                .setTitle(context.getResources().getString(R.string.error_string))
                .setPositiveButton(context.getResources().getString(R.string.dialog_settings_button_string), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        try
                        {
                            context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(context, "Error, settings can not be opened.", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        setNetworkStatusDialogShowing(false);
                    }
                })
                .setNegativeButton(context.getResources().getString(R.string.dialog_close_button_string), new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.dismiss();
                        setNetworkStatusDialogShowing(false);
                    }
                });
        networkStatusDialog = builder.create();
        if(!networkStatusDialog.isShowing())
        {
            networkStatusDialog.show();
            setNetworkStatusDialogShowing(true);
        }

    }

    public void hideAllDialogs()
    {
        hideTopListOptionsDialog();
        hideFavouritesListOptionsDialog();
        hideNetworkConnectionDialog();
    }

    public void hideTopListOptionsDialog()
    {
        if(topListOptionsDialog != null && topListOptionsDialog.isShowing())
            topListOptionsDialog.dismiss();
    }

    public void hideFavouritesListOptionsDialog()
    {
        if(favouritesListOptionsDialog != null && favouritesListOptionsDialog.isShowing())
            favouritesListOptionsDialog.dismiss();
    }

    public void hideNetworkConnectionDialog()
    {
        if(networkStatusDialog != null && networkStatusDialog.isShowing())
            networkStatusDialog.dismiss();
    }



    public boolean isFavouritesListOptionsShowing()
    {
        return this.isFavouritesListOptionsShowing;
    }
    public boolean isTopListOptionsDialogShowing()
    {
        return this.isTopListOptionsDialogShowing;
    }
    public boolean isNetworkStatusDialogShowing()
    {
        return isNetworkStatusDialogShowing;
    }

    private void setTopListOptionsDialogShowing(boolean showing)
    {
        this.isTopListOptionsDialogShowing = showing;
    }

    private void setFavouritesListOptionsDialogShowing(boolean showing)
    {
        this.isFavouritesListOptionsShowing = showing;
    }

    private void setNetworkStatusDialogShowing(boolean showing)
    {
        this.isNetworkStatusDialogShowing = showing;
    }

    private void deleteFavouriteSong(String songTitle)
    {
        itunezFeedContentProviderManager.deleteFavouriteSongTitle(songTitle);
    }

    private void addNewFavourite(FeedEntry feedEntry)
    {
        boolean isSuccess = itunezFeedContentProviderManager.addNewFavouriteSongTitle(feedEntry.getTitle(),feedEntry.getImageUrl());
        if(isSuccess)
            Toast.makeText(context,"New song added to favourites.",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context,"Song already in the favourites.",Toast.LENGTH_SHORT).show();

    }

    private void downloadImage(String imageUrl,int position)
    {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri downloadUri = Uri.parse(imageUrl);
        DownloadManager.Request request = new DownloadManager.Request(downloadUri);
        request.setDescription("Downloading "+imageUrl);
        request.setTitle("Download Manager");
        downloadManager.enqueue(request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI |DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "IMAGE_"+position+".jpg"));

        Toast.makeText(context,"Saving image to "+Environment.DIRECTORY_DOWNLOADS+" folder",Toast.LENGTH_SHORT).show();

    }

    
}
