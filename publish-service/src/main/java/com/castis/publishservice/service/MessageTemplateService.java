package com.castis.publishservice.service;

import com.castis.publishservice.dto.queue.MessageTemplateRequest;
import com.castis.publishservice.entity.MessageTemplate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class MessageTemplateService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }
    public static MessageTemplateRequest toRequest(MessageTemplate messageTemplate) throws JsonProcessingException {
        if (messageTemplate == null) return null;
        MessageTemplateRequest result;
        result = objectMapper.readValue(messageTemplate.getTemplateDetail(), MessageTemplateRequest.class);
        result.setTemplate(messageTemplate.getMessageString());
        return result;
    }
}
