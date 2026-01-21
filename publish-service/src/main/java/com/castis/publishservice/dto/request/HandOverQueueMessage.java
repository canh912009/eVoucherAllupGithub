package com.castis.publishservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HandOverQueueMessage {
    private String oldEv;
    private EndUserBERequest newUser;
    private String message;
}
