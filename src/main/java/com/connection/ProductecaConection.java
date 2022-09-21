package com.connection;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@Getter
@Setter
public class ProductecaConection {
	private String bearer;
	private String apiKey;
	private String url;
	
	private List<StockProducteca> stock;
	private List<PriceProducteca> prices;
	
	public ProductecaConection(String bearer, String apiKey, String url) {
		this.bearer = bearer;
		this.apiKey = apiKey;
		this.url = url;
	}
	
	public String sendRequest(Request request) throws Exception {
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Response response = null;
		String responseString = "";
		
		response = client.newCall(request).execute();
		ResponseBody body = response.body();
		try{
			responseString = body.string();
		}finally{
			if(!response.isSuccessful()) {
				throw new Exception("Error " + response.code() + " API Producteca: " + responseString);
			}
			if(body != null){
				body.close();
			}
		}
		return responseString;
	}
	
	
	
	
}
