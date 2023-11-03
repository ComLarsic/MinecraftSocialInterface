package net.msimod.common.chat;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.UUID;

/// Handles logging chat messages
public class ChatLogger {
    public static final int MAX_CHAT_LOG_SIZE = 100_000_000;

    /// A message in the chat
    public static class ChatMessageLog {
        public @Nullable UUID sender;
        public @Nullable String senderName;
        public String contents;

        public ChatMessageLog(@Nullable UUID sender, @Nullable String senderName, String contents) {
            this.sender = sender;
            this.senderName = senderName;
            this.contents = contents;
        }
    }

    /// The current chat log
    private final ArrayList<ChatMessageLog> chatLog = new ArrayList<ChatMessageLog>();

    /// Appends a message to the chat log.
    /// Will remove the oldest message if the log is full.
    public void append(UUID sender, String senderName, String contents) {
        chatLog.add(new ChatMessageLog(sender, senderName, contents));

        if (chatLog.size() > MAX_CHAT_LOG_SIZE) {
            chatLog.remove(0);
        }
    }

    /// Append a non-player message to the log
    public void appendNonPlayer(String contents) {
        chatLog.add(new ChatMessageLog(null, null, contents));

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
            if (message.sender == null) {
                builder.append(">");
            } else {
                builder.append(message.sender);
            }
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

            if (message.sender == null) {
                builder.append(">");
            } else {
                builder.append(message.sender);
            }
            builder.append(": ");
            builder.append(message.contents);
            builder.append("\n");
        }

        return builder.toString();
    }
}
