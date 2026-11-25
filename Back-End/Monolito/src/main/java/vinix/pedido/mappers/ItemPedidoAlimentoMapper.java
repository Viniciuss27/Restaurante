package vinix.pedido.mappers;

import org.mapstruct.Mapper;
import vinix.pedido.dtos.response.ItemPedidoAlimentoResponseDTO;
import vinix.pedido.entities.ItemPedidoAlimento;

@Mapper(componentModel = "spring")
public interface ItemPedidoAlimentoMapper {

  ItemPedidoAlimentoResponseDTO toResponse(ItemPedidoAlimento entity);

}
