package com.example.trackit.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.trackit.TrackItApplication
import com.example.trackit.data.food.BreakfastViewModel
import com.example.trackit.data.food.DinnerViewModel
import com.example.trackit.data.food.LunchViewModel
import com.example.trackit.data.food.SnackViewModel
import com.example.trackit.data.food.TotalViewModel
import com.example.trackit.data.weight.WeightViewModel
import com.example.trackit.ui.workout.WorkoutViewModel
import com.example.trackit.ui.workout.category.WorkoutCategoryViewModel
import com.example.trackit.ui.workout.exercise.WorkoutExerciseViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            WorkoutViewModel(trackItApplication().container.workoutItemsRepository)
        }

        initializer {
            WorkoutCategoryViewModel(trackItApplication().container.workoutCategoryRepository)
        }

        initializer {
            WorkoutExerciseViewModel(
                trackItApplication().container.workoutItemsRepository,
                trackItApplication().container.workoutCategoryRepository
            )
        }

        initializer {
            WeightViewModel(trackItApplication().container.weightRepository)
        }

        initializer {
            BreakfastViewModel(trackItApplication().container.foodRepository)
        }

        initializer {
            LunchViewModel(trackItApplication().container.foodRepository)
        }

        initializer {
            DinnerViewModel(trackItApplication().container.foodRepository)
        }

        initializer {
            SnackViewModel(trackItApplication().container.foodRepository)
        }

        initializer {
            TotalViewModel(trackItApplication().container.totalRepository)
        }
    }
}

fun CreationExtras.trackItApplication(): TrackItApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as TrackItApplication)