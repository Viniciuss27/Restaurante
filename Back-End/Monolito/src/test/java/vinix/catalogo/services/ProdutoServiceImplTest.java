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
import vinix.catalogo.dtos.request.ProdutoRequestDTO;
import vinix.catalogo.dtos.response.ProdutoResponseDTO;
import vinix.catalogo.entities.Categoria;
import vinix.catalogo.entities.Produto;
import vinix.catalogo.exceptions.NaoExistenteException;
import vinix.catalogo.mappers.ProdutoMapper;
import vinix.catalogo.mappers.ProdutoMapperImpl;
import vinix.catalogo.repositories.CategoriaRepository;
import vinix.catalogo.repositories.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceImplTest {

  @Mock private CategoriaRepository categoriaRepository;
  @Mock private ProdutoRepository repository;
  @Spy private ProdutoMapper mapper = new ProdutoMapperImpl();
  @InjectMocks private ProdutoServiceImpl service;
  @Captor private ArgumentCaptor<Specification<Produto>> specCaptor;

  private Categoria categoria(Long id, String nome) {
    return Categoria.builder().id(id).nome(nome).build();
  }

  private Produto produto(Long id, String nome, Categoria categoria) {
    return Produto.builder().id(id).nome(nome).descricao("Descrição " + nome)
        .categoria(categoria).imagemUrl("http://img/" + id + ".png").build();
  }

  // ---------- buscarId ----------

  @Test
  void buscarId_existente_deveRetornarProdutoComDadosDaCategoria() {
    Produto costela = produto(10L, "Costela", categoria(1L, "Carnes"));
    when(repository.findById(10L)).thenReturn(Optional.of(costela));

    ProdutoResponseDTO resultado = service.buscarId(10L);

    assertThat(resultado).isEqualTo(new ProdutoResponseDTO(
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
    when(repository.findAll(ArgumentMatchers.<Specification<Produto>>any()))
        .thenReturn(List.of(produto(10L, "Costela", carnes), produto(11L, "Churrasco", carnes)));

    List<ProdutoResponseDTO> resultado = service.buscar("c", "desc", "carnes", true);

    assertThat(resultado).extracting(ProdutoResponseDTO::nome).containsExactly("Costela", "Churrasco");
    verify(repository).findAll(specCaptor.capture());
    assertThat(specCaptor.getValue()).isNotNull();
  }

  @Test
  void buscar_semFiltrosESemResultados_deveRetornarListaVazia() {
    when(repository.findAll(ArgumentMatchers.<Specification<Produto>>any())).thenReturn(List.of());

    assertThat(service.buscar(null, null, null, null)).isEmpty();
  }

  // ---------- criar ----------

  @Test
  void criar_categoriaExistente_deveSalvarProdutoDisponivelComACategoria() {
    Categoria carnes = categoria(1L, "Carnes");
    when(categoriaRepository.findById(1L)).thenReturn(Optional.of(carnes));
    when(repository.save(any(Produto.class))).thenAnswer(invocacao -> {
      Produto salvo = invocacao.getArgument(0);
      salvo.setId(10L);
      return salvo;
    });

    ProdutoResponseDTO resultado = service.criar(
        new ProdutoRequestDTO("Costela", "Assada", 1L, "http://img/costela.png"));

    assertThat(resultado).isEqualTo(new ProdutoResponseDTO(
        10L, "Costela", "Assada", 1L, "Carnes", "http://img/costela.png", true));

    ArgumentCaptor<Produto> captor = ArgumentCaptor.forClass(Produto.class);
    verify(repository).save(captor.capture());
    Produto salvo = captor.getValue();
    assertThat(salvo.getCategoria()).isSameAs(carnes);
    assertThat(salvo.isDisponivel()).isTrue();
  }

  @Test
  void criar_categoriaInexistente_deveLancarNaoExistenteSemSalvar() {
    when(categoriaRepository.findById(5L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.criar(new ProdutoRequestDTO("Costela", "Assada", 5L, null)))
        .isInstanceOf(NaoExistenteException.class)
        .hasMessageContaining("categoria");

    verify(repository, never()).save(any());
  }

  // ---------- atualizar ----------

  @Test
  void atualizar_valido_deveAlterarCamposETrocarCategoria() {
    Categoria carnes = categoria(1L, "Carnes");
    Categoria massas = categoria(2L, "Massas");
    Produto existente = produto(10L, "Costela", carnes);
    when(repository.findById(10L)).thenReturn(Optional.of(existente));
    when(categoriaRepository.findById(2L)).thenReturn(Optional.of(massas));

    ProdutoResponseDTO resultado = service.atualizar(
        10L, new ProdutoRequestDTO("Macarrão", "Alho e óleo", 2L, "http://img/novo.png"));

    assertThat(existente.getNome()).isEqualTo("Macarrão");
    assertThat(existente.getDescricao()).isEqualTo("Alho e óleo");
    assertThat(existente.getImagemUrl()).isEqualTo("http://img/novo.png");
    assertThat(existente.getCategoria()).isSameAs(massas);
    assertThat(resultado.categoriaNome()).isEqualTo("Massas");
    verify(repository, never()).save(any());
  }

  @Test
  void atualizar_produtoInexistente_deveLancarNaoExistenteSemBuscarCategoria() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.atualizar(99L, new ProdutoRequestDTO("X", null, 1L, null)))
        .isInstanceOf(NaoExistenteException.class);

    verifyNoInteractions(categoriaRepository);
  }

  @Test
  void atualizar_categoriaInexistente_deveLancarNaoExistenteSemAlterarProduto() {
    Produto existente = produto(10L, "Costela", categoria(1L, "Carnes"));
    when(repository.findById(10L)).thenReturn(Optional.of(existente));
    when(categoriaRepository.findById(7L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.atualizar(10L, new ProdutoRequestDTO("Novo nome", null, 7L, null)))
        .isInstanceOf(NaoExistenteException.class)
        .hasMessageContaining("categoria");

    assertThat(existente.getNome()).isEqualTo("Costela");
  }

  // ---------- alterarDisponibilidade ----------

  @Test
  void alterarDisponibilidade_desativar_deveMarcarIndisponivel() {
    Produto existente = produto(10L, "Costela", categoria(1L, "Carnes"));
    when(repository.findById(10L)).thenReturn(Optional.of(existente));

    service.alterarDisponibilidade(10L, false);

    assertThat(existente.isDisponivel()).isFalse();
  }

  @Test
  void alterarDisponibilidade_reativar_deveMarcarDisponivel() {
    Produto existente = produto(10L, "Costela", categoria(1L, "Carnes"));
    existente.setDisponivel(false);
    when(repository.findById(10L)).thenReturn(Optional.of(existente));

    service.alterarDisponibilidade(10L, true);

    assertThat(existente.isDisponivel()).isTrue();
  }

  @Test
  void alterarDisponibilidade_estadoJaAplicado_deveSerIdempotente() {
    Produto existente = produto(10L, "Costela", categoria(1L, "Carnes"));
    existente.setDisponivel(false);
    when(repository.findById(10L)).thenReturn(Optional.of(existente));

    service.alterarDisponibilidade(10L, false);

    assertThat(existente.isDisponivel()).isFalse();
  }

  @Test
  void alterarDisponibilidade_produtoInexistente_deveLancarNaoExistente() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.alterarDisponibilidade(99L, true))
        .isInstanceOf(NaoExistenteException.class);
  }
}
