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


class MessagesViewHolder(view: View): RecyclerView.ViewHolder(view)

data class Message(val sender: String, val messageBody: String)

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

//                update()
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
//
//    // update method to be called when UI needs to be refreshed
//    private fun update(){
//        message_list.adapter = MessagesAdapter(messages, this)
//    }

    private fun saveMessage(sender: String, messageBody: String) {
        val key = messagesDB.push().key
        key ?: return

        val message = Message(sender, messageBody)

        messagesDB.child(key).setValue(message)
    }
}
//
//private class MessagesAdapter(private val messages: List<Message>, val context: Context): RecyclerView.Adapter<MessagesViewHolder>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
//        return MessagesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message, parent, false))
//    }
//
//    override fun getItemCount(): Int {
//        return messages.count()
//    }
//
//    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
//        val message = messages[position]
//
//        holder.itemView.sender_label.text = message.sender
//        holder.itemView.message_body_label.text = message.messageBody
//    }
//}