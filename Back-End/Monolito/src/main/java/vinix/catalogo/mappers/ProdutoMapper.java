package vinix.catalogo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vinix.catalogo.dtos.request.ProdutoRequestDTO;
import vinix.catalogo.dtos.response.ProdutoResponseDTO;
import vinix.catalogo.entities.Produto;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

  @Mapping(source = "categoria.id", target = "categoriaId")
  @Mapping(source = "categoria.nome", target = "categoriaNome")
  ProdutoResponseDTO response(Produto produto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "dataCadastro", ignore = true)
  @Mapping(target = "dataAlteracao", ignore = true)
  @Mapping(target = "disponivel", ignore = true)
  @Mapping(target = "categoria", ignore = true)
  Produto request(ProdutoRequestDTO request);
}
