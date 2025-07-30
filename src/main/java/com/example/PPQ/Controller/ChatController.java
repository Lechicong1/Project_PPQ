package com.example.PPQ.Controller;

import com.example.PPQ.Payload.Response.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage message, Principal principal) {
        // principal.getName() là username của người gửi (nếu dùng Spring Security)
        System.out.println("Tin nhắn từ: " + message.getSender() + " → " + message.getReceiver());

        // Gửi tin nhắn đến người nhận
        messagingTemplate.convertAndSendToUser(
                message.getReceiver(),
                "/queue/messages", // sẽ là /user/{username}/queue/messages
                message.getContent()
        );
    }
}
