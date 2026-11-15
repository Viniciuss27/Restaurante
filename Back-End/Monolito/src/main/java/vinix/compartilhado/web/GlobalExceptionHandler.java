package vinix.compartilhado.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vinix.compartilhado.excecoes.ConflitoException;
import vinix.compartilhado.excecoes.NaoExistenteException;
import vinix.compartilhado.excecoes.RegraDeNegocioException;

import java.util.Map;
import java.util.Objects;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_CONTENT;


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
    problema.setProperty("erros", ex.getBindingResult().getFieldErrors().stream()
        .map(e -> Map.of(
            "campo", e.getField(),
            "mensagem", Objects.requireNonNullElse(e.getDefaultMessage(), "valor inválido")))
        .toList());

    return handleExceptionInternal(ex, problema, headers, BAD_REQUEST, request);
  }

  @ExceptionHandler(NaoExistenteException.class)
  public ProblemDetail naoExistente(NaoExistenteException ex) {
    log.warn("Recurso não encontrado: {}", ex.getMessage());
    return problema(NOT_FOUND, "Recurso não encontrado", ex.getMessage());
  }

  @ExceptionHandler(ConflitoException.class)
  public ProblemDetail conflito(ConflitoException ex) {
    log.warn("Conflito de dados: {}", ex.getMessage());
    return problema(CONFLICT, "Conflito de dados", ex.getMessage());
  }

  @ExceptionHandler(RegraDeNegocioException.class)
  public ProblemDetail regraDeNegocio(RegraDeNegocioException ex) {
    log.warn("Regra de negócio violada: {}", ex.getMessage());
    return problema(UNPROCESSABLE_CONTENT, "Regra de negócio violada", ex.getMessage());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ProblemDetail integridade(DataIntegrityViolationException ex) {
    log.warn("Violação de integridade: {}", ex.getMostSpecificCause().getMessage());
    return problema(CONFLICT, "Violação de integridade",
        "Não foi possível realizar a operação devido a uma restrição de integridade dos dados");
  }

  @ExceptionHandler(AuthenticationException.class)
  public ProblemDetail naoAutenticado(AuthenticationException ex) {
    log.warn("Falha de autenticação: {}", ex.getMessage());
    return problema(UNAUTHORIZED, "Não autenticado", "Credenciais ausentes ou inválidas");
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ProblemDetail acessoNegado(AccessDeniedException ex) {
    log.warn("Acesso negado: {}", ex.getMessage());
    return problema(FORBIDDEN, "Acesso negado", "Você não tem permissão para realizar esta operação");
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
