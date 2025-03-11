package br.com.alura.service;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.Endereco;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(agencia.getId(), resultado.getId());
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


    @Test
    @Order(8)
    public void deveriaAlterarDadosDaAgencia() {
       /* // Criando agência original com um endereço inicial
        Endereco enderecoOriginal = new Endereco(
                1L,
                "Rua Antiga",
                "Logradouro Antigo",
                "Complemento Antigo",
                100
        );

        Agencia agenciaOriginal = new Agencia(
                1L,
                "Nome Antigo",
                "Razão Social Antiga",
                "11.111.111/0001-11", // CNPJ antigo
                enderecoOriginal
        );*/

        Agencia agenciaOriginal = AgenciaFixture.criarAgencia();

        // Criando um novo endereço atualizado
        Endereco enderecoAtualizado = new Endereco(
                1L,
                "Rua Nova",
                "Logradouro Novo",
                "Complemento Novo",
                200
        );

        // Criando uma agência com os dados modificados, incluindo o endereço atualizado
        Agencia agenciaAtualizada = new Agencia(
                agenciaOriginal.getId(),
                "Novo Nome",
                "Nova Razão Social",
                "99.999.999/0001-99", // Novo CNPJ
                enderecoAtualizado
        );

        // Mockando a busca pela agência original
        Mockito.when(agenciaRepository.findById(agenciaOriginal.getId())).thenReturn(agenciaOriginal);

        // Chamando o método de alteração
        agenciaService.alterar(agenciaAtualizada);

        // Capturando os argumentos passados para o método update
        ArgumentCaptor<String> queryCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object[]> paramsCaptor = ArgumentCaptor.forClass(Object[].class);

        // Verificando se o método de atualização foi chamado no repositório com os parâmetros corretos
        Mockito.verify(agenciaRepository, Mockito.times(1)).update(
                queryCaptor.capture(),
                paramsCaptor.capture()
        );

        // Validando a query
        String expectedQuery = """
            UPDATE Agencia a
                SET a.nome = ?1,
                    a.razaoSocial = ?2,
                    a.cnpj = ?3,
                    a.endereco = ?4
            WHERE id = ?5
            """;
        assertEquals(expectedQuery, queryCaptor.getValue());

        // Validando os parâmetros
        Object[] expectedParams = new Object[]{
                agenciaAtualizada.getNome(),
                agenciaAtualizada.getRazaoSocial(),
                agenciaAtualizada.getCnpj(),
                agenciaAtualizada.getEndereco(),
                agenciaAtualizada.getId()
        };
        assertThat(paramsCaptor.getValue()).containsExactly(expectedParams);
    }






}
