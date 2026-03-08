package org.db.apicore.utils;

import io.restassured.response.Response;
import org.db.apicore.reporting.Reporter;

public class ApiLogger {
    public static void logRequest(String method,String url, String body){
        Reporter.info("----------- API Request ----------");
        Reporter.info("Methods: "+method);
        Reporter.info("URL: "+url);

        if(body !=null && !body.isEmpty()){
            Reporter.info("Request Body: "+body);
        }
    }

    public static void logresponse(Response response){
        Reporter.info("----- API RESPONSE -----");
        Reporter.info("Status Line: " + response.statusLine());
        Reporter.info("Headers: " + response.getHeaders());
        Reporter.info("Response Time: " + response.time() + " ms");
        Reporter.pass("API call completed successfully");

    }
}
