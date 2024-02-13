package com.project.carwash.services;

import java.util.ArrayList;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.carwash.entity.DetalleReserva;
import com.project.carwash.entity.DetalleReservaPK;
import com.project.carwash.entity.Reserva;
import com.project.carwash.repository.DetalleReservaRepository;
import com.project.carwash.repository.ReservaRepository;



@Service
public class ReservaService {
	private final ReservaRepository reservaRepository;
	private final DetalleReservaRepository detalleReservaRepository;
	
	public ReservaService(ReservaRepository reservaRepository, 
			DetalleReservaRepository detalleReservaRepository) {
		
		this.reservaRepository = reservaRepository;
		this.detalleReservaRepository = detalleReservaRepository;
	}
	
	@Transactional
	public void grabar(Reserva reserva, ArrayList<DetalleReserva> detalles) {
		try {
			reservaRepository.save(reserva);
			System.out.println("se ejecuta");
			
			for ( DetalleReserva det : detalles ) {
				detalleReservaRepository.save(det);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
