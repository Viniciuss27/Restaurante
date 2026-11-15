package vinix.catalogo.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.MapKeyEnumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import vinix.catalogo.Tamanho;
import vinix.catalogo.TipoProduto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
@Entity @Table(name = "tb_produtos")
public class Produto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 120)
  private String nome;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private TipoProduto tipo;

  // preço de cada tamanho: marmita (PEQUENA, MEDIA, GRANDE); bebida e sobremesa (UNICO)
  @Builder.Default
  @ElementCollection(fetch = FetchType.LAZY)
  @CollectionTable(name = "tb_produto_precos", joinColumns = @JoinColumn(name = "produto_id"))
  @MapKeyColumn(name = "tamanho", length = 10)
  @MapKeyEnumerated(EnumType.STRING)
  @Column(name = "preco", nullable = false, precision = 6, scale = 2)
  private Map<Tamanho, BigDecimal> precos = new HashMap<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "alimento_id")
  private Alimento alimento;

  @Builder.Default
  @Column(nullable = false)
  private boolean disponivel = true;

  @Column(nullable = false, updatable = false, name = "data_cadastro")
  @CreationTimestamp
  private Instant dataCadastro;

  @Column(nullable = false, name = "data_alteracao")
  @UpdateTimestamp
  private Instant dataAlteracao;

  // Vendável agora: produto ativo e, quando há alimento ligado, alimento também disponível
  public boolean estaDisponivelParaVenda() {
    return disponivel && (alimento == null || alimento.isDisponivel());
  }
}
