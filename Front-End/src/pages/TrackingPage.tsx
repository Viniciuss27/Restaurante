import { Link, useParams } from 'react-router-dom';
import { getLastOrder } from '../context/CartContext';

const steps = ['Pedido realizado', 'Pedido confirmado', 'Em preparação', 'Pronto para retirada', 'Finalizado'];

export default function TrackingPage() {
  const { id } = useParams();
  const order = getLastOrder();

  if (!order || order.id !== id) {
    return <div className="page-shell empty-state"><h1>Pedido não encontrado</h1><p>Não há um pedido local correspondente a este identificador.</p><Link className="button button-primary" to="/">Voltar ao início</Link></div>;
  }

  return (
    <div className="page-shell tracking-page">
      <span className="eyebrow eyebrow-dark">Acompanhamento</span>
      <h1>Andamento do pedido</h1>
      <p className="tracking-intro">Pedido {order.id}</p>
      <div className="tracking-demo">Visualização demonstrativa. Sem integração com o restaurante, não há atualização real de status.</div>
      <ol className="tracking-timeline">
        {steps.map((step, index) => (
          <li className={index === 0 ? 'timeline-step current' : 'timeline-step'} key={step}>
            <span className="timeline-dot">{index === 0 ? '✓' : ''}</span><div><strong>{step}</strong>{index === 0 && <small>Pedido registrado apenas neste navegador</small>}</div>
          </li>
        ))}
      </ol>
      <Link className="button button-primary" to="/">Voltar ao início</Link>
    </div>
  );
}