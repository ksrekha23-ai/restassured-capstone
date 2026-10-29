// ABOUTME: Centralizes API endpoint paths for all resources.
// ABOUTME: Provides constants for users, posts, and comments endpoints.

package com.capstone.utils;

public final class EndpointConstants {

    private EndpointConstants() {
    }

    public static final String USERS = "/public/v2/users";
    public static final String POSTS = "/public/v2/posts";
    public static final String COMMENTS = "/public/v2/comments";

    public static String userById(int id) {
        return USERS + "/" + id;
    }

    public static String postById(int id) {
        return POSTS + "/" + id;
    }

    public static String postsForUser(int userId) {
        return USERS + "/" + userId + "/posts";
    }

    public static String commentsForPost(int postId) {
        return POSTS + "/" + postId + "/comments";
    }
}
