import java.awt.*;  // para acessar Color e métodos de desenho

public class Linha extends Ponto        // extends --> indica herança
{
    // esta classe Linha herda (x, y) da classe Ponto, que são as coordenadas
    // do ponto inicial da reta; também herda a cor e os métodos de Ponto.
    // Em seguida define o ponto final:

    private Ponto pontoFinal;

    // construtor da classe Linha
    public Linha(int x1, int y1, int x2, int y2, Color novaCor)
    {
        // super() é o construtor da classe superior (ancestral, Ponto)
        // recebe os parâmetros que instanciam um ponto na classe Linha
        super(x1, y1, novaCor);   // instancia o ponto inicial da linha
        pontoFinal = new Ponto(x2,y2, novaCor);
    }

    public Ponto getPontoFinal()
    {
        return pontoFinal;
    }

    public void setPontoFinal(Ponto oFimDaLinha) throws  Exception
    {
        if (x < 0 || y < 0)
            throw new Exception("Coordenada negativa!!");
        pontoFinal.x = oFimDaLinha.x;
        pontoFinal.y = oFimDaLinha.y;
    }

    public int getXFinal()
    {
        return pontoFinal.x;
    }

    public int getYFinal()
    {
        return pontoFinal.y;
    }

    // sobrecarga do setter
    public void setPontoFinal(int x, int y) throws Exception
    {
        if (x < 0 || y < 0)
            throw new Exception("Coordenada negativa!!");
        pontoFinal.x = x;
        pontoFinal.y = y;
    }
    // Polimorfismo: uma classe que é herança de outra, implementa
    // o mesmo nome de método, mas com funcionamento diferente,
    // específico para esta classe
    public void desenhar(Graphics g)
    {
        g.setColor(cor);
        g.drawLine(super.x, super.y, pontoFinal.x, pontoFinal.y);
    }

    public void desenhar(Color corDiferente, Graphics g)
    {
        g.setColor(corDiferente);
        g.drawLine(x, y, pontoFinal.x, pontoFinal.y);
    }

    public String toString()
    {
        return "l;"+x+";"+y+";"+cor.getRed()+";"+cor.getGreen()+";"+cor.getBlue()+";"
                +pontoFinal.x+";"+pontoFinal.y;
    }
}

