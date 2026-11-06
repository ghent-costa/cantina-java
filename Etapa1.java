// ETAPA 1: produtos e abertura do caixa (roteiro 2 e 3.1)
// Novidade: constantes, estoque, login e caixa inicial

void main() {

    // constantes (não mudam)
    final String SENHA = "cantina2026";
    final double PRECO_COXINHA = 6.50;
    final double PRECO_PAO = 4.00;
    final double PRECO_SUCO = 5.00;
    final double PRECO_CAFE = 3.00;

    // estoque de cada produto
    int estoqueCoxinha = 20;
    int estoquePao = 30;
    int estoqueSuco = 15;
    int estoqueCafe = 40;

    // login
    String operador = IO.readln("Nome do operador: ");

    int tentativas = 0;
    boolean autenticado = false;

    while (tentativas < 3 && autenticado == false) {
        String senha = IO.readln("Senha: ");
        tentativas = tentativas + 1;

        if (senha.equals(SENHA)) {
            autenticado = true;
        } else {
            IO.println("Senha incorreta. Tentativas restantes: " + (3 - tentativas));
        }
    }

    if (autenticado == false) {
        IO.println("Acesso bloqueado. Programa encerrado.");
    } else {

        double caixaInicial = Double.parseDouble(IO.readln("Valor inicial em caixa: R$ "));
        while (caixaInicial < 0) {
            IO.println("Valor inválido. Não pode ser negativo.");
            caixaInicial = Double.parseDouble(IO.readln("Valor inicial em caixa: R$ "));
        }

        IO.println("Bem-vindo(a), " + operador + "! Caixa aberto.");
    }
}
