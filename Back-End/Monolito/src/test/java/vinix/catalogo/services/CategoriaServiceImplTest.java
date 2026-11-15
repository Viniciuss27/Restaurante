package vinix.catalogo.services;

import vinix.catalogo.GrupoAlimentar;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import vinix.catalogo.dtos.request.CategoriaRequestDTO;
import vinix.catalogo.dtos.response.CategoriaResponseDTO;
import vinix.catalogo.entities.Categoria;
import vinix.catalogo.mappers.CategoriaMapper;
import vinix.catalogo.mappers.CategoriaMapperImpl;
import vinix.catalogo.repositories.CategoriaRepository;
import vinix.catalogo.repositories.AlimentoRepository;
import vinix.compartilhado.excecoes.ConflitoException;
import vinix.compartilhado.excecoes.NaoExistenteException;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceImplTest {

  @Mock private AlimentoRepository alimentoRepository;
  @Mock private CategoriaRepository repository;
  @Spy private CategoriaMapper mapper = new CategoriaMapperImpl();
  @InjectMocks private CategoriaServiceImpl service;

  private Categoria categoria(Long id, String nome) {
    return Categoria.builder().grupo(GrupoAlimentar.CARNE).id(id).nome(nome).build();
  }

  // ---------- listar ----------

  @Test
  void listar_deveRetornarCategoriasMapeadas() {
    when(repository.findAll()).thenReturn(List.of(categoria(1L, "Carnes"), categoria(2L, "Massas")));

    List<CategoriaResponseDTO> resultado = service.listar();

    assertThat(resultado).containsExactly(
        new CategoriaResponseDTO(1L, "Carnes", GrupoAlimentar.CARNE), new CategoriaResponseDTO(2L, "Massas", GrupoAlimentar.CARNE));
  }

  @Test
  void listar_semCategorias_deveRetornarListaVazia() {
    when(repository.findAll()).thenReturn(List.of());

    assertThat(service.listar()).isEmpty();
  }

  // ---------- buscarId ----------

  @Test
  void buscarId_existente_deveRetornarCategoria() {
    when(repository.findById(1L)).thenReturn(Optional.of(categoria(1L, "Carnes")));

    assertThat(service.buscarId(1L)).isEqualTo(new CategoriaResponseDTO(1L, "Carnes", GrupoAlimentar.CARNE));
  }

  @Test
  void buscarId_inexistente_deveLancarNaoExistente() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.buscarId(99L))
        .isInstanceOf(NaoExistenteException.class)
        .hasMessageContaining("99");
  }

  // ---------- buscarCategoria ----------

  @Test
  void buscarCategoria_existente_deveRetornarCategoria() {
    when(repository.findByNomeIgnoreCase("carnes")).thenReturn(Optional.of(categoria(1L, "Carnes")));

    assertThat(service.buscarCategoria("carnes")).isEqualTo(new CategoriaResponseDTO(1L, "Carnes", GrupoAlimentar.CARNE));
  }

  @Test
  void buscarCategoria_inexistente_deveLancarNaoExistente() {
    when(repository.findByNomeIgnoreCase("sobremesas")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.buscarCategoria("sobremesas"))
        .isInstanceOf(NaoExistenteException.class)
        .hasMessageContaining("sobremesas");
  }

  // ---------- criar ----------

  @Test
  void criar_nomeNovo_deveSalvarERetornarComId() {
    when(repository.existsByNomeIgnoreCase("Carnes")).thenReturn(false);
    when(repository.save(any(Categoria.class))).thenAnswer(invocacao -> {
      Categoria salva = invocacao.getArgument(0);
      salva.setId(1L);
      return salva;
    });

    CategoriaResponseDTO resultado = service.criar(new CategoriaRequestDTO("Carnes", GrupoAlimentar.CARNE));

    assertThat(resultado).isEqualTo(new CategoriaResponseDTO(1L, "Carnes", GrupoAlimentar.CARNE));
    ArgumentCaptor<Categoria> captor = ArgumentCaptor.forClass(Categoria.class);
    verify(repository).save(captor.capture());
    assertThat(captor.getValue().getNome()).isEqualTo("Carnes");
  }

  @Test
  void criar_nomeDuplicado_deveLancarConflitoESemSalvar() {
    when(repository.existsByNomeIgnoreCase("carnes")).thenReturn(true);

    assertThatThrownBy(() -> service.criar(new CategoriaRequestDTO("carnes", GrupoAlimentar.CARNE)))
        .isInstanceOf(ConflitoException.class)
        .hasMessageContaining("carnes");

    verify(repository, never()).save(any());
  }

  // ---------- atualizar ----------

  @Test
  void atualizar_nomeLivre_deveAlterarONome() {
    Categoria existente = categoria(1L, "Carnes");
    when(repository.findById(1L)).thenReturn(Optional.of(existente));
    when(repository.existsByNomeIgnoreCaseAndIdNot("Massas", 1L)).thenReturn(false);

    CategoriaResponseDTO resultado = service.atualizar(1L, new CategoriaRequestDTO("Massas", GrupoAlimentar.CARNE));

    assertThat(existente.getNome()).isEqualTo("Massas");
    assertThat(resultado).isEqualTo(new CategoriaResponseDTO(1L, "Massas", GrupoAlimentar.CARNE));
    verify(repository, never()).save(any()); // dirty checking grava no commit
  }

  @Test
  void atualizar_idInexistente_deveLancarNaoExistenteSemChecarDuplicidade() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.atualizar(99L, new CategoriaRequestDTO("Massas", GrupoAlimentar.CARNE)))
        .isInstanceOf(NaoExistenteException.class);

    verify(repository, never()).existsByNomeIgnoreCaseAndIdNot(anyString(), anyLong());
  }

  @Test
  void atualizar_nomeDeOutraCategoria_deveLancarConflitoSemAlterar() {
    Categoria existente = categoria(1L, "Carnes");
    when(repository.findById(1L)).thenReturn(Optional.of(existente));
    when(repository.existsByNomeIgnoreCaseAndIdNot("Massas", 1L)).thenReturn(true);

    assertThatThrownBy(() -> service.atualizar(1L, new CategoriaRequestDTO("Massas", GrupoAlimentar.CARNE)))
        .isInstanceOf(ConflitoException.class);

    assertThat(existente.getNome()).isEqualTo("Carnes");
  }

  // ---------- excluir ----------

  @Test
  void excluir_semAlimentos_deveExcluir() {
    when(repository.findById(1L)).thenReturn(Optional.of(categoria(1L, "Carnes")));
    when(alimentoRepository.existsByCategoriaId(1L)).thenReturn(false);

    service.excluir(1L);

    verify(repository).deleteById(1L);
  }

  @Test
  void excluir_idInexistente_deveLancarNaoExistenteSemTocarEmAlimentos() {
    when(repository.findById(99L)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.excluir(99L)).isInstanceOf(NaoExistenteException.class);

    verifyNoInteractions(alimentoRepository);
    verify(repository, never()).deleteById(anyLong());
  }

  @Test
  void excluir_comAlimentosAssociados_deveLancarConflitoSemExcluir() {
    when(repository.findById(1L)).thenReturn(Optional.of(categoria(1L, "Carnes")));
    when(alimentoRepository.existsByCategoriaId(1L)).thenReturn(true);

    assertThatThrownBy(() -> service.excluir(1L))
        .isInstanceOf(ConflitoException.class)
        .hasMessageContaining("alimentos associados");

    verify(repository, never()).deleteById(anyLong());
  }
}
