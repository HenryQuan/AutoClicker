package org.github.henryquan.autoclicker

import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.Button
import org.github.henryquan.autoclicker.MainActivity

class FloatingView : Service(), View.OnClickListener {
    private var mWindowManager: WindowManager? = null
    private var myFloatingView: View? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        // getting the widget layout from xml using layout inflater
        myFloatingView = LayoutInflater.from(this).inflate(R.layout.floating_view, null)
        val layout_parms: Int
        layout_parms = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        // setting the layout parameters
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT, layout_parms,
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        // getting windows services and adding the floating view to it
        mWindowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        mWindowManager!!.addView(myFloatingView, params)

        // adding an touchlistener to make drag movement of the floating widget
        myFloatingView?.findViewById<View>(R.id.thisIsAnID)
            ?.setOnTouchListener(object : OnTouchListener {
                private var initialX = 0
                private var initialY = 0
                private var initialTouchX = 0f
                private var initialTouchY = 0f
                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    Log.d("TOUCH", "THIS IS TOUCHED")
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            initialX = params.x
                            initialY = params.y
                            initialTouchX = event.rawX
                            initialTouchY = event.rawY
                            return true
                        }
                        MotionEvent.ACTION_UP -> return true
                        MotionEvent.ACTION_MOVE -> {
                            // this code is helping the widget to move around the screen with
                            // fingers
                            params.x = initialX + (event.rawX - initialTouchX).toInt()
                            params.y = initialY + (event.rawY - initialTouchY).toInt()
                            mWindowManager!!.updateViewLayout(myFloatingView, params)
                            return true
                        }
                    }
                    return false
                }
            })
        val startButton = myFloatingView?.findViewById<View>(R.id.start) as Button
        startButton.setOnClickListener(this)
        val stopButton = myFloatingView?.findViewById<View>(R.id.stop) as Button
        stopButton.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (myFloatingView != null) mWindowManager!!.removeView(myFloatingView)
    }

    override fun onClick(v: View) {
        // Log.d("onClick","THIS IS CLICKED");
        val intent = Intent(applicationContext, AutoService::class.java)
        when (v.id) {
            R.id.start -> {
                // Log.d("START","THIS IS STARTED");
                val location = IntArray(2)
                myFloatingView!!.getLocationOnScreen(location)
                intent.putExtra("action", "play")
                intent.putExtra("x", location[0] - 1)
                intent.putExtra("y", location[1] - 1)
            }
            R.id.stop -> {
                intent.putExtra("action", "stop")
                mWindowManager!!.removeView(myFloatingView)
                val appMain = Intent(applicationContext, MainActivity::class.java)
            }
        }
        application.startService(intent)
    }
}