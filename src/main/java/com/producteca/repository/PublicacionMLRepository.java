package com.producteca.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.connection.DataProducteca;
import com.producteca.model.PublicacionML;

@Repository
public interface PublicacionMLRepository extends JpaRepository<PublicacionML,String>{

    @Query(value="SELECT * FROM "+DataProducteca.SCHEMA+".PublicacionML where configuracionecommerce_id = ?1 ", nativeQuery = true)
    List<PublicacionML> publications(String configuracionEcommerce);

    @Query(value="select sum(i.disponible) from "+DataProducteca.SCHEMA+".inventario i where i.deposito_id = ?1 and i.producto_id = ?2", nativeQuery = true)
    BigDecimal searchStockForId(String deposito_id, String producto_id);

    @Query(value="select p.nombre, p.codigo, p.unidadmedida_id from "+DataProducteca.SCHEMA+".producto p where p.id = ?1", nativeQuery = true)
    List<Object> dataProduct(String id);

    @Query(value = "select p.importe from "+DataProducteca.SCHEMA+".precio p where p.listaprecio_id = ?1 and " + 
                                                                                  "p.producto_id = ?2 and " +
                                                                                  "p.unidadmedida_id = ?3 and " +
                                                                                  "(p.porcantidad = false or " +
                                                                                  "(p.desde <= ?4 and p.hasta >= ?4))", nativeQuery = true)
    BigDecimal searchPriceForId(String listPrice_id, String product_id, String unitMeasure_id, BigDecimal quantity);

    @Query(value = "update "+DataProducteca.SCHEMA+".publicacionml set ultimostock = ?1 fechaactualizacionstock = ?2 where id = ?3", nativeQuery = true)
    void saveStockOk(BigDecimal stock, Date fechaActualizacion, String id);

    @Query(value = "update "+DataProducteca.SCHEMA+".publicacionml set ultimoprice = ?1 fechaactualizacionprice = ?2 where id = ?3", nativeQuery = true)
    void savePriceOk(BigDecimal price, Date fechaActualizacion, String id);

    @Query(value = "update "+DataProducteca.SCHEMA+".publicacionml set respuestaactualizacion = ?1 where id = ?2", nativeQuery = true)
    void saveRespuestaError(String error, String id);

}
