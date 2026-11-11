package vinix.catalogo.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatusCode status, WebRequest request) {

    ProblemDetail problema = ProblemDetail.forStatusAndDetail(
        BAD_REQUEST, "Um ou mais campos possuem valores inválidos");
    problema.setTitle("Erro de validação");
    problema.setProperty("erros", ex.getBindingResult().getFieldErrors().stream().map(e ->
            Map.of("campo", e.getField(), "mensagem",
                Objects.requireNonNullElse(e.getDefaultMessage(), "valor inválido"))).toList());


    return handleExceptionInternal(ex, problema, headers, BAD_REQUEST, request);
  }

  @ExceptionHandler(NaoExistenteException.class)
  public ProblemDetail naoExistente(NaoExistenteException ex) {
    log.warn("Recurso não encontrado: {}", ex.getMessage());
    return problema(NOT_FOUND, "Recurso não encontrado", ex.getMessage());
  }

  @ExceptionHandler({DuplicadoException.class, JaExistenteException.class})
  public ProblemDetail conflito(Exception ex) {
    log.warn("Conflito de dados: {}", ex.getMessage());
    return problema(CONFLICT, "Conflito de dados", ex.getMessage());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ProblemDetail integridade(DataIntegrityViolationException ex) {
    log.warn("Violação de integridade: {}", ex.getMostSpecificCause().getMessage());
    return problema(CONFLICT, "Violação de integridade",
        "Não foi possível realizar a operação devido a uma restrição de integridade dos dados");
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail inesperado(Exception ex) {
    log.error("Erro interno não tratado", ex);
    return problema(INTERNAL_SERVER_ERROR, "Erro interno", "Erro interno no servidor");
  }

  private ProblemDetail problema(HttpStatus status, String titulo, String detalhe) {
    ProblemDetail problema = ProblemDetail.forStatusAndDetail(status, detalhe);
    problema.setTitle(titulo);
    return problema;
  }
}