// ABOUTME: Generates random test data for API test payloads.
// ABOUTME: Provides unique emails, names, post titles, and comment bodies.

package com.capstone.utils;

import com.capstone.models.Comment;
import com.capstone.models.Post;
import com.capstone.models.User;

import java.util.UUID;

public class TestDataGenerator {

    private TestDataGenerator() {
    }

    public static String randomEmail() {
        return "test_" + UUID.randomUUID().toString().substring(0, 8) + "@testmail.com";
    }

    public static String randomName() {
        String[] firstNames = {"Rekha", "Arun", "Priya", "Raj", "Sita", "Vikram", "Meera", "Kiran"};
        String[] lastNames = {"Kumar", "Sharma", "Patel", "Singh", "Nair", "Reddy", "Gupta", "Das"};
        int fi = (int) (Math.random() * firstNames.length);
        int li = (int) (Math.random() * lastNames.length);
        return firstNames[fi] + " " + lastNames[li];
    }

    public static User randomUser() {
        return new User(
                randomName(),
                randomEmail(),
                Math.random() > 0.5 ? "male" : "female",
                "active"
        );
    }

    public static User randomUser(String gender, String status) {
        return new User(randomName(), randomEmail(), gender, status);
    }

    public static Post randomPost() {
        return new Post(
                "Test Post " + UUID.randomUUID().toString().substring(0, 6),
                "This is an automated test post body created for API testing."
        );
    }

    public static Comment randomComment() {
        return new Comment(
                randomName(),
                randomEmail(),
                "This is an automated test comment for API validation."
        );
    }
}
