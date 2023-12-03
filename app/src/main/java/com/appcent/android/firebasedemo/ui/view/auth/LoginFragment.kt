package com.appcent.android.firebasedemo.ui.view.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.appcent.android.firebasedemo.R
import com.appcent.android.firebasedemo.databinding.FragmentLoginBinding
import com.appcent.android.firebasedemo.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    override fun getViewBinding() = FragmentLoginBinding.inflate(layoutInflater)
}