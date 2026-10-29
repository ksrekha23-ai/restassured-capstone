// ABOUTME: Tests for the GoRest Post API covering read and create operations.
// ABOUTME: Maps to Assignment 4 (Product API) using post endpoints.

package com.capstone.tests;

import com.capstone.models.Post;
import com.capstone.models.User;
import com.capstone.utils.ApiHelper;
import com.capstone.utils.EndpointConstants;
import com.capstone.utils.TestDataGenerator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class PostApiTest extends BaseTest {

    private final List<Integer> createdUserIds = new ArrayList<>();

    @AfterMethod
    public void cleanup() {
        for (int id : createdUserIds) {
            ApiHelper.delete(EndpointConstants.USERS, id);
        }
        createdUserIds.clear();
    }

    private int createTestUser() {
        User user = TestDataGenerator.randomUser();
        Response response = ApiHelper.post(EndpointConstants.USERS, user);
        int userId = response.jsonPath().getInt("id");
        createdUserIds.add(userId);
        return userId;
    }

    @Test(description = "GET /posts - Retrieve all posts and validate response structure")
    public void getAllPosts() {
        Response response = ApiHelper.get(EndpointConstants.POSTS);

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertTrue(response.getTime() < 3000, "Response time should be under 3 seconds");

        List<?> posts = response.jsonPath().getList("$");
        Assert.assertFalse(posts.isEmpty(), "Posts list should not be empty");

        String firstTitle = response.jsonPath().getString("[0].title");
        Assert.assertNotNull(firstTitle, "First post should have a title");
    }

    @Test(description = "GET /posts/{id} - Create a post and retrieve it by ID")
    public void getPostById() {
        int userId = createTestUser();

        Post newPost = TestDataGenerator.randomPost();
        Response createResponse = ApiHelper.post(EndpointConstants.postsForUser(userId), newPost);
        Assert.assertEquals(createResponse.getStatusCode(), 201, "Post creation should return 201");

        int postId = createResponse.jsonPath().getInt("id");

        Response response = ApiHelper.getById(EndpointConstants.POSTS, postId);

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertEquals(response.jsonPath().getString("title"), newPost.getTitle());
        Assert.assertEquals(response.jsonPath().getString("body"), newPost.getBody());
        Assert.assertEquals(response.jsonPath().getInt("user_id"), userId);
    }

    @Test(description = "GET /users/{id}/posts - Retrieve posts filtered by user")
    public void searchPostsByUser() {
        int userId = createTestUser();

        Post post1 = TestDataGenerator.randomPost();
        Post post2 = TestDataGenerator.randomPost();
        ApiHelper.post(EndpointConstants.postsForUser(userId), post1);
        ApiHelper.post(EndpointConstants.postsForUser(userId), post2);

        Response response = ApiHelper.get(EndpointConstants.postsForUser(userId));

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");

        List<?> posts = response.jsonPath().getList("$");
        Assert.assertTrue(posts.size() >= 2, "User should have at least 2 posts");

        List<Integer> userIds = response.jsonPath().getList("user_id");
        for (int id : userIds) {
            Assert.assertEquals(id, userId, "All posts should belong to the created user");
        }
    }
}
