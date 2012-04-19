package com.clark.systeminfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;

public class ActivityFileSystem extends AbstractTextActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StringBuilder lBuilder = new StringBuilder();
        getFileSystemSpace(lBuilder, Environment.getRootDirectory()
                .getAbsolutePath());
        getFileSystemSpace(lBuilder, Environment.getDataDirectory()
                .getAbsolutePath());
        getFileSystemSpace(lBuilder, Environment.getDownloadCacheDirectory()
                .getAbsolutePath());
        getFileSystemSpace(lBuilder, Environment.getExternalStorageDirectory()
                .getAbsolutePath());

        String str = lBuilder.toString();
        Spannable spannable = new SpannableString(str);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            spannable.setSpan(new UnderlineSpan(), matcher.start(1),
                    matcher.end(1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(Color.YELLOW),
                    matcher.start(1), matcher.end(1),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        mTextView.setText(spannable);
    }

    private void getFileSystemSpace(StringBuilder lBuilder, String path) {
        StatFs statFs = new StatFs(path);
        int blockSize = statFs.getBlockSize();
        lBuilder.append("path : ").append(path).append("\n");
        lBuilder.append("The total number of blocks on the file system : ")
                .append(statFs.getBlockCount());
        lBuilder.append("[").append(getSize(statFs.getBlockCount(), blockSize))
                .append("]\n");

        lBuilder.append(
                "The total number of blocks that are free on the file system, including reserved blocks (that are not available to normal applications) : ")
                .append(statFs.getFreeBlocks());
        lBuilder.append("[").append(getSize(statFs.getFreeBlocks(), blockSize))
                .append("]\n");

        lBuilder.append(
                "The number of blocks that are free on the file system and available to applications : ")
                .append(statFs.getAvailableBlocks());
        lBuilder.append("[")
                .append(getSize(statFs.getAvailableBlocks(), blockSize))
                .append("]\n");
        lBuilder.append("\n");
    }

    private String getSize(long block, long blockSize) {
        long size = block * blockSize;
        if (size <= 0) {
            return "0K";
        } else if (size < ONE_KB) {
            return String.format("%.2f", size) + "K";
        } else if (size < ONE_MB) {
            return String.format("%.2f", size / ONE_KB) + "KB";
        } else if (size < ONE_GB) {
            return String.format("%.2f", size / ONE_MB) + "MB";
        } else {
            return String.format("%.2f", size / ONE_GB) + "GB";
        }
    }

    private Pattern pattern = Pattern.compile("^path : (\\S+)$",
            Pattern.MULTILINE);

    private static double ONE_KB = 1024.;
    private static double ONE_MB = ONE_KB * 1024.;
    private static double ONE_GB = ONE_MB * 1024.;
}
