package org.mifos.mobile.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.theme.MifosMobileTheme
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.fragments.base.BaseFragment

/**
 * Created by dilpreet on 31/7/17.
 */
@AndroidEntryPoint
class RegistrationComposeFragment : BaseFragment() {

    private lateinit var viewModel: RegistrationViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MifosMobileTheme {
                    RegistrationScreen(
                        onVerified = { showRegisteredSuccessfully() },
                        navigateBack = { activity?.onBackPressed() },
                    )
                }
            }
        }
    }

    private fun showRegisteredSuccessfully() {
        (activity as BaseActivity?)?.replaceFragment(
            RegistrationVerificationComposeFragment.newInstance(),
            true,
            R.id.container,
        )
    }

    companion object {
        fun newInstance(): RegistrationComposeFragment {
            return RegistrationComposeFragment()
        }
    }
}
