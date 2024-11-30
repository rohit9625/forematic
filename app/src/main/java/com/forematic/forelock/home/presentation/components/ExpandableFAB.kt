package com.forematic.forelock.home.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.forematic.forelock.ui.theme.ForeLockTheme

@Composable
fun ExpandableFAB(
    isExpanded: Boolean,
    menuItems: List<Pair<String, () -> Unit>>,
    onClick: ()-> Unit,
    modifier: Modifier = Modifier
) {
    val shadowElevation by animateDpAsState(
        targetValue = if (isExpanded) 6.dp else 0.dp,
        animationSpec = tween(durationMillis = 300, delayMillis = 250),
        label = "menu_elevation_anim"
    )

    val iconRotation by animateFloatAsState(
        targetValue = if (isExpanded) 45f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "icon_rotation_anim"
    )

    Column (
        modifier = modifier
            .width(IntrinsicSize.Max)
            .wrapContentSize(Alignment.BottomCenter),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
        ) {
            ElevatedCard(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .shadow(
                        elevation = shadowElevation,
                        shape = RoundedCornerShape(12.dp) // Adjust shape as needed
                    ),
                elevation = CardDefaults.elevatedCardElevation(0.dp)
            ) {
                menuItems.forEach { (text, onClick) ->
                    Text(
                        text = text,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onClick() }
                            .padding(12.dp)
                    )
                }
            }
        }
        ExtendedFloatingActionButton(
            onClick = onClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.zIndex(1f)
                        .graphicsLayer {
                            rotationZ = iconRotation
                        }
                )
            },
            text = {
                AnimatedContent(
                    targetState = isExpanded, label = "fab_text_anim",
                    transitionSpec = {
                        if (targetState) {
                            // Entering: Slide in from bottom
                            slideInVertically { height -> height } + fadeIn() togetherWith
                                    slideOutVertically { height -> -height } + fadeOut()
                        } else {
                            // Exiting: Slide out to bottom
                            slideInVertically { height -> -height } + fadeIn() togetherWith
                                    slideOutVertically { height -> height } + fadeOut()
                        }
                    }
                ) { target ->
                    if(target) {
                        Text(text = "Device Type")
                    } else {
                        Text(text = "New Device")
                    }
                }
            }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
private fun ExpandableFABPreview() {
    var isExpanded by remember { mutableStateOf(false) }

    ForeLockTheme {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            ExpandableFAB(
                isExpanded = isExpanded,
                menuItems = listOf(
                    "R21 Relay" to { /* Navigate to R21 Relay setup */ },
                    "G64 Relay" to { /* Navigate to G64 Relay setup */ },
                    "G24 Intercom" to { /* Navigate to G24 Intercom setup */ }
                ),
                onClick = { isExpanded = !isExpanded },
                modifier = Modifier
                    .padding(16.dp)
                    .widthIn(max = 156.dp)
            )
        }
    }
}