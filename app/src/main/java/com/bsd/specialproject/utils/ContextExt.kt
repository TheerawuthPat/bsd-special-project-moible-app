package com.bsd.specialproject.utils

import android.content.Context
import android.widget.Toast

fun Context.showToastMessage(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
