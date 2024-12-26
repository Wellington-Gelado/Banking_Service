package br.com.alura.service;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.Endereco;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.domain.http.SituacaoCadastral;
import br.com.alura.exceptions.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.alura.repository.AgenciaRepository;
import br.com.alura.service.http.SituacaoCadastralHttpService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@QuarkusTest
public class AgenciaServiceTest {

    @InjectMock
    private AgenciaRepository agenciaRepository;

    @InjectMock
    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    @Inject
    private AgenciaService agenciaService;

    private Agencia agencia = criarAgencia();
    private AgenciaHttp agenciaHttpInativa = criarAgenciaHttpInativa();
    private AgenciaHttp agenciaHttpAtiva = criarAgenciaHttpAtiva();



    @Test
    @Order(1)
    public void deveNaoCadastrarQuandoClienteRetornarNull() {

        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123"))
                .thenReturn(null);

        Assertions.assertThrows(AgenciaNaoAtivaOuNaoEncontradaException.class, () -> agenciaService.cadastrar(agencia));

        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia);
    }

    @Test
    @Order(2)
    public void deveNaoCadastrarQuandoClienteRetornarAgenciaInativa(){

        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123"))
                .thenReturn(agenciaHttpInativa);

        Assertions.assertThrows(AgenciaNaoAtivaOuNaoEncontradaException.class, () -> agenciaService.cadastrar(agencia));

        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia);
    }

    @Test
    @Order(3)
    public void deveCadastrarQuandoClienteRetornarSituacaoCadastralAtiva(){
        Agencia agencia = criarAgencia();

        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123"))
                .thenReturn(agenciaHttpAtiva);

        agenciaService.cadastrar(agencia);

        Mockito.verify(agenciaRepository).persist(agencia);
    }



    private Agencia criarAgencia() {
        Endereco endereco = new Endereco(1, "Quadra 1", "Teste", "Teste", 2);
        return new Agencia(1, "Agencia Teste", "Razao Agencia Teste", "123",  endereco);
    }

    private AgenciaHttp criarAgenciaHttpAtiva() {
        return new AgenciaHttp("Agencia Teste", "Razao Agencia Teste", "123", SituacaoCadastral.ATIVO);
    }

    private AgenciaHttp criarAgenciaHttpInativa() {
        return new AgenciaHttp("Agencia Teste", "Razao Agencia Teste", "123", SituacaoCadastral.INATIVO);
    }

}
