package com.bsd.specialproject.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bsd.specialproject.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

fun Activity.showCustomSnackBar(
    @DrawableRes drawable: Int? = null,
    message: String,
    @ColorRes mainBgId: Int,
    setGravityTop: Boolean = false,
    duration: Int = Snackbar.LENGTH_LONG,
    onDismiss: ((Unit) -> Unit)? = null
) {
    val snackBar = Snackbar.make(
        findViewById(android.R.id.content),
        "",
        duration
    ).addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
        override fun onShown(transientBottomBar: Snackbar?) {
            super.onShown(transientBottomBar)
        }

        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            super.onDismissed(transientBottomBar, event)
            onDismiss?.invoke(Unit)
        }
    })

    val inflater: LayoutInflater = layoutInflater
    val layout = inflater.inflate(R.layout.view_custom_snack_bar, null) as FrameLayout

    (layout.findViewById<View>(R.id.clSnackBar) as FrameLayout).background =
        ContextCompat.getDrawable(applicationContext, mainBgId)
    (layout.findViewById<View>(R.id.tvSnackBar) as TextView).text = message

    val snackBarView = snackBar.view as Snackbar.SnackbarLayout
    val parentParams = snackBarView.layoutParams as FrameLayout.LayoutParams
    parentParams.setMargins(0, 0, 0, 0)
    parentParams.gravity = Gravity.TOP
    snackBarView.layoutParams = parentParams
    parentParams.gravity = if (setGravityTop) Gravity.TOP else Gravity.BOTTOM
    snackBarView.setBackgroundColor(Color.TRANSPARENT)
    snackBarView.addView(layout, 0)
    snackBar.show()
}

fun Activity.checkAndRequestPermissions(permissions: Array<String>, code: Int, action: () -> Unit) {
    if (permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }) {
        action.invoke()
    } else {
        ActivityCompat.requestPermissions(this, permissions, code)
    }
}
