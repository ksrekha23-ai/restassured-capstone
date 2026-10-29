// ABOUTME: Tests for the GoRest User API covering all CRUD operations.
// ABOUTME: Validates status codes, response fields, and response time for 5 user endpoints.

package com.capstone.tests;

import com.capstone.config.ConfigReader;
import com.capstone.models.User;
import com.capstone.utils.ApiHelper;
import com.capstone.utils.EndpointConstants;
import com.capstone.utils.TestDataGenerator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

public class UserApiTest extends BaseTest {

    private final List<Integer> createdUserIds = new ArrayList<>();

    @AfterMethod
    public void cleanup() {
        for (int id : createdUserIds) {
            ApiHelper.delete(EndpointConstants.USERS, id);
        }
        createdUserIds.clear();
    }

    @Test(description = "GET /users - Retrieve all users and validate response")
    public void getAllUsers() {
        Response response = ApiHelper.get(EndpointConstants.USERS);

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertTrue(response.getTime() < 3000, "Response time should be under 3 seconds");

        List<?> users = response.jsonPath().getList("$");
        Assert.assertFalse(users.isEmpty(), "Users list should not be empty");
    }

    @Test(description = "GET /users/{id} - Retrieve a single user by ID")
    public void getUserById() {
        User newUser = TestDataGenerator.randomUser();
        Response createResponse = ApiHelper.post(EndpointConstants.USERS, newUser);
        int userId = createResponse.jsonPath().getInt("id");
        createdUserIds.add(userId);

        Response response = ApiHelper.getById(EndpointConstants.USERS, userId);

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertEquals(response.jsonPath().getString("name"), newUser.getName());
        Assert.assertEquals(response.jsonPath().getString("email"), newUser.getEmail());
        Assert.assertEquals(response.jsonPath().getString("gender"), newUser.getGender());
        Assert.assertEquals(response.jsonPath().getString("status"), newUser.getStatus());
    }

    @Test(description = "POST /users - Create a new user with POJO body")
    public void createUser() {
        User newUser = TestDataGenerator.randomUser();
        Response response = ApiHelper.post(EndpointConstants.USERS, newUser);

        Assert.assertEquals(response.getStatusCode(), 201, "Status code should be 201 Created");
        Assert.assertTrue(response.getTime() < 3000, "Response time should be under 3 seconds");

        int userId = response.jsonPath().getInt("id");
        createdUserIds.add(userId);

        Assert.assertTrue(userId > 0, "User ID should be a positive integer");
        Assert.assertEquals(response.jsonPath().getString("name"), newUser.getName());
        Assert.assertEquals(response.jsonPath().getString("email"), newUser.getEmail());
        Assert.assertEquals(response.jsonPath().getString("gender"), newUser.getGender());
        Assert.assertEquals(response.jsonPath().getString("status"), newUser.getStatus());
    }

    @Test(description = "PUT /users/{id} - Update an existing user")
    public void updateUser() {
        User newUser = TestDataGenerator.randomUser();
        Response createResponse = ApiHelper.post(EndpointConstants.USERS, newUser);
        int userId = createResponse.jsonPath().getInt("id");
        createdUserIds.add(userId);

        String updatedName = "Updated " + newUser.getName();
        User updatedUser = new User(updatedName, newUser.getEmail(), newUser.getGender(), newUser.getStatus());

        Response response = ApiHelper.put(EndpointConstants.USERS, userId, updatedUser);

        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        Assert.assertEquals(response.jsonPath().getString("name"), updatedName);
    }

    @Test(description = "DELETE /users/{id} - Delete a user and verify removal")
    public void deleteUser() {
        User newUser = TestDataGenerator.randomUser();
        Response createResponse = ApiHelper.post(EndpointConstants.USERS, newUser);
        int userId = createResponse.jsonPath().getInt("id");

        Response deleteResponse = ApiHelper.delete(EndpointConstants.USERS, userId);
        Assert.assertEquals(deleteResponse.getStatusCode(), 204, "Status code should be 204 No Content");

        Response getResponse = ApiHelper.getById(EndpointConstants.USERS, userId);
        Assert.assertEquals(getResponse.getStatusCode(), 404, "Deleted user should return 404");
    }

    @DataProvider(name = "userData")
    public Object[][] userData() {
        return new Object[][]{
                {"male", "active"},
                {"female", "active"},
                {"male", "inactive"},
                {"female", "inactive"},
        };
    }

    @Test(dataProvider = "userData", description = "POST /users - Create users with different gender and status combinations")
    public void createUserWithDataProvider(String gender, String status) {
        User newUser = TestDataGenerator.randomUser(gender, status);
        Response response = ApiHelper.post(EndpointConstants.USERS, newUser);

        Assert.assertEquals(response.getStatusCode(), 201, "Status code should be 201 Created");

        int userId = response.jsonPath().getInt("id");
        createdUserIds.add(userId);

        Assert.assertEquals(response.jsonPath().getString("gender"), gender);
        Assert.assertEquals(response.jsonPath().getString("status"), status);
    }
}
