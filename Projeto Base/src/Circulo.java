import java.awt.*;

public class Circulo extends Ponto
{
    // esta classe Circulo herda (x, y) da classe Ponto, que são as coordenadas
    // do ponto central do círculo; também herda a cor e os métodos de Ponto.
    // Em seguida definimos o atributo para guardar o comprimento do raio do
    // círculo:
    int raio;

    // construtor da classe Circulo, chama inicialmente o construtor
    // da classe Ponto (a classe superior, super) para gerar o ponto
    // central deste círculo
    public Circulo(int xCentro, int yCentro, int novoRaio, Color novaCor)
           throws Exception {
        super(xCentro, yCentro, novaCor);  // construtor de Ponto(x,y)
        setRaio(novoRaio);                 // define o valor do raio do círculo
    }

    public int getRaio()
    {
        return raio;
    }

    public void setRaio(int novoRaio) throws Exception {
        if (novoRaio <= 0)
            throw new Exception("Círculos precisam de raio >= 1");

        raio = novoRaio;
    }

    public void desenhar(Color corDesenho, Graphics g) {
        g.setColor(corDesenho);
        g.drawOval(x-raio, y-raio,  // centro - raio --> canto superior esquerdo
                   2*raio,2*raio);  // centro + raio --> largura e altura
    }



}
