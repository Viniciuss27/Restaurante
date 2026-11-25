package vinix.pedido.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import vinix.catalogo.Tamanho;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tb_item_pedido")
public class ItemPedido {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long produtoId;

  @Column(nullable = false, name = "nome_produto")
  private String nomeProduto;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private Tamanho tamanho;

  @Column(nullable = false, precision = 10, scale = 2)
  private BigDecimal precoUnitario;

  @Column(nullable = false)
  private Integer quantidade;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "pedido_id", nullable = false)
  private Pedido pedido;

  @OneToMany(
      mappedBy = "itemPedido", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ItemPedidoAlimento> alimentos = new ArrayList<>();

  public List<ItemPedidoAlimento> getItens() {
    return Collections.unmodifiableList(alimentos);
  }

  void setPedido(Pedido pedido) {
    this.pedido = pedido;
  }

  public void adicionarAlimento(ItemPedidoAlimento alimento) {
    alimentos.add(alimento);
    alimento.setItemPedido(this);
  }
}

