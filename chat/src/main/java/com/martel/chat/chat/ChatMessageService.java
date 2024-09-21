package com.martel.chat.chat;

import org.springframework.stereotype.Service;

import com.martel.chat.chatroom.ChatRoomService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    
    private final ChatMessageRepository repository;
    private final ChatRoomService chatRoomService;

    public ChatMessage save(ChatMessage chatMessage) {

        var chatId = chatRoomService.getChatRoomId(
            chatMessage.getSenderId(),
            chatMessage.getRecipientId(),
            true
            ).orElseThrow(); //Create exception
        chatMessage.setChatId(chatId);

        repository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(
        String senderId,
        String recipientId
    ) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        return chatId.map(repository :: findByChatId).orElse(new ArrayList<>());
    }
}
