import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class VetorPonto implements ManterVetor
{
    private Ponto[] figuras;
    private int quantosDados,
                posicaoAtual,
                tamanhoFisico;

    public VetorPonto(int tamanhoDesejado)
    {
        figuras = new Ponto[tamanhoDesejado];
        tamanhoFisico = tamanhoDesejado;
        quantosDados = 0;
        posicaoAtual = -1;  // nenhuma posição visitada
    }

    public int tamanho()
    {
        return quantosDados;
    }

    private void expandirVetor()
    {
        tamanhoFisico += 10;
        var vetorMaior = Arrays.copyOf(figuras, tamanhoFisico);

        // ou, para criar e copiar com código:
        // var vetorMaior = new Ponto[tamanhoFisico];
        // for (int i = 0; i < quantosDados; i++)
        //    vetorMaior[i] = figuras[i];

        figuras = vetorMaior;
    }

    @Override
    public void incluirAoFinal(Ponto novo) {
        if (quantosDados >= tamanhoFisico)
           expandirVetor();

        figuras[quantosDados++] = novo;
    }

    @Override
    public void excluir(int posicaoDeExclusao) throws Exception {
        if (posicaoDeExclusao < 0 || posicaoDeExclusao >= quantosDados)
            throw new Exception("Exclusão fora dos limites do vetor.");

        quantosDados--;
        for (int i = posicaoDeExclusao; i < quantosDados; i++)
            figuras[i] = figuras[i+1];
    }

    @Override
    public boolean existe(Ponto procurado) {
        boolean achou = false;
        for (int i = 0; i < quantosDados && !achou; i++)
            if (figuras[i].getX() == procurado.getX() &&
                figuras[i].getY() == procurado.getY())
            {
                posicaoAtual = i;
                achou = true;
            }
        return achou;
    }

    @Override
    public boolean lerArquivo(String nomeDoArquivo) throws Exception {
        BufferedReader arqFiguras = new BufferedReader(
                new FileReader(nomeDoArquivo));
        try {
            String linha = arqFiguras.readLine();
            while (linha != null)
            {
                String[] campos = linha.split(";");
                String tipoDaFigura = campos[0];
                int xBase = Integer.parseInt(campos[1]);
                int yBase = Integer.parseInt(campos[2]);
                int corR  = Integer.parseInt(campos[3]);
                int corG  = Integer.parseInt(campos[4]);
                int corB  = Integer.parseInt(campos[5]);
                Color cor = new Color(corR, corG, corB);
                switch (tipoDaFigura)
                {
                    case "p" : incluirAoFinal( new Ponto(xBase,yBase, cor) ); // é ponto
                        break;
                    case "l" : // figura é uma linha
                        int xFinal =Integer.parseInt(campos[6]);
                        int yFinal =Integer.parseInt(campos[7]);
                        incluirAoFinal( new Linha(xBase, yBase, xFinal, yFinal, cor) );
                        break;
                    case "c" : // figura é um círculo
                        int raio = Integer.parseInt(campos[6]);
                        incluirAoFinal( new Circulo(xBase, yBase, raio, cor) );
                        break;
                    case "o" : // figura é uma oval
                        int raioA =Integer.parseInt(campos[6]);
                        int raioB =Integer.parseInt(campos[7]);
                        incluirAoFinal( new Oval(xBase, yBase, raioA, raioB, cor) );
                        break;
                }
                linha = arqFiguras.readLine();
            }
            arqFiguras.close();
            return true;
        }
        catch (IOException ioe){
            System.out.println("Erro de leitura no arquivo");
        }
        return false;
    }

    @Override
    public void gravarArquivo(String nomeDoArquivo) throws Exception {

    }

    @Override
    public Ponto getDado(int posicao) throws Exception {
        if (posicao < 0 || posicao >= quantosDados)
            throw new Exception("Indexação inválida: "+posicao);

        return figuras[posicao];
    }

    @Override
    public void setDado(Ponto novoDado, int posicao) throws Exception {
        if (posicao < 0 || posicao > quantosDados)
            throw new Exception("Indexação inválida: "+posicao);

        figuras[posicao] = novoDado;
    }

    @Override
    public int getPosicaoAtual() {
        return posicaoAtual;
    }

    @Override
    public void irParaOInicio() {
        posicaoAtual = 0;
    }

    @Override
    public void irParaOFim() {
        posicaoAtual = quantosDados;
    }

    @Override
    public void irParaOProximo() {
        if (posicaoAtual < quantosDados-1)
            posicaoAtual++;
    }

    @Override
    public void irParaOAnterior() {
        if (posicaoAtual > 0)
            posicaoAtual--;
    }

    @Override
    public ArrayList<Ponto> getDados() {
        var osDados = new ArrayList<Ponto>(); // vetor de pontos
        for (int i=0; i< quantosDados; i++)
            osDados.add(figuras[i]);
        return osDados;
    }

    // nada será feito se a aplicação chamar os métodos abaixo
    @Override
    public void incluirEmOrdem(int posicaoDeInclusao, Ponto novo) throws Exception {
        // não incluiremos em ordem
    }

    @Override
    public int getOndeEsta() {
        return 0;
    }
    @Override
    public void ordenar() {  }
}
