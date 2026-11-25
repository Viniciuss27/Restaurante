package vinix.pedido.mappers;

import org.mapstruct.Mapper;
import vinix.pedido.dtos.response.ItemPedidoResponseDTO;
import vinix.pedido.entities.ItemPedido;
import java.util.List;

@Mapper(componentModel = "spring", uses = ItemPedidoAlimentoMapper.class)
public interface ItemPedidoMapper {

  ItemPedidoResponseDTO toResponse(ItemPedido entity);

  List<ItemPedidoResponseDTO> toResponseList(List<ItemPedido> entities);
}
