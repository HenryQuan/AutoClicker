package org.github.henryquan.autoclicker;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

public class FloatingManager {
    private static FloatingManager mInstance;
    private WindowManager mWindowManager;
    private Context mContext;

    private FloatingManager(Context context) {
        mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(
                Context.WINDOW_SERVICE); //获得WindowManager对象
    }

    public static FloatingManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FloatingManager(context);
        }
        return mInstance;
    }

    /**
     * adds floating view
     *
     * @param view
     * @param params
     * @return
     */
    protected boolean addView(View view, WindowManager.LayoutParams params) {
        try {
            mWindowManager.addView(view, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * removes floating view
     *
     * @param view
     * @return
     */
    protected boolean removeView(View view) {
        try {
            mWindowManager.removeView(view);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param view
     * @param params
     * @return
     */
    protected boolean updateView(View view, WindowManager.LayoutParams params) {
        try {
            mWindowManager.updateViewLayout(view, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}