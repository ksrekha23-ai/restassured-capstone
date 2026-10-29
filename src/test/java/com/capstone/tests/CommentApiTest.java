// ABOUTME: Tests for the GoRest Comment API covering comment creation on posts.
// ABOUTME: Validates the full chain: create user, create post, add comment.

package com.capstone.tests;

import com.capstone.models.Comment;
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

public class CommentApiTest extends BaseTest {

    private final List<Integer> createdUserIds = new ArrayList<>();

    @AfterMethod
    public void cleanup() {
        for (int id : createdUserIds) {
            ApiHelper.delete(EndpointConstants.USERS, id);
        }
        createdUserIds.clear();
    }

    @Test(description = "POST /posts/{id}/comments - Create a comment on a post")
    public void createCommentOnPost() {
        User user = TestDataGenerator.randomUser();
        Response userResponse = ApiHelper.post(EndpointConstants.USERS, user);
        int userId = userResponse.jsonPath().getInt("id");
        createdUserIds.add(userId);

        Post post = TestDataGenerator.randomPost();
        Response postResponse = ApiHelper.post(EndpointConstants.postsForUser(userId), post);
        Assert.assertEquals(postResponse.getStatusCode(), 201, "Post creation should return 201");
        int postId = postResponse.jsonPath().getInt("id");

        Comment comment = TestDataGenerator.randomComment();
        Response response = ApiHelper.post(EndpointConstants.commentsForPost(postId), comment);

        Assert.assertEquals(response.getStatusCode(), 201, "Status code should be 201 Created");
        Assert.assertEquals(response.jsonPath().getString("name"), comment.getName());
        Assert.assertEquals(response.jsonPath().getString("email"), comment.getEmail());
        Assert.assertEquals(response.jsonPath().getString("body"), comment.getBody());
        Assert.assertEquals(response.jsonPath().getInt("post_id"), postId);
    }
}
