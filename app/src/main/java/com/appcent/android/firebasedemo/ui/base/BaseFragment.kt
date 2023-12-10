package com.appcent.android.firebasedemo.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.appcent.android.firebasedemo.domain.Constants
import com.appcent.android.firebasedemo.domain.util.KeyboardVisibilityListener
import com.appcent.android.firebasedemo.domain.util.NavigationAnimation
import com.appcent.android.firebasedemo.domain.util.NavigationUtils
import com.appcent.android.firebasedemo.domain.util.extensions.showToast
import com.appcent.android.firebasedemo.ui.view.MainActivity
import kotlinx.coroutines.delay
import timber.log.Timber


abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    private val mainActivity: MainActivity?
        get() = activity as? MainActivity

    private var isProgressShowing = true

    private var keyboardVisibilityListener: KeyboardVisibilityListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSystemBars()
        getFragmentArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        setObservers()
        initUi()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            delay(Constants.DELAYED_INIT_UI_DURATION)
            delayedInitUi()
        }
        initSaveStateListener()
        keyboardVisibilityListener = KeyboardVisibilityListener(requireActivity(), object : KeyboardVisibilityListener.OnKeyboardVisibilityListener {
            override fun onKeyboardVisible() {
                // Handle keyboard visible event
                this@BaseFragment.onKeyboardVisible()
            }

            override fun onKeyboardHidden() {
                // Handle keyboard hidden event
                this@BaseFragment.onKeyboardHidden()
            }
        })
    }

    open fun  onKeyboardVisible() {}
    open fun  onKeyboardHidden() {}
    /** get arguments from previous fragment */
    open fun getFragmentArguments() {}

    /** get viewBinding from child fragment and set it in OnCreateView */
    abstract fun getViewBinding(): VB

    /** initialize click listeners of views here */
    open fun setClickListeners() {}

    /** implement data observers */
    open fun setObservers() {}

    /** init views here */
    open fun initUi() {}

    open fun delayedInitUi() {}

    /** init back stack entry*/
    open fun initSaveStateListener() {}

    /** change status bar and navigation bar color */
    open fun setSystemBars() {}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        keyboardVisibilityListener = null
    }

    fun getTitle() = findNavController().currentDestination?.label.toString()


    protected fun navBack() {
        try {
            findNavController().popBackStack()
        } catch (e: IllegalStateException) {
            Timber.e(e.toString())
        } catch (e: IllegalArgumentException) {
            Timber.e(e.toString())
        }
    }

    protected fun popBackStack(@IdRes destinationId: Int, inclusive: Boolean = false) {
        try {
            findNavController().popBackStack(destinationId, inclusive)
        } catch (e: IllegalStateException) {
            Timber.e(e.toString())
        } catch (e: IllegalArgumentException) {
            Timber.e(e.toString())
        }
    }

    fun showErrorDialog(error: String?) {
        showToast(error?:"")
    }

    open fun showProgress() {
        isProgressShowing = true
        mainActivity?.showProgress()
    }

    open fun hideProgress() {
        isProgressShowing = false
        mainActivity?.hideProgress()
    }


    fun nav(
        direction: NavDirections,
        anim: NavigationAnimation? = NavigationAnimation.HORIZONTAL,
        navController: NavController? = findNavController()
    ) {
        if (anim != null) {
            val currentNavOptions =
                navController?.currentDestination?.getAction(direction.actionId)?.navOptions
            try {
                val animatedNavOptions =
                    NavigationUtils.getAnimatedNavOptions(currentNavOptions, anim)
                navController?.navigate(direction, animatedNavOptions)
            } catch (e: IllegalStateException) {
                Timber.e(e.toString())
            } catch (e: IllegalArgumentException) {
                Timber.e(e.toString())
            }
        } else {
            try {
                navController?.navigate(direction)
            } catch (e: IllegalStateException) {
                Timber.e(e.toString())
            } catch (e: IllegalArgumentException) {
                Timber.e(e.toString())
            }
        }
    }

    protected fun navParent(
        direction: NavDirections,
        anim: NavigationAnimation = NavigationAnimation.HORIZONTAL
    ) {
        nav(direction, anim, parentFragment?.parentFragment?.findNavController())
    }

    override fun onDetach() {
        if (isProgressShowing) {
            mainActivity?.hideProgress()
        }
        super.onDetach()
    }
}