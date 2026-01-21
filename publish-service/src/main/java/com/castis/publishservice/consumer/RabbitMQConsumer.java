package com.castis.publishservice.consumer;

import com.castis.publishservice.dto.request.HandOverQueueMessage;
import com.castis.publishservice.dto.request.StatusQueue;
import com.castis.publishservice.service.ProcessService;
import com.castis.publishservice.service.VoucherService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * RabbitMQConsumer
 *
 * @author by daont on 08/05/2023
 * @project publish-service
 */


@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitMQConsumer {
	private final VoucherService service;
	private final ProcessService processService;
	private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
	@RabbitListener(queues = "${rabbitmq.queue.create-message-result}")
	public void updateCreateMessageStatus(String message) {
		try {
			log.info("========================================");
			log.info("received an generate message status message from queue");
			log.info("message: {}", message );
			StatusQueue statusQueue = gson.fromJson(message, StatusQueue.class);
			service.updatePublishStatus(statusQueue);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	@RabbitListener(queues = "${rabbitmq.queue.send-message-result}")
	public void updateSendMessageStatus(String message) {
		try {
			log.info("=======================================");
			log.info("received an update send message status message from queue");
			log.info("update send message: {}", message);
			List<StatusQueue> statusQueueList = gson.fromJson(message, new TypeToken<ArrayList<StatusQueue>>(){}.getType());
			service.updateSendMessageStatus(statusQueueList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
