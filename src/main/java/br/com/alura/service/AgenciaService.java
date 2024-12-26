package br.com.alura.service;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.exceptions.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.alura.repository.AgenciaRepository;
import br.com.alura.repository.EnderecoRepository;
import br.com.alura.service.http.SituacaoCadastralHttpService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.POST;
import org.eclipse.microprofile.rest.client.inject.RestClient;


import static br.com.alura.domain.http.SituacaoCadastral.ATIVO;

//GERENCIAMENTO DELEGADO AO QUARKUS PARA PODER INJETAR A CLASSE EM OUTRO RECURSO
//COMO UMA CONTROLLER FEITA PELO CDI
@ApplicationScoped
public class AgenciaService {
    //INFORMANDO QUE É UM RECURSO INJETÁVEL DO TIPO CLIENTE HTTP
    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;

    //FINAL SERVE PARA REPRESENTAR IMUTALIBILIDADE
    private final AgenciaRepository agenciaRepository;

    private final EnderecoRepository enderecoRepository;

    //QUARKUS INJETA ATRAVÉS DO CDI
    AgenciaService(AgenciaRepository agenciaRepository , EnderecoRepository enderecoRepository) {

        this.agenciaRepository = agenciaRepository;
        this.enderecoRepository = enderecoRepository;
    }

    @POST
    public void cadastrar(Agencia agencia) {
        AgenciaHttp agenciaHttp = situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());

        if (agenciaHttp != null && agenciaHttp.getSituacaoCadastral().equals(ATIVO)) {
            agenciaRepository.persist(agencia);
        } else {
            // Lança a RuntimeException com AgenciaNaoAtivaOuNaoEncontradaException como causa
            throw new RuntimeException(
                    "Erro ao tentar verificar a situação cadastral da agência com CNPJ " + agencia.getCnpj(),
                    new AgenciaNaoAtivaOuNaoEncontradaException(
                            "A agência com CNPJ " + agencia.getCnpj() + " não está ativa ou não foi encontrada."
                    )
            );
        }
    }




    public Agencia buscarPorId(Long id) {
        return agenciaRepository.findById(id);
    }

    public void deletar(Long id) {
        agenciaRepository.deleteById(id);
    }

    public void alterar(Agencia agencia) {
        try {
            agenciaRepository.update(
                    """
                            UPDATE Agencia a
                                SET a.nome = ?1,
                                    a.razaoSocial = ?2,
                                    a.cnpj = ?3,
                                    a.situacaoCadastral = ?4
                            WHERE id = ?5
                            """,
                    agencia.getNome(),
                    agencia.getRazaoSocial(),
                    agencia.getCnpj(),
                    agencia.getSituacaoCadastral(),
                    agencia.getId());
        } catch (IllegalStateException e) {
            System.out.println("Agência com ID " + agencia.getId() + " não encontrada");
        }
        // Busca a entidade pelo ID
       /* Agencia entidadeExistente = agenciaRepository.findById(agencia.getId());

        if (entidadeExistente != null) {
            // Atualiza os atributos desejados
            entidadeExistente.setNome(agencia.getNome());
            entidadeExistente.setRazaoSocial(agencia.getRazaoSocial());
            entidadeExistente.setCnpj(agencia.getCnpj());
        } else {
            throw new IllegalStateException("Agência com ID " + agencia.getId() + " não encontrada");
        }

    }*/
    }

}
