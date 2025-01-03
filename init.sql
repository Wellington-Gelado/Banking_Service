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

-- Inserção condicional de um endereço
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM endereco) THEN
        INSERT INTO endereco (rua, logradouro, complemento, numero)
        VALUES ('Quadra 10', 'Asa Norte', 'Apartamento 101', 100);
    END IF;
END $$;

-- Inserção condicional de uma agência, referenciando o endereço inserido (endereco_id = 1)
DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM agencia) THEN
        INSERT INTO agencia (nome, razao_social, cnpj, situacao_cadastral, endereco_id)
        VALUES ('Agencia BSB', 'Asa Norte AGENCIA BSB', '15130254000100', 'ATIVO', 1);
    END IF;
END $$;