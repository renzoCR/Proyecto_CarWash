package com.project.carwash.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.carwash.services.BoletaServices;
import com.project.carwash.services.SedeServices;

@Controller
@RequestMapping("/boleta")
public class BoletaController{
	@RequestMapping("/form")
	public String index (Model model) { //model interface{
		return "boleta";
	}
	
	@Autowired
	private SedeServices sedeServices;
		
	@Autowired
	private BoletaServices boletaServices;
	
	@GetMapping("/boletasXRangoFechas")
	public String buscarBoletasXRangoFechas(Model model) {
		model.addAttribute("sedes", sedeServices.listarSede());
		return "boletaXrango";
	}
	@GetMapping("/boletasXRangoFechass")
	public String buscarBoletasXRangoFechass(
	        @RequestParam("fechaInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
	        @RequestParam("fechaFin") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin,
	        @RequestParam("tipo") int codSede,
	        RedirectAttributes redirect,
	        Model model
	) {
	    try {
	        model.addAttribute("sedes", sedeServices.listarSede());
	        model.addAttribute("boletas", boletaServices.buscarBoletasXRangoDeFecha(fechaInicio, fechaFin, codSede));
	    } catch (Exception e) {
	        redirect.addFlashAttribute("ERROR", "Se produjo un error al buscar boletas en la base de datos.");
	        e.printStackTrace();
	    }

	    return "boletaXrango";
	}}
