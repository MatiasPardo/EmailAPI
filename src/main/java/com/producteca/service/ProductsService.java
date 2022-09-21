package com.producteca.service;


import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.connection.DataProducteca;
import com.connection.ProductProducteca;
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
	ConfiguracionmercadolibreRepository configuracionmercadolibreRepository;

    @Async
    public void update(DataProducteca producteca) throws Exception{
        Logger logger = Logger.getLogger("############ Producteca ############");
        logger.setLevel(Level.INFO);
        List<Configuracionmercadolibre> cons = configuracionmercadolibreRepository.searchConfiguration(producteca.getConfiguracionMercadoLibre_id());
        final Configuracionmercadolibre config = (Configuracionmercadolibre) cons.get(0);
        if(config != null){
            List<PublicacionML> publications = publicacionMLRepository.publications(config.getId());
            publications.stream().forEach((publicacion) -> {
                searchAttributesAndCommit(producteca, logger, config, publicacion);
            });
        }else{
            logger.info("No se encontro el configurador con id:" + producteca.getConfiguracionMercadoLibre_id());
        }
        
    }

    private void searchAttributesAndCommit(DataProducteca producteca, Logger logger, Configuracionmercadolibre confInit, PublicacionML publicacion) {
        List<Object> dataProduct = publicacionMLRepository.dataProduct(publicacion.getProducto().getId());
        Producto product = new Producto();
        UnidadMedida unit = new UnidadMedida();
        ProductProducteca productRequest = new ProductProducteca();

        product.setId(publicacion.getProducto().getId());
        product.setNombre((String) ((Object[]) dataProduct.get(0))[0]);
        product.setCodigo((String) ((Object[]) dataProduct.get(0))[1]);
        unit.setId((String) ((Object[]) dataProduct.get(0))[2]);
        product.setUnidadmedida(unit);
        publicacion.setProducto(product);
        publicacion.setRespuestaactualizacion("");
		productRequest.setSku(publicacion.getIdmercadolibre());
        producteca.setProductRequest(productRequest);
        producteca.assignPrice(logger, publicacion, confInit, product, publicacionMLRepository);
        producteca.assignStock(logger, publicacion, confInit, product, publicacionMLRepository);
        producteca.preparePostProductecaSaveTransaction(publicacion, producteca, publicacionMLRepository);
        logger.info("Actualizado: " + (publicacion.getRespuestaactualizacion() == null));
       
    }
}
