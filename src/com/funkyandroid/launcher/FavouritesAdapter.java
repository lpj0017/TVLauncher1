package com.funkyandroid.launcher;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Adapter for handling the horizontally scrolling list of favourite apps
 */
public class FavouritesAdapter extends BaseAdapter
    implements ListAdapter {

    private Context context;

    private List<FavoriteInfo> entries = new ArrayList<FavoriteInfo>();

    FavouritesAdapter(final Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public FavoriteInfo getItem(int i) {
        return entries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.favourite_entry, null);
        }

        final PackageManager pm = context.getPackageManager();

        final FavoriteInfo appInfo = getItem(i);
        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        try {
            Log.i("FunkyLauncher", appInfo.packageName);
            icon.setImageDrawable(pm.getApplicationIcon(appInfo.packageName));
        } catch(PackageManager.NameNotFoundException nameNotFoundException) {
            Log.e("FunkyLauncher", "Problem finding icon for package " + appInfo, nameNotFoundException);
            icon.setImageDrawable(pm.getApplicationIcon(context.getApplicationInfo()));
        }

        return view;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Add an LauncherEntry to the list of selectable options
     */

    public void add(final FavoriteInfo app) {
        entries.add(app);
        Collections.sort(entries, FavoriteInfoComparator.InstanceHolder.INSTANCE);
        notifyDataSetChanged();
    }}
