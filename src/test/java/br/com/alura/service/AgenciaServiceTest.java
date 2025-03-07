package br.com.alura.service;

import br.com.alura.domain.Agencia;
import br.com.alura.exceptions.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.alura.repository.AgenciaRepository;
import br.com.alura.service.http.SituacaoCadastralHttpService;
import br.com.alura.utils.AgenciaFixture;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.*;
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

    @BeforeEach
    public void setUp(){
        /*Configura o mock do repositório para não fazer nada a mais quando o método persistir é chamado.
        Isso é útil para garantir que o método persist do agenciaRepository não tenha efeitos colaterais
        durante os testes.*/
        Mockito.doNothing().when(agenciaRepository).persist(Mockito.any(Agencia.class));
    }

    @AfterEach
    public void tearDown() {
        // Limpeza após cada teste
        // Aqui você pode resetar mocks ou desfazer outras configurações
        Mockito.reset(agenciaRepository);
    }


    @Test
    @Order(1)
    public void deveriaNaoCadastrarQuandoClienteRetornarNull() {
        Agencia agencia = AgenciaFixture.criarAgencia();

        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123"))
                .thenReturn(null);

        Assertions.assertThrows(AgenciaNaoAtivaOuNaoEncontradaException.class, () -> agenciaService.cadastrar(agencia));

        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia);
    }

    @Test
    @Order(2)
    public void deveriaNaoCadastrarQuandoClienteRetornarAgenciaInativa(){
        Agencia agencia = AgenciaFixture.criarAgencia();

        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123"))
                .thenReturn(AgenciaFixture.criarAgenciaHttp("INATIVO"));

        Assertions.assertThrows(AgenciaNaoAtivaOuNaoEncontradaException.class, () -> agenciaService.cadastrar(agencia));

        Mockito.verify(agenciaRepository, Mockito.never()).persist(agencia);
    }

    @Test
    @Order(3)
    public void deveriaCadastrarQuandoClienteRetornarSituacaoCadastralAtiva(){
        Agencia agencia = AgenciaFixture.criarAgencia();

        Mockito.when(situacaoCadastralHttpService.buscarPorCnpj("123"))
                .thenReturn(AgenciaFixture.criarAgenciaHttp("ATIVO"));

        agenciaService.cadastrar(agencia);

        Mockito.verify(agenciaRepository).persist(agencia);
    }

    @Test
    @Order(4)
    public void deveriaBuscarAgenciaPorIdQuandoExistirOId(){
        Agencia agencia = AgenciaFixture.criarAgencia();

        Mockito.when(agenciaRepository.findById(agencia.getId()))
                .thenReturn(agencia);

        Agencia resultado = agenciaService.buscarPorId(agencia.getId());

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(agencia.getId(), resultado.getId());
        Mockito.verify(agenciaRepository).findById(agencia.getId());
    }

    @Test
    @Order(5)
    public void deveriaRetornarNullQuandoIdDaAgenciaNaoForEncontrada(){
        Long idInvalido = 999L;

        Mockito.when(agenciaRepository.findById(idInvalido))
                .thenReturn(null);

        Agencia resultado = agenciaService.buscarPorId(idInvalido);

        Assertions.assertNull(resultado);
        Mockito.verify(agenciaRepository).findById(idInvalido);
    }

    @Test
    @Order(6)
    public void deveriaLancarExcecaoQuandoTentarDeletarIdDaAgenciaNaoEncontrada(){
        Long idInvalido = 999L;

        // Simula que a agência não foi encontrada
        Mockito.when(agenciaRepository.findById(idInvalido)).thenReturn(null);

        // Verifica se a exceção é lançada corretamente
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            agenciaService.deletar(idInvalido);
        });

        // Verifica que o método deleteById NUNCA foi chamado
        Mockito.verify(agenciaRepository, Mockito.never()).deleteById(idInvalido);
    }

    @Test
    @Order(7)
    public void deveriaDeletarAgenciaPeloId(){
        Agencia agencia = AgenciaFixture.criarAgencia();

        //Buscando agência
        Mockito.when(agenciaRepository.findById(agencia.getId())).thenReturn(agencia);

        //Chamar o método deletar
        agenciaService.deletar(agencia.getId());

        //Verificando se o método deleteById foi chamado no repositório
        Mockito.verify(agenciaRepository).deleteById(agencia.getId());

    }



}
