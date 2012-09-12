package com.funkyandroid.launcher;

import android.content.Context;

/**
 * Created with IntelliJ IDEA.
 * User: alsutton
 * Date: 10/09/2012
 * Time: 12:29
 * To change this template use File | Settings | File Templates.
 */
public abstract class LauncherEntry {

    /**
     * The resource ID for the caption
     */

    private String caption;

    /**
     * The sort order for this entry
     */

    private int sortOrder = 0;

    /**
     * Constructor. Store the caption resource ID.
     */

    public LauncherEntry(final String caption) {
        this.caption = caption;
    }

    /**
     * Constructor. Store the caption resource ID and use a non-default sort order.
     */

    public LauncherEntry(final String caption, final int sortOrder) {
        this.caption = caption;
        this.sortOrder = sortOrder;
    }

    /**
     * Get the sort order for the entry
     */

    public int getSortOrder() {
        return sortOrder;
    }

    /**
     * Get the resource ID for the caption
     */

    public String getCaption() {
        return caption;
    }

    /**
     * Perform the required action when this entry is selected
     *
     * @param context The context in which this needs to operate.
     */

    public abstract void onSelected(final Context context);

    /**
     * Return the number of children of this entry
     *
     * @return The number of children.
     */

    public abstract int getChildrenCount();

    /**
     * Gets a specific child.
     *
     * @param position The position to get the child from.
     * @return The child.
     */

    public abstract Object getChild(int position);
}
