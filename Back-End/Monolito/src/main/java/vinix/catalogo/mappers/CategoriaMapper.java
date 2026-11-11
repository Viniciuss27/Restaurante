package vinix.catalogo.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vinix.catalogo.dtos.request.CategoriaRequestDTO;
import vinix.catalogo.dtos.response.CategoriaResponseDTO;
import vinix.catalogo.entities.Categoria;

@Mapper(componentModel = "spring")
public interface CategoriaMapper {

  CategoriaResponseDTO response(Categoria categoria);

  @Mapping(target = "id", ignore = true)
  Categoria request(CategoriaRequestDTO categoriaRequestDTO);
}
