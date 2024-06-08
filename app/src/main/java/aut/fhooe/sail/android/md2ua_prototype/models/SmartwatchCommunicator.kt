package aut.fhooe.sail.android.md2ua_prototype.models

import android.content.Context
import android.util.Log
import aut.fhooe.sail.android.md2ua_prototype.Communicator
import com.google.android.gms.tasks.Task
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.NodeClient
import com.google.android.gms.wearable.Wearable
import kotlinx.coroutines.tasks.await

class SmartwatchCommunicator(private val context: Context) : Communicator {
    private val messageClient: MessageClient = Wearable.getMessageClient(context)
    private val nodeClient: NodeClient = Wearable.getNodeClient(context)

    private var connectedNode: Node? = null
    var nodesFound: Boolean = false
    var messageSent: Boolean = false

    init {
        getConnectedNode()
    }

    private fun getConnectedNode() {
        nodeClient.connectedNodes.addOnSuccessListener { nodes ->
            for (node in nodes) {
                if (node.isNearby) {
                    connectedNode = node
                    nodesFound = true
                    Log.i("SmartwatchCommunicator", "Node ID: ${node.id}")
                    return@addOnSuccessListener
                }
            }
        }.addOnFailureListener { exception ->
            nodesFound = false
            Log.e("SmartwatchCommunicator", "Failed to retrieve connected nodes", exception)
        }
    }

    override suspend fun sendMessage(message: String) {
        val nodeId = connectedNode?.id ?: run {
            Log.e("SmartwatchCommunicator", "No connected node found")
            return
        }

        val messagePath = "/message"
        val messagePayload = message.toByteArray()

        Log.i("SmartwatchCommunicator", "Sending message to node $nodeId")

        try {
            val sendMessageTask: Task<Int> =
                messageClient.sendMessage(nodeId, messagePath, messagePayload)
            sendMessageTask.await()
            Log.i("SmartwatchCommunicator", "Message sent successfully")
            messageSent = true
        } catch (e: Exception) {
            Log.e("SmartwatchCommunicator", "Failed to send message", e)
            messageSent = false
        }
    }
}
