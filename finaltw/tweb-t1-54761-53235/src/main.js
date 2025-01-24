document.addEventListener('DOMContentLoaded', () => {
    const searchForm = document.getElementById('search-form');
    const resultsContainer = document.getElementById('results-container');
    resultsContainer.classList.add('hidden');

    /**
     * ======= SEÇÃO: BUSCA DE VIAGENS =======
     */

    // Captura o envio do formulário de busca
    searchForm.addEventListener('submit', (event) => {
        event.preventDefault();

        const origem = document.getElementById('origem').value.trim();
        const destino = document.getElementById('destino').value.trim();
        const data = document.getElementById('data').value.trim();

        // Validação: ao menos um critério deve ser preenchido
        if (!origem && !destino && !data) {
            alert('Por favor, insira ao menos um critério de busca.');
            return;
        }

        console.log(`Buscando viagens com origem: ${origem}, destino: ${destino}, data: ${data}`);

        buscarViagens(origem, destino, data, (viagens) => {
            const viagensFiltradas = aplicarFiltros(viagens, origem, destino, data);
            mostrarViagens(viagensFiltradas);
        });
    });

    // Busca viagens na API
    function buscarViagens(origem, destino, data, callback) {
        const url = 'https://magno.di.uevora.pt/tweb/t1/viagem/search';
        const bodyString = `origem=${encodeURIComponent(origem)}&destino=${encodeURIComponent(destino)}&data=${encodeURIComponent(data)}`;

        console.log('Enviando solicitação para buscar viagens:', bodyString);

        const xhr = new XMLHttpRequest();
        xhr.open('POST', url, true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        xhr.onload = function () {
            if (xhr.status === 200) {
                try {
                    const responseData = JSON.parse(xhr.responseText);
                    console.log('Viagens recebidas:', responseData);
                    callback(responseData.viagens);
                } catch (error) {
                    console.error('Erro ao processar a resposta:', error);
                    callback([]);
                }
            } else {
                console.error('Erro ao buscar viagens:', xhr.statusText);
                callback([]);
            }
        };

        xhr.onerror = () => console.error('Erro de rede ao buscar viagens.');
        xhr.send(bodyString);
    }

    // Aplica os filtros às viagens
    function aplicarFiltros(viagens, origem, destino, data) {
        return viagens.filter(viagem => {
            const matchOrigem = origem ? viagem.origem.place.toLowerCase().includes(origem.toLowerCase()) : true;
            const matchDestino = destino ? viagem.destino.place.toLowerCase().includes(destino.toLowerCase()) : true;
            const matchData = data ? formatDate(viagem.data) === data : true;
            return matchOrigem && matchDestino && matchData;
        });
    }

    function formatDate(dateString) {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    }

    // Mostra as viagens filtradas na interface
    function mostrarViagens(viagens) {
        resultsContainer.classList.remove('hidden');
        resultsContainer.innerHTML = '';

        if (viagens.length === 0) {
            const mensagemElement = document.createElement('div');
            mensagemElement.classList.add('viagem');
            mensagemElement.innerHTML = '<p>Nenhuma viagem disponível</p>';
            resultsContainer.appendChild(mensagemElement);
        } else {
            viagens.forEach(viagem => {
                const viagemElement = criarElementoViagem(viagem);
                resultsContainer.appendChild(viagemElement);
            });
        }
    }

    // Cria elemento HTML para uma viagem
    function criarElementoViagem(viagem) {
        const viagemElement = document.createElement('div');
        viagemElement.classList.add('viagem');
        viagemElement.setAttribute('v_id', viagem.v_id);
        viagemElement.innerHTML = `
            <h3>${viagem.origem.place} → ${viagem.destino.place}</h3>
            <p><strong>Data:</strong> ${viagem.data}</p>
            <p><strong>Condutor:</strong> ${viagem.condutor}</p>
            <p><strong>Passageiros:</strong> ${viagem.passageiros.join(", ")}</p>
            <div class="buttons">
                <button onclick="registarPedido(${viagem.v_id}, '${viagem.origem.place}', '${viagem.destino.place}', '${viagem.data}', '${viagem.condutor}')">Registar Pedido</button>
            </div>
        `;
        return viagemElement;
    }

    /**
     * ======= SEÇÃO: REGISTRO E ASSOCIAÇÃO =======
     */

    // Registra um pedido de viagem
    window.registarPedido = function (viagemId, origem, destino, data, condutor) {
        const url = 'https://magno.di.uevora.pt/tweb/t1/pedido/add';
        const bodyString = `viagemId=${viagemId}&origem=${origem}&destino=${destino}&data=${data}&condutor=${condutor}`;

        console.log('Enviando pedido de viagem:', bodyString);

        const xhr = new XMLHttpRequest();
        xhr.open('POST', url, true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        xhr.onload = function () {
            if (xhr.status === 200) {
                console.log('Pedido registrado com sucesso!');
                alert('Pedido registrado com sucesso!');
                updateButtonToJoin(viagemId);
            } else {
                console.error('Erro ao registar o pedido:', xhr.statusText);
                alert('Erro ao registar o pedido.');
            }
        };

        xhr.onerror = () => console.error('Erro ao registar pedido.');
        xhr.send(bodyString);
    };

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

    // Associa o usuário a uma viagem
    window.associarViagem = function (viagemId) {
        const url = `https://magno.di.uevora.pt/tweb/t1/viagem/${viagemId}/join`;
        const bodyString = `viagemId=${viagemId}`;

        console.log(`Enviando solicitação para associar à viagem ${viagemId}`);

        const xhr = new XMLHttpRequest();
        xhr.open('POST', url, true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        xhr.onload = function () {
            if (xhr.status === 200) {
                console.log('Você se associou à viagem com sucesso!');
                alert('Você se associou à viagem com sucesso!');
                updateButtonToLeave(viagemId);
            } else {
                console.error('Erro ao associar-se à viagem:', xhr.statusText);
                alert('Erro ao associar-se à viagem.');
            }
        };

        xhr.onerror = () => console.error('Erro ao associar-se à viagem.');
        xhr.send(bodyString);
    };  

    // Atualiza botão para "Join"
    function updateButtonToJoin(viagemId) {
        const viagemElement = document.querySelector(`.viagem[v_id="${viagemId}"] .buttons`);
        viagemElement.innerHTML = `<button onclick="associarViagem(${viagemId})">Join</button>`;
    }

    // Atualiza botão para "Leave"
    function updateButtonToLeave(viagemId) {
        const viagemElement = document.querySelector(`.viagem[v_id="${viagemId}"] .buttons`);
        viagemElement.innerHTML = `<button onclick="cancelarViagem(${viagemId})">Leave</button>`;
    }

    // Cancela a associação do usuário à viagem
    window.cancelarViagem = function (viagemId) {
        const url = `https://magno.di.uevora.pt/tweb/t1/viagem/${viagemId}/leave`;
        const bodyString = `viagemId=${viagemId}`;

        console.log(`Enviando solicitação para cancelar associação à viagem ${viagemId}`);

        const xhr = new XMLHttpRequest();
        xhr.open('POST', url, true);
        xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

        xhr.onload = function () {
            if (xhr.status === 200) {
                console.log('Você deixou a viagem com sucesso!');
                alert('Você deixou a viagem com sucesso!');
                updateButtonToJoin(viagemId);
            } else {
                console.error('Erro ao deixar a viagem:', xhr.statusText);
                alert('Erro ao deixar a viagem.');
            }
        };

        xhr.onerror = () => console.error('Erro ao deixar a viagem.');
        xhr.send(bodyString);
    };

    /**
     * ======= SEÇÃO: REMOÇÃO =======
     */

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
    function registarViagem () {
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
