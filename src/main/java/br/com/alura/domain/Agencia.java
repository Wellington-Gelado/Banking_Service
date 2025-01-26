package br.com.alura.domain;

import br.com.alura.domain.http.SituacaoCadastral;
import jakarta.persistence.*;

@Entity
public class Agencia {

    // Construtor padrão
    public Agencia() {
    }

    public Agencia(
            Long id,
            String nome,
            String razaoSocial,
            String cnpj,
            Endereco endereco) {
        this.id = id;
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.endereco = endereco;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @Column(name = "razao_social")
    private String razaoSocial;
    private String cnpj;

    @Column(name = "situacao_cadastral")
    private String situacaoCadastral;



    @OneToOne(cascade = CascadeType.ALL) // ou outro tipo de Cascade se não for necessário ALL
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public String getSituacaoCadastral() { return situacaoCadastral; }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setSituacaoCadastral(String situacaoCadastral) {
        this.situacaoCadastral = situacaoCadastral;
    }
}