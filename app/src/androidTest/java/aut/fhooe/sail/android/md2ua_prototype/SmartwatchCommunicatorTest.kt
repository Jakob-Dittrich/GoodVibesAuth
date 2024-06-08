package aut.fhooe.sail.android.md2ua_prototype

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import aut.fhooe.sail.android.md2ua_prototype.models.SmartwatchCommunicator
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class SmartwatchCommunicatorTest {
    private lateinit var smartwatchCommunicator: SmartwatchCommunicator

    @Before
    fun setUp() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        // Create an instance of SmartwatchCommunicator

        smartwatchCommunicator = SmartwatchCommunicator(appContext)

        runBlocking {
            // Wait until nodes are found or timeout after a certain period
            withTimeout(5000) {
                // Loop until nodes are found or timeout
                while (!smartwatchCommunicator.nodesFound) {
                    // Add a delay to avoid busy-waiting
                    delay(100)
                }
            }
        }
    }

    @Test
    fun findNodesTest() {


        // Use runBlocking to run a coroutine in the test
        runBlocking {
            // Wait until nodes are found or timeout after a certain period
            withTimeout(5000) {
                // Loop until nodes are found or timeout
                while (!smartwatchCommunicator.nodesFound) {
                    // Add a delay to avoid busy-waiting
                    delay(100)
                }
            }
        }

        // Assert that nodesFound is true
        assertTrue(smartwatchCommunicator.nodesFound)
    }


    @Test
    fun sendMessageTest() {
        /*
        runBlocking {
            smartwatchCommunicator.sendMessage("hello")
        }
        assertTrue(smartwatchCommunicator.messageSent)*/

        runBlocking {
            smartwatchCommunicator.sendMessage("hello")
            // Wait until nodes are found or timeout after a certain period
            withTimeout(5000) {
                // Loop until nodes are found or timeout
                while (!smartwatchCommunicator.messageSent) {
                    // Add a delay to avoid busy-waiting
                    delay(100)
                }
            }
        }
    }

}
