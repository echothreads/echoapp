package com.echo.app.feature.search.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.echo.app.Utils
import com.echo.app.feature.search.domain.TrendModel
import com.echo.app.ui.theme.EchoTheme

@Composable
fun TrendingCard(trend: TrendModel, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier
            .border(width = 1.dp, color = MaterialTheme.colorScheme.surfaceContainerHighest, shape = RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.CenterStart) {
            if (trend.imageUrl != null) {
                AsyncImage(
                    model = trend.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alpha = 0.7f,
                    modifier = Modifier.matchParentSize()
                )
            }
            Column(modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("#${trend.ranking} Ranking",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.weight(1f))
                Text("#${trend.name}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge)
                Text("${Utils.formatNumber(trend.echoes)} Echoes",
                    style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun TrendingCardPreview() {
    EchoTheme {
        TrendingCard(
            modifier = Modifier.size(248.dp),
            trend = TrendModel(
                name = "TrendName",
                echoes = 568395,
                ranking = 2
            )
        )
    }
}