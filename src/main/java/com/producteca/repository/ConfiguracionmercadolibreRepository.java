package com.producteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.producteca.model.Configuracionmercadolibre;

@Repository
public interface ConfiguracionmercadolibreRepository extends JpaRepository<Configuracionmercadolibre,String>{
    
}
