package vinix.catalogo.exceptions;

public class NaoExistenteException extends RuntimeException {
  public NaoExistenteException(String message) {
    super(message);
  }
}
