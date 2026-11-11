package vinix.catalogo.exceptions;

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
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.ServletWebRequest;
import vinix.catalogo.dtos.request.CategoriaRequestDTO;

class GlobalExceptionHandlerTest {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @SuppressWarnings("unused")
  private void metodoAlvo(CategoriaRequestDTO dto) {  }

  @Test
  void naoExistente_deveRetornar404() {
    ProblemDetail problema = handler.naoExistente(new NaoExistenteException("5 -> id não encontrado"));

    assertThat(problema.getStatus()).isEqualTo(404);
    assertThat(problema.getTitle()).isEqualTo("Recurso não encontrado");
    assertThat(problema.getDetail()).isEqualTo("5 -> id não encontrado");
  }

  @Test
  void jaExistente_deveRetornar409() {
    ProblemDetail problema = handler.conflito(new JaExistenteException("Carnes -> categoria já existe"));

    assertThat(problema.getStatus()).isEqualTo(409);
    assertThat(problema.getTitle()).isEqualTo("Conflito de dados");
    assertThat(problema.getDetail()).isEqualTo("Carnes -> categoria já existe");
  }

  @Test
  void duplicado_deveRetornar409() {
    ProblemDetail problema = handler.conflito(new DuplicadoException("registro duplicado"));

    assertThat(problema.getStatus()).isEqualTo(409);
    assertThat(problema.getDetail()).isEqualTo("registro duplicado");
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
  void erroInesperado_deveRetornar500SemVazarAMensagemOriginal() {
    ProblemDetail problema = handler.inesperado(new IllegalStateException("senha do banco: 123"));

    assertThat(problema.getStatus()).isEqualTo(500);
    assertThat(problema.getTitle()).isEqualTo("Erro interno");
    assertThat(problema.getDetail()).isEqualTo("Erro interno no servidor").doesNotContain("senha");
  }

  @Test
  void validacao_deveRetornar400ComListaDeCamposInvalidos() throws Exception {
    Method alvo = getClass().getDeclaredMethod("metodoAlvo", CategoriaRequestDTO.class);
    var resultado = new BeanPropertyBindingResult(new CategoriaRequestDTO(""), "categoriaRequestDTO");
    resultado.addError(new FieldError("categoriaRequestDTO", "nome", "O nome é obrigatório"));
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
