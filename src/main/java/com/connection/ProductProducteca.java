package com.connection;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ProductProducteca {
	
	private String id;
	private String sku;
	private String name;
	private String barcode;
	private String category;
	
	private List<StockProducteca> stock;
	private List<PriceProducteca> prices;
	
	public String getProduct(ProductecaConection producteca, String productId) throws Exception {
		Request request = new Request.Builder()
				  .url(producteca.getUrl() + "/products/" + productId)
				  .method("GET", null)
				  .addHeader("x-api-key", producteca.getApiKey())
				  .addHeader("Authorization", "Bearer " + producteca.getBearer())
				  .addHeader("Cookie", "express:sess=eyJwYXNzcG9ydCI6e319; express:sess.sig=6u4cyBgFwn9GiR5JKZzuFwXZz8k")
				  .build();
		
		String response = producteca.sendRequest(request);
		
		return response;
	}
	
	public String create(ProductecaConection producteca, ProductProducteca product) throws Exception {		
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, new Gson().toJson(product).toString());
		Request request = new Request.Builder()
				  .url(producteca.getUrl() + "/products/synchronize")
				  .method("POST", body)
				  .addHeader("x-api-key", producteca.getApiKey())
				  .addHeader("createifitdoesntexist", "true")
				  .addHeader("Authorization", "Bearer " + producteca.getBearer())
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		String response = producteca.sendRequest(request);				
		if(response.contains(product.getSku()))
			return new Gson().fromJson(response, ProductProducteca.class).getId();
		else
			throw new Exception("Error al crear producto con SKU: " + product.getSku() + "\n" + response);
	}
	
	public String postProperty(ProductecaConection producteca) throws Exception {		
		MediaType mediaType = MediaType.parse("application/json");
		RequestBody body = RequestBody.create(mediaType, new Gson().toJson(this));
		Request request = new Request.Builder()
				  .url(producteca.getUrl() + "/products/synchronize")
				  .method("POST", body)
				  .addHeader("x-api-key", producteca.getApiKey())
				  .addHeader("Authorization", "Bearer " + producteca.getBearer())
				  .addHeader("Content-Type", "application/json")
				  .build();
		
		String response = producteca.sendRequest(request);
		Boolean responseOk = Boolean.FALSE;
		try{
			JsonArray respArray = new Gson().fromJson(response, JsonArray.class);
			Iterator<JsonElement> it = respArray.iterator();
			while(it.hasNext()){
				JsonElement jsElem = it.next();
				String property = "product";
				JsonElement element = searchProp(jsElem, property);
				responseOk = element.getAsJsonObject().get("updated").getAsBoolean();
				if(responseOk) break;
			}
		}catch (Exception e){
		}
		return responseOk?null:response;
	}

	private JsonElement searchProp(JsonElement jsElem, String property) {
		if(existProp(jsElem)) return jsElem.getAsJsonObject().get(property);
		else searchProp(jsElem, property);
		return null;
	}

	private boolean existProp(JsonElement jsElem){
		return (jsElem != null && !jsElem.isJsonNull());
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<StockProducteca> getStocks() {
		if(stock == null)
			return new LinkedList<StockProducteca>();
		else
			return stock;
	}

	public void setStocks(List<StockProducteca> stock) {
		this.stock = stock;
	}

	public List<PriceProducteca> getPrices() {
		if(prices == null)
			return new LinkedList<PriceProducteca>();
		else
			return prices;
	}

	public void setPrices(List<PriceProducteca> prices) {
		this.prices = prices;
	}


}
