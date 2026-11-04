import { useMemo, useState } from 'react';
import { Link } from 'react-router-dom';
import { menuProducts, mealSizes, formatCurrency } from '../data/menu';
import { useCart } from '../context/CartContext';
import type { MealSize } from '../types';

const categories = ['Todos', 'Carnes', 'Acompanhamentos', 'Massas', 'Saladas'] as const;

export default function MenuPage() {
  const [category, setCategory] = useState<(typeof categories)[number]>('Todos');
  const [size, setSize] = useState<MealSize | ''>('');
  const [selected, setSelected] = useState<string[]>([]);
  const [note, setNote] = useState('');
  const [feedback, setFeedback] = useState('');
  const { addMeal, itemCount } = useCart();

  const visibleProducts = useMemo(() => category === 'Todos'
    ? menuProducts
    : menuProducts.filter((product) => product.category === category), [category]);

  function toggleProduct(id: string) {
    setSelected((current) => current.includes(id)
      ? current.filter((item) => item !== id)
      : [...current, id]);
  }

  function addToCart() {
    if (!size) {
      setFeedback('Escolha o tamanho da sua marmita.');
      return;
    }
    if (selected.length === 0) {
      setFeedback('Selecione pelo menos um item do cardápio.');
      return;
    }
    addMeal({ size, items: selected, note: note.trim() });
    setSelected([]);
    setNote('');
    setSize('');
    setFeedback('Marmita adicionada ao carrinho.');
  }

  const chosenSize = mealSizes.find((item) => item.id === size);

  return (
    <div className="page-shell menu-page">
      <div className="page-heading">
        <span className="eyebrow eyebrow-dark">Cheiro & Sabor</span>
        <h1>Monte sua marmita</h1>
        <p>Escolha o tamanho e selecione os itens que deseja.</p>
      </div>
      <div className="menu-layout">
        <section className="menu-builder">
          <div className="step-heading"><span>1</span><div><h2>Escolha o tamanho</h2><p>Selecione uma das opções disponíveis.</p></div></div>
          <div className="size-options">
            {mealSizes.map((option) => (
              <button type="button" key={option.id} className={size === option.id ? 'size-option selected' : 'size-option'} onClick={() => setSize(option.id)}>
                <span className="size-letter">{option.id}</span><span className="size-name">{option.name}</span><strong>{formatCurrency(option.price)}</strong>
              </button>
            ))}
          </div>
          <div className="step-heading food-step"><span>2</span><div><h2>Escolha os itens</h2><p>Marque os alimentos que deseja incluir.</p></div></div>
          <div className="category-tabs" aria-label="Categorias do cardápio">
            {categories.map((item) => (
              <button type="button" key={item} className={category === item ? 'category-tab active' : 'category-tab'} onClick={() => setCategory(item)}>{item}</button>
            ))}
          </div>
          <div className="food-grid">
            {visibleProducts.map((product) => {
              const isSelected = selected.includes(product.id);
              return (
                <button type="button" key={product.id} className={isSelected ? 'food-option selected' : 'food-option'} onClick={() => toggleProduct(product.id)} aria-pressed={isSelected}>
                  <img src={product.image} alt="" />
                  <span className="food-option-info"><strong>{product.name}</strong><small>{product.category}</small></span>
                  <span className="food-check" aria-hidden="true">{isSelected ? '✓' : '+'}</span>
                </button>
              );
            })}
          </div>
          <label className="field-label note-field">Observação da marmita <span>(opcional)</span>
            <textarea value={note} onChange={(event) => setNote(event.target.value)} placeholder="Alguma orientação para este pedido?" rows={3} maxLength={180} />
          </label>
        </section>
        <aside className="order-summary">
          <div className="summary-title"><span>Seu pedido</span><span className="summary-count">{selected.length} selecionado(s)</span></div>
          {chosenSize ? <div className="summary-size"><span>Marmita {chosenSize.name}</span><strong>{formatCurrency(chosenSize.price)}</strong></div> : <p className="summary-muted">Escolha um tamanho para começar.</p>}
          <div className="summary-selected">
            {selected.length ? selected.map((id) => {
              const product = menuProducts.find((item) => item.id === id);
              return product ? <div key={id}><span>{product.name}</span><button type="button" onClick={() => toggleProduct(id)} aria-label={`Remover ${product.name}`}>×</button></div> : null;
            }) : <p>Nenhum item selecionado.</p>}
          </div>
          <div className="summary-total"><span>Valor da marmita</span><strong>{chosenSize ? formatCurrency(chosenSize.price) : '—'}</strong></div>
          <button className="button button-primary button-full" type="button" onClick={addToCart}>Adicionar ao carrinho</button>
          {feedback && <p className={feedback.startsWith('Marmita') ? 'form-feedback success' : 'form-feedback'} role="status">{feedback}</p>}
          {itemCount > 0 && <Link to="/carrinho" className="summary-cart-link">Ver carrinho ({itemCount}) →</Link>}
          <p className="summary-footnote">Os itens selecionados compõem a marmita. O valor é definido pelo tamanho escolhido.</p>
        </aside>
      </div>
    </div>
  );
}