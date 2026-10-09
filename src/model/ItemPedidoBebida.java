package model;

public class ItemPedidoBebida extends ItemPedido {
    private boolean comGelo;

    //construtor vazio
    public ItemPedidoBebida() {
        super(); //conexão com a superclasse ItemPedido (para herdar as suas características)
    }

    //construtor com parâmetros (atributos)
    public ItemPedidoBebida(String nomeProduto, double precoUnitario, int quantidade, String observacoesCliente, String statusPreparo, boolean comGelo) {
        super(nomeProduto, precoUnitario, quantidade, observacoesCliente, statusPreparo);
        this.comGelo = comGelo;
    }

    //get e set
    public boolean isComGelo() { 
        return comGelo; 
    }
    
    public void setComGelo(boolean comGelo) { 
        this.comGelo = comGelo; 
    }

    //formatando o objeto
    @Override //sobrescreve
    public String toString() {
        return super.toString() + " | Com gelo? " + (comGelo ? "Sim" : "Não");
        //puxa a frase formatada da classe pai e adiciona a etapa do gelo para as bebidas
        //operador ternário (?) para retornar a resposta do usuário e formatar com o Sim/Não
    }
}