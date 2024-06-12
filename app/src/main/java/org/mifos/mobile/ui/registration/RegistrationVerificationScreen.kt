package org.mifos.mobile.ui.registration

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.MifosErrorComponent
import org.mifos.mobile.core.ui.component.MifosOutlinedTextField
import org.mifos.mobile.core.ui.component.MifosProgressIndicator
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.login.LoginActivity
import org.mifos.mobile.utils.Network
import org.mifos.mobile.utils.RegistrationUiState

@Composable
fun RegistrationVerificationScreen(
    viewModel: RegistrationViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
    finishActivity: ()->Unit)
{
    val uiState by viewModel.registrationUiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    RegistrationVerificationContent(uiState = uiState,
        verifyUser = { token, id -> viewModel.verifyUser(token, id) },
        finishActivity = finishActivity)
}


@Composable
    fun RegistrationVerificationContent(
    uiState: RegistrationUiState,
    verifyUser: (authenticationToken: String, requestID: String) -> Unit,
    finishActivity: () -> Unit
    ) {

    val context = LocalContext.current

    var requestID by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    var authenticationToken by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        when (uiState) {

            RegistrationUiState.Initial -> {

                Column(modifier = Modifier.fillMaxSize()) {

                    MifosOutlinedTextField(
                        value = requestID,
                        onValueChange = { requestID = it },
                        label = R.string.request_id,
                        supportingText = ""
                    )

                    MifosOutlinedTextField(
                        value = authenticationToken,
                        onValueChange = { authenticationToken = it },
                        label = R.string.authentication_token,
                        supportingText = ""
                    )

                    Button(
                        onClick = {
                            Toast.makeText(context, "Button Clicked", Toast.LENGTH_SHORT).show()
                            verifyUser(authenticationToken.toString(), requestID.toString())
                        },
                        Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, top = 4.dp),
                        contentPadding = PaddingValues(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSystemInDarkTheme()) Color(
                                0xFF9bb1e3
                            ) else Color(0xFF325ca8)
                        )
                    ) {
                        Text(text = stringResource(id = R.string.verify))
                    }
                }
            }

            is RegistrationUiState.Error -> {
                MifosErrorComponent(
                    isNetworkConnected = Network.isConnected(context),
                    isEmptyData = false,
                    isRetryEnabled = false
                )
            }

            RegistrationUiState.Loading -> {
                Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()

                MifosProgressIndicator(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp)
                        .background(MaterialTheme.colorScheme.background.copy(alpha = 0.7f))
                )
            }

            is RegistrationUiState.Success -> {

                context.startActivity(Intent(context, LoginActivity::class.java))
                Toast.makeText(
                    context,
                    getString(context, R.string.verified),
                    Toast.LENGTH_SHORT
                ).show()
                finishActivity()
            }
        }
    }
}

class RegistrationVerificationScreenPreviewProvider : PreviewParameterProvider<RegistrationUiState> {
    override val values: Sequence<RegistrationUiState>
        get() = sequenceOf(
            RegistrationUiState.Initial,
            RegistrationUiState.Loading,
            RegistrationUiState.Success,
            RegistrationUiState.Error(R.string.register)
        )
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun RegistrationVerificationScreenPreview(
    @PreviewParameter(RegistrationVerificationScreenPreviewProvider::class) registrationUiState : RegistrationUiState
) {
    MifosMobileTheme {
        RegistrationVerificationContent(
            uiState = registrationUiState,
            verifyUser = {_, _ -> /* Do nothing or handle dummy verification */ },
            finishActivity = {}
        )
    }
}