package br.com.alura.repository;

import br.com.alura.domain.Agencia;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;


//CDI : API de gestão de dependências do "contexto" QUarkus
//Por isso do ApplicationScoped
@ApplicationScoped
public class AgenciaRepository implements PanacheRepository<Agencia> {
}
