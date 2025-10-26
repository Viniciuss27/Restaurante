// Arrays de formulario e pedidos
let Pedido = [];
let Nome = [];
let Numero = [];
let Dia = [];
let Hora = [];
let Tipo = [];
let Obs = [];

function cadastrar(){// usando .value para pegar o value do id
    let nome = document.getElementById('nome').value
    let numero = document.getElementById('numero').value
    let dia = document.getElementById('dia').value
    let hora = document.getElementById('hora').value
    let tipo = document.getElementById('tipo').value
    let obs = document.getElementById('obs').value

   // usando .push para adicionar no array
   // colocando entre { } para pegar em String e não objeto
   Nome.push({nome})
   Numero.push({numero})
   Dia.push({dia})
   Hora.push({hora})
   Tipo.push({tipo})
   Obs.push({obs})
   

   // Atualiza o link do WhatsApp com o Array
    updateWhatsAppLink();
}

//adicionar ao carrinho
function addToCart(name) {
    Pedido.push({ name });
    
    // Atualiza o link do WhatsApp com o Array
    updateWhatsAppLink();
}

// Atualiza o link com os pedido
function updateWhatsAppLink() {
    const whatsappLink = document.getElementById('whatsapp-link');

    // abrindo os Arrays com forEach
    let message = `*Novo Pedido* \n \n`
    Nome.forEach(item => {
        message += `Cliente: ${item.nome} \n`;
    })
    Numero.forEach(item => {
        message += `Numero de contato: ${item.numero} \n`;
    })
    Dia.forEach(item => {
        message += `Retirada dia: ${item.dia}`;
    })
    Hora.forEach(item => {
        message += `, ás ${item.hora} horas \n`;
    })
    Tipo.forEach(item => {
        message += `Tamanho: ${item.tipo} \n`;
    })
    Obs.forEach(item => {
        message += `Obs: ${item.obs} \n \n segue a lista dos pedidos: \n \n`;
    })
    Pedido.forEach(item => {/*percarre o array, vai adicionando e pulando linha*/
        message += `${item.name} \n`;
    });
    
    // Formatar a URL do WhatsApp com o pedido
    const encodedMessage = encodeURIComponent(message);
    whatsappLink.href = `https://wa.me/27997169402?text=${encodedMessage}`;
    
    // Exibir o botão de finalizar pedido ao escolher um pedido
    whatsappLink.style.display = 'inline-block';
}

// Adicionar evento de clique aos botões "Adicionar ao Pedido"
document.querySelectorAll('.add-to-cart').forEach(button => {
    button.addEventListener('click', () => {
        const name = button.getAttribute('data-name');
        addToCart(name);

        //escolhendo a <tr>
		let linha = adicionados.insertRow()

		//criar a <td>
		linha.insertCell(0).innerHTML = name
       
    });
});
