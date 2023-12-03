package com.appcent.android.firebasedemo.domain.util

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout

class KeyboardVisibilityListener(
    private val activity: Activity,
    private val onKeyboardVisibilityListener: OnKeyboardVisibilityListener
) {

    private val rootView: View =
        (activity.findViewById(android.R.id.content) as FrameLayout).getChildAt(0)
    private var initialRect: Rect = Rect()

    init {
        rootView.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                val visibleRect = Rect()
                rootView.getWindowVisibleDisplayFrame(visibleRect)

                if (initialRect.bottom == 0) {
                    initialRect.set(visibleRect)
                    return true
                }

                if (visibleRect.bottom < initialRect.bottom) {
                    onKeyboardVisibilityListener.onKeyboardHidden()
                } else {
                    onKeyboardVisibilityListener.onKeyboardVisible()
                }

                return true
            }
        })

        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val visibleRect = Rect()
            rootView.getWindowVisibleDisplayFrame(visibleRect)

            if (initialRect.bottom == 0) {
                initialRect.set(visibleRect)
                return@addOnGlobalLayoutListener
            }

            if (visibleRect.bottom < initialRect.bottom) {
                onKeyboardVisibilityListener.onKeyboardVisible()
            } else {
                onKeyboardVisibilityListener.onKeyboardHidden()
            }
        }
    }

    interface OnKeyboardVisibilityListener {
        fun onKeyboardVisible()
        fun onKeyboardHidden()
    }
}
