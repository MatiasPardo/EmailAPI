package com.producteca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.connection.DataProducteca;
import com.producteca.model.Configuracionmercadolibre;

@Repository
public interface ConfiguracionmercadolibreRepository extends JpaRepository<Configuracionmercadolibre, String>{

    @Query(value="select * from "+DataProducteca.SCHEMA+".configuracionmercadolibre p where p.id = ?1",nativeQuery = true)
    List<Configuracionmercadolibre> searchConfiguration(String id);
    
}
