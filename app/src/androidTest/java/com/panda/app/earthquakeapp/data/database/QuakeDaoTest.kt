package com.panda.app.earthquakeapp.data.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@SmallTest
class QuakeDaoTest {
//    @get:Rule
//    var instantTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var quakeDatabase: QuakeDatabase
    private lateinit var quakeDao: QuakeDao
    lateinit var calendar: Calendar

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        calendar = Calendar.getInstance()
        quakeDatabase = Room.inMemoryDatabaseBuilder(context, QuakeDatabase::class.java)
            .allowMainThreadQueries().build()
        quakeDao = quakeDatabase.dao
    }

    @After
    fun tearDown() {
        quakeDatabase.close()
    }


    @Test
    fun insertAllQuakes() = runTest {
        val quakeList = listOf(
            DatabaseQuake(
                id = "1",
                title = "test1",
                time =calendar.timeInMillis,
                url = "https://www.google.com/",
                magnitude = 4.5,
                latitude = 20.0,
                longitude = 0.0,
                depth = 4.5
            ),
            DatabaseQuake(
                id = "2",
                title = "test2",
                time = calendar.timeInMillis,
                url = "https://www.google.com/",
                magnitude = 5.5,
                latitude = 10.0,
                longitude = 30.0,
                depth = 5.5
            )
        )

        quakeDao.insertAll(quakeList)

        val allQuakes = quakeDao.getQuakes()

        assertThat(allQuakes).contains(quakeList)
    }


}