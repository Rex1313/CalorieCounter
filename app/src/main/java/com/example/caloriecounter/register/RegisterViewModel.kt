package com.example.caloriecounter.register

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.caloriecounter.R
import com.example.caloriecounter.base.BaseViewModel
import com.example.caloriecounter.base.ResourceProvider
import com.example.caloriecounter.base.toHex
import com.example.caloriecounter.database.UserSettings
import com.example.caloriecounter.models.User
import com.example.caloriecounter.utils.md5
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class RegisterViewModel : BaseViewModel() {

    val error = MutableLiveData<String>()
    val uiState = MutableLiveData<RegisterUIState>()

    init {
        uiState.postValue(RegisterUIState(RegisterUIState.State.DEFAULT))
    }

    fun register(name: String, password: String, passwordConfirmation: String) {
        validate(name, password, passwordConfirmation)
    }

    private fun getUserModel(username: String, pwd: String): User {
        return User(username, md5(pwd).toHex(), pwd)
    }

    private fun validate(name: String, password: String, passwordConfirmation: String): Boolean {
        if (name.isNotBlank()) {
            if (password.isBlank() || passwordConfirmation.isBlank()) {
                uiState.postValue(RegisterUIState(RegisterUIState.State.ERROR).apply {
                    this.errorMessage =
                        ResourceProvider.getString(resourceId = R.string.error_password_cannot_be_blank)
                })
                return false;
            }
            if (password == passwordConfirmation) {
                uiState.postValue(RegisterUIState(RegisterUIState.State.WAIT))
                GlobalScope.launch {
                    val userResponse =
                        repository.registerUserOnServer(
                            getUserModel(
                                username = name,
                                pwd = password
                            )
                        )

                    Log.d("User Response", userResponse.toString());
                    userResponse.token?.let {
                        repository.addUserSetting(getUserSettingsModel(username = name, pwdMd5 = md5(password).toHex(), token = userResponse.token))
                        uiState.postValue(RegisterUIState(RegisterUIState.State.SUCCESS))
                    }

                }
                return true
            } else {
                uiState.postValue(RegisterUIState(RegisterUIState.State.ERROR).apply {
                    this.errorMessage =
                        ResourceProvider.getString(resourceId = R.string.error_password_doesnt_match)
                })
                return false
            }


        }

        uiState.postValue(RegisterUIState(RegisterUIState.State.ERROR).apply {
            this.errorMessage =
                ResourceProvider.getString(resourceId = R.string.error_name_must_not_be_blank)
        })
        return false
    }


    fun getUserSettingsModel(username: String, pwdMd5: String, token: String) =  UserSettings(null,username, pwdMd5, token)


}