package com.martel.chat.chatroom;

import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository repository;

    public Optional<String> getChatRoomId(
        String senderId,
        String recipientId,
        boolean createNewRoomIfNotExists
    ) {
        return repository.findBySenderIdAndRecipientId(senderId, recipientId)
            .map(ChatRoom :: getChatId)
            .or(() -> {
                if (createNewRoomIfNotExists) {
                    var chatId = createChatId(senderId, recipientId);
                    return Optional.of(chatId);
                }
                return Optional.empty();
            });
    }

    String createChatId(String senderId, String recipientId) {

        var chatId = String.format("%s_%s", senderId, recipientId);
        ChatRoom senderRecipient = ChatRoom.builder()
            .chatId(chatId)
            .senderId(senderId)
            .recipientId(recipientId)
            .build();

        ChatRoom recipientSender = ChatRoom.builder()
            .chatId(chatId)
            .senderId(recipientId)
            .recipientId(senderId)
            .build();

        repository.save(senderRecipient);
        repository.save(recipientSender);

        return chatId;
    }
    
}
