package com.example.dimagibot.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.TextView;

import com.example.dimagibot.R;
import com.example.dimagibot.models.Message;

import java.util.ArrayList;

/**
 * $ |-| ! V @ M
 * Created by Shivam Pokhriyal on 2019-10-22.
 */
public class ConversationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Message> messages;

    public ConversationAdapter(Context context) {
        this.context = context;
        messages = new ArrayList<>();
    }

    public void addMessages(Message message) {
        messages.add(message);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) {
            return null;
        }
        View view;
        if (viewType == Message.MessageType.SENT.getValue()) {
            view = inflater.inflate(R.layout.sender_message, parent, false);
        } else {
            view = inflater.inflate(R.layout.receiver_message, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder holder = (MyViewHolder) viewHolder;
        Message current = messages.get(position);
        holder.messageBody.setText(current.getText());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        return message.getType().getValue();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView messageBody;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ConstraintLayout layout = itemView.findViewById(R.id.message_box);
            layout.setOutlineProvider(ViewOutlineProvider.BACKGROUND);
            layout.setClipToOutline(true);
            messageBody = itemView.findViewById(R.id.message_body);
        }
    }
}
