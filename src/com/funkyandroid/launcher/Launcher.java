package com.funkyandroid.launcher;

import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.funkyandroid.launcher.bluetooth.ListenerService;
import com.funkyandroid.launcher.database.DBHelper;
import com.funkyandroid.launcher.launcherentries.AppCategoryEntry;
import com.funkyandroid.launcher.launcherentries.SystemLauncherEntry;
import com.funkyandroid.launcher.ui.HorizontalListView;

public class Launcher extends ExpandableListActivity {

    public static final String LOG_TAG = "FunkyLauncher";

    /**
     * The default web page for the browser
     */

    private static final String DEFAULT_WEBPAGE = "http://www.funkyandroid.com/";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        getExpandableListView().setGroupIndicator(null);

        new FavoritesBuilder().execute();
        new EntryBuilder().execute();

        startService(new Intent(this, ListenerService.class));
    }

    /**
     * Handle a list item click
     */

    @Override
    public void onGroupExpand(int position) {
        LauncherEntryAdapter adapter = (LauncherEntryAdapter) getExpandableListAdapter();
        if(adapter == null) {
            return;
        }

        LauncherEntry entry = (LauncherEntry) adapter.getGroup(position);
        entry.onSelected(this);

        super.onGroupExpand(position);
    }

    @Override
    public boolean onChildClick(ExpandableListView list, View view, int group, int child, long id) {
        LauncherEntryAdapter adapter = (LauncherEntryAdapter) getExpandableListAdapter();
        if(adapter == null) {
            return false;
        }

        LauncherEntry entry = (LauncherEntry) adapter.getGroup(group);
        if(!(entry instanceof AppCategoryEntry)) {
            return false;
        }

        final AppInfo appInfo =
                (AppInfo) ((AppCategoryEntry)entry).getChild(child);
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(appInfo.packageName);
        startActivity(launchIntent);

        return true;
    }

    /**
     * Runnable which rebuilds the list of categories in the list adapter
     */

    private class EntryBuilder extends AsyncTask<Void, Void, LauncherEntryAdapter> {

        private final String[] CATEGORY_COLUMNS = { "_id", "name" };


        @Override
        protected void onPostExecute(LauncherEntryAdapter adapter) {
            setListAdapter(adapter);
        }

        @Override
        protected LauncherEntryAdapter doInBackground(Void... voids) {
            LauncherEntryAdapter la = new LauncherEntryAdapter(Launcher.this);

            Intent searchIntent = new Intent("android.search.action.GLOBAL_SEARCH" );
            la.add(new SystemLauncherEntry(searchIntent, getText(R.string.search).toString(), 2));

            Intent settingsIntent = new Intent( "android.settings.SETTINGS" );
            la.add(new SystemLauncherEntry(settingsIntent, getText(R.string.settings).toString(), 3));

            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(DEFAULT_WEBPAGE));
            la.add(new SystemLauncherEntry(browserIntent, getText(R.string.browser).toString(), 1));
/*
            SQLiteDatabase db = new DBHelper(Launcher.this).getReadableDatabase();
            try {
                Cursor categories = db.query(DBHelper.CATEGORIES_TABLE, CATEGORY_COLUMNS, null, null, null, null, null );
                try {
                    while(categories.moveToNext()) {
                        la.add(new AppCategoryEntry(Launcher.this, categories.getInt(0), categories.getString(1)));
                    }
                } finally {
                    categories.close();
                }
            } finally {
                db.close();
            }
*/
            la.add(new AppCategoryEntry(Launcher.this, 1, getText(R.string.apps).toString()));

            return  la;
        }
    }

    /**
     * Runnable which rebuilds the list of favourites
     */

    private class FavoritesBuilder extends AsyncTask<Void, Void, FavouritesAdapter> {

        private final String[] FAVORITES_COLUMNS = { "package" };


        @Override
        protected void onPostExecute(final FavouritesAdapter adapter) {
            final HorizontalListView hsv = (HorizontalListView) findViewById(R.id.header_favorites);
            hsv.setAdapter(adapter);
            hsv.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {
                    adapter.remove(adapter.getItem(pos));
                    return true;
                }
            });

            getExpandableListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    long expandablePosition =  getExpandableListView().getExpandableListPosition(position);
                    if (ExpandableListView.getPackedPositionType(expandablePosition) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                        int group = ExpandableListView.getPackedPositionGroup(expandablePosition);
                        int child = ExpandableListView.getPackedPositionChild(expandablePosition);

                        LauncherEntryAdapter launcherEntryAdapter = (LauncherEntryAdapter) getExpandableListAdapter();
                        if (launcherEntryAdapter == null) {
                            return false;
                        }

                        LauncherEntry entry = (LauncherEntry) launcherEntryAdapter.getGroup(group);
                        if (!(entry instanceof AppCategoryEntry)) {
                            return false;
                        }

                        final AppInfo appInfo =
                                (AppInfo) ((AppCategoryEntry) entry).getChild(child);
                        adapter.save(new FavoriteInfo(appInfo));

                        return true;
                    }

                    return false;
                }
            });
        }

        @Override
        protected FavouritesAdapter doInBackground(Void... voids) {
            FavouritesAdapter adapter = new FavouritesAdapter(Launcher.this);

            PackageManager pm = getPackageManager();

            SQLiteDatabase db = new DBHelper(Launcher.this).getReadableDatabase();
            try {
                Cursor favorites = db.query(DBHelper.FAVORITES_TABLE, FAVORITES_COLUMNS, null, null, null, null, null );
                try {
                    while(favorites.moveToNext()) {
                        FavoriteInfo info = new FavoriteInfo();
                        info.packageName = favorites.getString(0);
                        try {
                            info.name = pm.getApplicationLabel(pm.getApplicationInfo(info.packageName, 0)).toString();
                        } catch (PackageManager.NameNotFoundException nameNotFoundException) {
                            info.name = info.packageName;
                        }

                        Log.i("FunkyLauncher", info.toString());
                        adapter.add(info);
                    }
                } finally {
                    favorites.close();
                }
            } finally {
                db.close();
            }

            return adapter;
        }
    }
}
