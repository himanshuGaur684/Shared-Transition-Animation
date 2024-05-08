package gaur.himanshu.sharedtransitionanimation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import gaur.himanshu.sharedtransitionanimation.ui.theme.SharedTransitionAnimationTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SharedTransitionAnimationTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(title = { Text(text = "Food List") })
                    }) { innerPadding ->
                    Log.d("TAG", "onCreate: ${innerPadding}")
                    Box(modifier = Modifier.padding(innerPadding)) {
                        FoodList(modifier = Modifier.padding(innerPadding))

                    }
                }
            }
        }
    }
}

data class Food(
    val name: String,
    @DrawableRes val image: Int,
    @StringRes val desc: Int
)

val foodList = listOf(
    Food(
        name = "Italian Pizza",
        image = R.drawable.pizza_1,
        desc = R.string.italian_pizza
    ),
    Food(
        name = "Mexican Pizza",
        image = R.drawable.pizza_1,
        desc = R.string.mexican_pizza
    ),
    Food(
        name = "Cheese Burger",
        image = R.drawable.burger,
        desc = R.string.cheese_burger
    ),
)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun FoodList(modifier: Modifier = Modifier) {
    SharedTransitionLayout {
        val isDetailsScreen = remember {
            mutableStateOf(false)
        }
        val index = remember {
            mutableStateOf(-1)
        }
        AnimatedContent(targetState = isDetailsScreen.value) { targetState ->
            if (targetState) {
                Column(
                    modifier = Modifier
                        .clickable {
                            isDetailsScreen.value = isDetailsScreen.value.not()
                        }
                        .padding(horizontal = 12.dp)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Image(
                        painter = painterResource(id = foodList.get(index.value).image),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .sharedElement(
                                state = rememberSharedContentState(key = "image-${index.value}"),
                                animatedVisibilityScope = this@AnimatedContent
                            )
                            .height(300.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = foodList.get(index.value).name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState(key = "name-${index.value}"),
                            animatedVisibilityScope = this@AnimatedContent
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(foodList.get(index.value).desc),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.sharedElement(
                            state = rememberSharedContentState(key = "desc-${index.value}"),
                            animatedVisibilityScope = this@AnimatedContent
                        )
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(foodList) { i, item ->
                        Row(modifier = Modifier
                            .clickable {
                                isDetailsScreen.value = isDetailsScreen.value.not()
                                index.value = i
                            }
                            .fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp)
                                    .weight(2f)
                            ) {
                                Text(
                                    text = item.name, style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.sharedElement(
                                        state = rememberSharedContentState(key = "name-${i}"),
                                        animatedVisibilityScope = this@AnimatedContent
                                    )
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = stringResource(id = item.desc),
                                    style = MaterialTheme.typography.bodyMedium,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 4,
                                    modifier = Modifier.sharedElement(
                                        state = rememberSharedContentState(key = "desc-${i}"),
                                        animatedVisibilityScope = this@AnimatedContent
                                    )
                                )
                            }
                            Image(
                                painter = painterResource(id = item.image),
                                contentDescription = null,
                                modifier = Modifier
                                    .weight(1f)
                                    .sharedElement(
                                        state = rememberSharedContentState(key = "image-${i}"),
                                        animatedVisibilityScope = this@AnimatedContent
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

