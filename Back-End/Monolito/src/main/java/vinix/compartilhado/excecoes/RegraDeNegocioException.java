package vinix.compartilhado.excecoes;

/** Violação de regra de negócio (resposta 422). Os módulos criam subclasses com significado próprio. */
public class RegraDeNegocioException extends RuntimeException {

  public RegraDeNegocioException(String mensagem) {
    super(mensagem);
  }

  public RegraDeNegocioException(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }
}
