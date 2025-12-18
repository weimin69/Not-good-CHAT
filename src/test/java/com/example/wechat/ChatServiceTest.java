package com.example.wechat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatServiceTest {
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new ChatService();
        chatService.registerUser("alice");
        chatService.registerUser("bob");
    }

    @Test
    void registerUserShouldFailWhenBlank() {
        assertThrows(IllegalArgumentException.class, () -> chatService.registerUser("   "));
    }

    @Test
    void sendMessageShouldStoreMessage() {
        Message message = chatService.sendMessage("alice", "bob", "你好");
        assertEquals("alice", message.getSender());
        assertEquals("bob", message.getRecipient());
        assertEquals("你好", message.getContent());
    }

    @Test
    void getConversationShouldReturnMessagesBothWays() {
        chatService.sendMessage("alice", "bob", "你好 Bob");
        chatService.sendMessage("bob", "alice", "你好 Alice");

        List<Message> conversation = chatService.getConversation("alice", "bob");
        assertEquals(2, conversation.size());
        assertEquals("alice", conversation.get(0).getSender());
        assertEquals("bob", conversation.get(1).getSender());
    }

    @Test
    void inboxShouldReturnOnlyReceivedMessages() {
        chatService.sendMessage("alice", "bob", "你好 Bob");
        chatService.sendMessage("bob", "alice", "你好 Alice");

        List<Message> inbox = chatService.getInbox("bob");
        assertEquals(1, inbox.size());
        assertEquals("alice", inbox.get(0).getSender());
    }
}
