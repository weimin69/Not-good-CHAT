# Not-good-CHAT

一个用 Java 编写的迷你版“微信”控制台程序，支持注册用户、发送消息、查看聊天记录和收件箱。应用内置了一些示例数据，运行后即可直接体验。

## 运行

```bash
mvn clean package
java -cp target/wechat-lite-1.0-SNAPSHOT.jar com.example.wechat.WeChatApp
```

运行后使用以下命令体验：

- `users` 列出现有用户。
- `register <username>` 注册新用户。
- `send <from> <to> <content>` 发送消息。
- `chat <userA> <userB>` 查看双方聊天记录。
- `inbox <username>` 查看用户收到的消息。
- `help` 查看帮助。
- `exit` 或 `quit` 退出程序。

## 测试

```bash
mvn test
```
