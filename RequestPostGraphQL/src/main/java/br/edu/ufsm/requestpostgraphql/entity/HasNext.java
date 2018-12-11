/*
 */
package br.edu.ufsm.requestpostgraphql.entity;

import lombok.Data;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Douglas Giordano
 */
@Data
public class HasNext {

    private boolean hasNext;
    private String endCursor;

    public HasNext(boolean hasNext, String endCursor) {
        this.hasNext = hasNext;
        this.endCursor = endCursor;
    }

    public static HasNext getPullEndCursor(JSONObject obj) throws JSONException {
        boolean hasNextPage = obj.getJSONObject("data").getJSONObject("repository").getJSONObject("pullRequests").getJSONObject("pageInfo").getBoolean("hasNextPage");
        String endCursor = null;
        endCursor = obj.getJSONObject("data").getJSONObject("repository").getJSONObject("pullRequests").getJSONObject("pageInfo").getString("endCursor");

        return new HasNext(hasNextPage, endCursor);
    }

    public static HasNext getIssueEndCursor(JSONObject obj) throws JSONException {
        boolean hasNextPage = obj.getJSONObject("data").getJSONObject("repository").getJSONObject("issues").getJSONObject("pageInfo").getBoolean("hasNextPage");
        String endCursor = null;
        endCursor = obj.getJSONObject("data").getJSONObject("repository").getJSONObject("issues").getJSONObject("pageInfo").getString("endCursor");
        return new HasNext(hasNextPage, endCursor);
    }
}
