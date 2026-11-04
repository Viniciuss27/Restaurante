import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from 'react';
import type { CartMeal, CheckoutData, CustomerOrder } from '../types';
import { mealSizes } from '../data/menu';

interface CartContextValue {
  meals: CartMeal[];
  itemCount: number;
  total: number;
  addMeal: (meal: Omit<CartMeal, 'id' | 'quantity'>) => void;
  changeQuantity: (id: string, amount: number) => void;
  removeMeal: (id: string) => void;
  finishOrder: (data: CheckoutData) => CustomerOrder;
}

const CartContext = createContext<CartContextValue | null>(null);
const CART_KEY = 'cheiro-sabor-cart';
const LAST_ORDER_KEY = 'cheiro-sabor-last-order';

function readCart(): CartMeal[] {
  try {
    const saved = localStorage.getItem(CART_KEY);
    return saved ? (JSON.parse(saved) as CartMeal[]) : [];
  } catch {
    return [];
  }
}

export function CartProvider({ children }: { children: ReactNode }) {
  const [meals, setMeals] = useState<CartMeal[]>(readCart);

  useEffect(() => {
    localStorage.setItem(CART_KEY, JSON.stringify(meals));
  }, [meals]);

  const total = useMemo(() => meals.reduce((sum, meal) => {
    const price = mealSizes.find((size) => size.id === meal.size)?.price ?? 0;
    return sum + price * meal.quantity;
  }, 0), [meals]);

  const itemCount = useMemo(
    () => meals.reduce((sum, meal) => sum + meal.quantity, 0),
    [meals],
  );

  function addMeal(meal: Omit<CartMeal, 'id' | 'quantity'>) {
    setMeals((current) => [...current, { ...meal, id: crypto.randomUUID(), quantity: 1 }]);
  }

  function changeQuantity(id: string, amount: number) {
    setMeals((current) => current
      .map((meal) => meal.id === id ? { ...meal, quantity: meal.quantity + amount } : meal)
      .filter((meal) => meal.quantity > 0));
  }

  function removeMeal(id: string) {
    setMeals((current) => current.filter((meal) => meal.id !== id));
  }

  function finishOrder(data: CheckoutData): CustomerOrder {
    const order: CustomerOrder = {
      ...data,
      id: `CS-${Date.now().toString().slice(-8)}`,
      createdAt: new Date().toISOString(),
      meals: [...meals],
      total,
    };
    localStorage.setItem(LAST_ORDER_KEY, JSON.stringify(order));
    setMeals([]);
    return order;
  }

  return (
    <CartContext.Provider value={{ meals, itemCount, total, addMeal, changeQuantity, removeMeal, finishOrder }}>
      {children}
    </CartContext.Provider>
  );
}

export function useCart() {
  const context = useContext(CartContext);
  if (!context) throw new Error('useCart deve ser utilizado dentro de CartProvider');
  return context;
}

export function getLastOrder(): CustomerOrder | null {
  try {
    const saved = localStorage.getItem(LAST_ORDER_KEY);
    return saved ? (JSON.parse(saved) as CustomerOrder) : null;
  } catch {
    return null;
  }
}