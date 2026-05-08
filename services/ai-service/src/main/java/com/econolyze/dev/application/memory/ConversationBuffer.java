package com.econolyze.dev.application.memory;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class ConversationBuffer {

    private static final int MAX_MESSAGES = 10;

    private final Map<Long, Deque<String>> conversations = new ConcurrentHashMap<>();

    public void append(Long userId, String role, String message) {
        Deque<String> messages = conversations.computeIfAbsent(userId, id -> new ArrayDeque<>());
        messages.addLast(role + ": " + message);

        while (messages.size() > MAX_MESSAGES) {
            messages.removeFirst();
        }
    }

    public String getRecentMessage(Long userId){
        Deque<String> messages = conversations.get(userId);
        if (messages == null || messages.isEmpty()) {
            return "Nenhum histórico encontrado nesta conversa";
        }
        return messages.stream().collect(Collectors.joining("\n"));
    }
}
