/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Arvore_B;

import java.util.Scanner;

public class ArvoreB {
    // Variavel privada do tipo int que armazena o Grau T da Arvore
    private int t; // Grau mínimo
    // Variavel que armazena a Raiz.
    private Node root;

    private class Node {
        // Variavel que armazena o estado do nó
        int n;
        // Um array que armazena as chaves do nó, definido através da variavel T
        int[] keys = new int[2 * t - 1];
        /* Array que armazena os filhos do nó. Ele pode ter até 2 * t filhos. 
        Isso está alinhado com a propriedade da árvore B.*/
        Node[] children = new Node[2 * t];
        // Variável booleana que indica se o nó é uma folha ou não
        boolean leaf = true;
        /* Este é o construtor da classe Node. Ele inicializa n como 0 e também 
        define todos os elementos do array children como null, indicando que o nó 
        não tem filhos quando é criado.*/
        Node(){
            n = 0;
            for (int i = 0; i < 2 * t; i++) {
                children[i] = null;}}
    }

    /* Este é um construtor público, recebe um parâmetro T, 
    que representa o grau mínimo da árvore B. this.t = t; 
    */
    public ArvoreB(int t) {
        this.t = t;
        /* Um novo nó é criado e atribuído à variável root. Quando uma nova instância
        da árvore B é criada, ela começa com um nó raiz vazio.*/
        root = new Node();}
    
    //Metodo de Impressão da Arvore
    public void printTree(Node x, int level) {
        // Se o nó existir. 
        if (x != null) {
            //Ele percorre as chaves do nó de trás para frente usando o loop
            for (int i = x.n - 1; i >= 0; i--) {
                /* Se o nó não for uma folha, faz uma chamada recursiva para imprimir 
                o filho à direita da chave atual.*/
                if (!x.leaf) {
                    printTree(x.children[i + 1], level + 1);}
                
                for (int j = 0; j < level; j++) {
                    System.out.print("\t");}
                /*A chave atual é impressa. Após o loop, se o nó não for uma folha, ele
                faz uma chamada recursiva para imprimir o filho mais à esquerda do nó.*/
                System.out.println(x.keys[i]);}
            
            if (!x.leaf) {
                printTree(x.children[0], level + 1);}
        }
    }
    //Metodo de Inserção
    public void insert(int key) {
        Node r = root;
        // Verifica se o nó raiz r está cheio, Se ele tem o número máximo de chaves (2*t - 1).
        if (r.n == 2 * t - 1) {
            // Se estiver cheia cria um novo Nó
            Node s = new Node();
            // Referencia da nova raiz 
            root = s;
            // O nó raiz anterior R torna-se o primeiro filho do novo nó raiz 
            s.children[0] = r;
            /* Dividir o nó filho que está cheio (neste caso, o antigo nó raiz). 
            Isso garante que a propriedade da árvore B de ter um nó não cheio antes 
            da inserção seja mantida.*/
            splitChild(s, 0);
            /*Agora que a raiz não está mais cheia (porque foi dividida), a chave é inserida 
            começando pelo novo nó raiz s usando o método */
            insertNonFull(s, key);} 
        /*Se o nó não estiver cheio, a chave é inserida diretamente começando 
        pelo nó raiz r usando o método*/
        else {
            insertNonFull(r, key);}
    }

    /* Método crucial na inserção de uma chave em uma árvore B. 
    Ele assume que o nó x passado como argumento não está cheio (ou seja, tem menos do que 2*t - 1 chaves) 
    e tenta inserir a chave K no nó apropriado.*/
    private void insertNonFull(Node x, int k) {
        // I e o índice da última chave no nó x
        int i = x.n - 1;
        /* Caso o nó X seja uma folha. A ideia aqui é inserir a chave K na posição correta para manter 
        as chaves em ordem crescente.*/
        if (x.leaf){
            // Desloca-se para a esquerda enquanto a chave K for menor que a chave no índice I de X
            while (i >= 0 && k < x.keys[i]) {
                // Desloca a chave atual para a direita para fazer espaço para K.
                x.keys[i + 1] = x.keys[i];
                // Move para a próxima posição à esquerda
                i--;}
            // Após o loop, a posição correta para inserir
            x.keys[i + 1] = k;
            // O contador de chaves N do nó X é incrementado:
            x.n++;} 
        
        // Caso o nó X não seja uma folha.
        else{
            /*Este loop procura a posição correta para inserir a chave K. A variável I começa como o 
        índice da última chave no nó X. O loop desloca-se para a esquerda (diminuindo I) enquanto K for 
        menor que a chave na posição I do nó X. O loop termina quando I chega a -1 (antes da primeira chave) 
        ou quando a chave K for maior ou igual à chave na posição I.*/
            while (i >= 0 && k < x.keys[i]) {
                i--;}
            /*Após o loop, I é incrementado para apontar para a posição do filho à direita da chave em I. 
            Se K fosse menor que todas as chaves no nó X, então I apontaria para a posição 0, que é a do 
            primeiro filho.*/
            i++;
            // Esta condição verifica se o filho do nó X na posição I está cheio
            if (x.children[i].n == 2 * t - 1) {
                // Divide o filho cheio.
                splitChild(x, i);
                /*  Se k for maior que a chave no índice i de x (que pode ter mudado após a divisão), 
                então mova para o próximo filho.*/
                if (k > x.keys[i]) {
                    i++;}}
            // Inserção é realizada recursivamente no filho apropriado
            insertNonFull(x.children[i], k);}
    }

