import java.util.ArrayList;
import java.util.List;

public interface ManterVetor
{
    // Numa interface, explicitamos os métodos que uma classe deverá ter
    // sem descrever como esses métodos serão internamente.

    // Assim, uma pessoa desenvolvedora poderá codificar sua própria versão da
    // classe usando o modelo definido na interface, com métodos internamente
    // codificados de acordo com o seu próprio raciocínio.

    // Uma outra pessoa desenvolvedora poderá codificar sua própria da classe
    // usando o modelo definido na interface, mas com métodos internamente
    // codificados de forma diferente da outra pessoa, de acordo com o seu
    // próprio raciocínio.

    // No entanto, ambas as classes poderão ser usadas no programa de aplicação,
    // sem que este tenha de ser modificado, pois ambas seguem o mesmo padrão,
    // o mesmo modelo.

    int tamanho();
    void incluirAoFinal(Ponto novo);
    void incluirEmOrdem(int posicaoDeInclusao, Ponto novo) throws Exception;
    void excluir(int posicaoDeExclusao) throws Exception;
    boolean existe(Ponto procurado);
    boolean lerArquivo(String nomeDoArquivo) throws Exception;
    void gravarArquivo(String nomeDoArquivo) throws Exception;
    Ponto getDado(int posicao) throws Exception;
    void setDado(Ponto novoDado, int posicao) throws Exception;
    int getPosicaoAtual();
    void irParaOInicio();
    void irParaOFim();
    void irParaOProximo();
    void irParaOAnterior();
    void ordenar();
    int getOndeEsta();
    ArrayList<Ponto> getDados();
}
