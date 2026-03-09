package com.joshi.listener;


import com.joshi.dto.DocumentFailedEvent;
import com.joshi.dto.DocumentProcessedEvent;
import com.joshi.service.WhatsAppService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DocumentEventListener {

    private final WhatsAppService whatsAppService;

    public DocumentEventListener(WhatsAppService whatsAppService) {
        this.whatsAppService = whatsAppService;
    }

    @KafkaListener(topics = "document.processed", groupId = "notification-group")
    public void handleDocumentProcessed(DocumentProcessedEvent event) {

        log.info("Received processed event for file: {}", event.getFileName());

        String message = "Document processed successfully: " + event.getFileName();
        whatsAppService.sendWhatsAppMessage(message);

    }

    @KafkaListener(topics = "document.failed", groupId = "notification-group")
    public void handleDocumentFailed(DocumentFailedEvent event) {

        log.info("Received failed event for file: {}", event.getFileName());

        String message = "Document processing failed for file: "
                + event.getFileName() + " Reason: " + event.getReason();

        whatsAppService.sendWhatsAppMessage(message);

    }
}