    /* Método fundamental em árvores B. Ele é invocado quando um nó está cheio (2*t - 1) e uma inserção 
    adicional precisa ser feita nele. Divide-se o nó cheio em dois e promove "uma" de suas chaves ao nó pai.*/
    private void splitChild(Node x, int i) {
        // Cria um novo nó Z. Este nó armazenará a metade das chaves do nó filho Y que está sendo dividido.
        Node z = new Node();
        // O nó Y é o nó filho de X na posição I que está cheio e precisa ser dividido.
        Node y = x.children[i];
        // A posição imediatamente após y no array de filhos de x é configurada para apontar para o novo nó z.
        x.children[i + 1] = z;
        // O novo nó z herda a propriedade leaf de y. Isso significa que se y era uma folha, z também será
        z.leaf = y.leaf;
        // O nó z terá t - 1 chaves depois da divisão
        z.n = t - 1;
        // A metade superior das chaves de y é copiada para z.
        for (int j = 0; j < t - 1; j++) {
            z.keys[j] = y.keys[j + t];}
        // Se y não é uma folha:
        if (!y.leaf) {
        /*Se y não for uma folha, ele terá filhos que precisam ser ajustados.
        Os filhos correspondentes à metade superior das chaves de y são movidos para z.*/ 
            for (int j = 0; j < t; j++) {
                z.children[j] = y.children[j + t];}}
        // Após a divisão, y terá t - 1 chaves.
        y.n = t - 1;
        /* Todos os filhos de x após a posição i são deslocados uma posição para a direita 
        para acomodar o novo nó z */
        for (int j = x.n; j >= i + 1; j--) {
            x.children[j + 1] = x.children[j];}

        /*As chaves de x são deslocadas à direita a partir da posição i para acomodar a chave 
        que será promovida de y*/
        for (int j = x.n - 1; j >= i; j--) {
            x.keys[j + 1] = x.keys[j];}
        // Promovendo a chave média de y para x:
        x.keys[i] = y.keys[t - 1];
        // Como uma chave foi adicionada a x, incrementamos o contador de chaves.
        x.n++;
    }

    // Metodo de deletação da arvore
    public void Delete(int key) {
        // Chama o método recursivo Delete, que realiza a operação real de deleção
        Delete(root, key);

        /* Esta condição verifica se, após a deleção, a raiz da árvore está vazia e
        se não é uma folha. Se ambas as condições forem verdadeiras, isso significa 
        que a raiz atual não tem chaves e tem exatamente um filho.*/
        if (root.n == 0 && !root.leaf) {
            // Se a condição anterior for verdadeira, então a raiz é atualizada para apontar para seu único filho.
            root = root.children[0];}
    }
    
    // Metodo de Delete da Arvore
    private void Delete(Node x, int k) {
        // Cria um indice
        int idx = 0;
            // Loop busca a posição da chave k ou onde ela deveria estar.
            while (idx < x.n && k > x.keys[idx]) {
                   idx++;}

        // Se a chave está em x e x é uma folha, Delete a chave.
        if (idx < x.n && x.keys[idx] == k && x.leaf) {
            //O loop for desloca todas as chaves à direita de k uma posição para a esquerda, efetivamente removendo k.
            for (int i = idx; i < x.n - 1; i++) {
                x.keys[i] = x.keys[i + 1];}
            // Decrementa o número de chaves em x depois de remover a chave.
            x.n--;
            return;}

        // Se a chave está em x e x não é uma folha.
        // Esta condição verifica se a chave k foi encontrada no nó x e que x não é uma folha.
        if (idx < x.n && x.keys[idx] == k) {
            // y é o filho do nó x que está à esquerda da chave k.
            Node y = x.children[idx];
            // z é o filho do nó x que está à direita da chave k.
            Node z = x.children[idx + 1];

        if (y.n >= t) {  // Caso 2a
            /* Chama o metodo responsável por encontrar o predecessor de `k` na subárvore cuja 
            raiz é `y`. O predecessor é basicamente a maior chave na subárvore de `y`.*/
            int pred = findPredecessor(y);
            // A chave `k` em `x` é substituída por seu predecessor.
            x.keys[idx] = pred;
            /* Agora que o predecessor tomou o lugar de `k` em `x`, o predecessor original deve ser 
            removido da subárvore de `y`.*/
            Delete(y, pred);} 
        
        else if (z.n >= t) {  // Caso 2b
            /*localiza o sucessor de `k` no subárvore cuja raiz é `z`. O sucessor é a menor chave na subárvore de `z`.
*/
            int succ = findSuccessor(z);
            x.keys[idx] = succ;
            Delete(z, succ);} 
        else {  // Caso 2c
            merge(x, idx);
            Delete(y, k);}
        return;}

        // Se a chave não está em x.
        Node child = x.children[idx];
        if (child.n < t) {
            fill(x, idx);}
        Delete(x.children[idx], k);
    }
    
