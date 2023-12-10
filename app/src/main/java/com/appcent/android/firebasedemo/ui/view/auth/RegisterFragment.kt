package com.appcent.android.firebasedemo.ui.view.auth

import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.appcent.android.firebasedemo.R
import com.appcent.android.firebasedemo.databinding.FragmentRegisterBinding
import com.appcent.android.firebasedemo.domain.util.extensions.collectFlow
import com.appcent.android.firebasedemo.domain.util.extensions.showToast
import com.appcent.android.firebasedemo.ui.base.BaseFragment
import com.appcent.android.firebasedemo.ui.view.auth.state.AuthenticationViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override fun getViewBinding() = FragmentRegisterBinding.inflate(layoutInflater)

    private val viewModel: AuthenticationViewModel by hiltNavGraphViewModels(R.id.nav_authentication)

    override fun setObservers() {
        super.setObservers()
        collectFlow(viewModel.authenticationViewState) { handleViewState(it) }
    }

    override fun setClickListeners() {
        super.setClickListeners()
        with(binding) {
            btnSignUp.setOnClickListener {
                val name = etName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val password = etPassword.text.toString().trim()
                viewModel.singIn(name, email, password)
            }
        }
    }

    private fun handleViewState(viewState: AuthenticationViewState) {
        when (viewState) {

            AuthenticationViewState.Loading -> {}

            is AuthenticationViewState.Success -> {
                showToast("Success Register")
            }

            is AuthenticationViewState.Error -> {
                showToast(viewState.errorMessage)
            }
        }
    }
}