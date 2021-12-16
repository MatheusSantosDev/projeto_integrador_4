 CREATE TABLE fechamento_comanda (
   idfechamento_comanda INT NOT NULL AUTO_INCREMENT,
   comanda_id  BIGINT NOT NULL,
   hora_de_fechamento TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
   valor_Total DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (idfechamento_comanda),
    FOREIGN KEY (comanda_id)
    REFERENCES comanda (id));