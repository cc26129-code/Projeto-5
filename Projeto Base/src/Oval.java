import java.awt.*;

public class Oval extends Ponto {
    private int raioA, raioB;

    public Oval(int xCentro, int yCentro, int novoRaioA, int novoRaioB, Color novaCor) throws Exception {
        super(xCentro, yCentro, novaCor);
        setRaios(novoRaioA, novoRaioB);
    }

    public void setRaios(int novoRaioA, int novoRaioB) throws Exception {
        if (novoRaioA <= 0 || novoRaioB <= 0)
            throw new Exception("Raios do oval precisam ser maiores que zero.");
        if (novoRaioA == novoRaioB)
            throw new Exception("Os raios do oval precisam ser diferentes.");
        raioA = novoRaioA;
        raioB = novoRaioB;
    }

    public int getRaioA() { return raioA; }
    public int getRaioB() { return raioB; }

    @Override public void desenhar(Graphics g) { desenhar(cor, g); }

    @Override public void desenhar(Color corDesenho, Graphics g) {
        g.setColor(corDesenho);
        g.drawOval(x - raioA, y - raioB, 2 * raioA, 2 * raioB);
    }

    @Override public String toString() {
        return "o;" + x + ";" + y + ";" + cor.getRed() + ";" + cor.getGreen() + ";" + cor.getBlue()
                + ";" + raioA + ";" + raioB;
    }
}