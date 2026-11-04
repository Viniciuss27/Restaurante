import type { MenuProduct, MealSize } from '../types';
import { assets } from '../assets';

export const menuProducts: MenuProduct[] = [
  { id: 'arroz', name: 'Arroz branco', category: 'Acompanhamentos', image: assets.arroz },
  { id: 'carne-ensopada', name: 'Carne ensopada', category: 'Carnes', image: assets.carneEnsopada },
  { id: 'churrasco', name: 'Churrasco no carvão', category: 'Carnes', image: assets.churrasco },
  { id: 'costela', name: 'Costela assada', category: 'Carnes', image: assets.costela },
  { id: 'feijao-tropeiro', name: 'Feijão tropeiro', category: 'Acompanhamentos', image: assets.feijaoTropeiro },
  { id: 'feijao', name: 'Feijão em caldo', category: 'Acompanhamentos', image: assets.feijao },
  { id: 'frango-assado', name: 'Frango assado', category: 'Carnes', image: assets.frangoAssado },
  { id: 'frango-ensopado', name: 'Frango ensopado', category: 'Carnes', image: assets.frangoEnsopado },
  { id: 'macarrao-parmegiana', name: 'Macarrão à parmegiana', category: 'Massas', image: assets.macarrao },
  { id: 'macarrao-alho', name: 'Macarrão alho e óleo', category: 'Massas', image: assets.macarraoAlho },
  { id: 'salada', name: 'Salada simples', category: 'Saladas', image: assets.salada },
];

export const mealSizes: { id: MealSize; name: string; price: number }[] = [
  { id: 'P', name: 'Pequena', price: 18 },
  { id: 'M', name: 'Média', price: 20 },
  { id: 'G', name: 'Grande', price: 25 },
];

export const formatCurrency = (value: number) =>
  new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(value);

export const getProductName = (id: string) =>
  menuProducts.find((product) => product.id === id)?.name ?? id;