package com.sina.weibotv.global;

import java.util.Arrays;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

public class PackageInfoCollector {
    private PackageInfo packageInfo;
    private ApplicationInfo applicationInfo;

    PackageInfoCollector(Context context) {
        super();
        PackageManager packageManager = context.getPackageManager();
        packageInfo = packageManager.getPackageArchiveInfo(
                context.getPackageCodePath(), PackageManager.GET_SIGNATURES);
        try {
            applicationInfo = packageManager.getApplicationInfo(
                    context.getPackageName(), 0);
        } catch (NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public long firstInstallTime() {
        return packageInfo.firstInstallTime;
    }

    public long lastUpdateTime() {
        return packageInfo.lastUpdateTime;
    }

    public String packageName() {
        return packageInfo.packageName;
    }

    public String sharedUserId() {
        return packageInfo.sharedUserId;
    }

    public int sharedUserLabel() {
        return packageInfo.sharedUserLabel;
    }

    public Signature[] signatures() {
        return packageInfo.signatures;
    }

    public int versionCode() {
        return packageInfo.versionCode;
    }

    public String versionName() {
        return packageInfo.versionName;
    }

    public String backupAgentName() {
        return applicationInfo.backupAgentName;
    }

    public String applicationClassName() {
        return applicationInfo.className;
    }

    public int compatibleWidthLimitDp() {
        return applicationInfo.compatibleWidthLimitDp;
    }

    public String dataDir() {
        return applicationInfo.dataDir;
    }

    public int applicationFlags() {
        return applicationInfo.flags;
    }

    public int largestWidthLimitDp() {
        return applicationInfo.largestWidthLimitDp;
    }

    public String manageSpaceActivityName() {
        return applicationInfo.manageSpaceActivityName;
    }

    public String nativeLibraryDir() {
        return applicationInfo.nativeLibraryDir;
    }

    public String processName() {
        return applicationInfo.processName;
    }

    public String publicSourceDir() {
        return applicationInfo.publicSourceDir;
    }

    public int requiresSmallestWidthDp() {
        return applicationInfo.requiresSmallestWidthDp;
    }

    public String[] sharedLibraryFiles() {
        return applicationInfo.sharedLibraryFiles;
    }

    public String sourceDir() {
        return applicationInfo.sourceDir;
    }

    public int targetSdkVersion() {
        return applicationInfo.targetSdkVersion;
    }

    public String taskAffinity() {
        return applicationInfo.taskAffinity;
    }

    public int theme() {
        return applicationInfo.theme;
    }

    public int uiOptions() {
        return applicationInfo.uiOptions;
    }

    public int uid() {
        return applicationInfo.uid;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(">>>>>>>>>>>>>>>>>>>>>>>>\nfirstInstallTime()=")
                .append(firstInstallTime()).append("\nlastUpdateTime()=")
                .append(lastUpdateTime()).append("\npackageName()=")
                .append(packageName()).append("\nsharedUserId()=")
                .append(sharedUserId()).append("\nsharedUserLabel()=")
                .append(sharedUserLabel()).append("\nsignatures()=")
                .append(Arrays.toString(signatures()))
                .append("\nversionCode()=").append(versionCode())
                .append("\nversionName()=").append(versionName())
                .append("\nbackupAgentName()=").append(backupAgentName())
                .append("\napplicationClassName()=")
                .append(applicationClassName())
                .append("\ncompatibleWidthLimitDp()=")
                .append(compatibleWidthLimitDp()).append("\ndataDir()=")
                .append(dataDir()).append("\napplicationFlags()=")
                .append(applicationFlags()).append("\nlargestWidthLimitDp()=")
                .append(largestWidthLimitDp())
                .append("\nmanageSpaceActivityName()=")
                .append(manageSpaceActivityName())
                .append("\nnativeLibraryDir()=").append(nativeLibraryDir())
                .append("\nprocessName()=").append(processName())
                .append("\npublicSourceDir()=").append(publicSourceDir())
                .append("\nrequiresSmallestWidthDp()=")
                .append(requiresSmallestWidthDp())
                .append("\nsharedLibraryFiles()=")
                .append(Arrays.toString(sharedLibraryFiles()))
                .append("\nsourceDir()=").append(sourceDir())
                .append("\ntargetSdkVersion()=").append(targetSdkVersion())
                .append("\ntaskAffinity()=").append(taskAffinity())
                .append("\ntheme()=").append(theme()).append("\nuiOptions()=")
                .append(uiOptions()).append("\nuid()=").append(uid())
                .append("\n<<<<<<<<<<<<<<<<<<<<<<<<");
        return builder.toString();
    }
}
