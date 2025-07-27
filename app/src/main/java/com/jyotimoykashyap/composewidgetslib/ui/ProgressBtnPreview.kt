package com.jyotimoykashyap.composewidgetslib.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jyotimoykashyap.composecomponents.ProgressButton

@Preview
@Composable
private fun ProgressBtnPreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /**
             * Usage example
             */
            ProgressButton(
                text = "Let's Get Started",
                onClick = {
                    println("On Click is triggered")
                },
                shape = RoundedCornerShape(corner = CornerSize(12.dp)),
                onProgressComplete = {
                    println("Progress is completed")
                }
            )
        }
    }
}