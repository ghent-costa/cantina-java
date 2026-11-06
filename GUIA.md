# Cantina: o código por etapas (versão iniciante)

Cada etapa é um arquivo que **compila e roda sozinho**. Leia a parte do roteiro, abra a etapa, entenda o que entrou de novo e teste. A Etapa 7 é o código completo.

| Etapa | Parte do roteiro | O que entra de novo |
|---|---|---|
| 1 | 2 e 3.1 | Constantes, estoque, login, caixa inicial |
| 2 | 3.2 | Menu que se repete |
| 3 | 3.4 e 3.5 | Consultar e repor estoque |
| 4 | 3.3 (passos 1 a 4) | Escolher itens da venda com estoque "reservado" |
| 5 | 3.3 (passos 5 e 6) e seção 4 | Desconto, pagamento, troco, cupom, totais do dia |
| 6 | 3.6 | Relatório parcial |
| 7 | 3.7 | Fechar caixa, conferência e gráfico |

Dica: no VS Code, clique com o botão direito num arquivo, "Select for Compare", depois no outro, "Compare with Selected". Você vê só o que mudou de uma etapa para outra.

---

## Antes de tudo: o que é o `equals`

Em Java, para comparar **números** usamos `==`. Para comparar **textos** (String) usamos `.equals(...)`.

```java
if (senha.equals(SENHA))      // a senha digitada é igual a "cantina2026"?
if (estudante.equals("S"))    // a pessoa digitou S?
```

Por que não `senha == SENHA`? Porque com texto o `==` não compara as letras, ele compara se é "a mesma caixinha na memória". O texto digitado é uma caixinha nova, então daria falso mesmo com a senha certa. **Não tem como fazer o login sem `equals`**, por isso ele aparece no código.

Para aceitar `S` maiúsculo e `s` minúsculo, o código simplesmente testa os dois: `estudante.equals("S") || estudante.equals("s")`.

O `!` na frente quer dizer "NÃO". Então `!estudante.equals("S")` quer dizer "estudante NÃO é S".

---

## Etapa 1: produtos e abertura do caixa

**O roteiro pede:** preços e estoque como constantes; nome do operador; senha com 3 tentativas; caixa inicial que não pode ser negativo; boas-vindas.

- `final` torna a variável uma constante (não pode mudar). Os estoques não são `final` porque mudam ao vender e repor.
- O login fica num `while (tentativas < 3 && autenticado == false)`. Ele para quando acabam as tentativas **ou** quando a pessoa acerta.
- `tentativas = tentativas + 1` conta cada tentativa. `3 - tentativas` mostra quantas restam.
- Não pode usar `System.exit`, então o programa usa `if (autenticado == false) { bloqueia } else { resto do programa }`. Se o login falha, ele só mostra a mensagem e chega ao fim.
- Caixa inicial: lê, e **enquanto** for negativo, avisa e lê de novo. Esse jeito "lê, valida com while, lê de novo" se repete no programa todo.

**Teste:** errar a senha 3 vezes; errar 1 e acertar; caixa `-10` e depois `100`.

---

## Etapa 2: menu principal

- `boolean caixaAberto = true;` e `while (caixaAberto == true)`. O menu repete até alguém fazer `caixaAberto = false`, o que só acontece na opção 5.
- `Integer.parseInt(...)` transforma o texto digitado em número inteiro.
- Os `if / else if` escolhem a opção. O `else` final pega qualquer número que não seja 1 a 5.
- As opções ainda mostram "(em construção)": primeiro a estrutura, depois cada parte.

**Teste:** digitar `9`, depois `2`, depois `5`.

---

## Etapa 3: consultar e repor estoque

Faço antes da venda porque é mais fácil e ajuda a testar a venda depois.

**Consultar:** para cada produto, três casos: se é `0` mostra ESGOTADO; senão, se é `<= 5`, mostra ESTOQUE BAIXO; senão, só o número. **O teste do zero vem primeiro** porque 0 também é menor que 5. Na ordem contrária, um produto zerado apareceria como "estoque baixo".

**Repor:** valida o código (`while (cod < 1 || cod > 4)`), valida a quantidade (`while (qtd <= 0)`) e soma no estoque do produto escolhido.

**Teste:** código `7`, quantidade `-1` e `0`, depois `5` pães. Consultar e ver 35.

---

## Etapa 4: registrar venda, parte 1 (escolher itens)

É a parte mais importante para explicar.

**O "reservado":** o roteiro diz que o estoque só pode baixar quando a venda for confirmada. Então, durante a venda, guardo quanto de cada produto já foi pedido em `reservadoCoxinha`, `reservadoPao`, etc. Essas variáveis começam em 0 a cada venda nova.

- O que ainda dá para vender nesta venda é `estoque - reservado`.
- Exemplo do roteiro: tem 8 coxinhas. Pede 5: reservado fica 5, sobram 3. Pede mais 5: recusa e mostra "Disponível: 3".

