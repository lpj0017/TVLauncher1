package com.funkyandroid.launcher.launcherentries;

import android.content.Context;
import android.content.Intent;
import com.funkyandroid.launcher.LauncherEntry;
import com.funkyandroid.launcher.LauncherEntryView;

/**
 * LauncherEntry which represents a system action
 */
public class SystemLauncherEntry extends LauncherEntry {

    /**
     * The intent to be fired if this item is selected
     */

    private Intent intent;

    public SystemLauncherEntry(final Intent intent, final String caption, final int sortOrder) {
        super(caption, sortOrder);
        this.intent = intent;
    }

    /**
     * Once selected the item will start an intent.
     *
     * @param context The context
     */
    public void onSelected(final Context context) {
        context.startActivity(intent);
    }

    /**
     * Return the number of children of this entry
     *
     * @return The number of children.
     */

    public int getChildrenCount() {
        return 0;
    }

    /**
     * Gets a specific child.
     *
     * @param position The position to get the child from.
     * @return The child.
     */

    public Object getChild(int position) {
        return null;
    }
}
