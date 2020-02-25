package com.tomkf.chatee

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.item_message.view.*

class ChatActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chat)

        message_list.layoutManager = LinearLayoutManager(this)

        val messages = listOf(
            Message("someguy@example.com", "Oh hai! Blah blah blah blah blahblahblah."),
            Message("someotherguy@example.com", "Yaya, blee blee bleeee.")
        )

        message_list.adapter = MessagesAdapter(messages, this)
    }
}

private class MessagesAdapter(private val messages: List<Message>, val context: Context): RecyclerView.Adapter<MessagesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        return MessagesViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message, parent, false))
    }

    override fun getItemCount(): Int {
        return messages.count()
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        val message = messages[position]

        holder.itemView.sender_label.text = message.sender
        holder.itemView.message_body_label.text = message.messageBody
    }
}

class MessagesViewHolder(view: View): RecyclerView.ViewHolder(view)

data class Message(val sender: String, val messageBody: String)