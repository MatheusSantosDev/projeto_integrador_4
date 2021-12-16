CREATE TABLE fechamento_pedido (

 id bigint NOT NULL AUTO_INCREMENT,

 cod_pedido bigint DEFAULT NULL,

 hora_de_fechamento timestamp NULL DEFAULT CURRENT_TIMESTAMP,

 valor_total decimal(10,2) DEFAULT NULL,

 PRIMARY KEY (id),

 FOREIGN KEY (cod_pedido) references pedido(id)

) ;

INSERT INTO `service`.`fechamento_pedido` (`cod_pedido`, `valor_total`) VALUES ('1', 15.00);

INSERT INTO `service`.`fechamento_pedido` (`cod_pedido`, `valor_total`) VALUES ('2', 15.00);


