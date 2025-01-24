document.addEventListener('DOMContentLoaded', () => {
    const listarViagensButton = document.querySelector('.viagem .button');
    const listarPedidosButton = document.querySelector('.pedidos .button');

    // URLs das APIs
    const viagensURL = 'https://magno.di.uevora.pt/tweb/t1/viagens/list';
    const pedidosURL = 'https://magno.di.uevora.pt/tweb/t1/pedidos/list';

    let currentPage = 1;
    const resultsPerPage = 4;
    let viagensData = []; // Store fetched viagens data

    // Cria contêineres para os resultados
    const viagensContainer = document.createElement('div');
    viagensContainer.className = 'resultados-viagens';
    listarViagensButton.parentElement.appendChild(viagensContainer);

    const pedidosContainer = document.createElement('div');
    pedidosContainer.className = 'resultados-pedidos';
    listarPedidosButton.parentElement.appendChild(pedidosContainer);

    // Função para buscar dados de viagens
    function listarViagens() {
        if (viagensContainer.innerHTML.trim()) {
            viagensContainer.innerHTML = ''; // Hide the list if it's already visible
            return;
        }

        const xhr = new XMLHttpRequest();
        xhr.open('GET', viagensURL, true);

        xhr.onload = function () {
            if (xhr.status === 200) {
                const data = JSON.parse(xhr.responseText);
                viagensData = data.viagens || [];
                console.log('Fetched viagens data:', viagensData); // Log fetched data
                renderizarTabelaViagens('Viagens Disponíveis', viagensData, viagensContainer);
            } else {
                exibirMensagemErro('Erro ao carregar viagens.', viagensContainer);
            }
        };

        xhr.onerror = function () {
            exibirMensagemErro('Erro ao carregar viagens.', viagensContainer);
        };

        xhr.send();
    }

    // Função para buscar dados de pedidos
    function listarPedidos() {
        if (pedidosContainer.innerHTML.trim()) {
            pedidosContainer.innerHTML = ''; // Hide the list if it's already visible
            return;
        }

        const xhr = new XMLHttpRequest();
        xhr.open('GET', pedidosURL, true);

        xhr.onload = function () {
            if (xhr.status === 200) {
                const data = JSON.parse(xhr.responseText);
                console.log('Fetched pedidos data:', data.pedidos); // Log fetched data
                renderizarTabela('Pedidos de Viagem', data.pedidos || [], pedidosContainer);
            } else {
                exibirMensagemErro('Erro ao carregar pedidos.', pedidosContainer);
            }
        };

        xhr.onerror = function () {
            exibirMensagemErro('Erro ao carregar pedidos.', pedidosContainer);
        };

        xhr.send();
    }

    // Função para exibir uma tabela paginada para viagens
    function renderizarTabelaViagens(titulo, itens, container) {
        const totalPages = Math.ceil(itens.length / resultsPerPage);

        const startIndex = (currentPage - 1) * resultsPerPage;
        const endIndex = startIndex + resultsPerPage;
        const itemsToDisplay = itens.slice(startIndex, endIndex);

        // Limpa o contêiner e cria a tabela
        container.innerHTML = `
            <div class="resultados">
                <h3>${titulo}</h3>
                <div class="filter-section">
                    <input type="text" id="filter-local" placeholder="Local">
                    <input type="date" id="filter-date">
                    <button id="filter-submit" class="button">Filtrar</button>
                </div>
                <table class="tabela-resultados">
                    <thead>
                        <tr>
                            <th>Origem</th>
                            <th>Destino</th>
                            <th>Data</th>
                            <th>Condutor</th>
                            <th>Passageiros</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${itemsToDisplay.map(item => criarLinhaTabela(item)).join('')}
                    </tbody>
                </table>
                <div class="paginacao">
                    <button class="pagina-anterior" ${currentPage === 1 ? 'disabled' : ''}>Anterior</button>
                    <span>Página ${currentPage} de ${totalPages}</span>
                    <button class="proxima-pagina" ${currentPage === totalPages ? 'disabled' : ''}>Próxima</button>
                </div>
            </div>
        `;

        // Eventos para os botões de paginação
        container.querySelector('.pagina-anterior').addEventListener('click', () => {
            if (currentPage > 1) {
                currentPage--;
                renderizarTabelaViagens(titulo, itens, container);
            }
        });

        container.querySelector('.proxima-pagina').addEventListener('click', () => {
            if (currentPage < totalPages) {
                currentPage++;
                renderizarTabelaViagens(titulo, itens, container);
            }
        });

        // Evento para o botão de filtro
        container.querySelector('#filter-submit').addEventListener('click', () => {
            currentPage = 1; // Reinicia para a primeira página
            filtrarViagens();
        });
    }

    // Função para exibir uma tabela paginada para pedidos
    function renderizarTabela(titulo, itens, container) {
        const totalPages = Math.ceil(itens.length / resultsPerPage);

        const startIndex = (currentPage - 1) * resultsPerPage;
        const endIndex = startIndex + resultsPerPage;
        const itemsToDisplay = itens.slice(startIndex, endIndex);

        // Limpa o contêiner e cria a tabela
        container.innerHTML = `
            <div class="resultados">
                <h3>${titulo}</h3>
                <table class="tabela-resultados">
                    <thead>
                        <tr>
                            <th>Origem</th>
                            <th>Destino</th>
                            <th>Data</th>
                            <th>Condutor</th>
                            <th>Passageiros</th>
                        </tr>
                    </thead>
                    <tbody>
                        ${itemsToDisplay.map(item => criarLinhaTabela(item)).join('')}
                    </tbody>
                </table>
                <div class="paginacao">
                    <button class="pagina-anterior" ${currentPage === 1 ? 'disabled' : ''}>Anterior</button>
                    <span>Página ${currentPage} de ${totalPages}</span>
                    <button class="proxima-pagina" ${currentPage === totalPages ? 'disabled' : ''}>Próxima</button>
                </div>
            </div>
        `;

        // Eventos para os botões de paginação
        container.querySelector('.pagina-anterior').addEventListener('click', () => {
            if (currentPage > 1) {
                currentPage--;
                renderizarTabela(titulo, itens, container);
            }
        });

        container.querySelector('.proxima-pagina').addEventListener('click', () => {
            if (currentPage < totalPages) {
                currentPage++;
                renderizarTabela(titulo, itens, container);
            }
        });
    }

    // Função para criar uma linha da tabela
    function criarLinhaTabela(item) {
        const origem = item.origem?.place || 'N/A';
        const destino = item.destino?.place || 'N/A';
        const data = item.data || 'N/A';
        const condutor = item.condutor || 'N/A';
        const passageiros = item.passageiros?.join(', ') || 'N/A';

        return `
            <tr>
                <td>${origem}</td>
                <td>${destino}</td>
                <td>${data}</td>
                <td>${condutor}</td>
                <td>${passageiros}</td>
            </tr>
        `;
    }

    // Função para exibir mensagens de erro
    function exibirMensagemErro(mensagem, container) {
        container.innerHTML = `
            <div class="erro">
                <p>${mensagem}</p>
            </div>
        `;
    }

    // Função para filtrar viagens
    function filtrarViagens() {
        const local = document.getElementById('filter-local').value.toLowerCase();
        const date = document.getElementById('filter-date').value;

        console.log('Filter local:', local); // Log input local
        console.log('Filter date:', date); // Log input date

        const filteredViagens = viagensData.filter(viagem => {
            const origem = viagem.origem?.place.toLowerCase() || '';
            const destino = viagem.destino?.place.toLowerCase() || '';
            const data = viagem.data || '';

            // Convert data to yyyy-mm-dd format
            const formattedData = new Date(data).toISOString().split('T')[0];
            return (origem.includes(local) || destino.includes(local)) && (!date || formattedData === date);
        });

        console.log('Filtered viagens:', filteredViagens); // Log filtered results

        renderizarTabelaViagens('Viagens Disponíveis', filteredViagens, viagensContainer);
    }

    // Adiciona os eventos aos botões
    listarViagensButton.addEventListener('click', () => {
        currentPage = 1; // Reinicia para a primeira página
        listarViagens();
    });

    listarPedidosButton.addEventListener('click', () => {
        currentPage = 1; // Reinicia para a primeira página
        listarPedidos();
    });



    //Requests


    function registar_Pedido() {
        // Pede os dados de origem, destino, data e hora
        const origem = prompt("Digite a origem:");
        if (!origem) return alert("Origem é obrigatória!");
    
        const destino = prompt("Digite o destino:");
        if (!destino) return alert("Destino é obrigatório!");
    
        const data = prompt("Digite a data da viagem (no formato YYYY-MM-DD):");
        if (!data) return alert("Data é obrigatória!");
    
        const hora = prompt("Digite a hora da viagem (no formato HH:MM):");
        if (!hora) return alert("Hora é obrigatória!");
        
        const Passageiro = prompt("Digite o nome do passageiro:");
        if (!Passageiro) return alert("Nome do passageiro é obrigatório!");
    
        // Envia os dados para o servidor
        const url = 'https://magno.di.uevora.pt/tweb/t1/pedido/add';
        const bodyString = `origem=${encodeURIComponent(origem)}&destino=${encodeURIComponent(destino)}&data=${encodeURIComponent(data)}&hora=${encodeURIComponent(hora)}&nomePassageiro=${encodeURIComponent(Passageiro)}`;
    
        console.log('Enviando solicitação para registar viagem:', bodyString);
    
        const xhr = new XMLHttpRequest();
        xhr.open('POST', url, true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    
        xhr.onload = function () {
            if (xhr.status === 200) {
                console.log('Pedido registrado com sucesso!');
                alert('Pedido registrado com sucesso!');
            } else {
                console.error('Erro ao registar pedido:', xhr.statusText);
                alert('Erro ao registar pedido.');
            }
        };
    
        xhr.onerror = () => console.error('Erro ao registar pedido.');
        xhr.send(bodyString);
    }


 // Função para remover pedido do passageiro
 function removerPedido() {
    const passageiroId = prompt("Digite o seu ID de passageiro:");
    if (!passageiroId) return;

    console.log(`Enviando solicitação para remover pedido do passageiro ${passageiroId}`);

    const url = 'https://magno.di.uevora.pt/tweb/t1/pedido/remove';
    const bodyString = `passageiro_id=${passageiroId}`;

    const xhr = new XMLHttpRequest();
    xhr.open('POST', url, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    xhr.onload = function () {
        if (xhr.status === 200) {
            console.log('Pedido removido com sucesso!');
            alert('Pedido removido com sucesso!');
        } else {
            console.error('Erro ao remover pedido:', xhr.statusText);
            alert('Erro ao remover pedido.');
        }
    };

    xhr.onerror = () => console.error('Erro de rede ao remover pedido.');
    xhr.send(bodyString);
}

 /**
 * ======= SEÇÃO: registar E REMOVER VIAGENS =======
 */

// Registra uma viagem pedindo dados ao usuário
window.registarViagem = function () {
// Pede os dados de origem, destino, data e condutor
const origem = prompt("Digite o nome da cidade de origem:");
if (!origem) return alert("Origem é obrigatória!");

const destino = prompt("Digite o nome da cidade de destino:");
if (!destino) return alert("Destino é obrigatório!");

const data = prompt("Digite a data da viagem (no formato YYYY-MM-DD):");
if (!data) return alert("Data é obrigatória!");

const condutor = prompt("Digite o nome do condutor:");
if (!condutor) return alert("Condutor é obrigatório!");

// Envia os dados para o servidor
const url = 'https://magno.di.uevora.pt/tweb/t1/viagem/add';
const bodyString = `origem=${encodeURIComponent(origem)}&destino=${encodeURIComponent(destino)}&data=${encodeURIComponent(data)}&condutor=${encodeURIComponent(condutor)}`;

console.log('Enviando solicitação para registar viagem:', bodyString);

const xhr = new XMLHttpRequest();
xhr.open('POST', url, true);
xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

xhr.onload = function () {
    if (xhr.status === 200) {
        console.log('Viagem registrada com sucesso!');
        alert('Viagem registrada com sucesso!');
    } else {
        console.error('Erro ao registar viagem:', xhr.statusText);
        alert('Erro ao registar viagem.');
    }
};

xhr.onerror = () => console.error('Erro ao registar viagem.');
xhr.send(bodyString);
};


// Remove uma viagem pedindo o ID ao usuário
window.removerViagem = function () {
// Pede o ID da viagem
const viagemId = prompt("Digite o ID da viagem que deseja remover:");
if (!viagemId) return alert("ID da viagem é obrigatório!");

// Envia a solicitação para remover a viagem
const url = `https://magno.di.uevora.pt/tweb/t1/viagem/remove`;
const bodyString = `viagemId=${encodeURIComponent(viagemId)}`;

console.log('Enviando solicitação para remover viagem:', bodyString);

const xhr = new XMLHttpRequest();
xhr.open('POST', url, true);
xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

xhr.onload = function () {
    if (xhr.status === 200) {
        console.log('Viagem removida com sucesso!');
        alert('Viagem removida com sucesso!');
    } else {
        console.error('Erro ao remover viagem:', xhr.statusText);
        alert('Erro ao remover viagem.');
    }
};
xhr.onerror = () => console.error('Erro ao remover viagem.');
xhr.send(bodyString);
};
document.getElementById('remover-pedido').addEventListener('click', removerPedido);
document.getElementById('registar-pedido').addEventListener('click', registar_Pedido);
document.getElementById('registar-viagem').addEventListener('click', registarViagem);
document.getElementById('remover-viagem').addEventListener('click', removerViagem);
});