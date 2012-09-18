package tv.aosp.launcher;

/**
 * Comparator for launcher entries
 */

import java.util.Comparator;

/**
 * Comparator to ensure that launcher entries are sorted.
 */

class LauncherEntryComparator implements Comparator<LauncherEntry> {
    public int compare(final LauncherEntry first, final LauncherEntry second) {
        int sortOrder = first.getSortOrder() - second.getSortOrder();
        if(sortOrder != 0) {
            return sortOrder;
        }

        return second.getCaption().compareTo(first.getCaption());
    }

    // Singleton pattern

    public static class InstanceHolder {
        static final LauncherEntryComparator INSTANCE = new LauncherEntryComparator();
    }
}
