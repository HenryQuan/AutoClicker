package org.github.henryquan.autoclicker

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.content.Intent
import android.graphics.Path
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast

class AutoService : AccessibilityService() {
    private var mHandler: Handler? = null
    private var mX = 0
    private var mY = 0
    private var mRunnable: IntervalRunnable? = null
    override fun onCreate() {
        super.onCreate()
        val handlerThread = HandlerThread("auto-handler")
        handlerThread.start()
        mHandler = Handler(handlerThread.looper)
    }

    override fun onServiceConnected() {}
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d("Service", "SERVICE STARTED")
        val action = intent.getStringExtra("action")
        if (action == "play") {
            mX = intent.getIntExtra("x", 0)
            // Log.d("x_value",Integer.toString(mX));
            mY = intent.getIntExtra("y", 0)
            if (mRunnable == null) {
                mRunnable = IntervalRunnable()
            }
            // playTap(mX,mY);
            // mHandler.postDelayed(mRunnable, 1000);
            mHandler!!.post(mRunnable!!)
            Toast.makeText(baseContext, "Started", Toast.LENGTH_SHORT).show()
        } else if (action == "stop") {
            mHandler!!.removeCallbacksAndMessages(null)
        }
        return super.onStartCommand(intent, flags, startId)
    }

    //@RequiresApi(api = Build.VERSION_CODES.N)
    private fun playTap(x: Int, y: Int) {
        // Log.d("TAPPED","STARTED TAPpING");
        val swipePath = Path()
        val x = x.toFloat()
        val y = y.toFloat()
        swipePath.moveTo(x, y)
        swipePath.lineTo(x + 100, y + 100)
        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(
            StrokeDescription(swipePath, 20, 1000)
        )
        val description = gestureBuilder.build()
        // dispatchGesture(gestureBuilder.build(), null, null);
        Log.d("hello", "hello?")
        dispatchGesture(description, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                Log.d("Gesture Completed", "Gesture Completed")
                super.onCompleted(gestureDescription)
                // mHandler.postDelayed(mRunnable, 1);
                mHandler!!.post(mRunnable!!)
            }

            override fun onCancelled(gestureDescription: GestureDescription) {
                Log.d("Gesture Cancelled", "Gesture Cancelled");
                super.onCancelled(gestureDescription)
            }
        }, null)
        // Log.d("hi","hi?");
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {}
    override fun onInterrupt() {}

    private inner class IntervalRunnable : Runnable {
        override fun run() {
            Log.d("clicked", "click")
            playTap(mX, mY)
        }
    }
}