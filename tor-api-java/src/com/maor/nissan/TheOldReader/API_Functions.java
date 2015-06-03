package com.maor.nissan.TheOldReader;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

public class API_Functions {
	final String USER_AGENT = "Mozilla/5.0";
	final String DOMAIN_URL = "https://theoldreader.com";
	final String API_URL 	= "/reader/api/0";
	
	private String token = "";
	private String response = "";

	
	public API_Functions(String email, String password) throws Exception {
		this.token = getToken(email, password).substring(22);
	}
	
	public String toString() {
		return "Token is: " + this.token + "\n"
				+ "Response is: " + this.response;
	}
	
	public String getToken(String email, String password) throws Exception {
		String dataToSend = "&Email=" + email + ""
				+ "&Passwd=" + password + "&client=snufknn&service=reader&accountType=HOSTED";
		HttpsURLConnection con = sendRequest(DOMAIN_URL + API_URL + "/accounts/ClientLogin", "POST", dataToSend );
		return getResponse(con);
	}
	
	public String getUserInfo() throws Exception {
		HttpsURLConnection con = sendRequest(DOMAIN_URL + API_URL + "/user-info?output=json", "GET", this.token );
		String[] value = {"userId", "userName", "userProfileId", "userEmail", "isBloggerUser", "signupTimeSec", "isMultiLoginEnabled", "isPremium"};
		return jsonToString(getResponse(con), value);
	}
	
	public String getPreferencesList() throws Exception {
		HttpsURLConnection con = sendRequest(DOMAIN_URL + API_URL + "/preference/list?output=json", "GET", this.token );
		return getResponse(con);
	}
	
	public String getFoldersTagList() throws Exception {
		HttpsURLConnection con = sendRequest(DOMAIN_URL + API_URL + "/tag/list?output=json", "GET", this.token );
		String[] value = {"sortid", "id"};
		String[] objects = {"tags"};
		return jsonToStringArray(getResponse(con), objects, value);
		//return getResponse(con);
	}
	
	public String getSubscriptionsUnreadCount() throws Exception{
		HttpsURLConnection con = sendRequest(DOMAIN_URL + API_URL + "/unread-count?output=json", "GET", this.token );
		String[] value = {"id", "count", "newestItemTimestampUsec"};
		String[] objects = {"unreadcounts"};
		return jsonToStringArray(getResponse(con), objects, value);
	}
	
	public String getSubscriptionsList() throws Exception{
		HttpsURLConnection con = sendRequest(DOMAIN_URL + API_URL + "/subscription/list?output=json", "GET", this.token );
		String[] value = {"id", "title", "sortid", "firstitemmsec", "url", "htmlUrl", "iconUrl"};
		String[] objects = {"subscriptions"};
		return jsonToStringArray(getResponse(con), objects, value);
	}
	
	public String getItemsIdsAll() throws Exception {
		HttpsURLConnection con = sendRequest(DOMAIN_URL + API_URL + "/stream/items/ids?output=json&s=user/-/state/com.google/reading-list", "GET", this.token );
		String[] value = {"id", "directStreamIds", "timestampUsec"};
		String[] objects = {"itemRefs"};
		return jsonToStringArray(getResponse(con), objects, value);
	}
	
	public String getItemsIdsRead() throws Exception {
		HttpsURLConnection con = sendRequest(DOMAIN_URL + API_URL + "/stream/items/ids?output=json&s=user/-/state/com.google/read", "GET", this.token );
		String[] value = {"id", "directStreamIds", "timestampUsec"};
		String[] objects = {"itemRefs"};
		return jsonToStringArray(getResponse(con), objects, value);
	}
	
	public String getItemsIdsStarred() throws Exception {
		HttpsURLConnection con = sendRequest(DOMAIN_URL + API_URL + "/stream/items/ids?output=json&s=user/-/state/com.google/starred", "GET", this.token );
		String[] value = {"id", "directStreamIds", "timestampUsec"};
		String[] objects = {"itemRefs"};
		return jsonToStringArray(getResponse(con), objects, value);	
	}
	
