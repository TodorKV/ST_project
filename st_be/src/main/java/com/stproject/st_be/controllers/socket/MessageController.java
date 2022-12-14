package com.stproject.st_be.controllers.socket;

import java.security.Principal;

import com.ft.dto.ResponseMessageDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {

    @MessageMapping("/message")
    @SendToUser("/topic/private-messages")
    public ResponseEntity<ResponseMessageDto> getPrivateMessage(final String message,
            final Principal principal) {

        return new ResponseEntity<>(new ResponseMessageDto(message), HttpStatus.OK);

    }
}
