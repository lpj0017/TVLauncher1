package com.funkyandroid.launcher;

import android.app.ExpandableListActivity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.funkyandroid.launcher.database.DBHelper;
import com.funkyandroid.launcher.launcherentries.AppCategoryEntry;
import com.funkyandroid.launcher.launcherentries.SystemLauncherEntry;

public class Launcher extends ExpandableListActivity {

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

        new ListBuilder().execute();
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

        final AppCategoryEntry.AppInfo appInfo =
                (AppCategoryEntry.AppInfo) ((AppCategoryEntry)entry).getChild(child);
        Intent launchIntent = getPackageManager().getLaunchIntentForPackage(appInfo.packageName);
        startActivity(launchIntent);

        return true;
    }

    /**
     * Runnable which rebuilds the list of categories in the list adapter
     */

    private class ListBuilder extends AsyncTask<Void, Void, LauncherEntryAdapter> {

        private final String[] CATEGORY_COLUMNS = { "_id", "name" };

        private SQLiteDatabase db;

        ListBuilder() {
            db = new DBHelper(Launcher.this).getReadableDatabase();
        }

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


            Cursor categories = db.query(DBHelper.CATEGORIES_TABLE, CATEGORY_COLUMNS, null, null, null, null, null );
            try {
                while(categories.moveToNext()) {
                    la.add(new AppCategoryEntry(Launcher.this, categories.getInt(0), categories.getString(1)));
                }
            } finally {
                categories.close();
            }

            return  la;
        }
    }

}
