package interfaces;

import model.ItemPedido;

//interface de operações (mostra o que o projeto deve fazer)
public interface OperacoesPedido {
    public boolean inserir(ItemPedido entidade);
    public ItemPedido pesquisar(ItemPedido entidade);
    public boolean remover(int id);
    public boolean atualizar(int id, String novoValor);
}