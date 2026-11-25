package vinix.pedido.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vinix.pedido.entities.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
