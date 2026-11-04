import { useState, type FormEvent } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { formatCurrency, getProductName, mealSizes } from '../data/menu';
import { useCart } from '../context/CartContext';
import type { CheckoutData } from '../types';

function localDateString(date: Date) {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  return `${year}-${month}-${day}`;
}

export default function CheckoutPage() {
  const { meals, total, finishOrder } = useCart();
  const navigate = useNavigate();
  const [form, setForm] = useState<CheckoutData>({
    name: '', phone: '', pickupDate: localDateString(new Date()), pickupTime: '', observations: '',
  });
  const [error, setError] = useState('');

  if (!meals.length) {
    return <div className="page-shell empty-state"><h1>Não há itens para finalizar</h1><p>Adicione uma marmita antes de continuar.</p><Link className="button button-primary" to="/cardapio">Voltar ao cardápio</Link></div>;
  }

  function update<K extends keyof CheckoutData>(key: K, value: CheckoutData[K]) {
    setForm((current) => ({ ...current, [key]: value }));
  }

  function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const digits = form.phone.replace(/\D/g, '');
    if (digits.length < 10 || digits.length > 11) {
      setError('Informe um telefone com DDD.');
      return;
    }
    const pickup = new Date(`${form.pickupDate}T${form.pickupTime}:00`);
    const now = new Date();
    if (!form.pickupTime || Number.isNaN(pickup.getTime()) || pickup.getTime() < now.getTime() + 20 * 60 * 1000) {
      setError('Escolha um horário com pelo menos 20 minutos de antecedência.');
      return;
    }
    const minutes = pickup.getHours() * 60 + pickup.getMinutes();
    const morning = minutes >= 9 * 60 && minutes <= 14 * 60;
    const evening = minutes >= 17 * 60 && minutes <= 21 * 60;
    if (!morning && !evening) {
      setError('O horário de retirada deve estar entre 9h e 14h ou entre 17h e 21h.');
      return;
    }
    if (!form.name.trim()) {
      setError('Informe o nome para identificação do pedido.');
      return;
    }
    const order = finishOrder({ ...form, name: form.name.trim(), phone: digits });
    navigate('/pedido-confirmado', { state: { order } });
  }

  return (
    <div className="page-shell checkout-page">
      <div className="page-heading"><span className="eyebrow eyebrow-dark">Quase lá</span><h1>Dados para retirada</h1><p>Preencha seus dados e escolha quando deseja retirar.</p></div>
      <div className="checkout-layout">
        <form className="checkout-form" onSubmit={handleSubmit}>
          <div className="step-heading"><span>1</span><div><h2>Quem vai retirar?</h2><p>Usaremos os dados para identificar o pedido.</p></div></div>
          <label className="field-label">Nome completo<input value={form.name} onChange={(event) => update('name', event.target.value)} placeholder="Seu nome" autoComplete="name" required /></label>
          <label className="field-label">Telefone com DDD<input value={form.phone} onChange={(event) => update('phone', event.target.value)} placeholder="(27) 99999-9999" inputMode="tel" autoComplete="tel" required /></label>
          <div className="step-heading checkout-step"><span>2</span><div><h2>Quando deseja retirar?</h2><p>Todos os dias, das 9h às 14h e das 17h às 21h.</p></div></div>
          <div className="form-row">
            <label className="field-label">Data<input type="date" min={localDateString(new Date())} value={form.pickupDate} onChange={(event) => update('pickupDate', event.target.value)} required /></label>
            <label className="field-label">Horário<input type="time" value={form.pickupTime} onChange={(event) => update('pickupTime', event.target.value)} required /></label>
          </div>
          <label className="field-label">Observações gerais <span>(opcional)</span><textarea value={form.observations} onChange={(event) => update('observations', event.target.value)} rows={3} maxLength={300} placeholder="Informações adicionais sobre a retirada..." /></label>
          {error && <p className="form-feedback" role="alert">{error}</p>}
          <div className="checkout-actions"><Link className="text-link" to="/carrinho">← Voltar ao carrinho</Link><button className="button button-primary" type="submit">Finalizar pedido <span>→</span></button></div>
          <p className="demo-notice">Esta é uma finalização demonstrativa no navegador. O pedido ainda não é enviado ao restaurante.</p>
        </form>
        <aside className="order-summary checkout-summary">
          <div className="summary-title">Resumo do pedido</div>
          {meals.map((meal) => {
            const size = mealSizes.find((item) => item.id === meal.size)!;
            return <div className="checkout-line" key={meal.id}><div><strong>{meal.quantity}× Marmita {size.name}</strong><small>{meal.items.map(getProductName).join(', ')}</small></div><strong>{formatCurrency(size.price * meal.quantity)}</strong></div>;
          })}
          <div className="summary-total"><span>Total</span><strong>{formatCurrency(total)}</strong></div>
        </aside>
      </div>
    </div>
  );
}