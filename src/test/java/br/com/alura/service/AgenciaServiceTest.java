package br.com.alura.service;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.Endereco;
import br.com.alura.domain.http.SituacaoCadastral;
import br.com.alura.exceptions.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.alura.repository.AgenciaRepository;
import br.com.alura.service.http.SituacaoCadastralHttpService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
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

    @Test
    public void deveNaoCadastrarQuandoClienteRetornarNull() {
        Agencia agencia = criarAgencia();

        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123"))
                .thenReturn(null);

        // Espera que a RuntimeException seja lançada
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class,
                () -> agenciaService.cadastrar(agencia));

        // Verifica se a causa da RuntimeException é uma AgenciaNaoAtivaOuNaoEncontradaException
        Assertions.assertTrue(exception.getCause() instanceof AgenciaNaoAtivaOuNaoEncontradaException);

        // Verifica que o método persist não foi chamado
        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia);
    }



    private Agencia criarAgencia() {
        Endereco endereco = new Endereco(1, "", "", "", 2);
        return new Agencia(1, "", "", "", "", endereco);
    }

}
