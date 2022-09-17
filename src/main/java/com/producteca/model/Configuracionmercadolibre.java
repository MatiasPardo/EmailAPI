package com.producteca.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(schema = "morapanty")
@Getter
@Setter
public class Configuracionmercadolibre {
    
    @Id
    String id;
    Ecommerce ecommerce;
    @Column(name = "urlecommerce")
	String urlEcommerce;
    @Column(name = "horastokenvalido")
	Integer horastokenValido;
    @Column(name = "accesstoken")
    String accessToken;
    @Column(name = "appid")
    String appId;
    @Column(name = "secretkey")
	String secretKey;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
	private ListaPrecio listaprecio;

    @ManyToOne(optional=false, fetch=FetchType.LAZY)
	private Deposito stockmercadolibre;

}
