package com.sina.openapi;

import java.io.FileInputStream;

import junit.framework.TestCase;

public class Apiv3Test extends TestCase {
    private ApiClient apiClient;

    protected void setUp() throws Exception {
        super.setUp();
        if (apiClient == null) {
            apiClient = ApiClient.clientLogin("13439630106", "3625300");
        }
    }

    // public void testFriendsTimeline() throws Exception {
    // String execute = apiClient.execute(ApiRequest.getFriendsTimeline(null,
    // null, 20, 1, 0, 0));
    // assertTrue(isNotBlank(execute));
    // System.out.println(execute);
    // }

    // public void testUpdateStatus() throws Exception {
    // String execute = apiClient.execute(ApiRequest.updateStatus("测试微博",
    // null, 0., 0.));
    // assertTrue(isNotBlank(execute));
    // System.out.println(execute);
    // }

    public void testUploadStatus() throws Exception {
        apiClient.execute(ApiRequest.uploadStatus("测试微博2", new FileInputStream(
                "Screenshot-1.png"), 60.19, -160.99));
    }
    
    
}
