package vinix.catalogo;

/** Visão imutável de um alimento para outros módulos. Nunca expõe a entidade JPA. */
public record AlimentoResumo(Long id, String nome, String descricao, boolean disponivel) {}
