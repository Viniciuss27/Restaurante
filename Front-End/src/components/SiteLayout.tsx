import { useState } from 'react';
import { Link, NavLink, Outlet } from 'react-router-dom';
import { assets } from '../assets';
import { useCart } from '../context/CartContext';

export default function SiteLayout() {
  const { itemCount } = useCart();
  const [menuOpen, setMenuOpen] = useState(false);
  const closeMenu = () => setMenuOpen(false);

  return (
    <>
      <div className="top-note">Retirada na loja · Pedidos com pelo menos 20 minutos de antecedência</div>
      <header className="site-header">
        <div className="header-inner">
          <Link to="/" className="brand" onClick={closeMenu} aria-label="Cheiro e Sabor - início">
            <img src={assets.logo} alt="Logo Cheiro & Sabor" />
            <span><strong>Cheiro & Sabor</strong><small>Restaurante</small></span>
          </Link>
          <button className="mobile-menu-toggle" type="button" onClick={() => setMenuOpen(!menuOpen)} aria-label="Abrir menu">
            <span></span><span></span><span></span>
          </button>
          <nav className={menuOpen ? 'main-nav is-open' : 'main-nav'}>
            <NavLink to="/" onClick={closeMenu}>Início</NavLink>
            <NavLink to="/cardapio" onClick={closeMenu}>Cardápio</NavLink>
            <NavLink to="/carrinho" className="cart-nav" onClick={closeMenu}>
              Carrinho <span className="cart-count">{itemCount}</span>
            </NavLink>
          </nav>
        </div>
      </header>
      <main><Outlet /></main>
      <footer className="site-footer">
        <div className="footer-inner">
          <div><strong>Cheiro & Sabor</strong><p>Comida feita para fazer parte do seu dia.</p></div>
          <div><strong>Funcionamento</strong><p>Todos os dias, das 9h às 14h e das 17h às 21h.</p></div>
          <div><strong>Retirada</strong><p>Rua Ernesto Silva, nº 34<br />Campo Verde · Cariacica/ES</p></div>
        </div>
        <div className="footer-bottom">© {new Date().getFullYear()} Restaurante Cheiro & Sabor</div>
      </footer>
    </>
  );
}