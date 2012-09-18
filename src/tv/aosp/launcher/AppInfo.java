package tv.aosp.launcher;

/**
 * Class holding the required information about an application.
 */

public class AppInfo {
    public String appName;
    public String packageName;

    public String toString() {
        return packageName+":"+appName;
    }
}
