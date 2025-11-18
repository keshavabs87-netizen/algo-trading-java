package com.personal.algo.order.controller;

import com.personal.algo.order.model.KiteSession;
import com.personal.algo.order.service.KiteService;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class AuthController {

    @Value("${kite.api.key}")
    private String apiKey;

    @Value("${kite.redirect.uri}")
    private String redirectUri;

    private final KiteService kiteService;

    public AuthController(KiteService kiteService) {
        this.kiteService = kiteService;
    }

    @GetMapping("/api/auth/login-url")
    public String loginUrl() {
        return "redirect:" + kiteService.getLoginUrl(redirectUri);
    }

    @GetMapping("/kite-callback")
    public ResponseEntity<?> callback(@RequestParam Map<String,String> query) {
        try {
            String requestToken = query.get("request_token");

            KiteSession session = kiteService.generateSession(requestToken);

            return ResponseEntity.ok(
                    Map.of(
                            "access_token", session.getAccessToken(),
                            "public_token", session.getPublicToken()
                    )
            );

        } catch (Exception | KiteException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
