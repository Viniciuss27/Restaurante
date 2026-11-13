package vinix.compartilhado.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import vinix.compartilhado.excecoes.ConflitoException;
import vinix.compartilhado.excecoes.NaoExistenteException;
import vinix.compartilhado.excecoes.RegraDeNegocioException;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  private static class AlimentoIndisponivelException extends RegraDeNegocioException {
    AlimentoIndisponivelException(String mensagem) {
      super(mensagem);
    }
  }

  private record DtoDeTeste(String nome) {}

  @SuppressWarnings("unused")
  private void metodoAlvo(DtoDeTeste dto) {}

  @Test
  void naoExistente_deveRetornar404() {
    ProblemDetail problema = handler.naoExistente(new NaoExistenteException("5 -> id não encontrado"));

    assertThat(problema.getStatus()).isEqualTo(404);
    assertThat(problema.getTitle()).isEqualTo("Recurso não encontrado");
    assertThat(problema.getDetail()).isEqualTo("5 -> id não encontrado");
  }

  @Test
  void conflito_deveRetornar409() {
    ProblemDetail problema = handler.conflito(new ConflitoException("Carnes -> categoria já existe"));

    assertThat(problema.getStatus()).isEqualTo(409);
    assertThat(problema.getTitle()).isEqualTo("Conflito de dados");
    assertThat(problema.getDetail()).isEqualTo("Carnes -> categoria já existe");
  }

  @Test
  void regraDeNegocio_deveRetornar422() {
    ProblemDetail problema = handler.regraDeNegocio(new RegraDeNegocioException("retirada em menos de 20 min"));

    assertThat(problema.getStatus()).isEqualTo(422);
    assertThat(problema.getTitle()).isEqualTo("Regra de negócio violada");
    assertThat(problema.getDetail()).isEqualTo("retirada em menos de 20 min");
  }

  @Test
  void subclasseDeRegraDeNegocioDeOutroModulo_ehTratadaPelaBase() {
    ProblemDetail problema = handler.regraDeNegocio(new AlimentoIndisponivelException("Alimento 3 indisponível"));

    assertThat(problema.getStatus()).isEqualTo(422);
    assertThat(problema.getDetail()).isEqualTo("Alimento 3 indisponível");
  }

  @Test
  void violacaoDeIntegridade_deveRetornar409SemExporDetalhesDoBanco() {
    var excecao = new DataIntegrityViolationException(
        "could not execute statement", new RuntimeException("duplicate key value violates uk_categorias_nome"));

    ProblemDetail problema = handler.integridade(excecao);

    assertThat(problema.getStatus()).isEqualTo(409);
    assertThat(problema.getTitle()).isEqualTo("Violação de integridade");
    assertThat(problema.getDetail()).doesNotContain("uk_categorias_nome");
  }

  @Test
  void naoAutenticado_deveRetornar401ComMensagemGenerica() {
    ProblemDetail problema = handler.naoAutenticado(new BadCredentialsException("usuário joao não existe"));

    assertThat(problema.getStatus()).isEqualTo(401);
    assertThat(problema.getTitle()).isEqualTo("Não autenticado");
    assertThat(problema.getDetail()).doesNotContain("joao");
  }

  @Test
  void acessoNegado_deveRetornar403() {
    ProblemDetail problema = handler.acessoNegado(new AccessDeniedException("pedido de outro cliente"));

    assertThat(problema.getStatus()).isEqualTo(403);
    assertThat(problema.getTitle()).isEqualTo("Acesso negado");
    assertThat(problema.getDetail()).doesNotContain("outro cliente");
  }

  @Test
  void erroInesperado_deveRetornar500SemVazarAMensagemOriginal() {
    ProblemDetail problema = handler.inesperado(new IllegalStateException("senha do banco: 123"));

    assertThat(problema.getStatus()).isEqualTo(500);
    assertThat(problema.getTitle()).isEqualTo("Erro interno");
    assertThat(problema.getDetail()).isEqualTo("Erro interno no servidor").doesNotContain("senha");
  }

  @Test
  void validacao_deveRetornar400ComListaDeCamposInvalidos() throws Exception {
    Method alvo = getClass().getDeclaredMethod("metodoAlvo", DtoDeTeste.class);
    var resultado = new BeanPropertyBindingResult(new DtoDeTeste(""), "dtoDeTeste");
    resultado.addError(new FieldError("dtoDeTeste", "nome", "O nome é obrigatório"));
    var excecao = new MethodArgumentNotValidException(new MethodParameter(alvo, 0), resultado);

    ResponseEntity<Object> resposta = handler.handleMethodArgumentNotValid(
        excecao, new HttpHeaders(), HttpStatus.BAD_REQUEST,
        new ServletWebRequest(new MockHttpServletRequest("POST", "/categorias")));

    assertThat(resposta.getStatusCode().value()).isEqualTo(400);
    assertThat(resposta.getBody()).isInstanceOf(ProblemDetail.class);
    ProblemDetail problema = (ProblemDetail) resposta.getBody();
    assertThat(problema.getTitle()).isEqualTo("Erro de validação");
    assertThat(problema.getProperties()).containsEntry("erros",
        List.of(Map.of("campo", "nome", "mensagem", "O nome é obrigatório")));
  }
}
