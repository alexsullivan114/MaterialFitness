package peoples.materialfitness.Util;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import peoples.materialfitness.Core.MaterialFitnessApplication;

/**
 * Created by Alex Sullivan on 4/27/2016.
 */
public class ScreenUtils
{
    public static int getScreenWidth()
    {
        WindowManager wm = (WindowManager) MaterialFitnessApplication.getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getScreenHeight()
    {
        WindowManager wm = (WindowManager) MaterialFitnessApplication.getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }
}
