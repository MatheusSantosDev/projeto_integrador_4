#trigger para inserir o pedido na cozinha assim que o mesmo for criado

delimiter $
create trigger Inserir_Pedido_Cozinha after insert
on pedido
for each row
begin
	insert into cozinha (cod_pedido, finalizado) values (NEW.id, "não");
end$


# trigger para mudar o status do pedido para "pronto" assim que o mesmo for
# marcado como finalizado na cozinha

delimiter $
create trigger Pedido_Pronto after update
on cozinha
for each row
begin
	update pedido set status = "Pronto" where pedido.id = OLD.cod_pedido;
end$


# trigger para registrar o valor do pedido na tabela "fechamento_pedido"
delimiter $

create trigger Pedido_Efetuado after insert
on pedido
for each row
begin
	declare valor decimal(10,2);
    call valor_produto(NEW.produto_id, valor);
	insert into fechamento_pedido (cod_pedido, hora_de_fechamento, valor_total)
				values (NEW.id, NEW.hora_do_pedido, (valor * NEW.quantidade));
end $


# função para pegar o valor do produto
delimiter $

create procedure valor_produto (IN produto_id bigint, OUT valor_produto decimal(10,2))
begin
	select valor INTO valor_produto from produto where produto.id = produto_id;
end $


# trigger para criar um id de fechamento de comanda no banco de dados
delimiter $

create trigger Comanda_fechamento after insert 
on pedido
for each row
begin
	declare valor decimal(10,2);
    declare existe bigint;
    call valor_produto(NEW.produto_id, valor);
    call comanda_existe(NEW.comanda_id, existe);
    
	if existe is null then
		insert into fechamento_comanda (comanda_id, hora_de_fechamento, valor_Total) 
		values (NEW.comanda_id, NEW.hora_do_pedido, (valor * NEW.quantidade));
	else
		update fechamento_comanda set hora_de_fechamento = NEW.hora_do_pedido,
								  valor_Total = valor_Total + (valor * NEW.quantidade) 
                                  where fechamento_comanda.comanda_id = NEW.comanda_id;
	end if;
end	$


# função para ver se já existe uma comanda "fechada"

delimiter $ 
create procedure comanda_existe (IN id_comanda bigint, OUT existe bigint)
begin
	select comanda_id into existe from fechamento_comanda where fechamento_comanda.comanda_id = id_comanda;
end $