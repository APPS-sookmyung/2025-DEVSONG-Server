package com.devsong.server.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageDto {

    private Long roomId;    //방 번호
    private String content; //내용
}