package com.producteca.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.connection.DataProducteca;

import lombok.Getter;
import lombok.Setter;

@Table(schema = DataProducteca.SCHEMA)
@Entity
@Getter
@Setter
public class PublicacionML {
    public final static String a = "";

	@Id
    String id;
    Ecommerce tipoecommerce;
	Date fechaactualizacionprecio;
	Date fechaactualizacionstock;
	String usuarioactualizacionstock;
	String usuarioactualizacionprecio;
	BigDecimal ultimoprecio;
	BigDecimal ultimostock;
	String idmercadolibre;
	String idproducto;
	String respuestaactualizacion;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	private Configuracionmercadolibre configuracionecommerce;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	private Producto producto;

	public String getRespuestaactualizacion(){
		return this.respuestaactualizacion != null ? respuestaactualizacion : "";
	}

	public void setRespuestaactualizacion(String respuesta){
		this.respuestaactualizacion = (respuesta.length() > 255) ?  respuesta.substring(0, 255) : respuesta;
	}

}

