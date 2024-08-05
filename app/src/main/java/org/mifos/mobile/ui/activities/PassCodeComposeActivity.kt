package org.mifos.mobile.ui.activities

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mifos.compose.PasscodeRepository
import com.mifos.compose.component.PasscodeScreen
import com.mifos.compose.theme.MifosPasscodeTheme
import com.mifos.compose.utility.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.datastore.PreferencesHelper
import org.mifos.mobile.ui.login.LoginActivity
import javax.inject.Inject

@AndroidEntryPoint
class PassCodeComposeActivity : AppCompatActivity() {

    @Inject
    lateinit var passcodeRepository: PasscodeRepository

    @JvmField
    @Inject
    var preferencesHelper: PreferencesHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MifosPasscodeTheme {
                PasscodeScreen(
                    onForgotButton = { onPasscodeForgot() },
                    onSkipButton = { onPasscodeSkip() },
                    onPasscodeConfirm = { onPassCodeReceive(it) },
                    onPasscodeRejected = { onPasscodeReject() },
                    activity = this,
                    onBiometricAuthenticated = { launchNextActivity() }
                )
            }
        }
    }

    private fun onPassCodeReceive(passcode: String) {
        if (passcodeRepository.getSavedPasscode() == passcode) {
            launchNextActivity()
        }
    }

    private fun launchNextActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
        Toast.makeText(this, "New Screen", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun onPasscodeReject() {}

    private fun onPasscodeForgot() {
        PreferenceManager(this).hasPasscode = false
        showLogoutDialog()
        Toast.makeText(this, "Forgot Passcode", Toast.LENGTH_SHORT).show()
    }

    private fun onPasscodeSkip() {
        Toast.makeText(this, "Skip Button", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(this, R.style.RedDialog)
            .setTitle(R.string.dialog_logout)
            .setIcon(R.drawable.ic_logout)
            .setPositiveButton(getString(R.string.logout)) { _, _ ->
                preferencesHelper?.clear()
                val i = Intent(this@PassCodeComposeActivity, LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()
                Toast.makeText(this, R.string.logged_out_successfully, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(getString(R.string.cancel)) { dialogInterface, _ -> dialogInterface.dismiss() }
            .create()
            .show()
    }
}