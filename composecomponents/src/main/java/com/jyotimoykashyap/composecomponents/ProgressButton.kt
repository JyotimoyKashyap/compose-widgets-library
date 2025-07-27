package com.jyotimoykashyap.composecomponents

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection


/**
 * A custom progress button Composable that displays an animated fill
 * from left to right, contained within the button's specified shape.
 *
 * This button behaves like a standard Material Design button but includes
 * a visual progress indicator that animates its width from 0% to 100%
 * over a given duration. The progress animation is always clipped to match
 * the button's corners, ensuring a seamless visual effect.
 *
 * Example Usage:
 * ```kotlin
 * SpeseProgressButton(
 *   text = "Let's Get Started",
 *   onClick = {
 *      println("On Click is triggered")
 *   },
 *   shape = RoundedCornerShape(corner = CornerSize(12.dp)),
 *   onProgressComplete = {
 *      println("Progress is completed")
 *   }
 * )
 * ```
 *
 * @param text The text to display inside the button.
 * @param shape The shape of the button. The progress animation will be
 * clipped to this shape, ensuring its corners match.
 * Defaults to [CircleShape], which creates a pill-like button.
 * @param progressDurationMillis The duration of the progress animation in milliseconds.
 * Defaults to 5000ms (5 seconds).
 * @param onClick The callback to be invoked when the button is clicked.
 * @param onProgressComplete The callback to be invoked when the progress animation has fully completed.
 * Defaults to an empty lambda, so it's optional.
 */
@Composable
fun ProgressButton(
    text: String,
    shape: Shape = CircleShape,
    progressDurationMillis: Int = 5000,
    onClick: () -> Unit,
    onProgressComplete: () -> Unit = {}
) {
    val color = MaterialTheme.colorScheme.primary
    val animatedWithFraction = remember { Animatable(0f) }
    val density = LocalDensity.current
    val layoutDirection = LayoutDirection.Ltr
    val defaultButtonClipShape = shape
    // LaunchedEffect to start the animation when the composable enters the composition
    LaunchedEffect(Unit) {
        animatedWithFraction.animateTo(
            targetValue = 1f, // Animate to 1f (100% of width)
            animationSpec = tween(durationMillis = progressDurationMillis, easing = LinearEasing)
        )
        onProgressComplete()
    }
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(IntrinsicSize.Max)
            .drawBehind {
                val currentSize = this.size
                // Get the size of the drawing area (which is the button's size)
                // Get the shorter side of the button (width or height)
                val outline = defaultButtonClipShape
                    .createOutline(
                        currentSize,
                        layoutDirection,
                        density
                    )
                val buttonShapePath = Path().apply { addOutline(outline) }

                // Calculate the width of the animating progress rectangle based on the animated value
                val progressRectWidth = currentSize.width * animatedWithFraction.value
                clipPath(buttonShapePath) {
                    drawRect(
                        color = color,
                        size = Size(
                            // animate this width to move from 0 .. width
                            width = progressRectWidth,
                            height = currentSize.height
                        ),
                    )
                }
            },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = shape
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}