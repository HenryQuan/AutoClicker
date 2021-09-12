package org.github.henryquan.autoclicker

import android.content.Context
import android.view.View
import android.view.WindowManager

class FloatingManager private constructor(private val mContext: Context) {
    private val mWindowManager: WindowManager

    /**
     * adds floating view
     *
     * @param view
     * @param params
     * @return
     */
    protected fun addView(view: View?, params: WindowManager.LayoutParams?): Boolean {
        try {
            mWindowManager.addView(view, params)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * removes floating view
     *
     * @param view
     * @return
     */
    protected fun removeView(view: View?): Boolean {
        try {
            mWindowManager.removeView(view)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * @param view
     * @param params
     * @return
     */
    protected fun updateView(view: View?, params: WindowManager.LayoutParams?): Boolean {
        try {
            mWindowManager.updateViewLayout(view, params)
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    companion object {
        private var mInstance: FloatingManager? = null
        fun getInstance(context: Context): FloatingManager? {
            if (mInstance == null) {
                mInstance = FloatingManager(context)
            }
            return mInstance
        }
    }

    init {
        mWindowManager = mContext.getSystemService(
            Context.WINDOW_SERVICE
        ) as WindowManager //获得WindowManager对象
    }
}