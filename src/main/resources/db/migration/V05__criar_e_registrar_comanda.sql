CREATE TABLE comanda (
  id BIGINT NOT NULL AUTO_INCREMENT,
  cod_mesa BIGINT NOT NULL,
  status varchar(10) not null,
  usuario_codigo bigint not null,

  PRIMARY KEY (id),
  FOREIGN KEY (cod_mesa) REFERENCES mesa (id),
  FOREIGN KEY (usuario_codigo) references usuario(codigo));
  