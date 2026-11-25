package vinix.pedido.entities;

import java.util.Optional;

public enum StatusPedido {
  REALIZADO,
  CONFIRMADO,
  EM_PREPARACAO,
  PRONTO,
  FINALIZADO;

    public Optional<StatusPedido> proximo() {
        int i = ordinal() + 1;
        return i < values().length ? Optional.of(values()[i]) : Optional.empty();
    }
}
