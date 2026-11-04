import { Link } from 'react-router-dom';
import { getProductName, mealSizes, formatCurrency } from '../data/menu';
import { useCart } from '../context/CartContext';

export default function CartPage() {
  const { meals, total, changeQuantity, removeMeal } = useCart();

  if (!meals.length) {
    return <div className="page-shell empty-state"><span className="empty-icon">＋</span><h1>Seu carrinho está vazio</h1><p>Escolha o tamanho da marmita e os itens do cardápio para montar seu pedido.</p><Link className="button button-primary" to="/cardapio">Ir para o cardápio</Link></div>;
  }

  return (
    <div className="page-shell cart-page">
      <div className="page-heading"><span className="eyebrow eyebrow-dark">Seu pedido</span><h1>Carrinho</h1><p>Confira suas marmitas antes de continuar.</p></div>
      <div className="cart-layout">
        <section className="cart-list">
          {meals.map((meal, index) => {
            const size = mealSizes.find((item) => item.id === meal.size)!;
            return (
              <article className="cart-meal" key={meal.id}>
                <div className="cart-meal-number">{String(index + 1).padStart(2, '0')}</div>
                <div className="cart-meal-content">
                  <div className="cart-meal-heading"><div><span className="eyebrow eyebrow-dark">Marmita {size.id}</span><h2>{size.name}</h2></div><strong>{formatCurrency(size.price * meal.quantity)}</strong></div>
                  <ul>{meal.items.map((id) => <li key={id}>{getProductName(id)}</li>)}</ul>
                  {meal.note && <p className="cart-note">Observação: {meal.note}</p>}
                  <div className="cart-meal-actions">
                    <div className="quantity-control"><button type="button" onClick={() => changeQuantity(meal.id, -1)} aria-label="Diminuir quantidade">−</button><span>{meal.quantity}</span><button type="button" onClick={() => changeQuantity(meal.id, 1)} aria-label="Aumentar quantidade">+</button></div>
                    <button type="button" className="remove-button" onClick={() => removeMeal(meal.id)}>Remover</button>
                  </div>
                </div>
              </article>
            );
          })}
          <Link className="text-link back-menu" to="/cardapio">← Continuar escolhendo</Link>
        </section>
        <aside className="order-summary cart-summary">
          <div className="summary-title">Resumo do pedido</div>
          <div className="summary-total"><span>{meals.reduce((sum, meal) => sum + meal.quantity, 0)} marmita(s)</span><strong>{formatCurrency(total)}</strong></div>
          <p className="summary-footnote">A retirada é realizada na loja. O horário será informado na próxima etapa.</p>
          <Link className="button button-primary button-full" to="/finalizar">Continuar para retirada</Link>
        </aside>
      </div>
    </div>
  );
}