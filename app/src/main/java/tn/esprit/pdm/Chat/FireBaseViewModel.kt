package tn.esprit.pdm.Chat

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class FireBaseViewModel @Inject constructor() : ViewModel() {

    private val _state1 = MutableStateFlow<SignInState>(SignInState.Nothing)
    val state1 = _state1.asStateFlow()

    fun loginFireBase(email: String, password: String) {
        _state1.value = SignInState.Loading
        // Firebase signIn
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.let {
                        _state1.value = SignInState.Success
                        return@addOnCompleteListener
                    }
                    _state1.value = SignInState.Error

                } else {
                    _state1.value = SignInState.Error
                }
            }
    }

    private val _state2 = MutableStateFlow<SignUpState>(SignUpState.Nothing)
    val state2 = _state2.asStateFlow()

    fun signUpFireBase(image: String, name: String, email: String, password: String) {
        _state2.value = SignUpState.Loading

        // Convert image URL (String) to Uri
        val imageUri = Uri.parse(image)

        // Firebase signUp
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result.user?.let {
                        it.updateProfile(
                            com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                .setPhotoUri(imageUri)  // Use the Uri here
                                .setDisplayName(name)
                                .build()
                        )?.addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                _state2.value = SignUpState.Success
                            } else {
                                _state2.value = SignUpState.Error
                            }
                        }
                    } ?: run {
                        _state2.value = SignUpState.Error
                    }
                } else {
                    _state2.value = SignUpState.Error
                }
            }
    }

}

sealed class SignInState {
    object Nothing : SignInState()
    object Loading : SignInState()
    object Success : SignInState()
    object Error : SignInState()
}
sealed class SignUpState {
    object Nothing : SignUpState()
    object Loading : SignUpState()
    object Success : SignUpState()
    object Error : SignUpState()
}