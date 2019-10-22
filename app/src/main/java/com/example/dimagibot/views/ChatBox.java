package com.example.dimagibot.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.dimagibot.R;

/**
 * $ |-| ! V @ M
 * Created by Shivam Pokhriyal on 2019-10-22.
 */
public class ChatBox extends LinearLayout {

    EditText messageEditText;
    ImageButton sendMessageButton;
    private ChatBoxEvents listener;

    public ChatBox(Context context) {
        this(context, null, 0);
    }

    public ChatBox(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatBox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void createView(final ChatBoxEvents listener) {
        removeAllViews();
        this.listener = listener;

        LayoutInflater inflater = (LayoutInflater) getContext().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.chatbox, null);

        LinearLayout mainEditTextLayout = view.findViewById(R.id.edit_text_linear_layout);
        messageEditText = mainEditTextLayout.findViewById(R.id.conversation_message);

        sendMessageButton = view.findViewById(R.id.conversation_send);
        sendMessageButton.setAlpha(0.2f);
        sendMessageButton.setEnabled(false);

        ((LinearLayout) view).removeAllViews();

        attachListeners();

        addView(mainEditTextLayout);
        addView(sendMessageButton);
    }

    public void attachListeners() {
        sendMessageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = messageEditText.getText().toString().trim();
                if (listener != null) {
                    listener.onSendButtonClicked(text);
                }
                messageEditText.getText().clear();
            }
        });

        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0 && charSequence.toString().trim().length() > 0) {
                    sendMessageButton.setAlpha(1.0f);
                    sendMessageButton.setEnabled(true);
                } else {
                    sendMessageButton.setAlpha(0.2f);
                    sendMessageButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    public interface ChatBoxEvents {
        void onSendButtonClicked(String text);
    }

}
