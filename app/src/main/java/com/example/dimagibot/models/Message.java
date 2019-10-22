package com.example.dimagibot.models;

/**
 * $ |-| ! V @ M
 * Created by Shivam Pokhriyal on 2019-10-22.
 */
public class Message {
    public enum MessageType {
        RECEIVED(0),
        SENT(1);

        private int type;

        MessageType(int i) {
            type = i;
        }

        public int getValue() {
            return type;
        }
    }

    private String key;
    private String text;
    private MessageType type;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "key='" + key + '\'' +
                ", text='" + text + '\'' +
                ", type=" + type +
                '}';
    }
}
