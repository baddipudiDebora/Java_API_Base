package org.db.apicore.auth;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import software.amazon.awssdk.auth.credentials.AwsSessionCredentials;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.sts.StsClient;
import software.amazon.awssdk.services.sts.model.AssumeRoleRequest;

import java.time.Instant;

public class TokenManager {

    private static TokenManager instance;

    private final String roleArn;
    private final String clientId;
    private final String secretName;
    private final String tokenUrl;

    private String cachedToken;
    private Instant expiryTime;

    private TokenManager(String roleArn, String clientId, String secretName, String tokenUrl) {
        this.roleArn = roleArn;
        this.clientId = clientId;
        this.secretName = secretName;
        this.tokenUrl = tokenUrl;
    }

    public static synchronized TokenManager init(String roleArn,
                                                 String clientId,
                                                 String secretName,
                                                 String tokenUrl) {
        if (instance == null) {
            instance = new TokenManager(roleArn, clientId, secretName, tokenUrl);
        }
        return instance;
    }

    public static TokenManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TokenManager not initialized");
        }
        return instance;
    }

    public synchronized String getToken() {

        if (cachedToken == null || Instant.now().isAfter(expiryTime)) {

            // -------------------------
            // 1. Assume Role
            // -------------------------
            StsClient sts = StsClient.builder().build();

            var assumeRole = sts.assumeRole(
                    AssumeRoleRequest.builder()
                            .roleArn(roleArn)
                            .roleSessionName("api-test-session")
                            .build()
            );

            AwsSessionCredentials tempCreds = AwsSessionCredentials.create(
                    assumeRole.credentials().accessKeyId(),
                    assumeRole.credentials().secretAccessKey(),
                    assumeRole.credentials().sessionToken()
            );

            // -------------------------
            // 2. Fetch Secret from Secrets Manager
            // -------------------------
            SecretsManagerClient secrets = SecretsManagerClient.builder()
                    .credentialsProvider(() -> tempCreds)
                    .build();

            String clientSecret = secrets.getSecretValue(
                    GetSecretValueRequest.builder()
                            .secretId(secretName)
                            .build()
            ).secretString();

            // -------------------------
            // 3. Generate Token
            // -------------------------
            var response = RestAssured.given()
                    .contentType(ContentType.JSON)
                    .body("""
                            {
                              "client_id": "%s",
                              "client_secret": "%s",
                              "grant_type": "client_credentials"
                            }
                            """.formatted(clientId, clientSecret))
                    .post(tokenUrl);

            this.cachedToken = response.jsonPath().getString("access_token");
            int expiresIn = response.jsonPath().getInt("expires_in");

            // Refresh 30 seconds early
            this.expiryTime = Instant.now().plusSeconds(expiresIn - 30);
        }

        return cachedToken;
    }
}
