import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class VetorPonto implements ManterVetor {
    private Ponto[] figuras;
    private int quantosDados, posicaoAtual, tamanhoFisico;

    public VetorPonto(int tamanhoDesejado) {
        tamanhoFisico = Math.max(1, tamanhoDesejado);
        figuras = new Ponto[tamanhoFisico];
        quantosDados = 0;
        posicaoAtual = -1;
    }

    public int tamanho() { return quantosDados; }

    private void expandirVetor() {
        tamanhoFisico += 10;
        figuras = Arrays.copyOf(figuras, tamanhoFisico);
    }

    public void incluirAoFinal(Ponto novo) {
        if (novo == null) throw new IllegalArgumentException("A figura não pode ser nula.");
        if (quantosDados >= tamanhoFisico) expandirVetor();
        figuras[quantosDados++] = novo;
    }

    public void excluir(int posicao) throws Exception {
        if (posicao < 0 || posicao >= quantosDados) throw new Exception("Índice fora dos limites.");
        for (int i = posicao; i < quantosDados - 1; i++) figuras[i] = figuras[i + 1];
        figuras[--quantosDados] = null;
        if (quantosDados == 0) posicaoAtual = -1;
        else if (posicaoAtual == posicao) posicaoAtual = Math.min(posicao, quantosDados - 1);
        else if (posicaoAtual > posicao) posicaoAtual--;
    }

    public void limpar() {
        Arrays.fill(figuras, 0, quantosDados, null);
        quantosDados = 0;
        posicaoAtual = -1;
    }

    public boolean existe(Ponto procurado) {
        if (procurado == null) return false;
        for (int i = 0; i < quantosDados; i++) {
            if (figuras[i].getX() == procurado.getX() && figuras[i].getY() == procurado.getY()) {
                posicaoAtual = i;
                return true;
            }
        }
        posicaoAtual = -1;
        return false;
    }

    public boolean lerArquivo(String nomeDoArquivo) throws Exception {
        ArrayList<Ponto> carregadas = new ArrayList<>();
        try (BufferedReader leitor = new BufferedReader(new FileReader(nomeDoArquivo))) {
            String linha;
            int numeroLinha = 0;
            while ((linha = leitor.readLine()) != null) {
                numeroLinha++;
                if (linha.trim().isEmpty()) continue;
                String[] c = linha.trim().split("\\s*;\\s*");
                try {
                    if (c.length < 6) throw new IOException("Formato inválido na linha " + numeroLinha + ".");
                    String tipo = c[0].trim();
                    int x = inteiro(c, 1), y = inteiro(c, 2);
                    Color cor = new Color(inteiro(c, 3), inteiro(c, 4), inteiro(c, 5));
                    switch (tipo) {
                        case "p":
                            exigirCampos(c, 6, numeroLinha);
                            carregadas.add(new Ponto(x, y, cor));
                            break;
                        case "l":
                            exigirCampos(c, 8, numeroLinha);
                            carregadas.add(new Linha(x, y, inteiro(c, 6), inteiro(c, 7), cor));
                            break;
                        case "c":
                            exigirCampos(c, 7, numeroLinha);
                            carregadas.add(new Circulo(x, y, inteiro(c, 6), cor));
                            break;
                        case "o":
                            exigirCampos(c, 8, numeroLinha);
                            carregadas.add(new Oval(x, y, inteiro(c, 6), inteiro(c, 7), cor));
                            break;
                        case "r":
                            exigirCampos(c, 8, numeroLinha);
                            carregadas.add(new Retangulo(x, y, inteiro(c, 6), inteiro(c, 7), cor));
                            break;
                        case "pl":
                            if (c.length < 10 || (c.length - 6) % 2 != 0)
                                throw new IOException("Polilinha inválida na linha " + numeroLinha + ": informe pelo menos dois pontos.");
                            ArrayList<Ponto> pontos = new ArrayList<>();
                            for (int i = 6; i < c.length; i += 2)
                                pontos.add(new Ponto(inteiro(c, i), inteiro(c, i + 1), cor));
                            carregadas.add(new Polilinha(pontos, cor));
                            break;
                        default:
                            throw new IOException("Tipo de figura desconhecido na linha " + numeroLinha + ": " + tipo);
                    }
                } catch (NumberFormatException | IllegalArgumentException ex) {
                    throw new IOException("Dados inválidos na linha " + numeroLinha + ".", ex);
                } catch (ArrayIndexOutOfBoundsException ex) {
                    throw new IOException("Faltam dados na linha " + numeroLinha + ".", ex);
                }
            }
        }

        // Só substitui o desenho atual depois que o arquivo inteiro foi validado.
        limpar();
        for (Ponto figura : carregadas) incluirAoFinal(figura);
        return true;
    }

    private int inteiro(String[] campos, int indice) {
        return Integer.parseInt(campos[indice].trim());
    }

    private void exigirCampos(String[] campos, int quantidade, int linha) throws IOException {
        if (campos.length != quantidade)
            throw new IOException("Quantidade de campos inválida na linha " + linha + ".");
    }

    public void gravarArquivo(String nomeDoArquivo) throws Exception {
        try (PrintWriter escritor = new PrintWriter(new BufferedWriter(new FileWriter(nomeDoArquivo)))) {
            for (int i = 0; i < quantosDados; i++) escritor.println(figuras[i].toString());
            if (escritor.checkError()) throw new IOException("Falha ao gravar o arquivo.");
        }
    }

    public Ponto getDado(int posicao) throws Exception {
        if (posicao < 0 || posicao >= quantosDados) throw new Exception("Índice inválido: " + posicao);
        return figuras[posicao];
    }

    public void setDado(Ponto novoDado, int posicao) throws Exception {
        if (posicao < 0 || posicao >= quantosDados) throw new Exception("Índice inválido: " + posicao);
        if (novoDado == null) throw new IllegalArgumentException("A figura não pode ser nula.");
        figuras[posicao] = novoDado;
    }

    public int getPosicaoAtual() { return posicaoAtual; }
    public void irParaOInicio() { posicaoAtual = quantosDados == 0 ? -1 : 0; }
    public void irParaOFim() { posicaoAtual = quantosDados - 1; }
    public void irParaOProximo() {
        if (quantosDados == 0) posicaoAtual = -1;
        else if (posicaoAtual < quantosDados - 1) posicaoAtual++;
    }
    public void irParaOAnterior() {
        if (posicaoAtual > 0) posicaoAtual--;
        else if (quantosDados == 0) posicaoAtual = -1;
    }

    public ArrayList<Ponto> getDados() {
        ArrayList<Ponto> dados = new ArrayList<>();
        for (int i = 0; i < quantosDados; i++) dados.add(figuras[i]);
        return dados;
    }

    public void incluirEmOrdem(int posicao, Ponto novo) throws Exception {
        if (posicao < 0 || posicao > quantosDados) throw new Exception("Índice inválido.");
        if (novo == null) throw new IllegalArgumentException("A figura não pode ser nula.");
        if (quantosDados >= tamanhoFisico) expandirVetor();
        for (int i = quantosDados; i > posicao; i--) figuras[i] = figuras[i - 1];
        figuras[posicao] = novo;
        quantosDados++;
        if (posicaoAtual >= posicao) posicaoAtual++;
    }

    public int getOndeEsta() { return posicaoAtual; }

    public void ordenar() {
        // Não é utilizada pelo editor; a interface exige o método.
    }
}