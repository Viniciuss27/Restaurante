package vinix.pedido.mappers;

import org.mapstruct.Mapper;
import vinix.pedido.dtos.response.PedidoResponseDTO;
import vinix.pedido.entities.Pedido;
import java.util.List;

@Mapper(componentModel = "spring", uses = ItemPedidoMapper.class)
public interface PedidoMapper {

  PedidoResponseDTO toResponse (Pedido entity);

  List<PedidoResponseDTO> toResponseList (List<Pedido> entity);
}
