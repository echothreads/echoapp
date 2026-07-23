package com.echo.app.feature.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecureTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldLabelPosition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echo.app.R
import com.echo.app.ui.theme.EchoTheme
import com.echo.app.ui.widgets.TintedTextButton

@Composable
fun LoginView() {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            IconButton(onClick = {},
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent, disabledContainerColor = Color.Transparent)) {
                Icon(painterResource(R.drawable.arrow_back),
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = stringResource(R.string.back_button))
            }
            // TOP SECTION: Logo and Info
            Column(
                modifier = Modifier.align(Alignment.TopCenter)
                    .padding(vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painterResource(R.drawable.outline_account_circle),
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null,
                )

                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Account", textAlign = TextAlign.Center, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        val titleStr = stringResource(R.string.app_name_caps)
                        val disclaimerStr = stringResource(R.string.register_disclaimer, titleStr)
                        Text(disclaimerStr,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            val useremailState = rememberTextFieldState()
            val passwordState = rememberTextFieldState()
            Column(modifier = Modifier.align(Alignment.BottomCenter), horizontalAlignment = Alignment.CenterHorizontally) {
                TextField(state = useremailState,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource((R.string.login_entry_hint)), textAlign = TextAlign.Center)},
                    labelPosition = TextFieldLabelPosition.Attached(expandedAlignment = Alignment.CenterHorizontally),
                    colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )
                Spacer(modifier = Modifier.padding(vertical = 6.dp))
                SecureTextField(state = passwordState,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource((R.string.login_password_entry_hint)), textAlign = TextAlign.Center)},
                    labelPosition = TextFieldLabelPosition.Attached(expandedAlignment = Alignment.CenterHorizontally),
                    colors = TextFieldDefaults.colors(focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent),
                    shape = RoundedCornerShape(8.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password))
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp).width(38.dp), thickness = 4.dp)
                TintedTextButton(
                    onClick = { /* login */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                ) {
                    Text(stringResource(R.string.login_caps), letterSpacing = 1.sp, fontWeight = FontWeight.Medium)
                }
                Text(buildAnnotatedString {
                    val str = stringResource(R.string.login_forgot_password)
                    val link = LinkAnnotation.Url(
                            url = "https://localhost:8000/forgot", // TODO: UNHARDCODE
                            styles = androidx.compose.ui.text.TextLinkStyles(
                                style = SpanStyle(
                                    //color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                    textDecoration = TextDecoration.Underline
                                )
                            )
                        )
                        withLink(link) {
                            append(str)
                        }
                    },
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                lineHeight = 16.sp)
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun LoginViewPreview() {
    EchoTheme {
        LoginView()
    }
}