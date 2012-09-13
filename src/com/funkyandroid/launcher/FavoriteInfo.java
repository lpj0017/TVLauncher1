package com.funkyandroid.launcher;

/**
 * Created with IntelliJ IDEA.
 * User: alsutton
 * Date: 13/09/2012
 * Time: 10:49
 * To change this template use File | Settings | File Templates.
 */
public class FavoriteInfo {
    public String packageName;
    public String name;
    public int position;

    /**
     * Null constructor.
     */

    public FavoriteInfo() {
        super();
    }

    public FavoriteInfo(final AppInfo appInfo) {
        packageName = appInfo.packageName;
        name = appInfo.appName;
    }

    public String toString() {
        return packageName+":"+name;
    }
}
