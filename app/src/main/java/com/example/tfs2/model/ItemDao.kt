package com.example.tfs2.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ItemDao {
    @Query("SELECT * FROM items ORDER BY date ASC")
    fun getAll(): Flow<List<Item>>

    @Query("SELECT * FROM items WHERE id = :id")
    fun getOne(id: Int): Item

    @Query("SELECT * FROM items WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getExpiringProducts(startDate: LocalDate, endDate: LocalDate): List<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: Item)

    @Update
    suspend fun update(product: Item)

    @Delete
    suspend fun delete(product: Item)
}