package com.company.books.backend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.books.backend.model.Libro;
import com.company.books.backend.model.dao.ILibroDao;
import com.company.books.backend.response.LibroResponseRest;

@Service
public class LibroServiceImpl implements ILibroService{

	private static final Logger log = LoggerFactory.getLogger(LibroServiceImpl.class);
	
	@Autowired 
	private ILibroDao libroDao;
	
	@Override
	@Transactional(readOnly=true)
	public ResponseEntity<LibroResponseRest>  buscarLibros() {
		
		log.info( "inicio de metodo buscarLibros()" );
		
		LibroResponseRest response = new LibroResponseRest();
		
		try {
			
			List<Libro> libro = (List<Libro>) libroDao.findAll();
			response.getLibroResponse().setLibro(libro);
			response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
			
		}catch (Exception e) {
			response.setMetadata("Respuesta noOk", "-1", "Error al consultar libros");
			log.error("error al consultar libros", e.getMessage());
			e.getStackTrace();
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR ); // devuelve 500
		}
		
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK ); // devuelve 200
		
	}

	@Override
	@Transactional(readOnly=true)
	public ResponseEntity<LibroResponseRest> buscarPorId(Long id) {
		log.info( "inicio de metodo buscar libro  PorId()" );
		LibroResponseRest response = new LibroResponseRest();
		List<Libro> list = new ArrayList<>();
		
		try { 
			log.info( "buscando Por Id()" );
			Optional<Libro> libro = libroDao.findById(id);
			if(libro.isPresent()) {
				list.add(libro.get());
				response.getLibroResponse().setLibro(list);
				
			}else {
				log.error("Error al consultar el libro");
				response.setMetadata("Respuesta nok", "-1", "libro no encontradao");
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		}catch ( Exception e ) {
			
			log.error("Error al consultar el libro");
			response.setMetadata("Respuesta nok", "-1", "libro no encontradao");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			
		}
		response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);
		
	}

	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> crear(Libro libro) {
		log.info( "inicio de metodo crear libro" );
		LibroResponseRest response = new LibroResponseRest();
		List<Libro> list = new ArrayList<>();
		
		try {
			
			Libro libroguardado = libroDao.save(libro);
			if(libroguardado  != null ) {
				list.add(libroguardado);
				response.getLibroResponse().setLibro(list);
			} else {
				log.error("Error al crear la catogoria");
				response.setMetadata("Respuesta nok", "-1", "Libro no creada");
				return new ResponseEntity<LibroResponseRest> (response, HttpStatus.BAD_REQUEST ); 
			}
			
		}catch (Exception e) {
			log.error("Error al crear el libro");
			response.setMetadata("Respuesta nok", "-1", "Error al crear libro");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR ); 
		}
		
		response.setMetadata("Respuesta ok", "00", "libro creado");
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK ); // devuelve 200
		
	}

	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> actualizar(Libro libro, Long id) {
		log.info( "inicio de metodo actualizar libro" );
		LibroResponseRest response = new LibroResponseRest();
		List<Libro> list = new ArrayList<>();
		
		Optional<Libro> libroBuscado = libroDao.findById(id);
		
		try { 
			
			if(libroBuscado.isPresent()) {
				libroBuscado.get().setNombre(libro.getNombre());
				libroBuscado.get().setDescripcion(libro.getDescripcion());
				libroBuscado.get().setCategoria(libro.getCategoria() );
				Libro libroActualizar = libroDao.save(libroBuscado.get());
				
				if( libroActualizar != null ) {
					response.setMetadata("Respuesta ok", "00", "Libro actualizado");
					list.add(libroActualizar);
					response.getLibroResponse().setLibro(list);
				}else {
					log.error("Error al actualizar el libro");
					response.setMetadata("Respuesta nok", "-1", "libro no actualizado");
					return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR );
				}
				
				
			} else {
				log.error("Error al actualizar el libro");
				response.setMetadata("Respuesta nok", "-1", "Libro no actualizado");
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.NOT_FOUND ); 
			}
			
		}catch (Exception e) {
			log.error("Error al actualizar el libro", e.getMessage());
			e.getStackTrace();
			response.setMetadata("Respuesta nok", "-1", "libro no actualizado");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR ); 
		}
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK ); // devuelve 200
	}

	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> eliminar(Long id) {
		log.info( "inicio de metodo eliminar libro" );
		LibroResponseRest response = new LibroResponseRest();
		
		try {
			//eliminar libro
			libroDao.deleteById(id);
			response.setMetadata("Respuesta ok", "00", "Libro eliminado");
			
		}catch ( Exception e ){
			log.error("Error al eliminar la catogoria", e.getMessage());
			e.getStackTrace();
			response.setMetadata("Respuesta nok", "-1", "Libro no eliminado");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR ); 
		}
		
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK ); // devuelve 200
	}
		
	
}
