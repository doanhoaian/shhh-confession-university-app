package vn.dihaver.tech.shhh.confession.feature.auth.viewmodel

import androidx.lifecycle.ViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import vn.dihaver.tech.shhh.confession.core.domain.model.UserSession
import vn.dihaver.tech.shhh.confession.feature.auth.AuthViewModel

@HiltViewModel(assistedFactory = ConfirmInfoViewModel.Factory::class)
class ConfirmInfoViewModel @AssistedInject constructor(
    @Assisted private val authViewModel: AuthViewModel
) : ViewModel() {

    companion object {
        private const val TAG = "AAA-ConfirmInfoViewModel"
    }

    val userSession: UserSession?
        get() = authViewModel.userSession.value


    @AssistedFactory
    interface Factory {
        fun create(authViewModel: AuthViewModel): ConfirmInfoViewModel
    }
}