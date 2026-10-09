package application;

import java.awt.Dimension; //trabalha o tamanho da tela
import javax.swing.JOptionPane; //cria os pop-ups (painéis)
import javax.swing.JScrollPane; //adiciona scrolls
import javax.swing.JTextArea; //suporta mais textos (para exibirmos o menu)
import model.ItemPedido;
import model.ItemPedidoBebida;
import service.GerenciadorPedido;

class ProdutoCardapio {
    int id;
    String nome;
    double preco;
    String categoria;
    String descricao;

    //vitrine para mostrar os itens para o usuário (evita iniciarmos o id com 42 por conta das instâncias dos itens)
    public ProdutoCardapio(int id, String categoria, String nome, double preco, String descricao) {
        this.id = id;
        this.categoria = categoria;
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
    }
}

public class Main {
    
    //array contendo os itens do cardápio pré-setados
    private static ProdutoCardapio[] menuFixo = new ProdutoCardapio[41];

    public static void main(String[] args) {
        inicializarCardapio();
        GerenciadorPedido gerenciador = new GerenciadorPedido();
        int opcao = 0;

        JOptionPane.showMessageDialog(null, "Bem-vindo à cafeteria CineCoffee!");

        while (opcao != 6) {
            String menuPrincipal = "°✰⋆.･°⟡⋆ MENU PRINCIPAL °✰⋆.･°⟡⋆\n"
                    + "1. Ver cardápio e Fazer pedido\n"
                    + "2. Pesquisar item no pedido\n"
                    + "3. Atualizar observação do item\n"
                    + "4. Remover item do pedido\n"
                    + "5. Ver carrinho (listar pedidos)\n"
                    + "6. Finalizar pedido / Sair\n"
                    + "°✰⋆.*:･°⟡⋆.*:･°✰⋆.*:･°⟡⋆.*:･✰⋆.･°⟡⋆\n\n"
                    + "Escolha uma opção:";

            try {
                String entrada = JOptionPane.showInputDialog(null, menuPrincipal);
                if (entrada == null) break; 
                opcao = Integer.parseInt(entrada);

                switch (opcao) {
                    case 1: //ver cardápio e pedir
                        boolean continuarPedindo = true;
                        
                        while (continuarPedindo) {
                            String textoCardapio = construirTextoCardapio();
                            
                            //cria área de texto com barra de rolagem (menu)
                            JTextArea textArea = new JTextArea(textoCardapio);
                            textArea.setEditable(false);
                            JScrollPane scrollPane = new JScrollPane(textArea);
                            scrollPane.setPreferredSize(new Dimension(500, 400));
                            
                            //mostra o cardápio e pede o id do produto
                            String inputId = JOptionPane.showInputDialog(null, scrollPane, "Cardápio - Digite o ID do item desejado (ou 0 para cancelar):", JOptionPane.PLAIN_MESSAGE);
                            
                            if (inputId == null || inputId.equals("0") || inputId.trim().isEmpty()) {
                                break; //sai do laço de pedidos e volta para o menu principal
                            }
                            
                            int idEscolhido = Integer.parseInt(inputId);
                            ProdutoCardapio produtoEscolhido = buscarProdutoNoCardapio(idEscolhido);
                            
                            if (produtoEscolhido != null) {
                                int qtd = Integer.parseInt(JOptionPane.showInputDialog("Você escolheu: " + produtoEscolhido.nome + "\nQual a quantidade?"));
                                String obs = JOptionPane.showInputDialog("Alguma observação? (Ex: Sem açúcar, ponto da carne, etc):");
                                
                                //verifica se é bebida para perguntar do gelo
                                boolean comGelo = false;
                                if (produtoEscolhido.categoria.contains("Bebida")) {
                                    int geloOpcao = JOptionPane.showConfirmDialog(null, "Gostaria de adicionar gelo?", "Opção de Gelo", JOptionPane.YES_NO_OPTION);
                                    comGelo = (geloOpcao == JOptionPane.YES_OPTION);
                                    
                                    //instancia uma bebida
                                    ItemPedidoBebida novoPedido = new ItemPedidoBebida(produtoEscolhido.nome, produtoEscolhido.preco, qtd, obs, "Em preparo", comGelo);
                                    gerenciador.inserir(novoPedido);
                                } else {
                                    //instancia um item (Comida/Salgado/Sobremesa)
                                    ItemPedido novoPedido = new ItemPedido(produtoEscolhido.nome, produtoEscolhido.preco, qtd, obs, "Em preparo");
                                    gerenciador.inserir(novoPedido);
                                }
                                
                                JOptionPane.showMessageDialog(null, produtoEscolhido.nome + " adicionado ao carrinho!");
                                
                                //pergunta se quer continuar pedindo
                                int maisAlgumaCoisa = JOptionPane.showConfirmDialog(null, "Deseja adicionar mais algum item do cardápio?", "Continuar Comprando?", JOptionPane.YES_NO_OPTION);
                                continuarPedindo = (maisAlgumaCoisa == JOptionPane.YES_OPTION);
                                
                            } else {
                                JOptionPane.showMessageDialog(null, "ID de produto inválido. Tente novamente.");
                            }
                        }
                        break;

                    case 2: //pesquisar
                        int idPesquisa = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do pedido no carrinho para pesquisar:"));
                        ItemPedido mockPesquisa = new ItemPedido(idPesquisa); 
                        ItemPedido resultado = gerenciador.pesquisar(mockPesquisa);
                        if (resultado != null) {
                            JOptionPane.showMessageDialog(null, "Item Encontrado no Carrinho:\n" + resultado.toString());
                        } else {
                            JOptionPane.showMessageDialog(null, "Item não encontrado no carrinho!");
                        }
                        break;

                    case 3: //atualizar
                        int idAtualizar = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do item no carrinho que deseja atualizar:"));
                        String novaObs = JOptionPane.showInputDialog("Digite a nova observação:");
                        if (gerenciador.atualizar(idAtualizar, novaObs)) {
                            JOptionPane.showMessageDialog(null, "Observação atualizada com sucesso!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Item não encontrado no carrinho.");
                        }
                        break;

                    case 4: //remover
                        int idRemover = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do item para remover do carrinho:"));
                        if (gerenciador.remover(idRemover)) {
                            JOptionPane.showMessageDialog(null, "Item removido do carrinho com sucesso!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Item não encontrado no carrinho.");
                        }
                        break;

                    case 5: //carrinho               
                        JOptionPane.showMessageDialog(null, "°✰⋆.*:･°⟡⋆.*:･°✰⋆.*:･°⟡⋆.*:･°✰⋆.*:･°⟡⋆.*:･ SEU CARRINHO °✰⋆.*:･°⟡⋆.*:･°✰⋆.*:･°⟡⋆.*:･°✰⋆.*:･°⟡⋆.*:･\n\n" + gerenciador.listarTodos());
                        break;

                    case 6: //finalizar pedido / sair
                        double totalGasto = gerenciador.calcularTotalPedido();
                        
                        //se o carrinho estiver vazio, apenas se despede e sai
                        if (totalGasto == 0.0) {
                            JOptionPane.showMessageDialog(null, "Nenhum pedido foi realizado. Agradecemos a visita e volte sempre!");
                            break;
                        }

                        // 1. Mostra o resumo detalhado com o total calculado (já considerando as quantidades)
                        String resumoFinal = "°✰⋆.*:･°⟡⋆.*:･°✰⋆.*:･°⟡⋆.*:･°✰⋆.*:･°⟡⋆.*:･ RESUMO DO PEDIDO °✰⋆.*:･°⟡⋆.*:･°✰⋆.*:･°⟡⋆.*:･°✰⋆.*:･°⟡⋆.*:･\n\n"
                                + gerenciador.listarTodos()
                                + "\n- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -\n"
                                + "TOTAL A PAGAR: R$ " + String.format("%.2f", totalGasto);
                                
                        JOptionPane.showMessageDialog(null, resumoFinal, "Fechamento de Conta", JOptionPane.INFORMATION_MESSAGE);

                        //pergunta a forma de pagamento + botões
                        String[] opcoesPagamento = {"Cartão de Crédito", "Cartão de Débito", "Pix", "Dinheiro"};
                        int escolhaPagamento = JOptionPane.showOptionDialog(null,
                                "Selecione a forma de pagamento:",
                                "Método de Pagamento",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                opcoesPagamento,
                                opcoesPagamento[0]);

                        //tratamento caso o usuário feche a janela
                        String formaPagamentoStr = (escolhaPagamento >= 0) ? opcoesPagamento[escolhaPagamento] : "Não informada (A combinar com o garçom)";

                        //informa sobre o garçom e encerra o fluxo do sistema
                        String mensagemDespedida = "Valor total: R$ " + String.format("%.2f", totalGasto) + "\n"
                                + "Forma de pagamento escolhida: " + formaPagamentoStr + "\n\n"
                                + "Por favor, aguarde em sua mesa.\n"
                                + "Em instantes, nosso garçom irá até você com a maquininha para finalizar o pagamento.\n\n"
                                + "A cafeteria CineCoffee agradece a sua preferência. Volte sempre!";
                        
                        JOptionPane.showMessageDialog(null, mensagemDespedida, "Pedido Finalizado", JOptionPane.PLAIN_MESSAGE);
                        break;

                    default:
                        JOptionPane.showMessageDialog(null, "Opção inválida!");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Erro: Digite apenas números válidos.");
            }
        }
    }

    //métodos auxiliares

    private static String construirTextoCardapio() {
        StringBuilder sb = new StringBuilder();
        String categoriaAtual = "";
        
        for (ProdutoCardapio p : menuFixo) {
            if (p != null) {
                if (!p.categoria.equals(categoriaAtual)) {
                    sb.append("\n✰⋆.*:･°⟡⋆.*:･°✰⋆.*:･°⟡⋆.*:･°✰⋆.* ").append(p.categoria).append(" ✰⋆.*:･°⟡⋆.*:･°✰⋆.*:･°⟡⋆.*:･°✰⋆.*\n\n");
                    categoriaAtual = p.categoria;
                }
                sb.append("[").append(p.id).append("] ").append(p.nome)
                  .append(" - R$ ").append(String.format("%.2f", p.preco)).append("\n")
                  .append("    ").append(p.descricao).append("\n\n");
            }
        }
        return sb.toString();
    }

    private static ProdutoCardapio buscarProdutoNoCardapio(int id) {
        for (ProdutoCardapio p : menuFixo) {
            if (p != null && p.id == id) {
                return p;
            }
        }
        return null;
    }

    //itens do menu
    private static void inicializarCardapio() {
        int i = 0;
        //bebidas com cafeína
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas com cafeína", "Interestelar - Coado", 5.00, "Café coado tradicional, preparado na hora, com sabor suave e aroma marcante.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas com cafeína", "O Poderoso Chefão - Expresso", 5.00, "Café expresso intenso e encorpado, servido em dose concentrada.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas com cafeína", "La La Land - Capuccino", 8.00, "Combinação cremosa de café, leite vaporizado e espuma de leite.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas com cafeína", "Moulin Rouge! - Moccacino", 8.50, "Bebida que une café, leite e chocolate em uma mistura doce e equilibrada.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas com cafeína", "Casablanca — Cappuccino Especial", 8.50, "Versão especial do cappuccino com toque extra de cremosidade e sabor.");

        //bebidas sem cafeína
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas sem cafeína", "A Fantástica Fábrica de Chocolate — Chocolate Quente", 12.00, "Chocolate quente cremoso, perfeito para aquecer e adoçar o dia.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas sem cafeína", "O Jardim Secreto — Chá de Camomila", 8.50, "Chá leve e aromático, conhecido por suas propriedades relaxantes.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas sem cafeína", "Bob Marley One Love — Chá Verde", 8.50, "Bebida refrescante com sabor suave e características antioxidantes.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas sem cafeína", "Batman: o cavaleiro das trevas - Chá Preto", 8.50, "Chá de sabor forte e marcante, ideal para quem aprecia bebidas intensas.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas sem cafeína", "Alice no País das Maravilhas — Chá Branco", 8.50, "Chá delicado e suave, com aroma leve e agradável.");

        //bebidas geladas
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas geladas", "Barbie — Pink Lemonade", 12.50, "Limonada rosa refrescante, levemente doce e cítrica.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas geladas", "O Grinch — Matcha Gelado", 9.00, "Bebida gelada à base de chá verde matcha, refrescante e diferenciada.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas geladas", "Laranja Mecânica - Suco de Laranja", 12.00, "Suco natural de laranja, rico em sabor e refrescância.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas geladas", "A Bela Adormecida — Suco de Maracujá", 12.00, "Suco leve e tropical com sabor marcante de maracujá.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas geladas", "Moranguinho — Suco de Morango", 12.00, "Suco doce e frutado preparado com morangos selecionados.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas geladas", "A Cor Púrpura — Suco de Uva", 12.00, "Suco de uva encorpado, com sabor intenso e adocicado.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas geladas", "Bob Esponja - Suco de Abacaxi", 12.00, "Suco tropical refrescante com o sabor característico do abacaxi.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas geladas", "Palavras que Borbulham como Refrigerante", 8.00, "Bebida gaseificada servida gelada para acompanhar qualquer refeição.");
        menuFixo[i++] = new ProdutoCardapio(i, "Bebidas geladas", "Avatar: O Caminho da Água — Água", 4.00, "Água mineral gelada, ideal para hidratação e leveza.");

        //salgados
        menuFixo[i++] = new ProdutoCardapio(i, "Salgados", "Aladdin — Esfiha Fechada", 8.00, "Massa assada recheada, macia por dentro e dourada por fora.");
        menuFixo[i++] = new ProdutoCardapio(i, "Salgados", "Ratatouille — Croissant", 12.00, "Folhado leve e amanteigado, com textura crocante.");
        menuFixo[i++] = new ProdutoCardapio(i, "Salgados", "O Senhor dos Anéis — Bagel", 12.00, "Pão em formato de anel, macio e versátil para diferentes acompanhamentos.");
        menuFixo[i++] = new ProdutoCardapio(i, "Salgados", "A Fuga das Galinhas — Torta de Frango", 8.50, "Torta salgada recheada com frango temperado e massa macia.");
        menuFixo[i++] = new ProdutoCardapio(i, "Salgados", "O Menino Maluquinho - Pão de Queijo", 12.00, "Tradicional pão de queijo mineiro, crocante por fora e macio por dentro.");
        menuFixo[i++] = new ProdutoCardapio(i, "Salgados", "Terrifier — Torta de Carne", 8.50, "Torta salgada recheada com carne temperada e muito sabor.");
        menuFixo[i++] = new ProdutoCardapio(i, "Salgados", "Moana - Torta de Palmito", 8.50, "Torta cremosa recheada com palmito, leve e saborosa.");

        //lanches
        menuFixo[i++] = new ProdutoCardapio(i, "Lanches", "Chicken Little — Lanche de Patê de Frango", 12.50, "Sanduíche recheado com patê de frango cremoso e temperado.");
        menuFixo[i++] = new ProdutoCardapio(i, "Lanches", "Minha Mãe é uma Peça — Pão na Chapa", 6.00, "Pão tostado na chapa com manteiga, simples e tradicional.");
        menuFixo[i++] = new ProdutoCardapio(i, "Lanches", "O Auto da Compadecida — Misto Quente", 12.00, "Sanduíche quente de queijo e presunto, tostado na medida certa.");
        menuFixo[i++] = new ProdutoCardapio(i, "Lanches", "Carandiru — Bauru", 12.00, "Lanche clássico preparado com queijo, presunto e tomate.");
        menuFixo[i++] = new ProdutoCardapio(i, "Lanches", "Chaves — Sanduíche de Presunto", 6.00, "Sanduíche leve e saboroso feito com fatias de presunto.");
        menuFixo[i++] = new ProdutoCardapio(i, "Lanches", "Procurando Nemo — Lanche Natural de Atum", 12.00, "Lanche fresco preparado com atum e ingredientes leves.");

        //sobremesas
        menuFixo[i++] = new ProdutoCardapio(i, "Sobremesas", "Matilda — Bolo de Chocolate", 10.90, "Bolo macio e recheado com muito sabor de chocolate.");
        menuFixo[i++] = new ProdutoCardapio(i, "Sobremesas", "Branca de Neve — Torta de Maçã", 14.50, "Sobremesa tradicional preparada com maçãs e massa delicada.");
        menuFixo[i++] = new ProdutoCardapio(i, "Sobremesas", "Os Simpsons — Donut", 8.00, "Rosquinha macia coberta com glacê doce e saboroso.");
        menuFixo[i++] = new ProdutoCardapio(i, "Sobremesas", "Zootopia — Bolo de Cenoura", 10.90, "Bolo fofinho de cenoura com cobertura de chocolate.");
        menuFixo[i++] = new ProdutoCardapio(i, "Sobremesas", "Moça com Brinco de Pérola — Torta Holandesa", 14.50, "Sobremesa cremosa com base crocante e cobertura de chocolate.");
        menuFixo[i++] = new ProdutoCardapio(i, "Sobremesas", "King Kong — Banoffee", 14.50, "Torta feita com banana, doce de leite e chantilly.");
        menuFixo[i++] = new ProdutoCardapio(i, "Sobremesas", "Maria Antonieta — Brownie", 8.50, "Doce de chocolate denso e macio, com sabor intenso.");
        menuFixo[i++] = new ProdutoCardapio(i, "Sobremesas", "Vanelope — Brigadeiro de Taça", 12.00, "Brigadeiro cremoso servido em taça, perfeito para os amantes de chocolate.");
        menuFixo[i++] = new ProdutoCardapio(i, "Sobremesas", "Jovens Titãs - Waffles", 12.50, "Waffle dourado e macio, servido com cobertura especial e muito sabor.");
    }
}