package com.example.wechat;

import java.util.Objects;
import java.util.UUID;

/**
 * Simple value object representing a registered user.
 */
public final class User {
    private final String id;
    private final String username;

    public User(String username) {
        this(UUID.randomUUID().toString(), username);
    }

    public User(String id, String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        this.id = Objects.requireNonNull(id, "id");
        this.username = username.trim();
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && username.equals(user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
