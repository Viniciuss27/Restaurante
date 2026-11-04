# Cheiro & Sabor — Front-end

Front-end demonstrativo do restaurante Cheiro & Sabor, construído com React, TypeScript e Vite.

## Requisitos

- Node.js 20.19+ ou 22.12+
- npm

## Executar localmente

1. Extraia o ZIP.
2. Abra um terminal dentro da pasta `Restaurante`.
3. Instale as dependências:

   ```bash
   npm install
   ```

4. Inicie o servidor de desenvolvimento:

   ```bash
   npm run dev
   ```

5. Abra o endereço local exibido pelo Vite (normalmente `http://localhost:5173`).

## Verificar build de produção

```bash
npm run build
npm run preview
```

## Páginas incluídas

- `/` — tela inicial
- `/cardapio` — seleção do tamanho e dos itens da marmita
- `/carrinho` — revisão, alteração de quantidade e remoção
- `/finalizar` — dados de identificação e horário de retirada
- `/pedido-confirmado` — resumo local do pedido
- `/acompanhar/:id` — linha do tempo demonstrativa

## Regras atuais preservadas

- Marmita pequena: R$ 18,00
- Marmita média: R$ 20,00
- Marmita grande: R$ 25,00
- Itens do cardápio são escolhas que compõem a marmita, não produtos com preço individual.
- Retirada: todos os dias, das 9h às 14h e das 17h às 21h.
- O formulário exige pelo menos 20 minutos de antecedência.

## Importante antes de integrar ao backend

Esta versão é apenas front-end. Carrinho e último pedido ficam no `localStorage` do navegador. O checkout não envia pedidos ao restaurante, não realiza pagamento e não consulta disponibilidade real. O acompanhamento também é demonstrativo.

As fotografias e o logo são carregados da pasta `imagens` do repositório público original no GitHub. Portanto, é necessária conexão com a internet para exibi-los. Para tornar o projeto totalmente independente, copie os arquivos de imagem para `public/imagens` e ajuste `src/assets.ts`.

## Próximas integrações

Quando o backend Spring Boot estiver pronto, substituir a persistência local por chamadas HTTP, implementar autenticação se necessário e conectar o acompanhamento a eventos reais (por exemplo, SSE).