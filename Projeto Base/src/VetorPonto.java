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
        if (quantosDados >= tamanhoFisico) expandirVetor();
        figuras[quantosDados++] = novo;
    }

    public void excluir(int posicao) throws Exception {
        if (posicao < 0 || posicao >= quantosDados) throw new Exception("Índice fora dos limites.");
        for (int i = posicao; i < quantosDados - 1; i++) figuras[i] = figuras[i + 1];
        figuras[--quantosDados] = null;
    }

    public void limpar() {
        Arrays.fill(figuras, 0, quantosDados, null);
        quantosDados = 0;
        posicaoAtual = -1;
    }

    public boolean existe(Ponto procurado) {
        for (int i = 0; i < quantosDados; i++) {
            if (figuras[i].getX() == procurado.getX() && figuras[i].getY() == procurado.getY()) {
                posicaoAtual = i;
                return true;
            }
        }
        return false;
    }

    public boolean lerArquivo(String nomeDoArquivo) throws Exception {
        try (BufferedReader leitor = new BufferedReader(new FileReader(nomeDoArquivo))) {
            limpar();
            String linha;
            int numeroLinha = 0;
            while ((linha = leitor.readLine()) != null) {
                numeroLinha++;
                if (linha.trim().isEmpty()) continue;
                String[] c = linha.trim().split("\\s*;\\s*");
                if (c.length < 6) throw new IOException("Formato inválido na linha " + numeroLinha);
                String tipo = c[0].trim();
                int x = Integer.parseInt(c[1].trim()), y = Integer.parseInt(c[2].trim());
                Color cor = new Color(Integer.parseInt(c[3].trim()), Integer.parseInt(c[4].trim()),
                        Integer.parseInt(c[5].trim()));
                switch (tipo) {
                    case "p": incluirAoFinal(new Ponto(x, y, cor)); break;
                    case "l": incluirAoFinal(new Linha(x, y, Integer.parseInt(c[6].trim()),
                            Integer.parseInt(c[7].trim()), cor)); break;
                    case "c": incluirAoFinal(new Circulo(x, y, Integer.parseInt(c[6].trim()), cor)); break;
                    case "o": incluirAoFinal(new Oval(x, y, Integer.parseInt(c[6].trim()),
                            Integer.parseInt(c[7].trim()), cor)); break;
                    case "r": incluirAoFinal(new Retangulo(x, y, Integer.parseInt(c[6].trim()),
                            Integer.parseInt(c[7].trim()), cor)); break;
                    case "pl":
                        ArrayList<Ponto> pontos = new ArrayList<>();
                        for (int i = 6; i + 1 < c.length; i += 2)
                            pontos.add(new Ponto(Integer.parseInt(c[i].trim()), Integer.parseInt(c[i + 1].trim()), cor));
                        incluirAoFinal(new Polilinha(pontos, cor));
                        break;
                    default: throw new IOException("Tipo de figura desconhecido na linha " + numeroLinha + ": " + tipo);
                }
            }
            return true;
        } catch (NumberFormatException ex) {
            throw new IOException("Há um número inválido no arquivo de figuras.", ex);
        }
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
        figuras[posicao] = novoDado;
    }

    public int getPosicaoAtual() { return posicaoAtual; }
    public void irParaOInicio() { posicaoAtual = quantosDados == 0 ? -1 : 0; }
    public void irParaOFim() { posicaoAtual = quantosDados - 1; }
    public void irParaOProximo() { if (posicaoAtual < quantosDados - 1) posicaoAtual++; }
    public void irParaOAnterior() { if (posicaoAtual > 0) posicaoAtual--; }
    public ArrayList<Ponto> getDados() {
        ArrayList<Ponto> dados = new ArrayList<>();
        for (int i = 0; i < quantosDados; i++) dados.add(figuras[i]);
        return dados;
    }
    public void incluirEmOrdem(int posicao, Ponto novo) throws Exception {
        if (posicao < 0 || posicao > quantosDados) throw new Exception("Índice inválido.");
        if (quantosDados >= tamanhoFisico) expandirVetor();
        for (int i = quantosDados; i > posicao; i--) figuras[i] = figuras[i - 1];
        figuras[posicao] = novo;
        quantosDados++;
    }
    public int getOndeEsta() { return posicaoAtual; }
    public void ordenar() { }
}