package com.example.services.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.services.Model.Produto;



public interface ProdutoRepository extends JpaRepository<Produto,Long>{

}
