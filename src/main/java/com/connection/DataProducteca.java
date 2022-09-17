package com.connection;

import java.util.logging.Logger;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import org.springframework.stereotype.Component;

import com.producteca.model.PublicacionML;
import com.producteca.repository.PublicacionMLRepository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
public class DataProducteca {
	
	public final static String SCHEMA = "morapanty";
	public static final String SYSTEM = "SYSTEM";

	@NotBlank
	String configuracionMercadoLibre_id;

	@NotBlank
	String esquema;

	@NotBlank
	String appId;

	@NotBlank
	String secretKey;

	@NotBlank
	String urlEcommerce;

	String idStock;

	@NotNull
	Boolean price;

	@NotNull
	Boolean stock;

	//@Scheduled(cron = "*/15 * * * * *")
	public void automatic(){
		Logger logger = Logger.getLogger("Products");
        logger.setLevel(Level.INFO);
		logger.info("Haciendo algo");
	}

	@Transactional
	public Boolean preparePostProductecaSaveTransaction(PublicacionML publicacion, DataProducteca producteca, PublicacionMLRepository repoML) {
		ProductecaConection conexion = new ProductecaConection(producteca.getSecretKey(), producteca.getAppId(), producteca.getUrlEcommerce());
		ProductProducteca product = new ProductProducteca();
		product.setSku(publicacion.getIdmercadolibre());
		
		List<StockProducteca> stocks = new LinkedList<StockProducteca>();
		StockProducteca stock = new StockProducteca();
		stock.setAvailableQuantity(publicacion.getUltimostock().intValue());
		stock.setQuantity(publicacion.getUltimostock().intValue());
		stock.setWarehouse("Default");
		stocks.add(stock);
	
		product.setStocks(stocks);
		
		Boolean updated = Boolean.FALSE;
		try {
			updated = product.update(conexion, product);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(updated){
			repoML.save(publicacion);
		}
		return updated;
	}
}