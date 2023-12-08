package com.appcent.android.firebasedemo.ui.view.auth

import android.widget.Toast
import androidx.fragment.app.viewModels
import com.appcent.android.firebasedemo.databinding.FragmentLoginBinding
import com.appcent.android.firebasedemo.domain.util.extensions.collectFlow
import com.appcent.android.firebasedemo.ui.base.BaseFragment
import com.appcent.android.firebasedemo.ui.view.auth.state.LoginViewState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override fun getViewBinding() = FragmentLoginBinding.inflate(layoutInflater)

    private val viewModel: LoginViewModel by viewModels()

    override fun initUi() {
        super.initUi()

        collectFlow(viewModel.loginViewState) {
            handleViewState(it)
        }
    }

    private fun handleViewState(viewState: LoginViewState) {
        when (viewState) {
            is LoginViewState.Error -> {
                Toast.makeText(requireContext(), viewState.errorMessage, Toast.LENGTH_LONG).show()
            }

            LoginViewState.Loading -> {

            }

            is LoginViewState.Success -> {
                viewState.data
            }
        }
    }
}