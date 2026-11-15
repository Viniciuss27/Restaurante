package vinix.catalogo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import vinix.catalogo.dtos.request.AlimentoRequestDTO;
import vinix.catalogo.dtos.response.AlimentoResponseDTO;
import vinix.catalogo.entities.Categoria;
import vinix.catalogo.entities.Alimento;
import vinix.catalogo.mappers.AlimentoMapper;
import vinix.catalogo.mappers.AlimentoMapperImpl;
import vinix.catalogo.repositories.CategoriaRepository;
import vinix.catalogo.repositories.AlimentoRepository;
import vinix.compartilhado.excecoes.NaoExistenteException;

@ExtendWith(MockitoExtension.class)
class AlimentoServiceImplTest {

  @Mock private CategoriaRepository categoriaRepository;
  @Mock private AlimentoRepository repository;
  @Spy private AlimentoMapper mapper = new AlimentoMapperImpl();
  @InjectMocks private AlimentoServiceImpl service;
  @Captor private ArgumentCaptor<Specification<Alimento>> specCaptor;

  private Categoria categoria(Long id, String nome) {
    return Categoria.builder().id(id).nome(nome).build();
  }

  private Alimento alimento(Long id, String nome, Categoria categoria) {
    return Alimento.builder().id(id).nome(nome).descricao("Descrição " + nome)
        .categoria(categoria).imagemUrl("http://img/" + id + ".png").build();
  }

  // ---------- buscarId ----------

  @Test
  void buscarId_existente_deveRetornarAlimentoComDadosDaCategoria() {
    Alimento costela = alimento(10L, "Costela", categoria(1L, "Carnes"));
    when(repository.findById(10L)).thenReturn(Optional.of(costela));

    AlimentoResponseDTO resultado = service.buscarId(10L);

    assertThat(resultado).isEqualTo(new AlimentoResponseDTO(
        10L, "Costela", "Descrição Costela", 1L, "Carnes", "http://img/10.png", true));
  }

