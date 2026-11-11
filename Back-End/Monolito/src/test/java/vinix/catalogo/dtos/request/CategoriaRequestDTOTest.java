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

class CategoriaRequestDTOTest {

  private static Validator validator;

  @BeforeAll
  static void criarValidador() {
    validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  private Set<String> camposInvalidos(CategoriaRequestDTO dto) {
    return validator.validate(dto).stream()
        .map(v -> v.getPropertyPath().toString()).collect(Collectors.toSet());
  }

  @Test
  void nomeValido_naoDeveGerarViolacoes() {
    assertThat(validator.validate(new CategoriaRequestDTO("Carnes"))).isEmpty();
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"   "})
  void nomeNuloOuEmBranco_deveSerInvalido(String nome) {
    assertThat(camposInvalidos(new CategoriaRequestDTO(nome))).containsExactly("nome");
  }

  @Test
  void nomeObrigatorio_deveUsarMensagemEsperada() {
    Set<String> mensagens = validator.validate(new CategoriaRequestDTO(null)).stream()
        .map(ConstraintViolation::getMessage).collect(Collectors.toSet());

    assertThat(mensagens).containsExactly("O nome é obrigatório");
  }

  @Test
  void nomeComMenosDeTresCaracteres_deveSerInvalido() {
    assertThat(camposInvalidos(new CategoriaRequestDTO("ab"))).containsExactly("nome");
  }

  @Test
  void nomeComTresCaracteres_deveSerValido() {
    assertThat(validator.validate(new CategoriaRequestDTO("abc"))).isEmpty();
  }

  @Test
  void nomeComMaisDeCemCaracteres_deveSerInvalido() {
    assertThat(camposInvalidos(new CategoriaRequestDTO("a".repeat(101)))).containsExactly("nome");
  }

  @Test
  void nomeComCemCaracteres_deveSerValido() {
    assertThat(validator.validate(new CategoriaRequestDTO("a".repeat(100)))).isEmpty();
  }
}
