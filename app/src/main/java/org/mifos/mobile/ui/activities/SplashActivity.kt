package org.mifos.mobile.ui.activities

import android.content.Intent
import android.os.Bundle
import com.mifos.compose.utility.PreferenceManager
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.login.LoginActivity
import org.mifos.mobile.core.common.Constants

/*
* Created by saksham on 01/June/2018
*/
class SplashActivity : BaseActivity() {

    private var passcodePreferencesHelper: PreferenceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        val intent: Intent?
        super.onCreate(savedInstanceState)
        passcodePreferencesHelper = PreferenceManager(this)
        if (passcodePreferencesHelper?.hasPasscode == true) {
            intent = Intent(this, PassCodeComposeActivity::class.java)
            intent.putExtra(Constants.INTIAL_LOGIN, true)
        } else {
            intent = Intent(this, LoginActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
