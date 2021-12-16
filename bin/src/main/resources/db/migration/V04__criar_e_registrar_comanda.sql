CREATE TABLE comanda (

 id bigint NOT NULL AUTO_INCREMENT,

 cod_mesa bigint NOT NULL,

 cod_produto bigint NOT NULL,

 quantidade int DEFAULT NULL,

 PRIMARY KEY (id),

 foreign key (cod_mesa) references mesa(id),

 foreign key (cod_produto) references produto(id)

);

INSERT INTO comanda (cod_mesa,cod_produto,quantidade) VALUES(1,1,15);