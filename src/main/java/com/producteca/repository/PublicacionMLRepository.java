package com.producteca.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.connection.DataProducteca;
import com.producteca.model.Configuracionmercadolibre;
import com.producteca.model.PublicacionML;

@Repository
public interface PublicacionMLRepository extends JpaRepository<PublicacionML,String>{

    @Query(value="SELECT * FROM "+DataProducteca.SCHEMA+".PublicacionML where configuracionecommerce_id = ?1 ", nativeQuery = true)
    List<PublicacionML> publications(String configuracionEcommerce);

    @Query(value="select sum(i.disponible) from "+DataProducteca.SCHEMA+".inventario i where i.deposito_id = ?1 and i.producto_id = ?2", nativeQuery = true)
    BigDecimal searchStockForId(String deposito_id, String producto_id);

    @Query(value="select p.nombre, p.codigo, p.unidadmedida_id from "+DataProducteca.SCHEMA+".producto p where p.id = ?1", nativeQuery = true)
    List<Object> nameProduct(String id);

    @Query(value = "select p.importe from "+DataProducteca.SCHEMA+".precio p where p.listaprecio_id = ?1 and " + 
                                                                                  "p.producto_id = ?2 and " +
                                                                                  "p.unidadmedida_id = ?3 and " +
                                                                                  "(p.porCantidad = false or " +
                                                                                  "(p.desde <= ?4 and p.hasta >= ?4))", nativeQuery = true)
    BigDecimal searchPriceForId(String listPrice_id, String product_id, String unitMeasure_id, BigDecimal quantity);

    @Query(value="select * from "+DataProducteca.SCHEMA+".producto p where p.id = ?1", nativeQuery = true)
    Configuracionmercadolibre nameProduct2(String id);

}
