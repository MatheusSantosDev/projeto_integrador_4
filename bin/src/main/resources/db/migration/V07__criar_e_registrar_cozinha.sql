CREATE TABLE cozinha (

 id bigint NOT NULL AUTO_INCREMENT,

 cod_pedido bigint NOT NULL,

 finalizado enum('sim','nao') NOT NULL,

 PRIMARY KEY (id),

 FOREIGN KEY (cod_pedido) references pedido(id)

) ;

INSERT INTO `service`.`cozinha` (`cod_pedido`, `finalizado`) VALUES ('1', 'nao');

INSERT INTO `service`.`cozinha` (`cod_pedido`, `finalizado`) VALUES ('2', 'nao');

