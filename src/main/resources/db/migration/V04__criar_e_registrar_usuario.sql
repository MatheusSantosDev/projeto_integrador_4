CREATE TABLE  IF NOT EXISTS usuario (	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,	
nome VARCHAR(50) NOT NULL,	
email VARCHAR(50) NOT NULL,	
senha VARCHAR(150) NOT NULL) 
ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE  IF NOT EXISTS permissao (	codigo BIGINT(20) PRIMARY KEY,	
descricao VARCHAR(50) NOT NULL) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS usuario_permissao (	codigo_usuario BIGINT(20) NOT NULL,	
codigo_permissao BIGINT(20) NOT NULL,	PRIMARY KEY (codigo_usuario, codigo_permissao),	
FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo),	
FOREIGN KEY (codigo_permissao) REFERENCES permissao(codigo)) 
ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO usuario (codigo, nome, email, senha) values 
(1, 'Administrador', 'admin@gmail.com', '$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.');
#senha default "admin"

INSERT INTO permissao (codigo, descricao) values 
(1, 'ROLE_CATEGORIA');

INSERT INTO permissao (codigo, descricao) values 
(2, 'ROLE_PRODUTO');

INSERT INTO permissao (codigo, descricao) values 
(3, 'ROLE_USUARIO');

INSERT INTO permissao (codigo, descricao) values 
(4, 'ROLE_COZINHA');

INSERT INTO permissao (codigo, descricao) values 
(5, 'ROLE_GARCOM');

#GARCOM -> PEDIDO, COMANDA E CAIXA

INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 1);

INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 2);

INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 3);

INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 4);

INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 5);
