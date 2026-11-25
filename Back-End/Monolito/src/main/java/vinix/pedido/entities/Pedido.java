package vinix.pedido.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import vinix.compartilhado.excecoes.RegraDeNegocioException;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_pedido")
public class Pedido {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private UUID clienteId;

  @Column(nullable = false, name = "nome_cliente")
  private String nomeCliente;

  @Column(nullable = false, name = "telefone_cliente")
  private String telefoneCliente;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private StatusPedido status;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal total;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private Instant dataCadastro;

  @Column(nullable = false, updatable = false)
  private Instant retiradaEm;

  @OneToMany(
      mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ItemPedido> itens = new ArrayList<>();

  public List<ItemPedido> getItens() {
    return Collections.unmodifiableList(itens);
  }

  public void adicionarItem(ItemPedido item) {
    itens.add(item);
    item.setPedido(this);
  }

  public void avancarStatus() {
    this.status = status.proximo()
        .orElseThrow(() -> new RegraDeNegocioException("O pedido já foi finalizado"));
  }
}