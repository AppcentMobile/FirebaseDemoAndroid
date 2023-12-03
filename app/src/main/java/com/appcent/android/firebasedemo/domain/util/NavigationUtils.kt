package com.appcent.android.firebasedemo.domain.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.appcent.android.firebasedemo.R

object NavigationUtils {

    fun <T> NavBackStackEntry.useSavedStateValue(key: String, handler: (T?) -> Unit) {
        if (savedStateHandle.contains(key)) {
            val newValue = savedStateHandle.get<T>(key)
            handler(newValue)
            savedStateHandle.remove<T>(key)
        }
    }

    // Use for transferring data from dialog.
    fun Fragment.startSavedStateListener(fragmentId: Int, savedStateListener: NavBackStackEntry.() -> Unit) {
        var navBackStackEntry: NavBackStackEntry? = null
        try {
            navBackStackEntry = findNavController().getBackStackEntry(fragmentId)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        if (navBackStackEntry != null) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    // coroutine launch is added to wait for layout to be laid out.
                    viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                        savedStateListener(navBackStackEntry)
                    }
                }
            }

            navBackStackEntry.lifecycle.addObserver(observer)

            viewLifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        navBackStackEntry.lifecycle.removeObserver(observer)
                    }
                }
            })
        }
    }

    fun <T> Fragment.setNavigationResult(key: String, value: T) {
        findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
    }

    fun getAnimatedNavOptions(navOptions: NavOptions?, anim: NavigationAnimation): NavOptions? {
        if (navOptions == null) return null
        if (anim == NavigationAnimation.NONE) return navOptions

        return when (anim) {
            NavigationAnimation.HORIZONTAL -> {
                createNavOptionsSlideHorizontal()
            }
            NavigationAnimation.BOTTOM_SHEET -> {
                createNavOptionsAnimateBottomSheet()
            }
            NavigationAnimation.FADE -> {
                createNavOptionsFade()
            }
            NavigationAnimation.ZOOM -> {
                createNavOptionsZoom()
            }
            NavigationAnimation.PROFILE -> {
                createNavOptionsAnimateProfile()
            }
            NavigationAnimation.HORIZONTAL_WITHOUT_DESTROY -> {
                createNavOptionsSlideHorizontalWithoutDestroy()
            }
            NavigationAnimation.FADE_WITHOUT_DESTROY -> {
                createNavOptionsSlideFadeWithoutDestroy()
            }
            else -> {
                createNavOptionsFade()
            }
        }.apply {
            navOptions.run {
                setPopUpTo(popUpToId, isPopUpToInclusive())
                setLaunchSingleTop(shouldLaunchSingleTop())
            }
        }.build()
    }

    private fun createNavOptionsSlideHorizontal() = NavOptions.Builder()
        .setEnterAnim(R.anim.anim_slide_enter)
        .setExitAnim(R.anim.anim_slide_exit)
        .setPopEnterAnim(R.anim.anim_slide_pop_enter)
        .setPopExitAnim(R.anim.anim_slide_pop_exit)

    private fun createNavOptionsSlideHorizontalWithoutDestroy() = NavOptions.Builder()
        .setEnterAnim(R.anim.anim_slide_enter)
        .setExitAnim(R.anim.no_anim)
        .setPopExitAnim(R.anim.anim_slide_pop_exit)

    private fun createNavOptionsSlideFadeWithoutDestroy() = NavOptions.Builder()
        .setEnterAnim(R.anim.anim_fade_in)
        .setExitAnim(R.anim.no_anim)
        .setPopExitAnim(R.anim.anim_fade_out)

    private fun createNavOptionsFade() = NavOptions.Builder()
        .setEnterAnim(R.anim.anim_fade_in)
        .setExitAnim(R.anim.anim_fade_out)
        .setPopEnterAnim(R.anim.anim_fade_in)
        .setPopExitAnim(R.anim.anim_fade_out)

    private fun createNavOptionsZoom() = NavOptions.Builder()
        .setEnterAnim(R.anim.anim_zoom_in)
        //.setExitAnim(R.anim.anim_zoom_out)
        .setPopEnterAnim(R.anim.anim_zoom_in)
        //.setPopExitAnim(R.anim.anim_zoom_out)

    private fun createNavOptionsAnimateBottomSheet() = NavOptions.Builder()
        .setEnterAnim(R.anim.anim_enter_bottom)
        .setExitAnim(R.anim.anim_exit_bottom)

    private fun createNavOptionsAnimateProfile() = NavOptions.Builder()
        .setEnterAnim(R.anim.anim_enter_bottom)
        .setExitAnim(R.anim.anim_fade_out)
        .setPopEnterAnim(R.anim.anim_fade_in)
        .setPopExitAnim(R.anim.anim_exit_bottom)
}

enum class NavigationAnimation {
    HORIZONTAL, FADE, ZOOM ,BOTTOM_SHEET, PROFILE, HORIZONTAL_WITHOUT_DESTROY, FADE_WITHOUT_DESTROY ,NONE
}

fun <T> NavController.setNavigationResult(key: String, value: T) {
    previousBackStackEntry?.savedStateHandle?.set(key, value)
}
