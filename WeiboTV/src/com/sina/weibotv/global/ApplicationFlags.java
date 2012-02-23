package com.sina.weibotv.global;

import android.content.pm.ApplicationInfo;

public class ApplicationFlags {

    public static boolean isAllowBackup(int flags) {
        return ApplicationInfo.FLAG_ALLOW_BACKUP == (flags & ApplicationInfo.FLAG_ALLOW_BACKUP);
    }

    public static boolean isAllowClearUserData(int flags) {
        return ApplicationInfo.FLAG_ALLOW_CLEAR_USER_DATA == (flags & ApplicationInfo.FLAG_ALLOW_CLEAR_USER_DATA);
    }

    public static boolean isAllowTaskReparenting(int flags) {
        return ApplicationInfo.FLAG_ALLOW_TASK_REPARENTING == (flags & ApplicationInfo.FLAG_ALLOW_TASK_REPARENTING);
    }

    public static boolean isDebuggable(int flags) {
        return ApplicationInfo.FLAG_DEBUGGABLE == (flags & ApplicationInfo.FLAG_DEBUGGABLE);
    }

    public static boolean isInstalledOnExternalStorage(int flags) {
        return ApplicationInfo.FLAG_EXTERNAL_STORAGE == (flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE);
    }

    public static boolean requestedLargeHeap(int flags) {
        return ApplicationInfo.FLAG_LARGE_HEAP == (flags & ApplicationInfo.FLAG_LARGE_HEAP);
    }

    public static boolean supportLargerScreens(int flags) {
        return ApplicationInfo.FLAG_SUPPORTS_LARGE_SCREENS == (flags & ApplicationInfo.FLAG_SUPPORTS_LARGE_SCREENS);
    }

    public static boolean supportNormalScreens(int flags) {
        return ApplicationInfo.FLAG_SUPPORTS_NORMAL_SCREENS == (flags & ApplicationInfo.FLAG_SUPPORTS_NORMAL_SCREENS);
    }

    public static boolean supportSmallerScreens(int flags) {
        return ApplicationInfo.FLAG_SUPPORTS_SMALL_SCREENS == (flags & ApplicationInfo.FLAG_SUPPORTS_SMALL_SCREENS);
    }

    public static boolean supportExtraLargeScreens(int flags) {
        return ApplicationInfo.FLAG_SUPPORTS_XLARGE_SCREENS == (flags & ApplicationInfo.FLAG_SUPPORTS_XLARGE_SCREENS);
    }

    public static boolean isInstalledInSystemImage(int flags) {
        return ApplicationInfo.FLAG_SYSTEM == (flags & ApplicationInfo.FLAG_SYSTEM);
    }

    public static boolean isUpdatedSystemVersion(int flags) {
        return ApplicationInfo.FLAG_UPDATED_SYSTEM_APP == (flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP);
    }

    public static String toString(int flags) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        stringBuilder.append("\n isAllowBackup: " + isAllowBackup(flags));
        stringBuilder.append("\n isAllowClearUserData: "
                + isAllowClearUserData(flags));
        stringBuilder.append("\n isAllowTaskReparenting: "
                + isAllowTaskReparenting(flags));
        stringBuilder.append("\n isDebuggable: " + isDebuggable(flags));
        stringBuilder.append("\n isInstalledOnExternalStorage: "
                + isInstalledOnExternalStorage(flags));
        stringBuilder.append("\n requestedLargeHeap: "
                + requestedLargeHeap(flags));
        stringBuilder.append("\n supportLargerScreens: "
                + supportLargerScreens(flags));
        stringBuilder.append("\n supportNormalScreens: "
                + supportNormalScreens(flags));
        stringBuilder.append("\n supportSmallerScreens: "
                + supportSmallerScreens(flags));
        stringBuilder.append("\n supportExtraLargeScreens: "
                + supportExtraLargeScreens(flags));
        stringBuilder.append("\n isInstalledInSystemImage: "
                + isInstalledInSystemImage(flags));
        stringBuilder.append("\n isUpdatedSystemVersion: "
                + isUpdatedSystemVersion(flags));
        stringBuilder.append("\n]");
        return stringBuilder.toString();
    }
}
