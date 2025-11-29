package com.jyotimoykashyap.composecomponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.isActive
import kotlin.random.Random

/**
 * A [Modifier] that applies a dot grid with a propagating ripple animation.
 *
 * This modifier draws a grid of dots over the composable it's applied to.
 * Ripples originate from either the center or random points, expanding
 * outwards and causing the dots they pass over to briefly increase in size.
 * The animation uses a frame-based clock for smooth and reliable updates.
 *
 * @param dotColor The color of the dots in the grid.
 * @param dotSpacing The distance between the centers of adjacent dots.
 * @param dotRadius The base radius of each dot when not rippling.
 * @param animationDurationMillis The total duration for a single ripple to expand and fade out.
 * @param rippleSpawnIntervalMillis The time interval between the spawning of new ripples.
 * @param randomRippleCenter If true, ripples will originate from random points on the screen.
 *                           If false, ripples will originate from the center of the composable.
 *
 * @sample com.jyotimoykashyap.composecomponents.DotGridPattern
 *
 * Side Effects:
 * - Performance: For extremely large composables or very small `dotSpacing` and `dotRadius`,
 *   drawing many dots can have a performance impact, especially on older devices.
 *   The animation loop runs every frame, potentially causing continuous recompositions
 *   if not handled carefully, though this implementation optimizes by only updating
 *   `currentFrameTime` which triggers the `drawBehind` block.
 * - Memory: Stores a mutable list of active ripple start times. While a cleanup mechanism
 *   is in place, a very high `rippleSpawnIntervalMillis` combined with a long
 *   `animationDurationMillis` could lead to many ripples being tracked concurrently,
 *   though this is unlikely to be a significant issue in most use cases.
 */
@Composable
fun Modifier.ripplingDotGrid(
    dotColor: Color = Color.Black.copy(alpha = 0.08f),
    dotSpacing: Dp = 20.dp,
    dotRadius: Dp = 1.dp,
    animationDurationMillis: Int = 3000,
    rippleSpawnIntervalMillis: Int = 2000,
    randomRippleCenter: Boolean = true
): Modifier {

    // 1. Store just the START TIME of each active ripple
    val rippleStartTimes = remember { mutableStateListOf<Long>() }

    // 2. A state to hold the current frame time. Reading this in drawBehind triggers the redraw.
    val currentFrameTime = remember { mutableStateOf(0L) }

    // 3. The "Game Loop": Updates time and manages the list of ripples
    LaunchedEffect(Unit) {
        var lastSpawnTime = 0L

        // This loop runs every single frame
        while (isActive) {
            withFrameMillis { frameTime ->
                currentFrameTime.value = frameTime

                // A. Spawn new ripple if enough time has passed
                if (frameTime - lastSpawnTime > rippleSpawnIntervalMillis) {
                    rippleStartTimes.add(frameTime)
                    lastSpawnTime = frameTime
                }

                // B. Cleanup: Remove ripples that are effectively finished
                // (We keep them a bit longer to ensure they fully fade out)
                rippleStartTimes.removeAll { startTime ->
                    frameTime - startTime > animationDurationMillis + 1000
                }
            }
        }
    }

    return this.drawBehind {
        // Read the state to ensure this block runs every frame
        val now = currentFrameTime.value

        // Static grid params
        val spacingPx = dotSpacing.toPx()
        val baseRadiusPx = dotRadius.toPx()
        val maxRippleDistance = size.width.coerceAtLeast(size.height) * 0.8f
        val rippleMaxScale = 2.0f // Make it pop a bit more

        // Loop through dots
        var x = 0f
        while (x <= size.width) {
            var y = 0f
            while (y <= size.height) {
                val currentDotCenter = Offset(x, y)
                var currentRadius = baseRadiusPx

                // --- RIPPLE LOGIC ---
                // Iterate through all active start times
                rippleStartTimes.forEach { startTime ->
                    // 1. Calculate how long this ripple has been alive
                    val elapsedTime = now - startTime

                    if (elapsedTime >= 0) {
                        // 2. Calculate Progress (0.0 to 1.0)
                        val progress = elapsedTime.toFloat() / animationDurationMillis

                        // 3. Determine Center
                        // We use a Random seeded with the startTime so the center
                        // stays the same for the life of the ripple, but changes for the next one.
                        val random = Random(startTime)
                        val rippleCenter = if (randomRippleCenter) {
                            Offset(
                                x = random.nextFloat() * size.width,
                                y = random.nextFloat() * size.height
                            )
                        } else {
                            center
                        }

                        // 4. Calculate Distance and Phase
                        val distanceToCenter = (currentDotCenter - rippleCenter).getDistance()
                        val normalizedDistance = distanceToCenter / maxRippleDistance

                        // The "wave" travels outwards.
                        // (progress * 1.5) makes the wave travel faster than the fade out
                        val ripplePhase = (progress * 1.5f) - normalizedDistance

                        // 5. Apply Scale if the wave is passing this dot
                        if (ripplePhase > 0f && ripplePhase < 0.3f) {
                            // Create a smooth bell curve for the scale
                            // 0.0 -> 1.0 -> 0.0
                            val waveHeight = kotlin.math.sin(ripplePhase / 0.3f * Math.PI).toFloat()
                            val scaleToAdd = (rippleMaxScale - 1f) * waveHeight

                            // Add to current radius (allows overlapping ripples)
                            currentRadius += baseRadiusPx * scaleToAdd
                        }
                    }
                }

                drawCircle(
                    color = dotColor,
                    radius = currentRadius,
                    center = currentDotCenter
                )
                y += spacingPx
            }
            x += spacingPx
        }
    }
}

@Preview
@Composable
private fun DotGridPattern() {
    MaterialTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color(0xFF673AB7)
        ) {
            Column(
                modifier = Modifier
                    .ripplingDotGrid(
                        dotColor = Color.White.copy(alpha = 0.6f), // Make dots a bit more visible
                        dotSpacing = 30.dp, // Adjust spacing as needed
                        dotRadius = 1.dp,
                        animationDurationMillis = 4000, // Each ripple lasts 4 seconds
                        rippleSpawnIntervalMillis = 1500, // New ripple every 1.5 seconds
                        randomRippleCenter = true // Ripples start from random spots
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Hello",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge
                )
            }
        }
    }
}