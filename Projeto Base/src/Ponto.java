import java.awt.*;

public class Ponto {
    protected int x, y;
    protected Color cor;

    public Ponto(int cX, int cY, Color qualCor) {
        x = cX;
        y = cY;
        cor = qualCor;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void setX(int novoX) throws Exception {
        if (novoX < 0) throw new Exception("x não pode ser negativo!");
        x = novoX;
    }

    public void setY(int novoY) throws Exception {
        if (novoY < 0) throw new Exception("y não pode ser negativo!");
        y = novoY;
    }

    public void setCor(Color novaCor) { cor = novaCor; }
    public Color getCor() { return cor; }

    public void deslocar(int dx, int dy) {
        x += dx;
        y += dy;
    }

    public void desenhar(Graphics g) { desenhar(cor, g); }

    public void desenhar(Color corDesenho, Graphics g) {
        g.setColor(corDesenho);
        g.drawLine(x, y, x, y);
    }

    public String toString() {
        return "p;" + x + ";" + y + ";" + cor.getRed() + ";" + cor.getGreen() + ";" + cor.getBlue();
    }
}