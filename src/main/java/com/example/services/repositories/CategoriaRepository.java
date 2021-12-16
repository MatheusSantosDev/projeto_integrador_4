package com.example.services.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.services.Model.Categoria;


public interface CategoriaRepository extends JpaRepository<Categoria,Long> {

	
}
