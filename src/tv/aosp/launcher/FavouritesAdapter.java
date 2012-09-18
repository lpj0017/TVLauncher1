package tv.aosp.launcher;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import tv.aosp.launcher.database.DBHelper;

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
            icon.setImageDrawable(pm.getApplicationIcon(appInfo.packageName));
        } catch(PackageManager.NameNotFoundException nameNotFoundException) {
            icon.setImageDrawable(pm.getApplicationIcon(context.getApplicationInfo()));
        }

        return view;  //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * Add a favourite to the favorites bar
     */

    public void add(final FavoriteInfo app) {
        entries.add(app);
        Collections.sort(entries, FavoriteInfoComparator.InstanceHolder.INSTANCE);
        notifyDataSetChanged();
    }

    /**
     * Save a new favourite.
     */

    public void save(final FavoriteInfo app) {
        SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        try {
            db.execSQL("insert into "+DBHelper.FAVORITES_TABLE+"(package, bar) values ('"+app.packageName+"', 0)");
        } finally {
            db.close();
        }

        add(app);
    }
    /**
     * Remove a favorite from the favorites bar
     */

    public void remove(final FavoriteInfo app) {
        entries.remove(app);
        Collections.sort(entries, FavoriteInfoComparator.InstanceHolder.INSTANCE);

        SQLiteDatabase db = new DBHelper(context).getWritableDatabase();
        try {
            db.execSQL("delete from "+DBHelper.FAVORITES_TABLE+" where package = '"+app.packageName+"'");
        } finally {
            db.close();
        }
        notifyDataSetChanged();
    }
}
