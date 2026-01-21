package com.castis.publishservice.dto.queue;

import com.castis.publishservice.utils.status.GenerateMessageType;
import com.castis.publishservice.utils.status.PublishStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * PublishEvoucherQueueDTO
 *
 * @author by daont on 16/05/2023
 * @project publish-service
 */

@Data
//@Builder
@AllArgsConstructor
public class PublishQueueRequest {
	private Long id;
	private String name;
	private CampaignRequest campaign;
	private List<PublishDetailQueueRequest> publishDetails;
	private boolean isBooking;
	private String messageSubject;
	private String messageContent;
	private String messageCallingNumber;
	@JsonIgnore
	private String customerId;
	private CustomerRequest customer;
	private String bookingDate;
	private String publishDate;
	private String cancelDate;
	private boolean isTestSend;
	private boolean isReceiverNoDuplicateAllowed;
	private String smsType;
	private String templateId;
	private PublishStatus publishStatusCode;
	private String transferMessage;
	private GenerateMessageType type;
	private String senderName;
	private Integer voucherResendHistoryId;
}