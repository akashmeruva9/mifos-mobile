package org.mifos.mobile.ui.savings_make_transfer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.mifos.mobile.R
import org.mifos.mobile.core.ui.component.mifosComposeView
import org.mifos.mobile.ui.activities.base.BaseActivity
import org.mifos.mobile.core.model.enums.TransferType
import org.mifos.mobile.ui.fragments.base.BaseFragment
import org.mifos.mobile.ui.transfer_process.TransferProcessComposeFragment
import org.mifos.mobile.core.model.entity.payload.TransferPayload
import org.mifos.mobile.core.common.utils.DateHelper
import org.mifos.mobile.core.common.utils.getTodayFormatted


@AndroidEntryPoint
class SavingsMakeTransferComposeFragment : BaseFragment() {

    private val viewModel: SavingsMakeTransferViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? BaseActivity)?.hideToolbar()
        if (arguments != null) {
            viewModel.initArgs(
                accountId = arguments?.getLong(org.mifos.mobile.core.common.Constants.ACCOUNT_ID),
                transferType =  arguments?.getString(org.mifos.mobile.core.common.Constants.TRANSFER_TYPE),
                outstandingBalance = arguments?.getDouble(org.mifos.mobile.core.common.Constants.OUTSTANDING_BALANCE)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return mifosComposeView(requireContext()) {
            SavingsMakeTransferScreen(
                navigateBack = { activity?.onBackPressedDispatcher?.onBackPressed() },
                onCancelledClicked = { activity?.onBackPressedDispatcher?.onBackPressed() },
                reviewTransfer = { reviewTransfer(payload = it) }
            )
        }
    }

    private fun reviewTransfer(payload: ReviewTransferPayload) {
        val transferPayload = TransferPayload().apply {
            fromAccountId = payload.payFromAccount?.accountId
            fromClientId = payload.payFromAccount?.clientId
            fromAccountType = payload.payFromAccount?.accountType?.id
            fromOfficeId = payload.payFromAccount?.officeId
            toOfficeId = payload.payFromAccount?.officeId
            toAccountId = payload.payToAccount?.accountId
            toClientId = payload.payToAccount?.clientId
            toAccountType = payload.payToAccount?.accountType?.id
            transferDate = DateHelper.getSpecificFormat(DateHelper.FORMAT_dd_MMMM_yyyy, getTodayFormatted())
            transferAmount = payload.amount.toDoubleOrNull()
            transferDescription = payload.review
            fromAccountNumber = payload.payFromAccount?.accountNo
            toAccountNumber = payload.payToAccount?.accountNo
        }

        (activity as BaseActivity?)?.replaceFragment(
            TransferProcessComposeFragment.newInstance(transferPayload, TransferType.SELF),
            true,
            R.id.container,
        )
    }


    companion object {
        /**
         * Provides an instance of [SavingsMakeTransferFragment], use `transferType` as
         * `Constants.TRANSFER_PAY_TO` when we want to deposit and
         * `Constants.TRANSFER_PAY_FROM` when we want to make a transfer
         *
         * @param accountId    Saving account Id
         * @param transferType Type of transfer i.e. `Constants.TRANSFER_PAY_TO` or
         * `Constants.TRANSFER_PAY_FROM`
         * @return Instance of [SavingsMakeTransferFragment]
         */
        fun newInstance(accountId: Long?, transferType: String?): SavingsMakeTransferComposeFragment {
            val transferFragment = SavingsMakeTransferComposeFragment()
            val args = Bundle()
            if (accountId != null) args.putLong(org.mifos.mobile.core.common.Constants.ACCOUNT_ID, accountId)
            args.putString(org.mifos.mobile.core.common.Constants.TRANSFER_TYPE, transferType)
            transferFragment.arguments = args
            return transferFragment
        }

        fun newInstance(
            accountId: Long?,
            outstandingBalance: Double?,
            transferType: String?,
        ): SavingsMakeTransferComposeFragment {
            val transferFragment = SavingsMakeTransferComposeFragment()
            val args = Bundle()
            if (accountId != null) args.putLong(org.mifos.mobile.core.common.Constants.ACCOUNT_ID, accountId)
            args.putString(org.mifos.mobile.core.common.Constants.TRANSFER_TYPE, transferType)
            if (outstandingBalance != null) {
                args.putDouble(
                    org.mifos.mobile.core.common.Constants.OUTSTANDING_BALANCE,
                    outstandingBalance,
                )
            }
            args.putBoolean(org.mifos.mobile.core.common.Constants.LOAN_REPAYMENT, true)
            transferFragment.arguments = args
            return transferFragment
        }
    }
}
