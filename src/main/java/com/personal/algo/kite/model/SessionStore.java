package com.personal.algo.kite.model;

import org.springframework.stereotype.Component;

@Component
public class SessionStore {

    private KiteSession session; // simplest version

    public void saveSession(KiteSession s) {
        this.session = s;
    }

    public KiteSession getSession() {
        return session;
    }
}
