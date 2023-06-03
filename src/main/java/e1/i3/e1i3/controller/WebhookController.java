package e1.i3.e1i3.controller;

import e1.i3.e1i3.service.transaction.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class WebhookController {

    TransactionService transactionService;

    public WebhookController(TransactionService transactionService){this.transactionService = transactionService;}
    @PostMapping("/webhook")
    public ResponseEntity<String> createWebHook(@RequestBody Object eventData) throws IOException {

        transactionService.saveRecentTransaction(eventData);

        return ResponseEntity.ok("Event handled successfully");
    }
}