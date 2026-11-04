import { Link, useLocation } from 'react-router-dom';
import { formatCurrency, getProductName, mealSizes } from '../data/menu';
import { getLastOrder } from '../context/CartContext';
import type { CustomerOrder } from '../types';

export default function ConfirmationPage() {
  const location = useLocation();
  const order = (location.state as { order?: CustomerOrder } | null)?.order ?? getLastOrder();

  if (!order) {
    return <div className="page-shell empty-state"><h1>Nenhum pedido encontrado</h1><p>Finalize um pedido para visualizar esta página.</p><Link className="button button-primary" to="/cardapio">Ir para o cardápio</Link></div>;
  }

  return (
    <div className="page-shell confirmation-page">
      <div className="confirmation-mark">✓</div>
      <span className="eyebrow eyebrow-dark">Etapa demonstrativa concluída</span>
      <h1>Pedido organizado!</h1>
      <p className="confirmation-lead">Os dados do pedido foram salvos neste navegador. Esta tela ainda não representa uma confirmação do restaurante.</p>
      <section className="confirmation-card">
        <div className="confirmation-card-head"><div><small>Identificador local</small><strong>{order.id}</strong></div><span className="status-pill">Não enviado ao restaurante</span></div>
        <div className="confirmation-details"><div><small>Cliente</small><strong>{order.name}</strong></div><div><small>Telefone</small><strong>{order.phone}</strong></div><div><small>Retirada</small><strong>{order.pickupDate.split('-').reverse().join('/')} às {order.pickupTime}</strong></div></div>
        <div className="confirmation-meals">
          {order.meals.map((meal) => {
            const size = mealSizes.find((item) => item.id === meal.size)!;
            return <div className="confirmation-meal" key={meal.id}><div><strong>{meal.quantity}× Marmita {size.name}</strong><p>{meal.items.map(getProductName).join(' · ')}</p>{meal.note && <small>Obs.: {meal.note}</small>}</div><strong>{formatCurrency(size.price * meal.quantity)}</strong></div>;
          })}
        </div>
        {order.observations && <p className="confirmation-observation">Observações: {order.observations}</p>}
        <div className="summary-total"><span>Total</span><strong>{formatCurrency(order.total)}</strong></div>
      </section>
      <div className="confirmation-actions"><Link className="button button-primary" to="/">Voltar ao início</Link><Link className="button button-outline" to={`/acompanhar/${order.id}`}>Ver acompanhamento demonstrativo</Link></div>
    </div>
  );
}