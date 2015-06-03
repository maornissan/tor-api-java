package com.maor.nissan.TheOldReader;

public class Main {

	public static void main(String[] args) throws Exception {
		API_Functions functions = new API_Functions("USER", "PASS");
		//System.out.println(functions.toString());
		//System.out.println(functions.getUserInfo().toString());
		//System.out.println(functions.getSubscriptionsUnreadCount().toString());
		//System.out.println(functions.getFoldersTagList().toString());
		//System.out.println(functions.getSubscriptionsList().toString());
		//System.out.println(functions.getItemsIdsFolder().toString());
		System.out.println(functions.getItemContents("556d8aab7eb3dbc59500adc1").toString());
	}

}
