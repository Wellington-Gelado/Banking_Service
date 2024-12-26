package br.com.alura.exceptions;

import br.com.alura.domain.http.SituacaoCadastral;

public class AgenciaNaoAtivaOuNaoEncontradaException extends RuntimeException {

    // Construtor padrão que recebe uma mensagem personalizada
    public AgenciaNaoAtivaOuNaoEncontradaException(String message) {
        super(message);  // Passa a mensagem para a classe pai (RuntimeException)
    }

    @Override
    public String getMessage() {
        return super.getMessage();  // Retorna a mensagem passada no momento da instância
    }
}
