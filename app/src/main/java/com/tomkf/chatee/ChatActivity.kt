package com.tomkf.chatee

import android.content.Context
import android.os.Bundle
import android.os.Message
import android.provider.SyncStateContract.Helpers.update
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.item_message.view.*

class ChatActivity: AppCompatActivity() {
    private lateinit var messagesDB: DatabaseReference

    // create an instance level messages collection
    var messages: MutableList<Message> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)

        messagesDB = FirebaseDatabase.getInstance().getReference("Messages")

        // add the event listener to receive data from Firebase
        messagesDB.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                messages = mutableListOf()
                dataSnapshot.children.forEach {
                    val message = it.getValue(Message::class.java)
                    if (message != null) {
                        messages.add(message)
                    }
                }

                update()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read values: handle error
            }
        })

        send_message_button.setOnClickListener {
            val sender = FirebaseAuth.getInstance().currentUser?.email
            val message = message_input.text.toString()

            if (sender != null) {
                saveMessage(sender, message)
            }
        }

        message_list.layoutManager = LinearLayoutManager(this)
    }

    // update method to be called when UI needs to be refreshed
    private fun update(){
        message_list.adapter = MessagesAdaptor(messages, this)
    }

    private fun saveMessage(sender: String, messageBody: String) {
        val key = messagesDB.push().key
        key ?: return

        val message = Message(sender, messageBody)

        messagesDB.child(key).setValue(message)
    }


}
class MessagesViewHolder(view: View): RecyclerView.ViewHolder(view)

data class Message(val sender: String, val messageBody: String)