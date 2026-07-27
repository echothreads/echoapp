package com.echo.app.feature.profile.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight

import com.echo.app.R

@Composable
fun FollowedByText(
    name1: String,
    name2: String? = null,
    othersCount: Int = 0,
    onUserClick: (String) -> Unit
) {
    val fullText = when {
        name2 != null && othersCount > 0 -> pluralStringResource(
            id = R.plurals.followedby_more,
            count = othersCount,
            name1, name2, othersCount
        )
        name2 != null -> stringResource(R.string.followedby_2, name1, name2)
        else -> stringResource(R.string.followedby_1, name1)
    }

    val primaryColor = MaterialTheme.colorScheme.primary

    val annotatedText = buildAnnotatedString {
        append(fullText)

        fun applyClickableStyle(name: String) {
            val startIndex = fullText.indexOf(name)
            if (startIndex >= 0) {
                val endIndex = startIndex + name.length

                val link = LinkAnnotation.Clickable(
                    tag = name,
                    styles = TextLinkStyles(
                        style = SpanStyle(
                            color = primaryColor,
                            fontWeight = FontWeight.Bold
                        )
                    ),
                    linkInteractionListener = {
                        onUserClick(name)
                    }
                )

                addLink(link, start = startIndex, end = endIndex)
            }
        }

        applyClickableStyle(name1)
        if (name2 != null) applyClickableStyle(name2)
    }

    Text(
        text = annotatedText,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}