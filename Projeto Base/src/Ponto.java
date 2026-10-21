import java.awt.*; // abstract windowing toolkit

public class Ponto {
    protected int x, y;
    protected Color cor;

    public Ponto(int cX, int cY, Color qualCor) {
        x = cX;
        y = cY;
        cor = qualCor;
    }

    public int getX() {
        return x;
    }

    public void setX(int novoX) throws Exception {
        if (x < 0)
            throw new Exception("x não pode ser negativo!");
        x = novoX;
    }

    public int getY() {
        return y;
    }

    public void setY(int novoY) throws Exception {
        if (y < 0)
            throw new Exception("y não pode ser negativo!");
        y = novoY;
    }

    public void setCor(Color novaCor)
    {
        cor = novaCor;
    }

    public Color getCor()
    {
        return cor;
    }

    public void desenhar(Graphics g)
    {
        g.setColor(cor);        // define a cor de desenho na "tela"
        g.drawLine(x, y, x, y); // desenha um único pixel na "tela"
    }

    // sobrecarga do método desenhar: mesmo nome, diferentes parâmetros
    // é uma versão diferente do método desenhar()
    // a aplicação pode chamar desenhar() com um único parâmetro ou
    // chamar desenhar() com 2 parâmetros
    public void desenhar(Color corDiferente, Graphics g) {
        g.setColor(corDiferente);
        g.drawLine(x, y, x, y);
    }

    public String toString()
    {
        return "p;"+x+";"+y+";"+cor.getRed()+";"+cor.getGreen()+";"+cor.getBlue();
    }

}
