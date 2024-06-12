package org.mifos.mobile.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.fragments.base.BaseFragment

/**
 * Created by dilpreet on 31/7/17.
 */
@AndroidEntryPoint
class RegistrationVerificationFragment : BaseFragment() {

    private val registrationViewModel: RegistrationViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            RegistrationVerificationScreen(
                registrationViewModel,
                navigateBack = { activity?.onBackPressedDispatcher?.onBackPressed() } ,
                finishActivity = {activity?.finish()})
        }
    }

    companion object {
        fun newInstance(): RegistrationVerificationFragment {
            return RegistrationVerificationFragment()
        }
    }
}
