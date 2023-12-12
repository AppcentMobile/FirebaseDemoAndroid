package com.appcent.android.firebasedemo.ui.view.auth

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.appcent.android.firebasedemo.R
import com.appcent.android.firebasedemo.databinding.FragmentLoginBinding
import com.appcent.android.firebasedemo.domain.util.extensions.collectFlow
import com.appcent.android.firebasedemo.domain.util.extensions.showToast
import com.appcent.android.firebasedemo.ui.base.BaseFragment
import com.appcent.android.firebasedemo.ui.view.auth.state.AuthenticationViewState
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override fun getViewBinding() = FragmentLoginBinding.inflate(layoutInflater)

    private val viewModel: AuthenticationViewModel by hiltNavGraphViewModels(R.id.nav_authentication)

    override fun setClickListeners() {
        super.setClickListeners()
        with(binding) {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()
                viewModel.login(email, password)
            }
            btnRegister.setOnClickListener {
                nav(LoginFragmentDirections.actionLoginToRegisterFragment())
            }
        }
    }

    override fun setObservers() {
        super.setObservers()
        collectFlow(viewModel.authenticationViewState) { handleViewState(it) }
    }

    private fun handleViewState(viewState: AuthenticationViewState) {
        hideProgress()
        when (viewState) {

            AuthenticationViewState.Loading -> {
                showProgress()
            }

            is AuthenticationViewState.Success -> {
                nav(LoginFragmentDirections.actionLoginToConversations())
            }

            is AuthenticationViewState.Error -> {
                showToast(viewState.errorMessage)
            }
        }
    }
}