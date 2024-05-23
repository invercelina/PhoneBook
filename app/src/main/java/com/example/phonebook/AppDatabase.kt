package com.example.phonebook

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile              // DB를 CPU에서 돌리지 말고 RAM에서 돌리게 하려고 사용 (인스턴스나 DB와 같이 IO에서 쓰는 애들은 대부분 RAM으로)
        private var instance: AppDatabase? = null  // private을 쓰는 이유 instance를 바로 쓰지 못하게 하고 getDatabase로만 가능하게 하려고
        fun getDatabase(context: Context): AppDatabase {
            return instance ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java, "phonebook.db"
            ).build()
                .also { instance = it }
        }
    }
}