package org.mifos.mobile.ui.qr

/*
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.ui.beneficiary_application.BeneficiaryApplicationComposeFragment
import org.mifos.mobile.core.model.enums.BeneficiaryState
import org.mifos.mobile.ui.fragments.base.BaseFragment
import com.google.zxing.Result
import org.mifos.mobile.core.model.entity.beneficiary.Beneficiary
import org.mifos.mobile.feature.qr.qr_code_import.QrCodeImportScreen

@AndroidEntryPoint
class QrCodeImportComposeFragment : BaseFragment() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return mifosComposeView(requireContext()) {
            QrCodeImportScreen(
                navigateBack = {
                    activity?.onBackPressedDispatcher?.onBackPressed()
                },
                handleDecodedResult = { result ->
                    handleDecodedResult(result)
                },
                showRationaleChecker = {
                    checkPermissionRationale(it)
                }
            )
        }
    }

    /**
     * CallBack for [Image-Cropper] which retrieves data from QRCode
     * Opens [BeneficiaryApplicationFragment] with [BeneficiaryState] as
     * `BeneficiaryState.CREATE_QR`
     *
     * @param result contains the results from decoded QR bitmap
     */

    private fun handleDecodedResult(result: Result?) {
        val gson = Gson()
        try {
            val beneficiary = gson.fromJson(result?.text, Beneficiary::class.java)
            activity?.supportFragmentManager?.popBackStack()
            (activity as BaseActivity?)?.replaceFragment(
                BeneficiaryApplicationComposeFragment.newInstance(
                    BeneficiaryState.CREATE_QR,
                    beneficiary
                ),
                true,
                R.id.container,
            )
        } catch (e: JsonSyntaxException) {
            Toast.makeText(
                activity,
                getString(R.string.invalid_qr),
                Toast.LENGTH_SHORT,
            ).show()
        }
    }

    private fun checkPermissionRationale(permission: String): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)
    }

    override fun onResume() {
        super.onResume()
        (activity as? BaseActivity)?.hideToolbar()
    }

    companion object {
        fun newInstance(): QrCodeImportComposeFragment {
            return QrCodeImportComposeFragment()
        }
    }
}
*/