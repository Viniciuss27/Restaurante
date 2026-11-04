export type MealSize = 'P' | 'M' | 'G';

export interface MenuProduct {
  id: string;
  name: string;
  category: 'Carnes' | 'Acompanhamentos' | 'Massas' | 'Saladas';
  image: string;
}

export interface CartMeal {
  id: string;
  size: MealSize;
  items: string[];
  note: string;
  quantity: number;
}

export interface CheckoutData {
  name: string;
  phone: string;
  pickupDate: string;
  pickupTime: string;
  observations: string;
}

export interface CustomerOrder extends CheckoutData {
  id: string;
  createdAt: string;
  meals: CartMeal[];
  total: number;
}