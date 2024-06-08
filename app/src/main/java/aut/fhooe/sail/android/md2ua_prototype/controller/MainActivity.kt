package aut.fhooe.sail.android.md2ua_prototype

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import aut.fhooe.sail.android.md2ua_prototype.databinding.ActivityMainBinding
import aut.fhooe.sail.android.md2ua_prototype.interfaces.*
import aut.fhooe.sail.android.md2ua_prototype.models.*
import kotlinx.coroutines.runBlocking

interface Communicator {
    suspend fun sendMessage(message: String)
}

interface ProgramController: Subject {
    var info: String?

    fun stopConnection()

}

enum class VibrationPattern {
    PATTERN_31,
    PATTERN_2,
    CUSTOM
}


class MainActivity : AppCompatActivity(), Observer {
    private lateinit var bindingMain: ActivityMainBinding
    private var selectedPattern: VibrationPattern? = null

    private lateinit var screenOnReceiver: ScreenOnReceiver


    private val wsM: ProgramController = WebServerModel()
    lateinit var com: Communicator
    private var wrongPattern: Boolean = false

    var vibrationActivated: Boolean = true
        @SuppressLint("SetTextI18n")
        set(value) {
            field = value
            if(value) {
                bindingMain.textStatus.text = "Turned on"
            }
            else {

                bindingMain.textStatus.text = "Turned off"
            }
        }


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingMain = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingMain.root)
        com = SmartwatchCommunicator(this)

        screenOnReceiver = ScreenOnReceiver()
        val filter = IntentFilter(Intent.ACTION_SCREEN_ON)
        registerReceiver(screenOnReceiver, filter)


        wsM.registerObserver(this)

        setupSpinner()



        bindingMain.activityMainBtnSend.setOnClickListener {
            sendMessageBasedOnPattern()
        }

        val ipAddress = wsM.info
        bindingMain.tvInfo.text =
            "$ipAddress/turnOn to turn feature on " +
                    "\n$ipAddress/turnOff to turn feature off"
        vibrationActivated = true

    }

    override fun onDestroy() {
        super.onDestroy()
        wsM.removeObserver(this)
        wsM.stopConnection()
    }

    private fun setupSpinner() {
        // Access the Spinner through View Binding
        val ddVibration = bindingMain.activityMainDdVibration

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.vibration_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            ddVibration.adapter = adapter
        }

        // Set up the listener for when an item is selected
        ddVibration.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Update the selectedPattern property based on the selected item
                selectedPattern = when (position) {
                    0 -> VibrationPattern.PATTERN_31
                    1 -> VibrationPattern.PATTERN_2
                    2 -> VibrationPattern.CUSTOM
                    else -> null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun sendMessageBasedOnPattern() {
        runBlocking {
            if (vibrationActivated) {
                if (wrongPattern) {
                    com.sendMessage("1000000000")
                }
                else {
                    when (selectedPattern) {
                        VibrationPattern.PATTERN_31 -> com.sendMessage("0000000031")
                        VibrationPattern.PATTERN_2 -> com.sendMessage("0000000002")
                        VibrationPattern.CUSTOM -> com.sendMessage("0000000000")
                        else -> {
                            com.sendMessage("9000000001")
                            Toast.makeText(
                                this@MainActivity,
                                "no vibration pattern selected",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            } else {
                com.sendMessage("9000000000")
            }
        }
    }



    @SuppressLint("SetTextI18n")
    override fun update(case: ObserverCases) {
        when(case) {
            ObserverCases.main_route -> {
                bindingMain.textStatus.text = "main"
            }
            ObserverCases.turnOn_route -> {
                bindingMain.textStatus.text = "turnOn"
                vibrationActivated = true
            }
            ObserverCases.turnOff_route -> {
                bindingMain.textStatus.text = "turnOff"
                vibrationActivated = false
            }
            ObserverCases.vibrate_route -> {
                sendMessageBasedOnPattern()
            }
            ObserverCases.vibrate_rand_route -> {
                runBlocking {
                    com.sendMessage("1000000000")
                }
            }
            ObserverCases.vibrate_wrong_route -> {
                wrongPattern = !wrongPattern
            }
            ObserverCases.screenOn -> {
                runBlocking {
                    if (vibrationActivated) {
                        com.sendMessage("0123456789")
                    }
                    else {
                       com.sendMessage("0000000000")
                    }
                }
            }
        }
    }

    inner class ScreenOnReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_SCREEN_ON) {
                // Handle screen on event here
                sendMessageBasedOnPattern()
            }
        }
    }

}
