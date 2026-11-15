package vinix.catalogo.services;

import org.springframework.data.jpa.domain.Specification;
import vinix.catalogo.entities.Alimento;

public class AlimentoSpecification {

    public static Specification<Alimento> nomeContem(String nome) {
        return (root, query, cb) ->
            nome == null || nome.isBlank() ? null : cb.like(
                cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<Alimento> descricaoContem(String descricao) {
        return (root, query, cb) ->
            descricao == null || descricao.isBlank() ? null : cb.like(
                cb.lower(root.get("descricao")), "%" + descricao.toLowerCase() + "%");
    }

    public static Specification<Alimento> categoriaContem(String categoria) {
        return (root, query, cb) ->
            categoria == null || categoria.isBlank() ? null : cb.like(
                cb.lower(root.join("categoria").get("nome")), "%" + categoria.toLowerCase() + "%");
    }

    public static Specification<Alimento> disponivelIgual(Boolean disponivel) {
        return (root, query, cb) ->
            disponivel == null ? null : cb.equal(root.get("disponivel"), disponivel);
    }
}