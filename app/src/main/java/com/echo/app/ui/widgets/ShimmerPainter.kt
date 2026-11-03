package com.echo.app.ui.widgets

import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun shimmerPainter(): Painter {
    val transition = rememberInfiniteTransition(label = "shimmer")

    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_translate"
    )

    return remember {
        object : Painter() {
            override val intrinsicSize: Size = Size.Unspecified

            override fun DrawScope.onDraw() {
                val brush = Brush.linearGradient(
                    colors = listOf(
                        Color.LightGray.copy(alpha = 0.6f),
                        Color.LightGray.copy(alpha = 0.2f),
                        Color.LightGray.copy(alpha = 0.6f),
                    ),
                    start = Offset.Zero,
                    end = Offset(x = translateAnim.value, y = translateAnim.value)
                )
                drawRect(brush = brush)
            }
        }
    }
}