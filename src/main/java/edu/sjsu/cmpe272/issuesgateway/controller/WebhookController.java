package com.example.webhook.controller;

import com.example.webhook.model.WebhookEvent;
import com.example.webhook.repository.WebhookEventRepository;
import com.example.webhook.service.HmacService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@RestController
public class WebhookController {

    private static final Set<String> ALLOWED_EVENTS = Set.of("issues", "issue_comment", "ping");

    private final HmacService hmacService;
    private final WebhookEventRepository repository;
    private final ObjectMapper mapper = new ObjectMapper();

    public WebhookController(HmacService hmacService, WebhookEventRepository repository) {
        this.hmacService = hmacService;
        this.repository = repository;
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(
            @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature,
            @RequestHeader(value = "X-GitHub-Event", required = false) String eventType,
            @RequestHeader(value = "X-GitHub-Delivery", required = false) String deliveryId,
            @RequestBody byte[] rawBody) {

        // 1. Verify HMAC SHA-256 Signature (401 on mismatch or missing header)
        if (!hmacService.verifySignature(rawBody, signature)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // 2. Validate Event Type (400 on unknown event)
        if (eventType == null || !ALLOWED_EVENTS.contains(eventType)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Parse payload action
        String bodyString = new String(rawBody, StandardCharsets.UTF_8);
        String action = null;
        try {
            JsonNode root = mapper.readTree(bodyString);
            if (root.has("action")) {
                action = root.get("action").asText();
            }
        } catch (Exception ignored) {}

        // 3. Deduplicate on (X-GitHub-Delivery + action)
        String dedupeActionKey = (action != null) ? action : "N/A";
        if (repository.existsByDeliveryIdAndAction(deliveryId, dedupeActionKey)) {
            // No-op for redelivery
            return ResponseEntity.noContent().build();
        }

        // Save Event to DB
        WebhookEvent event = new WebhookEvent(deliveryId, eventType, dedupeActionKey, bodyString);
        repository.save(event);

        // Return 204 No Content on success
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/events")
    public ResponseEntity<List<WebhookEvent>> getEvents(
            @RequestParam(name = "limit", defaultValue = "10") int limit) {
        
        List<WebhookEvent> events = repository.findLatestEvents(PageRequest.of(0, limit));
        return ResponseEntity.ok(events);
    }
}
