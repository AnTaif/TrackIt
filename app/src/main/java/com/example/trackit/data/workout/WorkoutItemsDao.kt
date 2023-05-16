package com.example.trackit.data.workout

import androidx.room.*
import com.example.trackit.data.workout.WorkoutCategory
import com.example.trackit.data.workout.WorkoutEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WorkoutItemsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: WorkoutEntity)

    @Update
    suspend fun update(item: WorkoutEntity)


    @Query("DELETE FROM workout_items WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("SELECT * from workout_items WHERE id = :id")
    fun getItem(id: Int): Flow<WorkoutEntity>

    @Query("SELECT * from workout_items WHERE date = :date")
    fun getItemsOnDate(date: LocalDate): Flow<List<WorkoutEntity>>

    @Query("SELECT * from workout_items ORDER BY name DESC")
    fun getAllItems(): Flow<List<WorkoutEntity>>

    @Query("SELECT COUNT(*) from workout_items WHERE date = :date AND completed = 1")
    fun getCompletedItemCountOnDate(date: LocalDate): Flow<Int>

    @Query("SELECT DISTINCT date FROM workout_items WHERE completed = 1 ORDER BY date DESC LIMIT 10")
    fun getLastTenDatesWithCompletedExercise(): Flow<List<LocalDate>>
}

@Dao
interface WorkoutCategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: WorkoutCategory)

    @Update
    suspend fun update(item: WorkoutCategory)

    @Delete
    suspend fun delete(item: WorkoutCategory)

    @Query("SELECT * from workout_categories WHERE id = :id")
    fun getItem(id: Int): Flow<WorkoutCategory>

    @Query("SELECT * from workout_categories ORDER BY id DESC")
    fun getAllItems(): Flow<List<WorkoutCategory>>

}