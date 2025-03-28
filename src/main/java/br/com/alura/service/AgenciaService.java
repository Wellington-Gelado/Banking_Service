package br.com.alura.service;

import br.com.alura.domain.Agencia;
import br.com.alura.domain.http.AgenciaHttp;
import br.com.alura.domain.http.SituacaoCadastral;
import br.com.alura.exceptions.AgenciaNaoAtivaOuNaoEncontradaException;
import br.com.alura.repository.AgenciaRepository;
import br.com.alura.service.http.SituacaoCadastralHttpService;
import io.micrometer.core.instrument.MeterRegistry;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.POST;
import org.eclipse.microprofile.rest.client.inject.RestClient;


//GERENCIAMENTO DELEGADO AO QUARKUS PARA PODER INJETAR A CLASSE EM OUTRO RECURSO
//COMO UMA CONTROLLER FEITA PELO CDI
@ApplicationScoped
public class AgenciaService {


    //QUARKUS INJETA ATRAVÉS DO CDI
    AgenciaService(AgenciaRepository agenciaRepository , MeterRegistry meterRegistry) {

        this.agenciaRepository = agenciaRepository;
        this.meterRegistry = meterRegistry;
    }

    //FINAL SERVE PARA REPRESENTAR IMUTALIBILIDADE
    private final AgenciaRepository agenciaRepository;

    private final MeterRegistry meterRegistry;

    //INFORMANDO QUE É UM RECURSO INJETÁVEL DO TIPO CLIENTE HTTP
    @RestClient
    private SituacaoCadastralHttpService situacaoCadastralHttpService;


    @POST
    public void cadastrar(Agencia agencia) {
        AgenciaHttp agenciaHttp = situacaoCadastralHttpService.buscarPorCnpj(agencia.getCnpj());

        if (agenciaHttp != null && agenciaHttp.getSituacaoCadastral().equals(SituacaoCadastral.ATIVO)) {
            agencia.setSituacaoCadastral("ATIVO");
            agenciaRepository.persist(agencia);
            Log.info("A agencia com o CNPJ " + agencia.getCnpj() + " foi cadastrada!!");
            meterRegistry.counter("agencia_adicionada_counter").increment();
        } else {
            agencia.setSituacaoCadastral("INATIVO");
            Log.info("A agencia com o CNPJ " + agencia.getCnpj() + " não foi cadastrada!!");
            meterRegistry.counter("agencia_nao_adicionada_counter").increment();
            // Lança diretamente a exceção AgenciaNaoAtivaOuNaoEncontradaException
            throw new AgenciaNaoAtivaOuNaoEncontradaException(
                    "A agência com CNPJ " + agencia.getCnpj() + " não está ativa ou não foi encontrada."
            );
        }
    }



    public Agencia buscarPorId(Long id) {
        Log.info("A agencia com o id " +id+ " foi buscada!!");
        return agenciaRepository.findById(id);
    }

    public void deletar(Long id) {

        if (agenciaRepository.findById(id) == null) {
            Log.info("Não existe agência com o id " +id+ "!!");
            throw new EntityNotFoundException("Agência com ID " + id + " não encontrada.");
        }
        //DELETANDO A AGÊNCIA
        agenciaRepository.deleteById(id);

        Log.info("A agencia com o id " + id + " foi deletada!!");

    }

    public void alterar(Agencia agencia) {
        try {
            // Busca a agência original pelo ID
            Agencia agenciaOriginal = agenciaRepository.findById(agencia.getId());

            if (agenciaOriginal == null) {
                Log.info("Agência com ID " + agencia.getId() + " não encontrada");
                return;
            }

            // Registra o CNPJ antigo e o novo CNPJ
            Log.info("A agência com o CNPJ " + agenciaOriginal.getCnpj() + " foi alterada para: " + agencia.getCnpj() + "!!");

            // Atualiza a agência
            agenciaRepository.update(
                    """
                    UPDATE Agencia a
                        SET a.nome = ?1,
                            a.razaoSocial = ?2,
                            a.cnpj = ?3,
                            a.endereco = ?4
                    WHERE id = ?5
                    """,
                    agencia.getNome(),
                    agencia.getRazaoSocial(),
                    agencia.getCnpj(),
                    agencia.getEndereco(),
                    agencia.getId());
        } catch (IllegalStateException e) {
            Log.info("Erro ao atualizar a agência com ID " + agencia.getId());
        }
    }

}
