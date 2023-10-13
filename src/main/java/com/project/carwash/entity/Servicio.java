package com.project.carwash.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="tbservicio")
public class Servicio {
	@Id
	@GeneratedValue (strategy= GenerationType.IDENTITY)
	@Column(name="codservicio")
	private Integer codigo;
	
	@Column (name="nomservicio")
	private String nombre;
	
	@Column (name="precioservicio")
	private double precio;

	public Servicio() {
		
	}
	public Servicio(String nombre, double precio) {
		super();
		this.nombre = nombre;
		this.precio = precio;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}	
}