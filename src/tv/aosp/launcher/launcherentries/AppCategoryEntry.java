package tv.aosp.launcher.launcherentries;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import tv.aosp.launcher.*;

import java.util.*;

/**
 * A LauncherEntry which holds the details of an application category
 */
public class AppCategoryEntry extends LauncherEntry {
    private static final String[] COLUMNS = { "packageName" };

    /**
     * The apps in this category
     */

    private List<AppInfo> apps = new ArrayList<AppInfo>();

    /**
     * The intent to be fired if this item is selected
     */

    private int id;

    public AppCategoryEntry(final Context context, final int id, final String caption) {
        super(caption, 0);
        this.id = id;
        populateChildren(context);
    }

    /**
     * Once selected the item will start an intent.
     *
     * @param context The context
     */
    public void onSelected(final Context context) {
        return; // Do nothing, the group expansion handled via the widget is enough.
    }

    /**
     * Return the number of children of this entry
     *
     * @return The number of children.
     */

    public int getChildrenCount() {
        return apps.size();
    }

    /**
     * Gets a specific child.
     *
     * @param position The position to get the child from.
     * @return The child.
     */

    public Object getChild(int position) {
        return apps.get(position);
    }

    /**
     * Populate the children for this category
     */

    private void populateChildren(final Context context) {

        PackageManager pm = context.getPackageManager();
        Intent appStartIntent = new Intent(Intent.ACTION_MAIN);
        appStartIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        for(ResolveInfo packageInfo : pm.queryIntentActivities(appStartIntent, 0)) {
            AppInfo info = new AppInfo();
            try {
                info.packageName = packageInfo.activityInfo.packageName;
                info.appName = pm.getApplicationLabel(pm.getApplicationInfo(info.packageName, 0)).toString();
                apps.add(info);
            } catch(PackageManager.NameNotFoundException nnfe) {
                Log.e("FunkyLauncher", "Name Not Found", nnfe);
            }
        }

/*
        PackageManager pm = context.getPackageManager();
        SQLiteDatabase db = new DBHelper(context).getReadableDatabase();
        Cursor appsCursor = db.query(DBHelper.APPS_TABLE, COLUMNS, "category_id = "+id, null, null, null, null, null );
        while(appsCursor.moveToNext()) {
            final String packageName = appsCursor.getString(0);

            AppInfo info = new AppInfo();
            try {
                info.appName = pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0)).toString();
                info.packageName = packageName;
                apps.add(info);
            } catch(PackageManager.NameNotFoundException nnfe) {
                ; // Do nothing, just ignore it.
            }
        }
*/
        Collections.sort(apps, AppInfoComparator.InstanceHolder.INSTANCE);
    }
}
