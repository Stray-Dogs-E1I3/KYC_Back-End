package e1.i3.e1i3.controller;

import e1.i3.e1i3.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class WebhookController {

    private final TransactionService transactionService;

    @Autowired
    public WebhookController(TransactionService transactionService) {this.transactionService = transactionService;}

    @PostMapping("/webhook")
    public ResponseEntity<String> createWebHook(@RequestBody Object eventData) throws IOException {

        transactionService.saveRecentTransaction(eventData);

        return ResponseEntity.ok("Event handled successfully");
    }
}