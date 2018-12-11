package br.edu.ufsm.requestpostgraphql.service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Douglas Giordano
 */
public class RequestService {

    private final String[] tokens = {"",""};
    private int tokenNow = 1;
    private final String url = "https://api.github.com/graphql";
    private int tentativas = 0;

    public JSONObject getHttpClient(String query) {
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);

        // Create some NameValuePair for HttpPost parameters
        Header header[] = new BasicHeader[2];
        header[0] = new BasicHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        header[1] = new BasicHeader("Authorization", "bearer " + tokens[tokenNow]);
        post.setHeaders(header);
        try {
            post.setEntity(new StringEntity(query));
            HttpResponse response = client.execute(post);

            // Print out the response message
            String jsonResponse = EntityUtils.toString(response.getEntity());
            JSONObject o = null;
            try {
                o = new JSONObject(jsonResponse);
                if (o == null || o.isNull("data")) {
                    System.out.println("Data is null, query: " + jsonResponse);
                    return null;
                }

                if (verifyLimitNow(o)) {
                    changeHashAtive();
                }

                if (o.getJSONObject("data").isNull("repository")) {
                    System.out.println("Data is null, query: " + jsonResponse);
                    return null;
                }
            } catch (JSONException ex) {
                System.out.println("Error in get object json extract: " + ex.getMessage());
                tentativas = tentativas + 1;
                if (tentativas == 3) {
                    tentativas = 0;
                    return null;
                }
                return getHttpClient(query);
            }

            tentativas = 0;
            return o;

        } catch (org.apache.http.NoHttpResponseException ex) {
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void changeHashAtive() {
        if (tokenNow == (tokens.length - 1)) {
            tokenNow = 0;
        } else {
            tokenNow = tokenNow + 1;
        }
    }

    public boolean verifyLimitNow(JSONObject json) throws JSONException {
        int cost;
        try {
            cost = json.getJSONObject("data").getJSONObject("rateLimit").getInt("cost");
            int remaining = json.getJSONObject("data").getJSONObject("rateLimit").getInt("remaining");
            return remaining < cost;
        } catch (JSONException ex) {
            Logger.getLogger(RequestService.class.getName()).log(Level.SEVERE, null, ex);
            throw new JSONException("Rate limit not found.");
        }

    }
}
