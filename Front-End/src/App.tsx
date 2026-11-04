import { Navigate, Route, Routes } from 'react-router-dom';
import SiteLayout from './components/SiteLayout';
import HomePage from './pages/HomePage';
import MenuPage from './pages/MenuPage';
import CartPage from './pages/CartPage';
import CheckoutPage from './pages/CheckoutPage';
import ConfirmationPage from './pages/ConfirmationPage';
import TrackingPage from './pages/TrackingPage';

export default function App() {
  return (
    <Routes>
      <Route element={<SiteLayout />}>
        <Route path="/" element={<HomePage />} />
        <Route path="/cardapio" element={<MenuPage />} />
        <Route path="/carrinho" element={<CartPage />} />
        <Route path="/finalizar" element={<CheckoutPage />} />
        <Route path="/pedido-confirmado" element={<ConfirmationPage />} />
        <Route path="/acompanhar/:id" element={<TrackingPage />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Route>
    </Routes>
  );
}