package com.funkyandroid.launcher;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.funkyandroid.launcher.launcherentries.AppCategoryEntry;
import com.funkyandroid.launcher.launcherentries.SystemLauncherEntry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Adapter used to hold launcher options
 */
public class LauncherEntryAdapter extends BaseExpandableListAdapter {
    /**
     * The context in which this adapter is operating
     */

    private Context context;

    private List<LauncherEntry> entries = new ArrayList<LauncherEntry>();

    public LauncherEntryAdapter(final Context context) {
        super();
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return entries.size();
    }

    @Override
    public int getChildrenCount(int position) {
        return entries.get(position).getChildrenCount();
    }

    @Override
    public Object getGroup(int position) {
        return entries.get(position);
    }

    @Override
    public Object getChild(int group, int child) {
        return entries.get(group).getChild(child);
    }

    @Override
    public long getGroupId(int i) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int position, boolean expanded, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_entry, null);
        }

        LauncherEntryView lev = (LauncherEntryView)view.findViewById(R.id.entry);
        LauncherEntry entry = entries.get(position);
        lev.setEntry(entry);

        return view;
    }

    @Override
    public View getChildView(int group, int child, boolean expanded, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.app_category_list_entry, null);
        }

        final PackageManager pm = context.getPackageManager();

        final AppCategoryEntry.AppInfo appInfo = (AppCategoryEntry.AppInfo) getChild(group, child);
        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        try {
            icon.setImageDrawable(pm.getApplicationIcon(appInfo.packageName));
        } catch(PackageManager.NameNotFoundException nnfe) {
            Log.e("FunkyLauncher", "Problem finding icon for package "+appInfo, nnfe);
            icon.setImageDrawable(pm.getApplicationIcon(context.getApplicationInfo()));
        }

        TextView label = (TextView)view.findViewById(R.id.name);
        label.setText(appInfo.appName);

        return view;
    }

    @Override
    public boolean isChildSelectable(int group, int child) {
        if(entries.get(group) instanceof AppCategoryEntry) {
            return true;
        }
        return false;
    }

    /**
     * Add an LauncherEntry to the list of selectable options
     */

    public void add(final LauncherEntry entry) {
        entries.add(entry);
        Collections.sort(entries, LauncherEntryComparatorInstanceHolder.INSTANCE);
        notifyDataSetChanged();
    }

    /**
     * Comparator to ensure that launcher entries are sorted.
     */

    private static class LauncherEntryComparator implements Comparator<LauncherEntry> {
        public int compare(final LauncherEntry first, final LauncherEntry second) {
            int sortOrder = first.getSortOrder() - second.getSortOrder();
            if(sortOrder != 0) {
                return sortOrder;
            }

            return second.getCaption().compareTo(first.getCaption());
        }
    }

    private static class LauncherEntryComparatorInstanceHolder {
        private static final LauncherEntryComparator INSTANCE = new LauncherEntryComparator();
    }
}