    private int findPredecessor(Node x) {
        while (!x.leaf) {
            x = x.children[x.n];}
        return x.keys[x.n - 1];}

    private int findSuccessor(Node x) {
        while (!x.leaf) {
            x = x.children[0];}
        return x.keys[0];}
    
    private void merge(Node x, int idx) {
        Node y = x.children[idx];
        Node z = x.children[idx + 1];

        y.keys[t - 1] = x.keys[idx];

        for (int j = 0; j < z.n; j++) {
            y.keys[t + j] = z.keys[j];}

        if (!z.leaf) {
            for (int j = 0; j <= z.n; j++) {
                y.children[t + j] = z.children[j];}}

        for (int j = idx; j < x.n - 1; j++) {
            x.keys[j] = x.keys[j + 1];}

        for (int j = idx + 1; j < x.n; j++) {
            x.children[j] = x.children[j + 1];}

        y.n += z.n + 1;
        x.n--;
    }
    
    private void fill(Node x, int idx) {
        if (idx > 0 && x.children[idx - 1].n >= t) {
            borrowFromPrev(x, idx);} 
        else if (idx < x.n && x.children[idx + 1].n >= t) {
            borrowFromNext(x, idx);} 
        else {
            if (idx < x.n){
                merge(x, idx);} 
            else{
                merge(x, idx - 1);}}
    }
    
    private void borrowFromPrev(Node x, int idx) {
        Node child = x.children[idx];
        Node sibling = x.children[idx - 1];

        // Mover todas as chaves de 'child' um passo à frente
        for (int i = child.n - 1; i >= 0; i--) {
            child.keys[i + 1] = child.keys[i];}

        // Se 'child' não é uma folha, mover todos os seus filhos um passo à frente
        if (!child.leaf) {
            for (int i = child.n; i >= 0; i--) {
                child.children[i + 1] = child.children[i];}}

        // Configurar a primeira chave de 'child' como a chave `idx-1` de 'x'
        child.keys[0] = x.keys[idx - 1];

        // Mover o último filho do irmão como primeiro filho de 'child'
        if (!child.leaf) {
            child.children[0] = sibling.children[sibling.n];}

        // Mover a última chave do irmão para `x`
        x.keys[idx - 1] = sibling.keys[sibling.n - 1];

        child.n += 1;
        sibling.n -= 1;
    }
    
    private void borrowFromNext(Node x, int idx) {
        Node child = x.children[idx];
        Node sibling = x.children[idx + 1];

        // Configurar a última chave de 'child' como a chave `idx` de 'x'
        child.keys[child.n] = x.keys[idx];

        // Mover o primeiro filho do irmão para se tornar o último filho de 'child' 
        if (!child.leaf) {
            child.children[child.n + 1] = sibling.children[0];}

        // Mover a primeira chave do irmão para `x`
        x.keys[idx] = sibling.keys[0];

        // Mover todas as chaves em 'sibling' um passo para trás
        for (int i = 1; i < sibling.n; i++) {
            sibling.keys[i - 1] = sibling.keys[i];}

        // Se 'sibling' não é uma folha, mover todos os seus filhos um passo para trás
        if (!sibling.leaf) {
            for (int i = 1; i <= sibling.n; i++) {
                sibling.children[i - 1] = sibling.children[i];}}

        child.n += 1;
        sibling.n -= 1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite o valor de t:");
        int t = scanner.nextInt();
        ArvoreB tree = new ArvoreB(t);

        while (true) {
            System.out.println("1: Inserir\n2: Excluir\n3: Imprimir\n4: Sair");
            int option = scanner.nextInt();
            if (option == 1) {
                System.out.println("Digite o valor para inserir:");
                int value = scanner.nextInt();
                tree.insert(value);} 
            
            else if (option == 2) {
                System.out.println("Digite o valor para excluir:");
                int value = scanner.nextInt();
                tree.Delete(value);} 
            
            else if (option == 3) {
                tree.printTree(tree.root, 0);} 
            else {
                break;}
        }
        scanner.close();
    }
}
