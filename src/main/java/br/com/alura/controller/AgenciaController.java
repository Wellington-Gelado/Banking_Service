package br.com.alura.controller;

import br.com.alura.domain.Agencia;
import br.com.alura.service.AgenciaService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.jboss.resteasy.reactive.RestResponse;

@Path("/agencias")
public class AgenciaController {

    private final AgenciaService agenciaService;

    //INJEÇÃO AUTOMÁTICO PELO QUARKUS ATRAVÉS DO CDI
    AgenciaController(AgenciaService agenciaService) {
        this.agenciaService = agenciaService;
    }

    //TIPIFICA O STATUS CODE HTTP RETORNADO, DIFERENTE DO RESPONSE
    //CREATED = 201
    //@Context indica qual o path o cliente está acessando, algo assim
    @POST
    @Transactional
    public RestResponse<Void> cadastrar(Agencia agencia, @Context UriInfo uriInfo) {
        this.agenciaService.cadastrar(agencia);
        return RestResponse.created(uriInfo.getAbsolutePath());
    }

    @GET
    @Path("{id}")
    public  RestResponse<Agencia> buscarPorId(Long id){
        Agencia agencia = this.agenciaService.buscarPorId(id);
        return RestResponse.ok(agencia);
    }

    @DELETE
    @Transactional
    @Path("{id}")
    public RestResponse<Void> deletar(Long id){
        this.agenciaService.deletar(id);
        return RestResponse.ok();
    }

    @PUT
    @Transactional
    public RestResponse<Void> alterar(Agencia agencia){
        this.agenciaService.alterar(agencia);
        return RestResponse.ok();
    }
}

