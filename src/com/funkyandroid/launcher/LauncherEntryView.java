package com.funkyandroid.launcher;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.util.AttributeSet;

/**
 * View to display an entry line on the Launcher
 */

public class LauncherEntryView extends LinearLayout {
    /**
     * The layout parameters for an entry
     */

    private final ViewGroup.LayoutParams layout = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    /**
     * The entry being displayed
     */

    private LauncherEntry entry;

    /**
     * The text view for the caption.
     */

    private TextView caption;

    /**
     * Constructor.
     *
     * @param context The context in which we're running.
     */

    public LauncherEntryView(final Context context) {
        super(context, null);
    }

    public LauncherEntryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LauncherEntryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * Sets the launch entry relevant to this view
     *
     * @param entry The launch entry to display.
     */

    public void setEntry(final LauncherEntry entry ) {
        this.entry = entry;
        if(caption == null) {
            for( int i = 0 ; i < getChildCount() ; i++ ) {
                View v = getChildAt(i);
                if(v instanceof TextView) {
                    caption = (TextView)v;
                    break;
                }
            }
        }

        if(caption != null) {
              caption.setText(entry.getCaption());
        } else {
            Log.e("Launcher", "No TextView Child");
        }

    }
}
