package com.joshi.listener;


import com.joshi.dto.DocumentFailedEvent;
import com.joshi.dto.DocumentProcessedEvent;
import com.joshi.service.WhatsAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DocumentEventListener {

    @Autowired
    private WhatsAppService whatsAppService;

    private String message;

    @KafkaListener(topics = "document.processed", groupId = "notification-group")
    public void handleDocumentProcessed(DocumentProcessedEvent event) {
        log.info("Received processed event for file: {}", event.getFileName());
        if (event.getClassification().equalsIgnoreCase("resume")) {
            log.info("Current File processed is Resume");
            message = "Your resume '" + event.getFileName() + "' has been processed successfully. " +
                    "We’ll notify you about the next steps soon.";
        } else {
            log.info("Current File processed is document");
            message = "Your file '" + event.getFileName() + "' has been processed successfully.";
        }
        whatsAppService.sendWhatsAppMessage(message);
    }

    @KafkaListener(topics = "document.failed", groupId = "notification-group")
    public void handleDocumentFailed(DocumentFailedEvent event) {
        log.info("Received failed event for file: {}", event.getFileName());
        message = "Sorry, We are unable to proceed with your application";
        whatsAppService.sendWhatsAppMessage(message);
    }
}