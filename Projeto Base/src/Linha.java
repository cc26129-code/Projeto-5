import java.awt.*;

public class Linha extends Ponto {
    private Ponto pontoFinal;

    public Linha(int x1, int y1, int x2, int y2, Color novaCor) {
        super(x1, y1, novaCor);
        pontoFinal = new Ponto(x2, y2, novaCor);
    }

    public Ponto getPontoFinal() { return pontoFinal; }
    public int getXFinal() { return pontoFinal.getX(); }
    public int getYFinal() { return pontoFinal.getY(); }

    public void setPontoFinal(Ponto fim) throws Exception {
        setPontoFinal(fim.getX(), fim.getY());
    }

    public void setPontoFinal(int novoX, int novoY) throws Exception {
        if (novoX < 0 || novoY < 0) throw new Exception("Coordenada negativa!");
        pontoFinal.setX(novoX);
        pontoFinal.setY(novoY);
    }

    @Override public void deslocar(int dx, int dy) {
        super.deslocar(dx, dy);
        pontoFinal.deslocar(dx, dy);
    }

    @Override public void desenhar(Graphics g) { desenhar(cor, g); }

    @Override public void desenhar(Color corDesenho, Graphics g) {
        g.setColor(corDesenho);
        g.drawLine(x, y, pontoFinal.getX(), pontoFinal.getY());
    }

    @Override public String toString() {
        return "l;" + x + ";" + y + ";" + cor.getRed() + ";" + cor.getGreen() + ";" + cor.getBlue()
                + ";" + pontoFinal.getX() + ";" + pontoFinal.getY();
    }
}