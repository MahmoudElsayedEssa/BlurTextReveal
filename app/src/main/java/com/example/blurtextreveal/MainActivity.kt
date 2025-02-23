package com.example.blurtextreveal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.blurtextreveal.ui.theme.BlurTextRevealTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlurTextRevealTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                    ) {
                        TextRevealWithControls(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CompactSliderWithLabel(
    value: Float,
    onValueChange: (Float) -> Unit,
    label: String,
    valueRange: ClosedFloatingPointRange<Float>,
    valueDisplay: (Float) -> String = { it.toInt().toString() }
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label, style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = valueDisplay(value), style = MaterialTheme.typography.bodySmall
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun TextRevealWithControls(
    modifier: Modifier
) {
    var isTriggered by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf("Hello World!") }
    var direction by remember { mutableStateOf(RevealDirection.TOP_TO_BOTTOM) }
    var initialOffset by remember { mutableFloatStateOf(25f) }
    var blurDuration by remember { mutableIntStateOf(1000) }
    var initialBlur by remember { mutableFloatStateOf(8f) }
    var glowBlur by remember { mutableFloatStateOf(16f) }
    var glowAlpha by remember { mutableFloatStateOf(0.3f) }
    var staggerDelay by remember { mutableLongStateOf(50L) }
    val alphaDuration by remember { mutableIntStateOf(400) }

    Column(
        modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Preview Area
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                BlurTextReveal(
                    text = text,
                    color = Color.Black,
                    isTriggered = isTriggered,
                    config = BlurTextRevealConfig(
                        direction = direction,
                        initialOffset = initialOffset,
                        blurDuration = blurDuration,
                        initialBlur = initialBlur,
                        glowBlur = glowBlur,
                        glowAlpha = glowAlpha,
                        alphaDuration = alphaDuration
                    ),
                    staggerDelay = staggerDelay,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Control Panel
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Text Input and Direction
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(value = text,
                        onValueChange = { text = it },
                        label = { Text("Text") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    Column {
                        RevealDirection.entries.chunked(2).forEach { directions ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                directions.forEach { dir ->
                                    FilterChip(selected = direction == dir,
                                        onClick = { direction = dir },
                                        label = {
                                            Text(
                                                when (dir) {
                                                    RevealDirection.TOP_TO_BOTTOM -> "↓"
                                                    RevealDirection.BOTTOM_TO_TOP -> "↑"
                                                    RevealDirection.START_TO_END -> "→"
                                                    RevealDirection.END_TO_START -> "←"
                                                }, style = MaterialTheme.typography.bodySmall
                                            )
                                        })
                                }
                            }
                        }
                    }
                }

                HorizontalDivider()

                // Sliders in two columns
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Left column
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CompactSliderWithLabel(
                            value = initialOffset,
                            onValueChange = { initialOffset = it },
                            label = "Offset",
                            valueRange = 0f..50f
                        )

                        CompactSliderWithLabel(
                            value = initialBlur,
                            onValueChange = { initialBlur = it },
                            label = "Blur",
                            valueRange = 0f..20f
                        )

                        CompactSliderWithLabel(value = glowAlpha,
                            onValueChange = { glowAlpha = it },
                            label = "Glow",
                            valueRange = 0f..1f,
                            valueDisplay = { "${(it * 100).toInt()}%" })
                    }

                    // Right column
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CompactSliderWithLabel(value = blurDuration.toFloat(),
                            onValueChange = { blurDuration = it.toInt() },
                            label = "Duration",
                            valueRange = 100f..2000f,
                            valueDisplay = { "${it.toInt()}ms" })

                        CompactSliderWithLabel(value = staggerDelay.toFloat(),
                            onValueChange = { staggerDelay = it.toLong() },
                            label = "Delay",
                            valueRange = 0f..200f,
                            valueDisplay = { "${it.toInt()}ms" })

                        CompactSliderWithLabel(
                            value = glowBlur,
                            onValueChange = { glowBlur = it },
                            label = "Glow Blur",
                            valueRange = 0f..32f
                        )
                    }
                }

                // Trigger Button
                Button(
                    onClick = { isTriggered = !isTriggered }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isTriggered) "Reset" else "Reveal")
                }
            }
        }
    }
}

