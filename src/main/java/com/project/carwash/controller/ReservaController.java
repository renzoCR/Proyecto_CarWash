package com.project.carwash.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.project.carwash.entity.DetalleReserva;
import com.project.carwash.entity.DetalleReservaPK;
import com.project.carwash.entity.Reserva;
import com.project.carwash.entity.Servicio;
import com.project.carwash.services.ReservaService;
import com.project.carwash.services.ServicioServices;


import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/reserva")
public class ReservaController {
	
	@Autowired
	private ServicioServices servicioServices;
	
	@Autowired
	private ReservaService reservaService;
	
	@RequestMapping("/lista")
	public String lista(Model model) {
		model.addAttribute("servicios", servicioServices.listarTodo());
		return "Reserva";
	}
	
	@RequestMapping("/adicionar")
	@ResponseBody
	public List<Servicio> adicionar(
			@RequestParam("codigo") int cod,
			@RequestParam("nombre") String nombre,	
			@RequestParam("descripcion") String descripcion,
			@RequestParam("precio") double precio,
			HttpServletRequest request
			) 
	{
		List<Servicio> lista = null;
		
		if (request.getSession().getAttribute("datos") == null ) { 
			lista = new ArrayList<Servicio>(); 
		} else { 
			lista = (List<Servicio>) request.getSession().getAttribute("datos");
		}
		
		Servicio det = new Servicio();
		
		det.setCodigo(cod);
		det.setNombre(nombre);
		det.setDescripcion(descripcion);
		det.setPrecio(precio);
		
		lista.add(det);
		
		request.getSession().setAttribute("datos", lista);
		
		return lista;
	}
	
	@RequestMapping("/eliminar")
	@ResponseBody
	public List<Servicio> eliminar(
			@RequestParam("codigo") int codigo,
			HttpServletRequest request) 
	{
		List<Servicio> lista = (List<Servicio>) request.getSession().getAttribute("datos");
		lista.remove(codigo);
		return lista;
	}
	
	@RequestMapping("/grabar")
	public String grabar(
			@RequestParam("numero") int numero,
			@RequestParam("nombre") String nombre,
			@RequestParam("fecha") String fecha,
			HttpServletRequest request
			) {
		
		Reserva reserva = new Reserva();
		int codigoUsuario = (int) request.getSession().getAttribute("CODIGOUSUARIO");

		reserva.setNumero(numero);
		reserva.setCodUsuario(codigoUsuario);
		reserva.setFecha( LocalDateTime.parse(fecha) );
		reserva.setCodEstado(1);
		
		ArrayList<Servicio> servicios = (ArrayList<Servicio>) request.getSession().getAttribute("datos");
		ArrayList<DetalleReserva> detalle = new ArrayList<>();
		
		for (Servicio servicio : servicios) {
			DetalleReserva det = new DetalleReserva();
			DetalleReservaPK pk = new DetalleReservaPK();
			
			pk.setIdServicio( servicio.getCodigo() );
			pk.setIdReserva( numero );
			
			det.setId(pk);
			detalle.add(det);
		}
		
		reservaService.grabar(reserva, detalle);
		return "redirect:/reserva/lista";
	}

}
