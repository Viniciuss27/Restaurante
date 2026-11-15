package vinix.catalogo.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter @Builder
@Entity @Table(name = "tb_categorias")
public class Categoria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100, unique = true)
  private String nome;

  // Falso para categorias vendidas à parte (bebidas, sobremesas): alimentos que não entram na marmita
  @Builder.Default
  @Column(name = "compoe_marmita", nullable = false)
  private boolean compoeMarmita = true;

}
