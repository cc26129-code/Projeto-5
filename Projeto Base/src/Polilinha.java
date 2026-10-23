import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polilinha extends Ponto {
    private final ArrayList<Ponto> pontos;

    public Polilinha(List<Ponto> pontos, Color cor) throws Exception {
        super(validarPontos(pontos).get(0).getX(), pontos.get(0).getY(), cor);
        this.pontos = new ArrayList<>();
        for (Ponto ponto : pontos)
            this.pontos.add(new Ponto(ponto.getX(), ponto.getY(), cor));
    }

    private static List<Ponto> validarPontos(List<Ponto> pontos) throws Exception {
        if (pontos == null || pontos.size() < 2)
            throw new Exception("Uma polilinha precisa de pelo menos dois pontos.");
        return pontos;
    }

    public List<Ponto> getPontos() { return new ArrayList<>(pontos); }

    @Override public void setCor(Color novaCor) {
        super.setCor(novaCor);
        for (Ponto ponto : pontos) ponto.setCor(novaCor);
    }

    @Override public void deslocar(int dx, int dy) {
        super.deslocar(dx, dy);
        for (Ponto ponto : pontos) ponto.deslocar(dx, dy);
    }

    @Override public void desenhar(Graphics g) { desenhar(cor, g); }

    @Override public void desenhar(Color corDesenho, Graphics g) {
        if (pontos.size() < 2) return;
        g.setColor(corDesenho);
        for (int i = 1; i < pontos.size(); i++) {
            Ponto a = pontos.get(i - 1), b = pontos.get(i);
            g.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
        }
    }

    @Override public String toString() {
        StringBuilder texto = new StringBuilder("pl;").append(x).append(';').append(y).append(';')
                .append(cor.getRed()).append(';').append(cor.getGreen()).append(';').append(cor.getBlue());
        for (Ponto ponto : pontos)
            texto.append(';').append(ponto.getX()).append(';').append(ponto.getY());
        return texto.toString();
    }
}