	public String getItemsIdsLiked() throws Exception {
		HttpsURLConnection con = sendRequest(DOMAIN_URL + API_URL + "/stream/items/ids?output=json&s=user/-/state/com.google/like", "GET", this.token );
		String[] value = {"id", "directStreamIds", "timestampUsec"};
		String[] objects = {"itemRefs"};
		return jsonToStringArray(getResponse(con), objects, value);	
	}
	
	public String getItemsIdsShared() throws Exception {
		HttpsURLConnection con = sendRequest(DOMAIN_URL + API_URL + "/stream/items/ids?output=json&s=user/-/state/com.google/broadcast", "GET", this.token );
		String[] value = {"id", "directStreamIds", "timestampUsec"};
		String[] objects = {"itemRefs"};
		return jsonToStringArray(getResponse(con), objects, value);	
	}
	
	public String getItemsIdsCommented() throws Exception {
		HttpsURLConnection con = sendRequest(DOMAIN_URL + API_URL + "/stream/items/ids?output=json&s=user/-/state/com.google/broadcast-friends-comments", "GET", this.token );
		String[] value = {"id", "directStreamIds", "timestampUsec"};
		String[] objects = {"itemRefs"};
		return jsonToStringArray(getResponse(con), objects, value);	
	}
	
	public String getItemsIdsSharedByFriends() throws Exception {
		HttpsURLConnection con = sendRequest(DOMAIN_URL + API_URL + "/stream/items/ids?output=json&s=user/-/state/com.google/broadcast-friends", "GET", this.token );
		String[] value = {"id", "directStreamIds", "timestampUsec"};
		String[] objects = {"itemRefs"};
		return jsonToStringArray(getResponse(con), objects, value);	
	}
	
	public String getItemsIdsFolder() throws Exception {
		HttpsURLConnection con = sendRequest(DOMAIN_URL + API_URL + "/stream/items/ids?output=json&s=user/-/label", "GET", this.token );
		String[] value = {"id", "directStreamIds", "timestampUsec"};
		String[] objects = {"itemRefs"};
		return jsonToStringArray(getResponse(con), objects, value);	
	}
	
	public String getItemContents(String id) throws Exception{
		HttpsURLConnection con = sendRequest(DOMAIN_URL + API_URL + "/stream/items/contents?output=json", "POST", "i=" + id);
		String[] value = {"direction", "id", "title", "description", "self", "alternate", "updated", "items"};
		return jsonToString(getResponse(con), value);
	}
	
	private HttpsURLConnection sendRequest(String url, String method, String data) throws Exception {	
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setRequestMethod(method);
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		if(!this.token.isEmpty())
			con.setRequestProperty("Authorization", " GoogleLogin auth=" + this.token);
		con.setDoOutput(true);
		if(method.equals("POST")){
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(data);
			wr.flush();
			wr.close();
			}
		return con;
	}
	
	private String getResponse(HttpsURLConnection con) throws IOException {
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}
	
	private String jsonToStringArray(String response, String[] object, String[] value) {
		String responseString = "";
		JSONArray itemArr = new JSONArray();
		JSONObject items = new JSONObject(response);
		for(int k=0; k < object.length; k++)
			 itemArr = items.getJSONArray(object[k]);
				for(int i=0; i < itemArr.length(); i++) 
					for(int j=0; j < value.length; j++)
						responseString +="\n" + value[j] + " = "+ (String)itemArr.getJSONObject(i).get(value[j]).toString();
		return responseString;
	}	
	
	private String jsonToString(String response, String[] value) {
		String responseString = "";
		JSONObject items = new JSONObject(response);
		for(int i=0; i < items.length(); i++)
			responseString +="\n" + value[i] + " = " + items.get(value[i]).toString();
		return responseString;
	}
}