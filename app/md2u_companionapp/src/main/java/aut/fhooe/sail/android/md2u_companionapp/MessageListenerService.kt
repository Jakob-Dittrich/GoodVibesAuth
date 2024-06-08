package aut.fhooe.sail.android.md2u_companionapp

import android.content.Intent
import android.util.Log
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.WearableListenerService


class MessageListenerService : WearableListenerService() {
    override fun onMessageReceived(messageEvent: MessageEvent) {
        super.onMessageReceived(messageEvent)
        if (messageEvent.path == "/message") {
            val message = String(messageEvent.data)
            Log.d(TAG, "Message received: $message")

            // Handle the received message here according to your requirements
            // For example, you can send a broadcast to notify the UI
            val intent = Intent("MESSAGE_RECEIVED")
            intent.putExtra("message", message)
            sendBroadcast(intent)
        }
    }

    companion object {
        private const val TAG = "MessageListenerService"
    }
}
