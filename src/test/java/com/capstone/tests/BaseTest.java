// ABOUTME: Base test class with suite-level setup for REST Assured configuration.
// ABOUTME: Loads environment config, sets base URI, and configures logging.

package com.capstone.tests;

import com.capstone.config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeSuite;

public class BaseTest {

    @BeforeSuite
    public void setup() {
        ConfigReader.loadConfig();
        RestAssured.baseURI = ConfigReader.getBaseUrl();
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
}
