package com.keran.marinov.itunezfeed.list;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class SavedListParcelable implements Parcelable
{
	private ArrayList<FeedEntry> savedList = new ArrayList<FeedEntry>();

	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags)
	{
		// TODO Auto-generated method stub
		
	}
	
	public ArrayList<FeedEntry> getSavedList()
	{
		return savedList;
	}
	
	public void setSavedList(ArrayList<FeedEntry> list)
	{
		this.savedList = list;
	}

}
