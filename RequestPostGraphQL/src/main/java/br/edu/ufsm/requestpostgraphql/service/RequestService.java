package br.edu.ufsm.requestpostgraphql.service;

import br.edu.ufsm.requestpostgraphql.entity.Message;
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

    public RequestService() {
        LoadManagement.getInstance().init();
    }

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
                if (o == null || !o.isNull("errors")) {
                    throw new JSONException("");
                }

                if (verifyLimitNow(o)) {
                    changeHashAtive();
                }
            } catch (JSONException ex) {
                LoadManagement.getInstance().setHeavyLoad(true);
                LoadManagement.getInstance().breaking();
                Message.printError(jsonResponse.toString());
                Message.printError("(" + LoadManagement.getInstance().getWeight() + ") decreasing weight...");
                if (LoadManagement.getInstance().isHeavyLoad()) {
                    LoadManagement.getInstance().decrease();
                }
                return null;
            }
            LoadManagement.getInstance().fixing();
            LoadManagement.getInstance().isSucess();
            return o;

        } catch (org.apache.http.NoHttpResponseException ex) {
            Message.printError("NoHttpResponseException: " + ex.getMessage());
        } catch (IOException e) {
            Message.printError(e.getMessage());
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
            return remaining < (cost*2);
        } catch (JSONException ex) {
            Message.printError(json.toString());
            throw new JSONException("changing tokens...");
        }

    }
}
