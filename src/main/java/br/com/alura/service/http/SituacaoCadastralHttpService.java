package br.com.alura.service.http;
// MICROPROFILE: P/ ESPECIFICAÇÕES NO CONTEXTO CLOUD SERVICES
import br.com.alura.domain.http.AgenciaHttp;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@Path("/situacao-cadastral")
@RegisterRestClient(configKey = "situacao-cadastral-api")
public interface SituacaoCadastralHttpService {
// PRÓPRIO QUARKUS EM TEMPO DE BUILD VAI IMPLEMENTAR A INTERFACE POR BAIXO DOS PANOS
    //rest-client-jackson serializa o json de comunicação

    @GET
    @Path("{cnpj}") // passando cnpj na requisição GET
    AgenciaHttp buscarPorCnpj(String cnpj);
}
