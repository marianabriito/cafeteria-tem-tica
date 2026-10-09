package model;

public class ItemPedido {
    
    //variáveis 
    private static int contadorId = 1; // id auto-incrementado

    private int id;
    private String nomeProduto;
    private double precoUnitario;
    private int quantidade;
    private String observacoesCliente;
    private String statusPreparo;

    //construtor vazio
    public ItemPedido() {
    }

    //construtor com parâmetros (atributos)
    public ItemPedido(String nomeProduto, double precoUnitario, int quantidade, String observacoesCliente, String statusPreparo) {
        this.id = contadorId++;
        this.nomeProduto = nomeProduto;
        this.precoUnitario = precoUnitario;
        this.quantidade = quantidade;
        this.observacoesCliente = observacoesCliente;
        this.statusPreparo = statusPreparo;
    }

    //construtor id (usado na pesquisa)
    public ItemPedido(int id) {
        this.id = id;
    }

    //get e set
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNomeProduto() { return nomeProduto; }
    public void setNomeProduto(String nomeProduto) { this.nomeProduto = nomeProduto; }

    public double getPrecoUnitario() { return precoUnitario; }
    public void setPrecoUnitario(double precoUnitario) { this.precoUnitario = precoUnitario; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public String getObservacoesCliente() { return observacoesCliente; }
    public void setObservacoesCliente(String observacoesCliente) { this.observacoesCliente = observacoesCliente; }

    public String getStatusPreparo() { return statusPreparo; }
    public void setStatusPreparo(String statusPreparo) { this.statusPreparo = statusPreparo; }

    //formatando o objeto
    @Override //sobrescreve
    public String toString() {
        return "ID: " + id + " | Produto: " + nomeProduto + " | Qtd: " + quantidade + 
               " | Preço: R$" + precoUnitario + " | Obs: " + observacoesCliente + " | Status: " + statusPreparo;
    }
}