create table if not exists endereco(
    id serial primary key,
    rua text not null,
    logradouro text not null,
    complemento text not null,
    numero int not null
);

--drop table if exists Agencia cascade;
create table if not exists agencia(
    id serial primary key,
    nome text not null,
    razao_social text not null,
    cnpj text not null,
    endereco_id int references endereco(id),
    situacao_cadastral text not null
);


-- Inserir um endereço (precisa existir um endereço antes de inserir uma agência)
insert into endereco (rua, logradouro, complemento, numero)
    values ('Quadra 10', 'Asa Norte', 'Apartamento 101', 100);

-- Inserir uma agência, fazendo referência ao endereço inserido
-- O valor de 'endereco_id' é o id do endereço inserido (neste caso, assumimos que é 1)
insert into agencia (nome, razao_social, cnpj, endereco_id, situacao_cadastral)
    values ('Agencia BSB', 'Asa Norte AGENCIA BSB', '15130254000100', 1, 'ATIVO');