//@Composable
//fun TextRevealWithControls() {
//    var isTriggered by remember { mutableStateOf(false) }
//    var text by remember { mutableStateOf("Hello World!") }
//    var direction by remember { mutableStateOf(RevealDirection.TOP_TO_BOTTOM) }
//    var initialOffset by remember { mutableFloatStateOf(25f) }
//    var blurDuration by remember { mutableIntStateOf(1000) }
//    var initialBlur by remember { mutableFloatStateOf(8f) }
//    var glowBlur by remember { mutableFloatStateOf(16f) }
//    var glowAlpha by remember { mutableFloatStateOf(0.3f) }
//    var staggerDelay by remember { mutableLongStateOf(50L) }
//    var alphaDuration by remember { mutableIntStateOf(400) }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)
//    ) {
//        // Preview Area
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            contentAlignment = Alignment.Center
//        ) {
//            BlurTextReveal(
//                text = text,
//                color = Color.Black,
//                isTriggered = isTriggered,
//                config = BlurTextRevealConfig(
//                    direction = direction,
//                    initialOffset = initialOffset,
//                    blurDuration = blurDuration,
//                    initialBlur = initialBlur,
//                    glowBlur = glowBlur,
//                    glowAlpha = glowAlpha,
//                    alphaDuration = alphaDuration
//                ),
//                staggerDelay = staggerDelay,
//                fontWeight = FontWeight.Bold
//            )
//        }
//
//        // Control Panel
//        Card(
//            modifier = Modifier.fillMaxWidth(),
//            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
//        ) {
//            Column(
//                modifier = Modifier.padding(16.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                // Text Input
//                OutlinedTextField(
//                    value = text,
//                    onValueChange = { text = it },
//                    label = { Text("Text") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                // Direction Selector
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween
//                ) {
//                    RevealDirection.values().forEach { dir ->
//                        FilterChip(
//                            selected = direction == dir,
//                            onClick = { direction = dir },
//                            label = {
//                                Text(
//                                    dir.name.split("_").first(),
//                                    style = MaterialTheme.typography.bodySmall
//                                )
//                            }
//                        )
//                    }
//                }
//
//                // Sliders
//                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                    // Initial Offset
//                    Text("Initial Offset: ${initialOffset.toInt()}")
//                    Slider(
//                        value = initialOffset,
//                        onValueChange = { initialOffset = it },
//                        valueRange = 0f..50f
//                    )
//
//                    // Blur Duration
//                    Text("Blur Duration: ${blurDuration}ms")
//                    Slider(
//                        value = blurDuration.toFloat(),
//                        onValueChange = { blurDuration = it.toInt() },
//                        valueRange = 100f..2000f
//                    )
//
//                    // Initial Blur
//                    Text("Initial Blur: ${initialBlur.toInt()}")
//                    Slider(
//                        value = initialBlur,
//                        onValueChange = { initialBlur = it },
//                        valueRange = 0f..20f
//                    )
//
//                    // Glow Blur
//                    Text("Glow Blur: ${glowBlur.toInt()}")
//                    Slider(
//                        value = glowBlur,
//                        onValueChange = { glowBlur = it },
//                        valueRange = 0f..32f
//                    )
//
//                    // Glow Alpha
//                    Text("Glow Alpha: ${(glowAlpha * 100).toInt()}%")
//                    Slider(
//                        value = glowAlpha,
//                        onValueChange = { glowAlpha = it },
//                        valueRange = 0f..1f
//                    )
//
//                    // Stagger Delay
//                    Text("Stagger Delay: ${staggerDelay}ms")
//                    Slider(
//                        value = staggerDelay.toFloat(),
//                        onValueChange = { staggerDelay = it.toLong() },
//                        valueRange = 0f..200f
//                    )
//
//                    // Alpha Duration
//                    Text("Alpha Duration: ${alphaDuration}ms")
//                    Slider(
//                        value = alphaDuration.toFloat(),
//                        onValueChange = { alphaDuration = it.toInt() },
//                        valueRange = 100f..1000f
//                    )
//                }
//
//                // Trigger Button
//                Button(
//                    onClick = { isTriggered = !isTriggered },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text(if (isTriggered) "Reset" else "Reveal")
//                }
//            }
//        }
//    }
//}

// Optional: Add preset configurations
@Stable
data class TextRevealPreset(
    val name: String, val config: BlurTextRevealConfig, val staggerDelay: Long = 50L
)

val textRevealPresets = listOf(
    TextRevealPreset(
        "Smooth", BlurTextRevealConfig(
            initialOffset = 25f,
            blurDuration = 1000,
            initialBlur = 8f,
            glowBlur = 16f,
            glowAlpha = 0.3f,
            alphaDuration = 400
        )
    ), TextRevealPreset(
        "Sharp", BlurTextRevealConfig(
            initialOffset = 40f,
            blurDuration = 600,
            initialBlur = 12f,
            glowBlur = 24f,
            glowAlpha = 0.5f,
            alphaDuration = 300
        )
    ), TextRevealPreset(
        "Subtle", BlurTextRevealConfig(
            initialOffset = 15f,
            blurDuration = 1200,
            initialBlur = 4f,
            glowBlur = 8f,
            glowAlpha = 0.2f,
            alphaDuration = 800
        )
    )
)