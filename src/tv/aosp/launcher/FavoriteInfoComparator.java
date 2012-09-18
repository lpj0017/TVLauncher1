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

public class FavoriteInfoComparator implements Comparator<FavoriteInfo> {

    @Override
    public int compare(FavoriteInfo appInfo, FavoriteInfo appInfo1) {
        return appInfo.name.compareToIgnoreCase(appInfo1.name);
    }

    public static final class InstanceHolder {
        public static final FavoriteInfoComparator INSTANCE = new FavoriteInfoComparator();
    }
}
