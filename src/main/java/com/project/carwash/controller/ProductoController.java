package com.project.carwash.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.carwash.dto.detalle;
import com.project.carwash.entity.Carrito;
import com.project.carwash.entity.Empleado;
import com.project.carwash.entity.Producto;
import com.project.carwash.entity.ProductoCarrito;
import com.project.carwash.entity.TipoProducto;
import com.project.carwash.entity.Usuario;
import com.project.carwash.services.CarritoServices;
import com.project.carwash.services.ProductoServices;
import com.project.carwash.services.TipoProductoServices;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/producto")
public class ProductoController {
	@Autowired
	private ProductoServices pro;
	@Autowired
	private TipoProductoServices tipo;
	@Autowired
	private CarritoServices carro;

	public List<detalle> listaDetalle= new ArrayList<>();
	public List<Producto> productoFiltrado= new ArrayList<>();
	public TipoProducto tipoProducto;
	
	@RequestMapping("/lista")
	public String index(Model model) {
		model.addAttribute("productos", pro.listaProductos());
		model.addAttribute("tipo", tipo.listaTipo());
		return "MenuProducto";
	}
	@RequestMapping("/cambiarMenu")
	public String MenuCategoria(@RequestParam("codigoEnlace") Integer cod) {
		tipoProducto= tipo.buscarPorId(cod);
	    if (tipoProducto != null) {
		        List<Producto> listaProductos = pro.listaProductos();
		        List<Producto> lizta = new ArrayList<>();

		        for (Producto producto : listaProductos) {
		            if (producto.getTipoProducto().getCodigo().equals(cod)) {
		            		lizta.add(producto);
		            }
		        }
		         if(productoFiltrado==null) {
		        	productoFiltrado.addAll(lizta);
		        }else {
		        	productoFiltrado.clear();
		        	productoFiltrado.addAll(lizta);
				}
		        return "redirect:/producto/tipoProducto";
	    	}
	     return "redirect:/producto/lista"; 
	  }
	@RequestMapping("/tipoProducto")
	public String NuevoListado(Model model) {
		model.addAttribute("tipo", tipo.listaTipo());
		model.addAttribute("productosFiltrados", productoFiltrado);
		return "CambiarMenu";
	}
	/*añadir al carrito*/
	@RequestMapping("/buscar")
	@ResponseBody
	public Producto buscar(@RequestParam("codigo") int cod,
	        @RequestParam("nombre") String nombre,
	        @RequestParam("cantidad") int cantidad,
	        @RequestParam("precio") double precio) {
	    
		detalle det = new detalle();
	    det.setCodigo(cod);
	    det.setNombre(nombre);
	    det.setCantidad(cantidad);
	    det.setPrecio(precio);
	    det.setPrecioTotal(precio*cantidad);
	    boolean detalleE = false;

	    if (listaDetalle != null) {
	        for (detalle detalleExistente : listaDetalle) {
	            if (detalleExistente.getCodigo() == cod) {
	                System.out.println("Producto duplicado, no se agrega a la lista");
	                detalleE = true;
	                break;
	            }
	        }
	    }
	    if (!detalleE) {
	    	listaDetalle.add(det);
	        System.out.println("Número de productos agregados a la lista: " + listaDetalle.size());
	    }
	    return pro.buscarPorId(cod);
	}
	@RequestMapping("/resumen")
	public String mostrarCarrito(Model model){
			if(listaDetalle!=null) {
				model.addAttribute("detalle",listaDetalle);
				return "resumen";
			}else {
				return "resumenVacio";
			}
	}
	
	@RequestMapping("/modificarCantidad")
	@ResponseBody
	public detalle modificarCantidad( @RequestParam("codigo") int cod,
	        @RequestParam("cantidadModificada") int cantidadModificada){
		
		double precioTotal;
		detalle detalleObjeto = new detalle();
		for(detalle det:listaDetalle) {
			if(det.getCodigo()==cod) {
				precioTotal= det.getPrecio()*cantidadModificada;
			    det.setCantidad(cantidadModificada);
				det.setPrecioTotal(precioTotal);
				detalleObjeto= det;
			}
		}
		return detalleObjeto;
	}
	/*eliminar productoz del rezumen carrito*/
	@RequestMapping("/eliminar")
	public String eliminar(@RequestParam("codigo") int codigo,
			RedirectAttributes redirect) {
		detalle detalleEliminado  = new detalle();
		for(detalle d: listaDetalle) {
			if(d.getCodigo()==codigo) {
				detalleEliminado=d;
			}
		}
		listaDetalle.remove(detalleEliminado);
		redirect.addFlashAttribute("MENSAJE","Producto eliminado");
		return "redirect:/producto/resumen";
	}
	/*fializar compra*/
	@RequestMapping("/grabar")
	public String grabar(HttpServletRequest request,RedirectAttributes redirect){
		try {
			Carrito bean=new Carrito();
			//setear
			//carrito tieme fecha, codusu
			LocalDate fecha= LocalDate.now();
			bean.setFecha(fecha);
			Usuario usu=new Usuario();
			int cod=(int)request.getSession().getAttribute("CODIGOUSUARIO");
			
			usu.setCodigo(cod);
			bean.setUsuario(usu);
	
			List<ProductoCarrito> listaProductoCarrito= new ArrayList<ProductoCarrito>();
			//bucle
			for(detalle det:listaDetalle) {
				//crear objeto de la entidad BienRequerimiento
				ProductoCarrito br=new ProductoCarrito();
				br.setProducto(new Producto(det.getCodigo()));
				br.setCantidad(det.getCantidad());
				listaProductoCarrito.add(br);
			}
	        System.out.println("Número de productos agregados a la listaProductoCarrito : " + listaProductoCarrito.size());

			//enviar detalles dentor de bean
			bean.setListaCarrito(listaProductoCarrito);
			
			carro.saveRequerimiento(bean);
			listaDetalle.clear();
			redirect.addFlashAttribute("MENSAJE","Compra  exitosa");

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/producto/lista";
	}
	
}
