CREATE TABLE pedido (
   id BIGINT NOT NULL AUTO_INCREMENT,
   cod_mesa BIGINT NOT NULL,
   status VARCHAR(15) NOT NULL,
   hora_do_pedido DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   observacao VARCHAR(255) NULL DEFAULT NULL,
   cod_usuario BIGINT NOT NULL,
   comanda_id BIGINT NOT NULL,
   quantidade INT NOT NULL,
   produto_id BIGINT NOT NULL,
  PRIMARY KEY (id),
    FOREIGN KEY (cod_mesa)
    REFERENCES mesa (id),
    FOREIGN KEY (cod_usuario)
    REFERENCES usuario (codigo),
    FOREIGN KEY (comanda_id)
    REFERENCES comanda (id),
    FOREIGN KEY (produto_id)
    REFERENCES produto (id)); 

