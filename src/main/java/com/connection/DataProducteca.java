package com.connection;

import java.util.logging.Logger;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import org.springframework.stereotype.Component;

import com.producteca.model.Configuracionmercadolibre;
import com.producteca.model.Producto;
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

	ProductProducteca productRequest = null;

	//@Scheduled(cron = "*/15 * * * * *")
	public void automatic(){
		Logger logger = Logger.getLogger("Products");
        logger.setLevel(Level.INFO);
		logger.info("Tarea automatica");
	}

	@Transactional
	public void preparePostProductecaSaveTransaction(PublicacionML publicacion, DataProducteca producteca, PublicacionMLRepository repoML) {
		ProductecaConection conexion = new ProductecaConection(producteca.getSecretKey(), producteca.getAppId(), producteca.getUrlEcommerce());
		String response = null;

		try {
			response = productRequest.postProperty(conexion);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(response != null){
			publicacion.setRespuestaactualizacion(response + publicacion.getRespuestaactualizacion());
		}
//		savePublication(publicacion, repoML);
		repoML.save(publicacion); 
	}

	private void savePublication(PublicacionML publicacion, PublicacionMLRepository repoML) {
		if(publicacion.getRespuestaactualizacion().length() > 0){
			repoML.saveRespuestaError(publicacion.getId(), publicacion.getRespuestaactualizacion());
		}else{
			if(this.getStock())
				repoML.saveStockOk(publicacion.getUltimostock(), publicacion.getFechaactualizacionstock(), publicacion.getId());
			if(this.getPrice())
				repoML.savePriceOk(publicacion.getUltimoprecio(), publicacion.getFechaactualizacionprecio(), publicacion.getId());
		}
	}


	public void assignPrice(Logger logger, PublicacionML publicacion, Configuracionmercadolibre confInit, Producto product, PublicacionMLRepository publicacionMLRepository) {
		if(this.getPrice()){
			BigDecimal price = publicacionMLRepository.searchPriceForId(confInit.getListaprecio().getId(), 
																		product.getId(), 
																		product.getUnidadmedida().getId(),
																		new BigDecimal(1)); 
			if(price == null){
				publicacion.setRespuestaactualizacion(publicacion.getRespuestaactualizacion() + " No se encontro precio definido");
			}else{
				List<PriceProducteca> prices = new LinkedList<PriceProducteca>();
				PriceProducteca retailPrice = new PriceProducteca();
				publicacion.setUsuarioactualizacionprecio(DataProducteca.SYSTEM);
				publicacion.setFechaactualizacionprecio(new Date());
				retailPrice.setAmount(price.intValue());
				retailPrice.setCurrency("Local");
				retailPrice.setPriceList("Default");
				prices.add(retailPrice);
				productRequest.setPrices(prices);
			}

		}

	}

    public void assignStock(Logger logger, PublicacionML publicacion, Configuracionmercadolibre confInit, Producto product, PublicacionMLRepository publicacionMLRepository) {
		if(this.getStock()){
			BigDecimal stock = publicacionMLRepository.searchStockForId(confInit.getStockmercadolibre().getId(), publicacion.getProducto().getId());
			if(stock == null){
				publicacion.setRespuestaactualizacion(publicacion.getRespuestaactualizacion() + " No se encontro stock definido");
			}else{
				List<StockProducteca> stocks = new LinkedList<StockProducteca>();
				StockProducteca stockProd = new StockProducteca();
				publicacion.setUsuarioactualizacionstock(DataProducteca.SYSTEM);
				publicacion.setFechaactualizacionstock(new Date());
				stockProd.setAvailableQuantity(stock.intValue());
				stockProd.setQuantity(publicacion.getUltimostock().intValue());
				stockProd.setWarehouse("Default");
				stocks.add(stockProd);
				productRequest.setStocks(stocks);
				logger.info("Product codigo: " +   product.getCodigo()+ " Stock: " + stock);
			}
		}
    }
}