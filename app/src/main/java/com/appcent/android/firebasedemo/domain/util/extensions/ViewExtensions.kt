package com.appcent.android.firebasedemo.domain.util.extensions

import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * Created by hasan.arinc on 6.12.2023.
 */

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}
