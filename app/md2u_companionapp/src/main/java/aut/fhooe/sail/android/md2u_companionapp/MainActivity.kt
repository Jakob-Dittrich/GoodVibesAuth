package aut.fhooe.sail.android.md2u_companionapp

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import aut.fhooe.sail.android.md2u_companionapp.databinding.ActivityMainBinding
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Wearable
import java.nio.charset.StandardCharsets



class MainActivity : Activity(),
    MessageClient.OnMessageReceivedListener {
    private var activityContext: Context? = null

    private lateinit var binding: ActivityMainBinding

    private val TAG_MESSAGE_RECEIVED = "receive1"
    private var counter = 0

    /*private val wearableListener = MyWearableListener {
        doVibrate()
    }*/


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)


        activityContext = this

        binding.let {
            it.text.text = "Unlocks: $counter"
        }
    }


    override fun onMessageReceived(p0: MessageEvent) {
        Log.i(TAG_MESSAGE_RECEIVED, "onMessageReceived: $p0")
        var msg = String(p0.data, StandardCharsets.UTF_8)
        Log.i(TAG_MESSAGE_RECEIVED, "onMessageReceived: $msg")
        if(msg == "0123456789"){
            doVibrate()
            showToast("bbbwwwwwttttt :vibrate:")
            this.counter = counter + 1
            binding.text.text = "Unlocks: ${counter}"
        }
        else if(p0.data.toString() == "toast"){
            showToast("String received: ${p0.data.toString()}")
        }

    }

    fun doVibrate(){
        val vibrator: Vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    fun showToast(msg: String){
        val t: Toast = Toast.makeText(this,msg, Toast.LENGTH_SHORT)
        t.show()
    }


    override fun onPause() {
        super.onPause()
        try {
            Wearable.getMessageClient(activityContext!!).removeListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onResume() {
        super.onResume()
        try {
            Wearable.getMessageClient(activityContext!!).addListener(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



}


