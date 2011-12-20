package com.sina.openapi;

import java.io.File;

import junit.framework.TestCase;

import com.sina.openapi.net.OAuthHttpClient;

public class Apiv2Test extends TestCase {

    RPCOpenAPI rpcOpenAPI;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        if (rpcOpenAPI == null) {
            String[] tokenHolder = new String[2];
            OAuthHttpClient.login("13439630106", "3625300", tokenHolder);
            rpcOpenAPI = new RPCOpenAPI(new OAuthHttpClient(tokenHolder[0],
                    tokenHolder[1]));
        }
    }

    public void testUploadStatus() throws Exception {
        rpcOpenAPI.uploadStatus("Test", new File("Screenshot-1.png"), 60.,
                -140.);
    }
}
