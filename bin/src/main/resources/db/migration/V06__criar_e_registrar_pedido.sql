CREATE TABLE pedido (

 id bigint NOT NULL AUTO_INCREMENT,

 cod_mesa bigint NOT NULL,

 status varchar(15) NOT NULL,

 hora_do_pedido datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,

 data date NOT NULL,

 observacao varchar(255) DEFAULT NULL,

 cod_usuario bigint NOT NULL,

 PRIMARY KEY (id),

 FOREIGN KEY (cod_mesa) references mesa(id),

 FOREIGN KEY (cod_usuario) references usuario(codigo)

);

INSERT INTO `service`.`pedido` (`cod_mesa`, `status`, `data`, `observacao`, `cod_usuario`) VALUES ('2', 'Prerapação', '2021-04-13', 'Carnes Bem passadas', '1');

INSERT INTO `service`.`pedido` (`cod_mesa`, `status`, `data`, `observacao`, `cod_usuario`) VALUES ('1', 'Preparação', '2021-04-13', 'Carnes Mal passada', '1');