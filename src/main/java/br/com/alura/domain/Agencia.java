package br.com.alura.domain;

import jakarta.persistence.*;

@Entity
public class Agencia {
    // CONSTRUTOR PADRÃO
    Agencia () {

    }

    public Agencia (Integer id,
                    String nome,
                    String razaoSocial,
                    String cnpj,
                    String situacaoCadastral,
                    Endereco endereco) {
            this.id = id;
            this.nome = nome;
            this.razaoSocial = razaoSocial;
            this.cnpj = cnpj;
            this.situacaoCadastral = situacaoCadastral;
            this.endereco = endereco;

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @Column(name = "razao_social")
    private String razaoSocial;

    private String cnpj;

    @Column(name = "situacao_cadastral")
    private String situacaoCadastral;

    @OneToOne(cascade = CascadeType.ALL) // ou outro tipo de Cascade se não for necessário ALL
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    public Integer getId() {
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

    public String getSituacaoCadastral() {
        return situacaoCadastral;
    }

    public Endereco getEndereco() {
        return endereco;
    }
}