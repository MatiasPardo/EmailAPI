package com.producteca.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class Producto {
    
    @Id
    private String id;
    String nombre;
    String codigo;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
	private UnidadMedida unidadmedida;
	

}