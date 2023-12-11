package com.appcent.android.firebasedemo.ui.view.chats.userlist

import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.appcent.android.firebasedemo.data.model.User
import com.appcent.android.firebasedemo.databinding.FragmentUserListBinding
import com.appcent.android.firebasedemo.domain.util.extensions.collectFlow
import com.appcent.android.firebasedemo.ui.base.BaseFragment
import com.appcent.android.firebasedemo.ui.view.chats.userlist.state.UserListViewState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment:BaseFragment<FragmentUserListBinding>(), SearchView.OnQueryTextListener {

    private val viewModel: UserListViewModel by viewModels()


    private val usersAdapter = UsersAdapter() {userId->
        viewModel.getConversationId(userId)
    }

    private fun navToChat(conversationId: String) {
        nav(UserListFragmentDirections.actionUserListToChatDetail(conversationId))
    }

    override fun getViewBinding() =
        FragmentUserListBinding.inflate(layoutInflater)

    override fun initUi() {
        binding.rvUsers.adapter = usersAdapter
    }

    override fun delayedInitUi() {
        viewModel.getUsers()
    }

    override fun setObservers() {
        collectFlow(viewModel.userListviewState) {
            handleUiState(it)
        }
    }

    private fun handleUiState(userListViewState: UserListViewState) {
        hideProgress()
        when(userListViewState) {
            UserListViewState.Empty -> showEmptyView()
            is UserListViewState.Error -> showErrorDialog(userListViewState.errorMessage)
            UserListViewState.Loading -> showProgress()
            is UserListViewState.Success -> showUsers(userListViewState.userList)
            is UserListViewState.OpenConversation -> navToChat(userListViewState.conversationId)
        }
    }

    private fun showEmptyView() {
        with(binding) {
            emptyState.isVisible = true
            rvUsers.isVisible = false
        }
    }

    private fun showUsers(userList: List<User>) {
        with(binding) {
            rvUsers.isVisible = true
            emptyState.isVisible = false
        }
        usersAdapter.updateDate(userList)
    }

    override fun setClickListeners() {
        with(binding) {
            searchViewUsers.setOnQueryTextListener(this@UserListFragment)
        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.getUsers(newText)
        return true
    }
}