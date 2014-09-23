package com.keran.marinov.itunezfeed.list;

import android.os.Parcel;
import android.os.Parcelable;

public class FeedEntry implements Parcelable
{
	private String title;
	private String imageUrl;

    public FeedEntry(String title, String imageUrl)
    {
        this.title = title;
        this.imageUrl = imageUrl;
    }
    
    public String getTitle()
    {
    	return title;
    }

	public String getImageUrl()
	{
		return imageUrl;
	}

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
