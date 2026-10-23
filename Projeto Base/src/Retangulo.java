import java.awt.*;

public class Retangulo extends Ponto {
    private int largura;
    private int altura;

    public Retangulo(int x, int y, int largura, int altura, Color cor) throws Exception {
        super(x, y, cor);
        setDimensoes(largura, altura);
    }

    public int getLargura() { return largura; }
    public int getAltura() { return altura; }

    public void setDimensoes(int largura, int altura) throws Exception {
        if (largura <= 0 || altura <= 0)
            throw new Exception("A largura e a altura do retângulo devem ser maiores que zero.");
        this.largura = largura;
        this.altura = altura;
    }

    @Override public void desenhar(Graphics g) { desenhar(cor, g); }

    @Override public void desenhar(Color corDesenho, Graphics g) {
        g.setColor(corDesenho);
        g.drawRect(x, y, largura, altura);
    }

    @Override public String toString() {
        return "r;" + x + ";" + y + ";" + cor.getRed() + ";" + cor.getGreen() + ";" + cor.getBlue()
                + ";" + largura + ";" + altura;
    }
}