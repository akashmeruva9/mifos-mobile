package org.mifos.mobile.feature.user_profile.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.mifos.mobile.feature.third.party.user_profile.R
import org.mifos.mobile.feature.user_profile.utils.UserDetails

/**
 * @author pratyush
 * @since 20/12/2023
 */

@Composable
fun UserProfileDetails(
    userDetails: UserDetails
) {
    Column {
        Text(
            modifier = Modifier.padding(top = 16.dp, start = 8.dp),
            text = stringResource(id = R.string.user_details),
            color = MaterialTheme.colorScheme.onSecondary,
            style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.SemiBold),
        )
        Row(
            modifier = Modifier.padding(start = 8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                modifier = Modifier.padding(top = 8.dp, end = 8.dp),
                painter = painterResource(id = R.drawable.ic_phone_24dp),
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = null
            )
            if( userDetails.phoneNumber != null ){
                Text(
                    text = userDetails.phoneNumber,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }
        Row(
            modifier = Modifier.padding(start = 8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                modifier = Modifier.padding(top = 8.dp, end = 8.dp),
                painter = painterResource(id = R.drawable.ic_cake_24dp),
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = null
            )
            if( userDetails.dob != null ){
                Text(
                    text = userDetails.dob,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }
        Row(
            modifier = Modifier.padding(start = 8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Icon(
                modifier = Modifier.padding(top = 8.dp, end = 8.dp),
                painter = painterResource(id = R.drawable.ic_gender_24dp),
                tint = MaterialTheme.colorScheme.surfaceTint,
                contentDescription = null
            )
            if(userDetails.gender != null) {
                Text(
                    text = userDetails.gender,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }
    }
}