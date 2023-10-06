package net.msimod.common.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/// Handles logging chat messages
public class ChatLogger {
    public static final int MAX_CHAT_LOG_SIZE = 100;

    /// A message in the chat
    public static class ChatMessageLog {
        public UUID sender;
        public String contents;

        public ChatMessageLog(UUID sender, String contents) {
            this.sender = sender;
            this.contents = contents;
        }
    }

    /// The current chat log
    private final ArrayList<ChatMessageLog> chatLog = new ArrayList<ChatMessageLog>();

    /// Appends a message to the chat log.
    /// Will remove the oldest message if the log is full.
    public void append(UUID sender, String contents) {
        chatLog.add(new ChatMessageLog(sender, contents));

        if (chatLog.size() > MAX_CHAT_LOG_SIZE) {
            chatLog.remove(0);
        }
    }

    /// Gets the chat log
    public ArrayList<ChatMessageLog> getChatLog() {
        return chatLog;
    }

    /// Gets the chat log as a string
    public String getChatLogString() {
        var builder = new StringBuilder();

        for (var message : chatLog) {
            builder.append(message.sender.toString());
            builder.append(": ");
            builder.append(message.contents);
            builder.append("\n");
        }

        return builder.toString();
    }

    /// Gets the chat log as a string, with a maximum number of messages
    public String getChatLogString(int maxMessages) {
        var builder = new StringBuilder();

        for (int i = 0; i < maxMessages; i++) {
            var message = chatLog.get(i);

            builder.append(message.sender.toString());
            builder.append(": ");
            builder.append(message.contents);
            builder.append("\n");
        }

        return builder.toString();
    }
}
