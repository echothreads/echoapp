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
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.echo.app.R
import com.echo.app.ui.theme.EchoTheme
import com.echo.app.ui.widgets.TintedTextButton

@Composable
fun WelcomeScreen() {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            // TOP SECTION: Logo and Info
            Column(
                modifier = Modifier.align(Alignment.TopCenter)
                    .padding(vertical = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_echo_icon),
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null,
                )

                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //Text("Welcome to ", fontSize = 24.sp)
                    Text("E", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("C", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("H", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("O", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(modifier = Modifier.padding(horizontal = 8.dp)) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        val titleStr = stringResource(R.string.app_name_caps)
                        val welcomeStr = stringResource(R.string.welcome_message, titleStr)
                        Text(
                            text = buildAnnotatedString {
                                append(welcomeStr)

                                val start = welcomeStr.indexOf(titleStr)
                                val end = start + titleStr.length

                                if (start >= 0) {
                                    addStyle(
                                        style = SpanStyle(
                                            fontWeight = FontWeight.Black,
                                            letterSpacing = 2.sp
                                        ),
                                        start,
                                        end
                                    )
                                }
                            },
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TintedTextButton(
                    onClick = { /* register */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                ) {
                    Text(stringResource(R.string.register_caps), letterSpacing = 1.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.padding(vertical = 6.dp))
                TintedTextButton(
                    onClick = { /* login */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                ) {
                    Text(stringResource(R.string.login_caps), letterSpacing = 1.sp, fontWeight = FontWeight.Medium)
                }
                Spacer(modifier = Modifier.padding(vertical = 6.dp))
                Text(buildAnnotatedString {
                    val matchStr = stringResource(R.string.tos)
                    val str = stringResource(R.string.start_disclaimer, matchStr)
                    append(str)
                    val link = LinkAnnotation.Url(
                        url = "https://localhost:8000/terms", // TODO: UNHARDCODE
                        styles = androidx.compose.ui.text.TextLinkStyles(
                            style = SpanStyle(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                textDecoration = TextDecoration.Underline
                            )
                        )
                    )
                    val match = str.indexOf(matchStr)
                    if (match >= 0)
                        addLink(link, start = match, match + matchStr.length)
                },
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
fun WelcomePreview() {
    EchoTheme {
        WelcomeScreen()
    }
}