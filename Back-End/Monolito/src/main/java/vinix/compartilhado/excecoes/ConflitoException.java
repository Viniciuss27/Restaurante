package vinix.compartilhado.excecoes;

/** Conflito com o estado atual dos dados, como duplicidade (resposta 409). */
public class ConflitoException extends RuntimeException {

  public ConflitoException(String mensagem) {
    super(mensagem);
  }

  public ConflitoException(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }
}
