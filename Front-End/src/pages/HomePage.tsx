import { Link } from 'react-router-dom';
import { assets } from '../assets';
import { menuProducts, mealSizes, formatCurrency } from '../data/menu';

export default function HomePage() {
  const highlights = menuProducts.filter((product) =>
    ['churrasco', 'carne-ensopada', 'feijao-tropeiro', 'frango-assado'].includes(product.id),
  );

  return (
    <div className="home-page">
      <section className="hero">
        <div className="hero-photo" style={{ backgroundImage: `url(${assets.hero})` }} />
        <div className="hero-shade" />
        <div className="hero-content">
          <span className="eyebrow">Restaurante Cheiro & Sabor</span>
          <h1>Comida de verdade,<br /><em>do seu jeito.</em></h1>
          <p>Escolha seus acompanhamentos, monte sua marmita e organize a retirada.</p>
          <Link className="button button-primary" to="/cardapio">Monte sua marmita <span aria-hidden="true">→</span></Link>
        </div>
      </section>
      <section className="home-intro section-wrap">
        <div><span className="eyebrow eyebrow-dark">Nosso cardápio</span><h2>Seu almoço começa<br />com uma boa escolha.</h2></div>
        <p>Escolha o tamanho da marmita e combine os itens disponíveis no cardápio. Simples, direto e do seu jeito.</p>
      </section>
      <section className="highlight-section section-wrap">
        <div className="section-heading">
          <div><span className="eyebrow eyebrow-dark">Algumas opções</span><h2>Do cardápio</h2></div>
          <Link to="/cardapio" className="text-link">Ver cardápio completo <span>→</span></Link>
        </div>
        <div className="highlight-grid">
          {highlights.map((product) => (
            <article className="highlight-item" key={product.id}>
              <img src={product.image} alt={product.name} />
              <h3>{product.name}</h3>
            </article>
          ))}
        </div>
      </section>
      <section className="sizes-section">
        <div className="section-wrap">
          <span className="eyebrow">Escolha o tamanho</span>
          <h2>Uma opção para cada fome.</h2>
          <div className="size-preview">
            {mealSizes.map((size) => (
              <div className="size-preview-item" key={size.id}>
                <span>{size.id}</span><div><strong>{size.name}</strong><small>{formatCurrency(size.price)}</small></div>
              </div>
            ))}
          </div>
          <Link to="/cardapio" className="button button-light">Escolher minha marmita <span>→</span></Link>
        </div>
      </section>
    </div>
  );
}