package org.mifos.mobile.ui.registration

import android.content.Context
import androidx.core.content.ContextCompat.getString
import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import org.mifos.mobile.R
import org.mifos.mobile.core.data.repositories.UserAuthRepository
import org.mifos.mobile.utils.PasswordStrength
import org.mifos.mobile.utils.RegistrationUiState
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(private val userAuthRepositoryImp: UserAuthRepository) :
    ViewModel() {

    private val _registrationUiState =
        MutableStateFlow<RegistrationUiState>(RegistrationUiState.Initial)
    val registrationUiState: StateFlow<RegistrationUiState> get() = _registrationUiState

    private val _registrationVerificationUiState =
        MutableStateFlow<RegistrationUiState>(RegistrationUiState.Initial)
    val registrationVerificationUiState: StateFlow<RegistrationUiState> get() = _registrationVerificationUiState

    fun isInputFieldBlank(fieldText: String): Boolean {
        return fieldText.trim().isEmpty()
    }
    fun isPhoneNumberValid(fieldText: String?): Boolean {
        if (fieldText.isNullOrBlank()) {
            return false
        }

        val phoneNumberPattern = "^\\+?[0-9]{10,15}\$"
        val regex = phoneNumberPattern.toRegex()
        return regex.matches(fieldText.trim())
    }

    fun isInputLengthInadequate(fieldText: String): Boolean {
        return fieldText.trim().length < 6
    }

    fun inputHasSpaces(fieldText: String): Boolean {
        return fieldText.trim().contains(" ")
    }

    fun hasLeadingTrailingSpaces(fieldText: String): Boolean {
        return fieldText.trim().length < fieldText.length
    }

    fun isEmailInvalid(emailText: String): Boolean {
        return !PatternsCompat.EMAIL_ADDRESS.matcher(emailText.trim()).matches()
    }

    fun areFieldsValidated(
        context: Context,
        accountNumberContent: String,
        usernameContent: String,
        firstNameContent: String,
        lastNameContent: String,
        phoneNumberContent: String,
        emailContent: String,
        passwordContent: String,
        confirmPasswordContent: String,
    ): String {
        return when {
            isInputFieldBlank(accountNumberContent) -> {
                val errorMessage = context.getString(
                    R.string.error_validation_blank,
                    context.getString(R.string.account_number)
                )
                errorMessage
            }

            isInputFieldBlank(usernameContent) -> {
                val errorMessage = context.getString(
                    R.string.error_validation_blank,
                    context.getString(R.string.username)
                )
                errorMessage
            }

            isInputLengthInadequate(usernameContent) -> {
                val errorMessage = context.getString(R.string.error_username_greater_than_six)
                errorMessage
            }

            inputHasSpaces(usernameContent) -> {
                val errorMessage = context.getString(
                    R.string.error_validation_cannot_contain_spaces,
                    context.getString(R.string.username),
                    context.getString(R.string.not_contain_username),
                )
                errorMessage
            }

            isInputFieldBlank(firstNameContent) -> {
                val errorMessage = context.getString(
                    R.string.error_validation_blank,
                    context.getString(R.string.first_name)
                )
                errorMessage
            }

            isInputFieldBlank(lastNameContent) -> {
                val errorMessage = context.getString(
                    R.string.error_validation_blank,
                    context.getString(R.string.last_name)
                )
                errorMessage
            }

            !isPhoneNumberValid(phoneNumberContent) -> {
                val errorMessage = context.getString(R.string.invalid_phn_number)
                errorMessage
            }

            isInputFieldBlank(emailContent) -> {
                val errorMessage = context.getString(
                    R.string.error_validation_blank,
                    context.getString(R.string.email)
                )
                errorMessage
            }

            isInputFieldBlank(passwordContent) -> {
                val errorMessage = context.getString(
                    R.string.error_validation_blank,
                    context.getString(R.string.password)
                )
                errorMessage
            }

            hasLeadingTrailingSpaces(passwordContent) -> {
                val errorMessage = context.getString(
                    R.string.error_validation_cannot_contain_leading_or_trailing_spaces,
                    context.getString(R.string.password),
                )
                errorMessage
            }

            isEmailInvalid(emailContent) -> {
                val errorMessage = getString(context, R.string.error_invalid_email)
                errorMessage
            }

            isInputLengthInadequate(passwordContent) -> {
                val errorMessage = context.getString(
                    R.string.error_validation_minimum_chars,
                    context.getString(R.string.password),
                    context.resources.getInteger(R.integer.password_minimum_length),
                )
                errorMessage
            }

            passwordContent != confirmPasswordContent -> {
                val errorMessage = context.getString(R.string.error_password_not_match)
                errorMessage
            }

            else -> ""
        }
    }

    fun updatePasswordStrengthView(password: String, context: Context): Float {
        val str = PasswordStrength.calculateStrength(password)
        return when (str.getText(context)) {
            getString(context, R.string.password_strength_weak) -> 0.25f
            getString(context, R.string.password_strength_medium) -> 0.5f
            getString(context, R.string.password_strength_strong) -> 0.75f
            else -> 1f
        }
    }

    fun registerUser(
        accountNumber: String,
        authenticationMode: String,
        email: String,
        firstName: String,
        lastName: String,
        mobileNumber: String,
        password: String,
        username: String
    ) {
        viewModelScope.launch {
            _registrationUiState.value = RegistrationUiState.Loading
            userAuthRepositoryImp.registerUser(
                accountNumber,
                authenticationMode,
                email,
                firstName,
                lastName,
                mobileNumber,
                password,
                username
            ).catch {
                _registrationUiState.value =
                    RegistrationUiState.Error(R.string.could_not_register_user_error)
            }.collect {
                _registrationUiState.value = RegistrationUiState.Success
            }
        }
    }

    fun verifyUser(authenticationToken: String?, requestId: String?) {
        viewModelScope.launch {
            _registrationVerificationUiState.value = RegistrationUiState.Loading
            userAuthRepositoryImp.verifyUser(authenticationToken, requestId).catch {
                _registrationVerificationUiState.value =
                    RegistrationUiState.Error(R.string.could_not_register_user_error)
            }.collect {
                _registrationVerificationUiState.value =
                    RegistrationUiState.Success
            }
        }
    }
}