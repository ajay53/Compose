package com.example.compose

import SampleData
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.compose.ui.theme.ComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    RootElement(modifier = Modifier.fillMaxSize())
//                    PreviewConversation(modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
fun RootElement(modifier: Modifier = Modifier) {
    var shouldShowOnboarding by rememberSaveable {
        mutableStateOf(true)
    }
    Surface(modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            PreviewConversation(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun OnboardingScreen(onContinueClicked: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to Codelab Basics!")
        Button(modifier = Modifier.padding(vertical = 20.dp), onClick = onContinueClicked) {
            Text(text = "Continue")
        }
    }
}

@Composable
fun MessageCard(msg: Message, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(horizontal = 10.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "profile image",
            modifier = Modifier
//                .background(Color.White, shape = RectangleShape)
                .align(CenterVertically)
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape)
        )
        Spacer(modifier = Modifier.width(10.dp))

        //to store expanded value locally in memory
        var isExpanded by rememberSaveable { mutableStateOf(false) }

        //update surface as per expanded state
        val surfaceColorAnimation by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            label = "Body surface"
        )

        //update padding as per expanded state
        val extraPaddingAnimation by animateDpAsState(
            if (isExpanded) 8.dp else 4.dp,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "Extra Padding - Body Text"
        )

        Column(
            modifier = Modifier
                .padding(vertical = 5.dp)
//                .background(Color.Yellow)
                /*.clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { isExpanded = !isExpanded }*/
                .fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    /*modifier = Modifier.border(
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                    ),*/
                    modifier = Modifier
                        .fillMaxHeight()
//                        .background(Color.Cyan)
                        .align(alignment = Alignment.CenterVertically),
                    text = msg.author,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleLarge
                )

                /*IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(imageVector = R.drawable.ic_expand, null)
                }*/

                Image(
                    painter = if (isExpanded) {
                        painterResource(id = R.drawable.ic_collapse)
                    } else {
                        painterResource(
                            id = R.drawable.ic_expand
                        )
                    },
                    contentDescription = "expand_body_text",
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .clickable(
//                            interactionSource = remember { MutableInteractionSource() },
//                            indication = null
                        ) { isExpanded = !isExpanded }
//                        .background(Color.Blue, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                color = surfaceColorAnimation,
                modifier = Modifier
                    .animateContentSize()
                    .padding(horizontal = 2.dp)
            ) {
                Text(
                    text = msg.body,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    modifier = Modifier
                        .padding(
                            top = 4.dp,
                            bottom = extraPaddingAnimation,
                            start = 8.dp,
                            end = 8.dp
                        )
//                        .padding(all = 4.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun Conversation(messages: List<Message>, modifier: Modifier) {
    LazyColumn {
        itemsIndexed(messages) { index, message ->
            MessageCard(msg = message, modifier = modifier)
            if (index != messages.size - 1) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                        .height(1.dp)
                        .background(MaterialTheme.colorScheme.outline)
                )
            }
        }
    }
}

//@Preview(name = "Light Mode")
/*@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)*/
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewConversation(modifier: Modifier = Modifier) {
    ComposeTheme {
        Conversation(SampleData.conversationSample, modifier)
    }
}

//@Preview(name = "Root", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewRootElement() {
    RootElement(Modifier.fillMaxSize())
}

/*@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)*/
//@Preview(showBackground = true, name = "Message Card", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun PreviewMessageCard() {
    MessageCard(msg = Message("Author Name", "Bodyeeee"))
}
