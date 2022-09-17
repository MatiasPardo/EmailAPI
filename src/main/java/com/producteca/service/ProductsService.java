package com.producteca.service;


import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.connection.DataProducteca;
import com.producteca.model.Configuracionmercadolibre;
import com.producteca.model.Producto;
import com.producteca.model.PublicacionML;
import com.producteca.model.UnidadMedida;
import com.producteca.repository.ConfiguracionmercadolibreRepository;
import com.producteca.repository.PublicacionMLRepository;


@Service
public class ProductsService {

    @Autowired
	PublicacionMLRepository publicacionMLRepository;
    
    @Autowired
	ConfiguracionmercadolibreRepository configuracionRepository;
	
    
    @Async
    public void update(DataProducteca producteca) throws Exception{
        Logger logger = Logger.getLogger("############ Producteca ############");
        logger.setLevel(Level.INFO);
        List<PublicacionML> publicaciones = publicacionMLRepository.publications(producteca.getConfiguracionMercadoLibre_id());
        Optional<Configuracionmercadolibre> con = configuracionRepository.findById(producteca.getConfiguracionMercadoLibre_id());
        Configuracionmercadolibre conf = null;
        if(!con.isEmpty())
            conf = con.get();
        if(conf != null){
            publicaciones = publicacionMLRepository.publications(conf.getId());
            publicaciones.stream().forEach((publicacion) -> {
                searchAttributesAndCommit(producteca, logger, con, publicacion);
            });
        }
        
    }

    private void searchAttributesAndCommit(DataProducteca producteca, Logger logger, Optional<Configuracionmercadolibre> con,
            PublicacionML publicacion) {
        Configuracionmercadolibre confInit = con.get();
        List<Object> dataProduct = publicacionMLRepository.nameProduct(publicacion.getProducto().getId());
        Producto product = new Producto();
        UnidadMedida unit = new UnidadMedida();
        product.setId(publicacion.getProducto().getId());
        product.setNombre((String) ((Object[]) dataProduct.get(0))[0]);
        product.setCodigo((String) ((Object[]) dataProduct.get(0))[1]);
        unit.setId((String) ((Object[]) dataProduct.get(0))[2]);
        product.setUnidadmedida(unit);
        publicacion.setProducto(product);
        if(producteca.getPrice()){
            BigDecimal price = publicacionMLRepository.searchPriceForId(confInit.getListaprecio().getId(), 
                                                                        product.getId(), 
                                                                        product.getUnidadmedida().getId(),
                                                                        new BigDecimal(1)); 
            publicacion.setUltimoprecio(price);
            publicacion.setUsuarioactualizacionprecio(DataProducteca.SYSTEM);
			publicacion.setFechaactualizacionprecio(new Date());
            logger.info("Product codigo: " +   product.getCodigo()+ " Price: " + price);
        }
        if(producteca.getStock()){
            BigDecimal stock = publicacionMLRepository.searchStockForId(confInit.getStockmercadolibre().getId(), publicacion.getProducto().getId());
            publicacion.setUltimostock(stock);
            publicacion.setUsuarioactualizacionstock(DataProducteca.SYSTEM);
            publicacion.setFechaactualizacionstock(new Date());
            logger.info("Product codigo: " +   product.getCodigo()+ " Stock: " + stock);
        }
        Boolean update = producteca.preparePostProductecaSaveTransaction(publicacion, producteca, publicacionMLRepository);
        logger.info("Actualizado: " + update);
       
    }


}