**O laço da venda:**

- `int codigo = -1;` começa em -1 só para entrar no `while (codigo != 0)` na primeira vez.
- Um `if` para cada produto (1, 2, 3, 4). Dentro de cada um: calcula o disponível; se for 0, avisa que esgotou; senão pede a quantidade, valida, soma no reservado, calcula o subtotal e soma no `totalBruto`.
- Os 4 blocos são iguais, só mudam as variáveis. O roteiro avisa que sem vetores fica repetitivo, e é esperado.
- `else if (codigo != 0)` pega códigos inválidos (fora de 0 a 4).
- Se o operador digita 0 sem nada (`totalBruto == 0`), a venda é cancelada.

**Teste:** vender 12 coxinhas (sobram 8). Em outra venda pedir 5 e depois 5: a segunda tem que ser recusada.

---

## Etapa 5: registrar venda, parte 2 (regras e cupom)

**Totais do dia:** entram as variáveis lá no começo do programa (`vendidosCoxinha`..., `qtdVendas`, `totalArrecadado`, `totalDinheiro`, `totalPix`, `totalCartao`, `dinheiroRecebido`, `trocoDevolvido`, `maiorVenda`). Ficam **fora** do menu para não zerar a cada venda.

**Regras na ordem da seção 4:**

1. **Estudante:** pergunta S/N e repete se for outra coisa. Desconto de 10% só se for estudante **e** o total bruto for `>= 20`.
2. **Pagamento:** 1, 2 ou 3, repete se inválido.
3. **Cartão:** acréscimo de 3% sobre o valor **já com desconto**.
4. **Troco:** só no dinheiro. Enquanto o valor recebido for menor que o total, mostra quanto falta e pede de novo. Troco = recebido - total.
5. **Cupom:** para cada produto, `if (reservadoCoxinha > 0)` imprime a linha dele. Assim só aparecem os produtos que estão na venda.
6. **Confirma:** tira do estoque (`estoque = estoque - reservado`), soma em `vendidos`, soma no total da forma de pagamento e vê se é a maior venda.

**Diferença entre `totalDinheiro` e `dinheiroRecebido`:** `totalDinheiro` é o valor das vendas pagas em dinheiro (vai no relatório). `dinheiroRecebido` é a soma das notas que os clientes entregaram (vai na conta do caixa, junto com o troco).

**Teste (exemplo do roteiro):** 2 coxinhas + 2 sucos, estudante, cartão: bruto 23,00, desconto 2,30, acréscimo 0,62, final 21,32. Em dinheiro com 50: final 20,70 e troco 29,30. Testei e bate.

---

## Etapa 6: relatório parcial

- **Ticket médio:** se `qtdVendas == 0`, mostra "nenhuma venda" em vez de dividir por zero.
- **Mais vendido:** começa supondo que é a coxinha e compara com cada outro produto. Se algum vendeu **mais**, ele passa a ser o mais vendido. Em empate fica o primeiro, e o roteiro permite isso.

**Teste:** abrir o relatório sem nenhuma venda e depois de algumas vendas.

---

## Etapa 7: fechar caixa (código final)

- Pergunta S/N. Se for N, não faz nada e o menu volta sozinho.
- Se for S: mostra o relatório (é uma cópia do relatório da opção 4), depois a parte do caixa: **valor final = inicial + dinheiro recebido - troco devolvido**.
- **Gráfico:** para cada produto, um `for` imprime um `#` por unidade vendida com `IO.print` (que não pula linha). Depois `IO.println` mostra o número e pula a linha.
- `caixaAberto = false;` faz o menu parar e o programa termina com a despedida.

**Teste:** opção 5 com `x` (inválido), depois `N` (volta ao menu), depois 5 com `S`.

---

## Perguntas prováveis na apresentação

- **Por que `equals` e não `==` na senha?** Porque é texto. `==` serve para números.
- **Como o programa termina sem `System.exit`?** Pelo `if/else` do login e pela variável `caixaAberto`, que faz o `while` parar.
- **Para que serve o reservado?** Para não vender mais do que tem quando o mesmo produto é pedido mais de uma vez, sem mexer no estoque antes de confirmar a venda.
- **Por que o `codigo` começa em -1?** Para o `while (codigo != 0)` rodar na primeira vez.
- **Por que o acréscimo vem depois do desconto?** Porque o roteiro manda essa ordem, e o exemplo confirma (3% de 20,70).
- **Por que o código é tão repetido?** Porque não pode usar vetores nem criar funções. O próprio roteiro diz que isso é esperado.

## Cuidados

- Ao digitar valores com centavos, use **ponto**: `50.00` ou `12.5`. Com vírgula o `Double.parseDouble` dá erro.
- Os valores na tela podem aparecer com vírgula ou ponto, dependendo do idioma do computador. As duas formas estão certas.
