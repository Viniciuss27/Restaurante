package vinix.catalogo.dtos.request;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class ProdutoRequestDTOTest {

  private static Validator validator;

  @BeforeAll
  static void criarValidador() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  private ProdutoRequestDTO dto(String nome, String descricao, Long categoriaId, String imagemUrl) {
    return new ProdutoRequestDTO(nome, descricao, categoriaId, imagemUrl);
  }

  private Set<String> camposInvalidos(ProdutoRequestDTO dto) {
    return validator.validate(dto).stream()
        .map(v -> v.getPropertyPath().toString()).collect(Collectors.toSet());
  }

  @Test
  void dadosValidos_naoDeveGerarViolacoes() {
    assertThat(validator.validate(dto("Costela", "Assada", 1L, "http://img/costela.png"))).isEmpty();
  }

  @Test
  void camposOpcionaisNulos_deveSerValido() {
    assertThat(validator.validate(dto("Costela", null, 1L, null))).isEmpty();
  }

  // ---------- nome ----------

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void nomeNuloOuEmBranco_deveSerInvalido(String nome) {
    assertThat(camposInvalidos(dto(nome, null, 1L, null))).containsExactly("nome");
  }

  @Test
  void nomeObrigatorio_deveUsarMensagemEsperada() {
    Set<String> mensagens = validator.validate(dto(null, null, 1L, null)).stream()
        .map(ConstraintViolation::getMessage).collect(Collectors.toSet());

    assertThat(mensagens).containsExactly("O nome é obrigatório");
  }

  @Test
  void nomeForaDosLimites_deveSerInvalido() {
    assertThat(camposInvalidos(dto("ab", null, 1L, null))).containsExactly("nome");
    assertThat(camposInvalidos(dto("a".repeat(121), null, 1L, null))).containsExactly("nome");
  }

  @Test
  void nomeNosLimites_deveSerValido() {
    assertThat(validator.validate(dto("abc", null, 1L, null))).isEmpty();
    assertThat(validator.validate(dto("a".repeat(120), null, 1L, null))).isEmpty();
  }

  // ---------- descrição ----------

  @Test
  void descricaoComMaisDeMilCaracteres_deveSerInvalida() {
    assertThat(camposInvalidos(dto("Costela", "a".repeat(1001), 1L, null))).containsExactly("descricao");
  }

  @Test
  void descricaoComMilCaracteres_deveSerValida() {
    assertThat(validator.validate(dto("Costela", "a".repeat(1000), 1L, null))).isEmpty();
  }

  // ---------- categoria ----------

  @Test
  void categoriaIdNulo_deveSerInvalido() {
    assertThat(camposInvalidos(dto("Costela", null, null, null))).containsExactly("categoriaId");
  }

  @ParameterizedTest
  @ValueSource(longs = {0, -1})
  void categoriaIdNaoPositivo_deveSerInvalido(long categoriaId) {
    assertThat(camposInvalidos(dto("Costela", null, categoriaId, null))).containsExactly("categoriaId");
  }

  // ---------- imagem ----------

  @Test
  void imagemUrlComMaisDe2048Caracteres_deveSerInvalida() {
    assertThat(camposInvalidos(dto("Costela", null, 1L, "a".repeat(2049)))).containsExactly("imagemUrl");
  }

  @Test
  void imagemUrlCom2048Caracteres_deveSerValida() {
    assertThat(validator.validate(dto("Costela", null, 1L, "a".repeat(2048)))).isEmpty();
  }
}
