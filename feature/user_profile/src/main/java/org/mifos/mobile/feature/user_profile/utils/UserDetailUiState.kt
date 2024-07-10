package org.mifos.mobile.feature.user_profile.utils

import android.graphics.Bitmap
import org.mifos.mobile.core.model.entity.client.Client

sealed class UserDetailState {
    object Loading : UserDetailState()
    data class ShowError(val message: Int) : UserDetailState()
    data class ShowUserDetails(val client: Client) : UserDetailState()
    data class ShowUserImage(val image: Bitmap?) : UserDetailState()
}
