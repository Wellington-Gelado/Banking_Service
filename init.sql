-- Criação da tabela endereco
create table if not exists endereco (
    id serial primary key,
    rua text not null,
    logradouro text not null,
    complemento text not null,
    numero int not null
);

-- Criação da tabela agencia
create table if not exists agencia (
    id serial primary key,
    nome text not null,
    razao_social text not null,
    cnpj text not null,
    situacao_cadastral text,  -- Adicionando a coluna situacao_cadastral
    endereco_id int references endereco(id)
);

-- Inserir um endereço
insert into endereco (rua, logradouro, complemento, numero)
    values ('Quadra 10', 'Asa Norte', 'Apartamento 101', 100);

-- Inserir uma agência, fazendo referência ao endereço inserido
-- O valor de 'endereco_id' é o id do endereço inserido (neste caso, assumimos que é 1)
insert into agencia (nome, razao_social, cnpj, situacao_cadastral, endereco_id)
    values ('Agencia BSB', 'Asa Norte AGENCIA BSB', '15130254000100', 'ATIVO', 1);

