package com.project.carwash.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name="tb_usuario")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cod_usu")
	private int codigo;
	
	@Column(name="login")
	private String login;
	
	@Column(name = "password")
	private String clave;
	
	@ManyToOne
	@JoinColumn(name="idrol")
	private Rol rol;
	
	/*@OneToMany(mappedBy = "usuario")*/
	/*private List<Reserva> reservas;*/
	/*private List<Empleado> listaUsuarios;*/
	
	@JsonIgnore
	@OneToMany(mappedBy = "usuario")
	private List<Carrito> listaCarrito;
	
	

	public List<Carrito> getListaCarrito() {
		return listaCarrito;
	}

	public void setListaCarrito(List<Carrito> listaCarrito) {
		this.listaCarrito = listaCarrito;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}


}
