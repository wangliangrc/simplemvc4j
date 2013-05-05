
package com.baidu.aiu;

import android.os.Build;

/**
 * Created with IntelliJ IDEA.
 * 
 * @author guangongbo
 * @version 1.0 13-4-9
 */
class FixedResourcesFactory {
    public static FixedResources getFixedResources() {
        if (Build.VERSION.SDK_INT >= 9) {
            return ResourcesImpl9.INSTANCE;
        } else if (Build.VERSION.SDK_INT == 8) {
            return ResourcesImpl8.INSTANCE;
        } else {
            // Build.VERSION.SDK_INT < 8
            return ResourcesImpl4.INSTANCE;
        }
    }
}
