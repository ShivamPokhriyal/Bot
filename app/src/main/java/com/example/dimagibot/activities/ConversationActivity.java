package com.example.dimagibot.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.dimagibot.R;
import com.example.dimagibot.adapters.ConversationAdapter;
import com.example.dimagibot.models.Message;
import com.example.dimagibot.utils.VerticalSpaceItemDecoration;
import com.example.dimagibot.views.ChatBox;

import java.util.UUID;

public class ConversationActivity extends AppCompatActivity implements ChatBox.ChatBoxEvents {

    private RecyclerView recyclerView;
    private ChatBox chatBox;
    private ConversationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        chatBox = findViewById(R.id.chat_box);
        chatBox.createView(this);

        recyclerView = findViewById(R.id.message_list);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(10));
        adapter = new ConversationAdapter(this);
        recyclerView.setAdapter(adapter);

        setTitle("Chat");
    }

    @Override
    public void onSendButtonClicked(String text) {
        sendMessage(text, Message.MessageType.SENT);
    }

    public void sendMessage(String text, Message.MessageType type) {
        Message message = new Message();
        message.setKey(UUID.randomUUID().toString());
        message.setText(text);
        message.setType(type);

        adapter.addMessages(message);
        adapter.notifyItemInserted(adapter.getItemCount() - 1);
        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
    }
}
