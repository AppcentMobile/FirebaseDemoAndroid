package com.appcent.android.firebasedemo.ui.view.chats.userlist

import com.appcent.android.firebasedemo.domain.FirebaseDBHelper
import com.mkhakpaki.sinatobechanged.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val firebaseDBHelper: FirebaseDBHelper
) : BaseViewModel() {


}