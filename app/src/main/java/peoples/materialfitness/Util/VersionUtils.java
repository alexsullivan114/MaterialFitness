package peoples.materialfitness.Util;

import android.content.Context;
import android.os.Build;

/**
 * Created by Alex Sullivan on 12/26/15.
 */
public class VersionUtils
{
    public static boolean isLollipopOrGreater()
    {
        // Check if we're running on Android 5.0 or higher
        return Build.VERSION.SDK_INT >= 21;
    }
}
