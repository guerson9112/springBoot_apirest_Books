package com.company.books.backend.services;

import org.springframework.http.ResponseEntity;

import com.company.books.backend.response.LibroResponseRest;


public interface ILibroService {
	
	public ResponseEntity<LibroResponseRest> buscarLibros();
	public ResponseEntity<LibroResponseRest> buscarPorId(Long id);
}
