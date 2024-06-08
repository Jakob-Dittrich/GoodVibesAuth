package aut.fhooe.sail.android.md2ua_prototype

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import aut.fhooe.sail.android.md2ua_prototype.databinding.ActivityMainBinding
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import java.nio.charset.StandardCharsets

enum class VibrationPattern {
    PATTERN_31,
    PATTERN_2,
    CUSTOM,
    RANDOM
}

class MainActivity : Activity(), MessageClient.OnMessageReceivedListener {

    private lateinit var binding: ActivityMainBinding
    private val TAG_MessageReceived = "receive1"
    private var counter = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        binding.let {
            it.tvMain.text = "Unlocks: $counter"
            it.btnMain.setOnClickListener {
                showToast("hello World")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onMessageReceived(p0: MessageEvent) {
        Log.i(TAG_MessageReceived, "onMessageReceived: $p0")
        val msg = String(p0.data, StandardCharsets.UTF_8)
        Log.i(TAG_MessageReceived, "onMessageReceived: $msg")
        if(msg == "0000000031"){
            doVibrate2(patternType = VibrationPattern.PATTERN_31)
            showToast("bbbwwwwwttttt :vibrate3-1:")
            counter++
            binding.tvMain.text = "Unlocks: $counter"
        }
        else if(msg == "0000000002") {
            doVibrate2(patternType = VibrationPattern.PATTERN_2)
            showToast("bbbwwwwwttttt :vibrate2:")
            counter++
            binding.tvMain.text = "Unlocks: $counter"
        }
        else if(msg == "0000000000") {
            doVibrate2(patternType = VibrationPattern.PATTERN_2)
            showToast("bbbwwwwwttttt :custom:")
            counter++
            binding.tvMain.text = "Unlocks: $counter"
        }
        else if(msg == "1000000000") {
            doVibrate2(patternType = VibrationPattern.RANDOM)
            showToast("bbbwwwwwttttt :random:")
        }
        else if(p0.data.toString() == "toast"){
            showToast("String received: ${p0.data}")
        }
    }

    private fun doVibrate(){
        val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun doVibrate2(patternType: VibrationPattern) {
        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern = when (patternType) {
            VibrationPattern.PATTERN_31 -> longArrayOf(
                0,    // Initial delay
                60,   // Vibrate for 60ms (first in group 1)
                60,   // Pause for 60ms
                60,   // Vibrate for 60ms (second in group 1)
                60,   // Pause for 60ms
                60,   // Vibrate for 60ms (third in group 1)
                200,  // Pause for 200ms (between groups)
                60    // Vibrate for 60ms (single in group 2)
            )
            VibrationPattern.PATTERN_2 -> longArrayOf(
                0,    // Initial delay
                60,   // Vibrate for 60ms (first in group 1)
                60,   // Pause for 60ms
                60    // Vibrate for 60ms (second in group 1)
            )
            VibrationPattern.CUSTOM -> longArrayOf(
                0,    // Initial delay
                100,  // Vibrate for 100ms
                100,  // Pause for 100ms
                200,  // Vibrate for 200ms
                100,  // Pause for 100ms
                100,  // Vibrate for 100ms
                100,  // Pause for 100ms
                400   // Vibrate for 400ms
            )
            VibrationPattern.RANDOM -> longArrayOf(
                0,    // Initial delay
                200,  // Vibrate for 200ms
                100,  // Pause for 100ms
                200,  // Vibrate for 200ms
                100,  // Pause for 100ms
                400   // Vibrate for 400ms
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1))
        } else {
            vibrator.vibrate(pattern, -1)
        }
    }

    private fun showToast(msg: String){
        val t: Toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        t.show()
    }

    override fun onPause() {
        super.onPause()
        try {
            Wearable.getMessageClient(this).removeListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            Wearable.getMessageClient(this).addListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
