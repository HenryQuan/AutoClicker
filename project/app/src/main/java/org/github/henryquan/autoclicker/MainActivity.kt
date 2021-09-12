package org.github.henryquan.autoclicker

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            !Settings.canDrawOverlays(this)
        ) {
            askPermission()
        }
        findViewById<View>(R.id.startFloat).setOnClickListener(this)
    }

    private fun askPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION)
    }

    override fun onClick(v: View) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            startService(Intent(this@MainActivity, FloatingView::class.java))
            finish()
        } else if (Settings.canDrawOverlays(this)) {
            startService(Intent(this@MainActivity, FloatingView::class.java))
            finish()
        } else {
            askPermission()
            Toast
                .makeText(
                    this, "You need System Alert Window Permission to do this",
                    Toast.LENGTH_SHORT
                )
                .show()
        }
    }

    companion object {
        // FrameLayout mLayout;
        private const val SYSTEM_ALERT_WINDOW_PERMISSION = 2084
    }
}