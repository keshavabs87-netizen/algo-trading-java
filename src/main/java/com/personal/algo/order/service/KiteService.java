package com.personal.algo.order.service;

import com.personal.algo.order.model.KiteSession;
import com.personal.algo.order.model.SessionStore;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class KiteService {

    @Value("${kite.api.key}")
    private String apiKey;

    @Value("${kite.api.secret}")
    private String apiSecret;

    private final SessionStore sessionStore;

    public KiteService(SessionStore sessionStore) {
        this.sessionStore = sessionStore;
    }

    public String getLoginUrl(String redirectUri) {
        KiteConnect kite = new KiteConnect(apiKey);
        //kite.setRedirectUri(redirectUri);
        return kite.getLoginURL();
    }

    public KiteSession generateSession(String requestToken) throws Exception, KiteException {
        KiteConnect kite = new KiteConnect(apiKey);
        User user = kite.generateSession(requestToken, apiSecret);

        KiteSession session = new KiteSession(
                user.accessToken,
                user.publicToken
        );

        sessionStore.saveSession(session);

        return session;
    }

    public KiteConnect getAuthenticatedClient() {
        KiteSession s = sessionStore.getSession();
        KiteConnect kite = new KiteConnect(apiKey);
        kite.setAccessToken(s.getAccessToken());
        kite.setPublicToken(s.getPublicToken());
        return kite;
    }
}
