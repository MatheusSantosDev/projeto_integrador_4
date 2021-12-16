CREATE TABLE produto (

 id bigint NOT NULL AUTO_INCREMENT,

 nome varchar(55) NOT NULL,

 descricao varchar(255) NOT NULL,

 cod_categoria bigint DEFAULT NULL,

 valor decimal(10,2) NOT NULL,

 PRIMARY KEY (id),

 foreign key(cod_categoria) references categoria(id)

) ;

insert into produto (nome, descricao, cod_categoria,valor) values ('Lanche x','lanche grande com cheddar', 1, 5.20);
