package com.example.wechat;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Console entry point for the mini WeChat-style application.
 */
public class WeChatApp {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    public static void main(String[] args) {
        ChatService chatService = new ChatService();
        chatService.seedDemoData();
        System.out.println("欢迎使用迷你微信 (Java 版)。输入 help 查看命令。");
        runLoop(chatService);
    }

    private static void runLoop(ChatService chatService) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("wechat> ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = line.split("\\s+", 3);
                String command = parts[0].toLowerCase(Locale.ROOT);
                try {
                    switch (command) {
                        case "register" -> handleRegister(chatService, parts);
                        case "send" -> handleSend(chatService, parts);
                        case "chat" -> handleChat(chatService, parts);
                        case "inbox" -> handleInbox(chatService, parts);
                        case "users" -> handleUsers(chatService);
                        case "help" -> printHelp();
                        case "exit", "quit" -> {
                            System.out.println("再见！");
                            return;
                        }
                        default -> System.out.println("未知命令，输入 help 查看帮助。");
                    }
                } catch (IllegalArgumentException ex) {
                    System.out.println("错误: " + ex.getMessage());
                }
            }
        }
    }

    private static void handleRegister(ChatService chatService, String[] parts) {
        if (parts.length < 2) {
            System.out.println("用法: register <username>");
            return;
        }
        User user = chatService.registerUser(parts[1]);
        System.out.println("注册成功: " + user.getUsername());
    }

    private static void handleSend(ChatService chatService, String[] parts) {
        if (parts.length < 3) {
            System.out.println("用法: send <from> <to> <content>");
            return;
        }
        String[] usersAndContent = parts[2].split("\\s+", 2);
        if (usersAndContent.length < 2) {
            System.out.println("用法: send <from> <to> <content>");
            return;
        }
        String from = parts[1];
        String to = usersAndContent[0];
        String content = usersAndContent[1];
        Message message = chatService.sendMessage(from, to, content);
        System.out.println("消息已发送: " + message);
    }

    private static void handleChat(ChatService chatService, String[] parts) {
        if (parts.length < 3) {
            System.out.println("用法: chat <userA> <userB>");
            return;
        }
        List<Message> conversation = chatService.getConversation(parts[1], parts[2]);
        if (conversation.isEmpty()) {
            System.out.println("暂无聊天记录。");
            return;
        }
        conversation.forEach(msg -> System.out.printf("[%s] %s -> %s: %s%n",
                FORMATTER.format(msg.getTimestamp()), msg.getSender(), msg.getRecipient(), msg.getContent()));
    }

    private static void handleInbox(ChatService chatService, String[] parts) {
        if (parts.length < 2) {
            System.out.println("用法: inbox <username>");
            return;
        }
        List<Message> inbox = chatService.getInbox(parts[1]);
        if (inbox.isEmpty()) {
            System.out.println("暂无消息。");
            return;
        }
        inbox.forEach(msg -> System.out.printf("[%s] %s -> %s: %s%n",
                FORMATTER.format(msg.getTimestamp()), msg.getSender(), msg.getRecipient(), msg.getContent()));
    }

    private static void handleUsers(ChatService chatService) {
        List<User> users = chatService.listUsers();
        if (users.isEmpty()) {
            System.out.println("还没有用户，使用 register <username> 创建一个吧。");
            return;
        }
        users.forEach(u -> System.out.printf("- %s (id: %s)%n", u.getUsername(), u.getId()));
    }

    private static void printHelp() {
        System.out.println("可用命令：");
        System.out.println("  register <username>    注册新用户");
        System.out.println("  send <from> <to> <content>    发送消息");
        System.out.println("  chat <userA> <userB>  查看双方聊天记录");
        System.out.println("  inbox <username>      查看用户收到的消息");
        System.out.println("  users                 列出所有用户");
        System.out.println("  help                  查看帮助");
        System.out.println("  exit / quit           退出程序");
    }
}
