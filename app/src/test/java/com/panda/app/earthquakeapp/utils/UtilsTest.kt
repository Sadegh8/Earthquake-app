package com.panda.app.earthquakeapp.utils

import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth.assertThat
import com.panda.app.earthquakeapp.domain.model.Quake
import org.junit.Before
import org.junit.Test
import java.util.*

class UtilsTest {
    lateinit var calendar: Calendar

    @Before
    fun setUp() {
        calendar = Calendar.getInstance()
    }


    private fun subtractMinutesTimeCal(min: Int) {
        calendar.add(Calendar.MINUTE, -min)
    }

    @Test
    fun `should refresh list empty return true`() {
        val emptyQuakeList = emptyList<Quake>()
        assertThat(Utils.shouldRefresh(quakes = emptyQuakeList)).isTrue()
    }

    @Test
    fun `should refresh list time difference above 5 min return true`() {
        subtractMinutesTimeCal(7)
        val quakeList = listOf(
            Quake(
                id = "1",
                title = "test1",
                time = calendar.timeInMillis,
                url = "https://www.google.com/",
                magnitude = 4.5,
                coordinate = LatLng(0.0, 0.0),
                depth = 4.5
            ),
            Quake(
                id = "2",
                title = "test2",
                time = calendar.timeInMillis,
                url = "https://www.google.com/",
                magnitude = 5.5,
                coordinate = LatLng(0.0, 0.0),
                depth = 5.5
            )
        )
        assertThat(Utils.shouldRefresh(quakes = quakeList)).isTrue()
    }


    @Test
    fun `should refresh list time difference below 5 min return false`() {
        subtractMinutesTimeCal(4)
        val quakeList = listOf(
            Quake(
                id = "1",
                title = "test1",
                calendar.timeInMillis,
                url = "https://www.google.com/",
                magnitude = 4.5,
                coordinate = LatLng(30.0, 50.0),
                depth = 4.5
            ),
            Quake(
                id = "2",
                title = "test2",
                calendar.timeInMillis,
                url = "https://www.google.com/",
                magnitude = 5.5,
                coordinate = LatLng(0.0, 0.0),
                depth = 5.5
            )
        )
        assertThat(Utils.shouldRefresh(quakes = quakeList)).isFalse()
    }
}