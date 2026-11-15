package vinix.catalogo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vinix.catalogo.dtos.request.AlimentoRequestDTO;
import vinix.catalogo.dtos.response.AlimentoResponseDTO;
import vinix.catalogo.entities.Alimento;

@Mapper(componentModel = "spring")
public interface AlimentoMapper {

  @Mapping(source = "categoria.id", target = "categoriaId")
  @Mapping(source = "categoria.nome", target = "categoriaNome")
  AlimentoResponseDTO response(Alimento alimento);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "dataCadastro", ignore = true)
  @Mapping(target = "dataAlteracao", ignore = true)
  @Mapping(target = "disponivel", ignore = true)
  @Mapping(target = "categoria", ignore = true)
  Alimento request(AlimentoRequestDTO request);
}
