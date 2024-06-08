package aut.fhooe.sail.android.md2ua_prototype

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import aut.fhooe.sail.android.md2ua_prototype.models.WebServerModel

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before


@RunWith(AndroidJUnit4::class)
class WebServerTest {

    lateinit var wsm: WebServerModel

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        wsm = WebServerModel()
    }

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("aut.fhooe.sail.android.md2ua_prototype", appContext.packageName)
    }
}
