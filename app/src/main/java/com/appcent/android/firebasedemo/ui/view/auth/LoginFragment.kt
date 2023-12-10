package com.appcent.android.firebasedemo.ui.view.auth

import androidx.navigation.navGraphViewModels
import com.appcent.android.firebasedemo.R
import com.appcent.android.firebasedemo.databinding.FragmentLoginBinding
import com.appcent.android.firebasedemo.domain.util.extensions.collectFlow
import com.appcent.android.firebasedemo.domain.util.extensions.showToast
import com.appcent.android.firebasedemo.ui.base.BaseFragment
import com.appcent.android.firebasedemo.ui.view.auth.state.LoginViewState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override fun getViewBinding() = FragmentLoginBinding.inflate(layoutInflater)

    private val viewModel: LoginViewModel by navGraphViewModels(R.id.nav_authentication) {
        defaultViewModelProviderFactory
    }

    override fun setClickListeners() {
        super.setClickListeners()
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.login(email, password)
        }
    }

    override fun setObservers() {
        super.setObservers()
        collectFlow(viewModel.loginViewState) { handleViewState(it) }
    }

    private fun handleViewState(viewState: LoginViewState) {
        when (viewState) {
            is LoginViewState.Error -> {
                showToast(viewState.errorMessage)
            }

            LoginViewState.Loading -> {
            }

            is LoginViewState.Success -> {
                //TODO Chat Ekranına Yönlendirelecek
            }
        }
    }
}