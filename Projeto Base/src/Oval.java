import java.awt.*;

public class Oval extends Ponto
{
    // esta classe Oval herda (x, y) da classe Ponto, que são as coordenadas
    // do ponto central do círculo; também herda a cor e os métodos de Ponto.
    // Em seguida definimos od atributod para guardar od comprimentod do raio
    // horizontal e do raio vertical da oval:
    int raioA,      // raio horizontal
        raioB;      // raio vertical

    public Oval(int xCentro, int yCentro, int novoRaioA,
                int novoRaioB, Color novaCor) throws Exception {
        super(xCentro, yCentro, novaCor);  // construtor de Ponto(x,y)
        setRaios(novoRaioA, novoRaioB);
    }

    public void setRaios(int novoRaioA, int novoRaioB) throws Exception {
        if (novoRaioA <= 0 || novoRaioB <= 0)
            throw new Exception("Raios de oval precisam ser maiores que zero");

        if (novoRaioA == novoRaioB)
            throw new Exception("Raios de oval precisam ser diferentes");

        raioA = novoRaioA;
        raioB = novoRaioB;
    }

    public int getRaioA() {
        return raioA;
    }

    public int getRaioB() {
        return raioB;
    }

    public void desenhar(Color corDesenho, Graphics g) {
        g.setColor(corDesenho);
        g.drawOval(x-raioA, y-raioB,   // centro - raio
                   2*raioA, 2*raioB);  // largura e altura

    }
}
