package service;

import interfaces.OperacoesPedido;
import model.ItemPedido;

//garante que o que consta na estrutura da interface será implementado (nessa service)
public class GerenciadorPedido implements OperacoesPedido {
    
    //array
    private ItemPedido[] listaPedidos;
    private int quantidadeAtual;

    //construtor
    public GerenciadorPedido() {
        this.listaPedidos = new ItemPedido[2]; //inicializa o array em 2 para um redimensionamento rápido
        this.quantidadeAtual = 0; //os itens que o usuário pediu começam em 0
    }

    //método para redimensionar o array
    private void redimensionarArray() {
        //checa se o array está cheio
        if (quantidadeAtual == listaPedidos.length) {
            int novoTamanho = (int) (listaPedidos.length * 1.5); //aumenta 50%
            //casting para cortar a parte decimal (mantendo int)
            
            if(novoTamanho == listaPedidos.length) novoTamanho++; 
            
            ItemPedido[] novoArray = new ItemPedido[novoTamanho]; //instancia um novo tamanho para o array
            
            //passa os dados para novoArray (na mesma posição)
            for (int i = 0; i < listaPedidos.length; i++) {
                novoArray[i] = listaPedidos[i];
            }
            
            //atualização de ref (apontando para o novoArray, atualizado e com espaço)
            this.listaPedidos = novoArray;
        }
    }

    //método para retornar o índice no array (a partir do ID)
    private int pesquisarIndice(int id) {
        for (int i = 0; i < quantidadeAtual; i++) {
            if (listaPedidos[i].getId() == id) {
                return i;
            }
        }
        return -1; //caso não tiver encontrado o id
    }

    //método para guardar o pedido no array
    @Override
    public boolean inserir(ItemPedido entidade) {
        redimensionarArray(); //checa se é preciso aumentar o tamanho
        listaPedidos[quantidadeAtual] = entidade;
        quantidadeAtual++; //evita que os pedidos se sobrescrevam 
        return true;
    }

    //método para buscar o pedido
    @Override
    public ItemPedido pesquisar(ItemPedido entidadeMock) {
        //pega o id do objeto mock (passado por parâmetro)
        int indice = pesquisarIndice(entidadeMock.getId());
        if (indice != -1) {
            return listaPedidos[indice];
        }
        return null;
    }
    
    //método para remover o pedido
    @Override
    public boolean remover(int id) {
        int indice = pesquisarIndice(id);
        if (indice != -1) {
            //puxa os elementos para fechar o "buraco" ao remover
            for (int i = indice; i < quantidadeAtual - 1; i++) {
                listaPedidos[i] = listaPedidos[i + 1];
            }
            //limpa a última posição e diminui o contador
            listaPedidos[quantidadeAtual - 1] = null;
            quantidadeAtual--;
            return true;
        }
        return false;
    }

    //método para atualizar a observação do pedido
    @Override
    public boolean atualizar(int id, String novaObservacao) {
        int indice = pesquisarIndice(id);
        if (indice != -1) {
            listaPedidos[indice].setObservacoesCliente(novaObservacao);
            return true;
        }
        return false;
    }

    //método para listar tudo no painel
    public String listarTodos() {
        if (quantidadeAtual == 0) return "Nenhum pedido cadastrado.";
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < quantidadeAtual; i++) {
            sb.append(listaPedidos[i].toString()).append("\n");
        }
        sb.append("\n(Tamanho atual do array na memória: ").append(listaPedidos.length).append(")");
        return sb.toString();
    }
    
    //método para calcular o valor final a ser pago
    public double calcularTotalPedido() {
        double total = 0.0;
        for (int i = 0; i < quantidadeAtual; i++) {
            // Multiplica o preço de cada item pela quantidade selecionada e acumula
            total += listaPedidos[i].getPrecoUnitario() * listaPedidos[i].getQuantidade();
        }
        return total;
    }
}