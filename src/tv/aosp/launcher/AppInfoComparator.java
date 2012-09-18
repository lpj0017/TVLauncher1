package tv.aosp.launcher;

/**
 * Created with IntelliJ IDEA.
 * User: alsutton
 * Date: 13/09/2012
 * Time: 10:21
 * To change this template use File | Settings | File Templates.
 */

import java.util.Comparator;

/**
 * Comparator for sorting AppInfo objects
 */

public class AppInfoComparator implements Comparator<AppInfo> {

    @Override
    public int compare(AppInfo appInfo, AppInfo appInfo1) {
        return appInfo.appName.compareToIgnoreCase(appInfo1.appName);
    }

    public static final class InstanceHolder {
        public static final AppInfoComparator INSTANCE = new AppInfoComparator();
    }
}
