package vinix.compartilhado.excecoes;

/** Recurso inexistente (resposta 404). */
public class NaoExistenteException extends RuntimeException {

  public NaoExistenteException(String mensagem) {
    super(mensagem);
  }

  public NaoExistenteException(String mensagem, Throwable causa) {
    super(mensagem, causa);
  }
}
