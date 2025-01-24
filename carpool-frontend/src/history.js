document.addEventListener('DOMContentLoaded', () => {
    const historyForm = document.getElementById('history-form');
    const resultsContainer = document.getElementById('results-container');

    resultsContainer.classList.add('hidden');

    historyForm.addEventListener('submit', (event) => {
        event.preventDefault();
        const id = document.getElementById('id').value.trim();
        const type = document.getElementById('type').value;

        if (!id) {
            alert('Please enter an ID.');
            return;
        }

        const url = type === 'driver' 
            ? `https://magno.di.uevora.pt/tweb/t1/condutor/get/${encodeURIComponent(id)}`
            : `https://magno.di.uevora.pt/tweb/t1/passageiro/get/${encodeURIComponent(id)}`;

        console.log('Fetching URL:', url); 
        fetchData(url, type);
    });
    // Funcao de fetch de dados
    function fetchData(url, type) {
        const xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);

        xhr.onload = function () {
            if (xhr.status === 200) {
                try {
                    const data = JSON.parse(xhr.responseText);
                    console.log('Data fetched:', data); 
                    displayData(data, type);
                } catch (error) {
                    console.error('Error parsing JSON:', error);
                    alert('Failed to process the data.');
                }
            } else {
                console.error('Failed to fetch data:', xhr.statusText);
                alert('Failed to fetch data.');
            }
        };

        xhr.onerror = function () {
            console.error('Network error occurred while fetching data.');
            alert('Network error occurred.');
        };

        xhr.send();
    }
    // Função para exibir os dados
    function displayData(data, type) {
        if (!data || data.status !== 'ok') return;

        const result = data.result;
        if (result.viagens.length === 0) {
            resultsContainer.classList.add('hidden');
        } else {
            resultsContainer.classList.remove('hidden');
        }
        resultsContainer.innerHTML = `
            <p><strong>Rating:</strong> ${result.fiabilidade}</p>
            <p><strong>Total Trips:</strong> ${result.viagens.length}</p>
        `;

        result.viagens.forEach(trip => {
            console.log('Trip:', trip);
            const tripElement = document.createElement('div');
            tripElement.classList.add('trip');
            tripElement.innerHTML = `
                <h3>${trip.origem.place} → ${trip.destino.place}</h3>
                <p><strong>Data:</strong> ${trip.data}</p>
                <p><strong>Passengers:</strong> ${trip.passageiros.join(', ')}</p>
            `;
            resultsContainer.appendChild(tripElement);
        });
    }


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