  @Test
  void buscarId_inexistente_deveLancarNaoExistente() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.buscarId(99L))
        .isInstanceOf(NaoExistenteException.class)
        .hasMessageContaining("99");
  }

  // ---------- buscar ----------

  @Test
  void buscar_comFiltros_deveDelegarAoRepositorioEMapearResultado() {
    Categoria carnes = categoria(1L, "Carnes");
    when(repository.findAll(ArgumentMatchers.<Specification<Alimento>>any()))
        .thenReturn(List.of(alimento(10L, "Costela", carnes), alimento(11L, "Churrasco", carnes)));

    List<AlimentoResponseDTO> resultado = service.buscar("c", "desc", "carnes", true);

    assertThat(resultado).extracting(AlimentoResponseDTO::nome).containsExactly("Costela", "Churrasco");
    verify(repository).findAll(specCaptor.capture());
    assertThat(specCaptor.getValue()).isNotNull();
  }

  @Test
  void buscar_semFiltrosESemResultados_deveRetornarListaVazia() {
    when(repository.findAll(ArgumentMatchers.<Specification<Alimento>>any())).thenReturn(List.of());

    assertThat(service.buscar(null, null, null, null)).isEmpty();
  }

  // ---------- criar ----------

  @Test
  void criar_categoriaExistente_deveSalvarAlimentoDisponivelComACategoria() {
    Categoria carnes = categoria(1L, "Carnes");
    when(categoriaRepository.findById(1L)).thenReturn(Optional.of(carnes));
    when(repository.save(any(Alimento.class))).thenAnswer(invocacao -> {
      Alimento salvo = invocacao.getArgument(0);
      salvo.setId(10L);
      return salvo;
    });

    AlimentoResponseDTO resultado = service.criar(
        new AlimentoRequestDTO("Costela", "Assada", 1L, "http://img/costela.png"));

    assertThat(resultado).isEqualTo(new AlimentoResponseDTO(
        10L, "Costela", "Assada", 1L, "Carnes", "http://img/costela.png", true));

    ArgumentCaptor<Alimento> captor = ArgumentCaptor.forClass(Alimento.class);
    verify(repository).save(captor.capture());
    Alimento salvo = captor.getValue();
    assertThat(salvo.getCategoria()).isSameAs(carnes);
    assertThat(salvo.isDisponivel()).isTrue();
  }

  @Test
  void criar_categoriaInexistente_deveLancarNaoExistenteSemSalvar() {
    when(categoriaRepository.findById(5L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.criar(new AlimentoRequestDTO("Costela", "Assada", 5L, null)))
        .isInstanceOf(NaoExistenteException.class)
        .hasMessageContaining("categoria");

    verify(repository, never()).save(any());
  }

  // ---------- atualizar ----------

  @Test
  void atualizar_valido_deveAlterarCamposETrocarCategoria() {
    Categoria carnes = categoria(1L, "Carnes");
    Categoria massas = categoria(2L, "Massas");
    Alimento existente = alimento(10L, "Costela", carnes);
    when(repository.findById(10L)).thenReturn(Optional.of(existente));
    when(categoriaRepository.findById(2L)).thenReturn(Optional.of(massas));

    AlimentoResponseDTO resultado = service.atualizar(
        10L, new AlimentoRequestDTO("Macarrão", "Alho e óleo", 2L, "http://img/novo.png"));

    assertThat(existente.getNome()).isEqualTo("Macarrão");
    assertThat(existente.getDescricao()).isEqualTo("Alho e óleo");
    assertThat(existente.getImagemUrl()).isEqualTo("http://img/novo.png");
    assertThat(existente.getCategoria()).isSameAs(massas);
    assertThat(resultado.categoriaNome()).isEqualTo("Massas");
    verify(repository, never()).save(any());
  }

  @Test
  void atualizar_alimentoInexistente_deveLancarNaoExistenteSemBuscarCategoria() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.atualizar(99L, new AlimentoRequestDTO("X", null, 1L, null)))
        .isInstanceOf(NaoExistenteException.class);

    verifyNoInteractions(categoriaRepository);
  }

  @Test
  void atualizar_categoriaInexistente_deveLancarNaoExistenteSemAlterarAlimento() {
    Alimento existente = alimento(10L, "Costela", categoria(1L, "Carnes"));
    when(repository.findById(10L)).thenReturn(Optional.of(existente));
    when(categoriaRepository.findById(7L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.atualizar(10L, new AlimentoRequestDTO("Novo nome", null, 7L, null)))
        .isInstanceOf(NaoExistenteException.class)
        .hasMessageContaining("categoria");

    assertThat(existente.getNome()).isEqualTo("Costela");
  }

  // ---------- alterarDisponibilidade ----------

  @Test
  void alterarDisponibilidade_desativar_deveMarcarIndisponivel() {
    Alimento existente = alimento(10L, "Costela", categoria(1L, "Carnes"));
    when(repository.findById(10L)).thenReturn(Optional.of(existente));

    service.alterarDisponibilidade(10L, false);

    assertThat(existente.isDisponivel()).isFalse();
  }

  @Test
  void alterarDisponibilidade_reativar_deveMarcarDisponivel() {
    Alimento existente = alimento(10L, "Costela", categoria(1L, "Carnes"));
    existente.setDisponivel(false);
    when(repository.findById(10L)).thenReturn(Optional.of(existente));

    service.alterarDisponibilidade(10L, true);

    assertThat(existente.isDisponivel()).isTrue();
  }

  @Test
  void alterarDisponibilidade_estadoJaAplicado_deveSerIdempotente() {
    Alimento existente = alimento(10L, "Costela", categoria(1L, "Carnes"));
    existente.setDisponivel(false);
    when(repository.findById(10L)).thenReturn(Optional.of(existente));

    service.alterarDisponibilidade(10L, false);

    assertThat(existente.isDisponivel()).isFalse();
  }

  @Test
  void alterarDisponibilidade_alimentoInexistente_deveLancarNaoExistente() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.alterarDisponibilidade(99L, true))
        .isInstanceOf(NaoExistenteException.class);
  }
}
