package br.com.alura.domain.http;


public class AgenciaHttp {

    public AgenciaHttp(String nome,
                       String razaoSocial,
                       String cnpj,
                       SituacaoCadastral situacaoCadastral
                       ){
        this.nome = nome;
        this.razaoSocial = razaoSocial;
        this.cnpj = cnpj;
        this.situacaoCadastral = situacaoCadastral;

    }

    private final String nome;
    private final String razaoSocial;
    private final String cnpj;
    private final SituacaoCadastral situacaoCadastral;

    public String getNome() {
        return nome;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public SituacaoCadastral getSituacaoCadastral() {
        return situacaoCadastral;
    }


}
