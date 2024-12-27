package br.com.alura.utils;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.Endereco;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.domain.http.SituacaoCadastral;

public class AgenciaFixture {

    public static AgenciaHttp criarAgenciaHttp(String status){
        // Converte a string para o enum SituacaoCadastral
        SituacaoCadastral situacao;
        try {
            situacao = SituacaoCadastral.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + status);
        }

        return  new AgenciaHttp(
                "Agencia Teste",
                "Razao Social da Agencia Teste",
                "123",
                situacao);
    }

    public static Agencia criarAgencia() {
        Endereco endereco = new Endereco(
                1,
                "Rua de teste",
                "Logradouro de teste",
                "Complemento de teste",
                1);
        return new Agencia(
                1,
                "Agencia Teste",
                "Razao social da Agencia Teste",
                "123",
                endereco);
    }
}
