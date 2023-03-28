package com.example.roomdatabase.dao

import androidx.room.*
import com.example.roomdatabase.data.Student

@Dao
interface StudentsDao {

    @Query("SELECT * FROM students")
    suspend fun getListOfStudents(): List<Student>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStudents(student: Student)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStudents(student: Student)

    @Query("SELECT * FROM students WHERE id=:id")
    suspend fun getStudentById(id: Int): Student

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("SELECT * FROM students WHERE name LIKE '%' || :searchText || '%' OR surname LIKE '%' || :searchText || '%'")
    suspend fun searchStudentByName(searchText: String): List<Student>

}