package com.keran.marinov.itunezfeed.listener;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class TabListener<T extends Fragment> implements ActionBar.TabListener {
    private final Activity mActivity;
    private final String mTag;
    private final Class<T> mClass;
   // private final Bundle mArgs;
    private Fragment mFragment;
    private final String TAG = "TabListener";

    public TabListener(Activity activity, String tag, Class<T> clz) {
        mActivity = activity;
        mTag = tag;
        mClass = clz;
        mFragment = mActivity.getFragmentManager().findFragmentByTag(mTag);
    }

   public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

        if (mFragment == null) {
            mFragment = Fragment.instantiate(mActivity, mClass.getName());
            ft.replace(android.R.id.content, mFragment, mTag);
        } else {
            if (mFragment.isDetached()) {
                ft.attach(mFragment);
            }
        }
    }

    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (mFragment != null) {
            ft.detach(mFragment);
        }
    }

    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

}