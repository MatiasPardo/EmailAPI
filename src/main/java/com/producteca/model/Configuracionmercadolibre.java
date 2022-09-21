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
public class Configuracionmercadolibre {
    
    @Id
    String id;
    Ecommerce ecommerce;
	String urlecommerce;
	Integer horastokenvalido;
    String accesstoken;
    String appid;
	String secretkey;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
	private ListaPrecio listaprecio;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
	private Deposito stockmercadolibre;

}
