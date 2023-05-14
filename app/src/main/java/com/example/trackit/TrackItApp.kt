package com.example.trackit

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavArgumentBuilder
import androidx.navigation.NavType
import androidx.navigation.Navigator
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.trackit.data.Screen
import com.example.trackit.ui.FoodPage
import com.example.trackit.ui.Nutrition.Food.FoodScreen
import com.example.trackit.ui.ProfilePage
import com.example.trackit.ui.navigation.BottomBar
import com.example.trackit.ui.theme.TrackItTheme
import com.example.trackit.ui.workout.WorkoutPage
import com.example.trackit.ui.workout.category.WorkoutCategoryScreen
import com.example.trackit.ui.workout.exercise.WorkoutExerciseScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import java.time.LocalDate

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun TrackItApp(
    modifier: Modifier = Modifier,
){
    val navController = rememberAnimatedNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    // Current selected date in calendar
    val selectedDate = remember {
        mutableStateOf(LocalDate.now())
    }

    val currentScreen = Screen.valueOf(
        backStackEntry?.destination?.route ?: Screen.Profile.name
    )

    val bottomBarState = rememberSaveable { (mutableStateOf(true)) }

    when (backStackEntry?.destination?.route){
        Screen.Profile.name -> {
            bottomBarState.value = true
        }
        Screen.Food.name -> {
            bottomBarState.value = true
        }
        Screen.Workout.name -> {
            bottomBarState.value = true
        }
        Screen.WorkoutCategory.name -> {
            bottomBarState.value = false
        }
        Screen.NutritionFood.name -> {
            bottomBarState.value = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AnimatedNavHost(
            navController,
            startDestination = Screen.Profile.name,
            modifier = Modifier.weight(1f)
        ) {

            composable(
                route = Screen.Profile.name,
            ) {
                ProfilePage()
            }

            composable(
                route = Screen.Workout.name,
                exitTransition = {
                    ExitTransition.None
                }
            ) {
                WorkoutPage(
                    navigateToEntry = {
                        navController.navigate(Screen.WorkoutCategory.name)
                    },
                    selectedDate.value,
                )
            }

            composable(
                route = Screen.WorkoutCategory.name,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(400)
                    )
                },
            ) {
                WorkoutCategoryScreen(
                    onCategorySelect = { categoryId ->
                        navController.currentBackStackEntry?.savedStateHandle?.set(
                            "categoryId",
                            categoryId
                        )
                        navController.navigate(Screen.WorkoutExercise.name)
                    },
                    navigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Screen.WorkoutExercise.name
            ) {
                val categoryId =
                    navController.previousBackStackEntry?.savedStateHandle?.get<Int>("categoryId")
                WorkoutExerciseScreen(
                    categoryId,
                    navigateBack = { navController.popBackStack() },
                    navigateToWorkoutPage = { navController.navigate(Screen.Workout.name) },
                    selectedDate = selectedDate.value
                )
            }

            composable(
                route = Screen.Food.name
            ) {
                FoodPage(
                    navigateToEntry = { navController.navigate(Screen.NutritionFood.name) },
                    selectedDate = selectedDate.value
                )
            }

            composable(
                route = Screen.NutritionFood.name
            ) {
                FoodScreen(
                    navigateBack = { navController.popBackStack() },
                    navigateToFoodPage = { navController.navigate(Screen.Food.name) }
                )
            }
        }

        if (bottomBarState.value){
            BottomBar(
                selectedDate.value,
                bottomBarState.value,
                onDateSelected = { selectedDate.value = it },
                navController = navController,
                currentScreen = currentScreen
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTrackItApp(){
    TrackItTheme {
        TrackItApp()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewTrackItAppDarkTheme(){
    TrackItTheme(darkTheme = true) {
        TrackItApp()
    }
}