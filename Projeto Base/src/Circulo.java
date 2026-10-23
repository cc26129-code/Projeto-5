import java.awt.*;

public class Circulo extends Ponto {
    private int raio;

    public Circulo(int xCentro, int yCentro, int novoRaio, Color novaCor) throws Exception {
        super(xCentro, yCentro, novaCor);
        setRaio(novoRaio);
    }

    public int getRaio() { return raio; }

    public void setRaio(int novoRaio) throws Exception {
        if (novoRaio <= 0) throw new Exception("Círculos precisam de raio maior que zero.");
        raio = novoRaio;
    }

    @Override public void desenhar(Graphics g) { desenhar(cor, g); }

    @Override public void desenhar(Color corDesenho, Graphics g) {
        g.setColor(corDesenho);
        g.drawOval(x - raio, y - raio, 2 * raio, 2 * raio);
    }

    @Override public String toString() {
        return "c;" + x + ";" + y + ";" + cor.getRed() + ";" + cor.getGreen() + ";" + cor.getBlue() + ";" + raio;
    }
}