import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Editor extends JFrame {
    private final MeuJPanel pnlDesenho = new MeuJPanel();
    private final VetorPonto figuras = new VetorPonto(10);
    private final List<Integer> selecionadas = new ArrayList<>();
    private Color corAtual = Color.BLACK;
    private Ponto pontoInicial;
    private String modo = "";
    private final JLabel status = new JLabel("Mensagem: escolha uma ferramenta.");
    private final JFileChooser seletorArquivo = new JFileChooser(new File("."));

    private JButton botao(String texto) {
        JButton b = new JButton(texto);
        b.setFocusable(false);
        return b;
    }

    public Editor() {
        super("Editor Gráfico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);

        JPanel ferramentas = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        adicionarBotao(ferramentas, "Abrir", this::abrir);
        adicionarBotao(ferramentas, "Salvar", this::salvar);
        adicionarBotao(ferramentas, "Ponto", () -> iniciar("ponto", "Clique no local do ponto."));
        adicionarBotao(ferramentas, "Linha", () -> iniciar("linha", "Clique no ponto inicial da linha."));
        adicionarBotao(ferramentas, "Círculo", () -> iniciar("circulo", "Clique no centro do círculo."));
        adicionarBotao(ferramentas, "Oval", () -> iniciar("oval", "Clique no centro do oval."));
        adicionarBotao(ferramentas, "Retângulo", () -> iniciar("retangulo", "Clique no canto inicial do retângulo."));
        adicionarBotao(ferramentas, "Polilinha", () -> iniciar("polilinha", "Clique nos pontos da polilinha; dê duplo clique para terminar."));
        adicionarBotao(ferramentas, "Cores", this::escolherCor);
        adicionarBotao(ferramentas, "Selecionar", this::selecionar);
        adicionarBotao(ferramentas, "Mudar cor", this::mudarCorSelecionadas);
        adicionarBotao(ferramentas, "Deslocar", this::deslocarSelecionadas);
        adicionarBotao(ferramentas, "Remover", this::removerSelecionadas);
        adicionarBotao(ferramentas, "Limpar seleção", this::limparSelecao);
        adicionarBotao(ferramentas, "Apagar", this::apagarTudo);

        setLayout(new BorderLayout());
        add(ferramentas, BorderLayout.NORTH);
        add(pnlDesenho, BorderLayout.CENTER);
        add(status, BorderLayout.SOUTH);

        seletorArquivo.setFileFilter(new FileNameExtensionFilter("Arquivos de texto (*.txt)", "txt"));
        setVisible(true);
    }

    private void adicionarBotao(JPanel painel, String texto, Runnable acao) {
        JButton b = botao(texto);
        b.addActionListener(e -> acao.run());
        painel.add(b);
    }

    private void iniciar(String novoModo, String mensagem) {
        modo = novoModo;
        pontoInicial = null;
        pnlDesenho.iniciarPolilinha();
        status.setText("Mensagem: " + mensagem);
    }

    private void escolherCor() {
        Color escolhida = JColorChooser.showDialog(this, "Escolher cor de desenho", corAtual);
        if (escolhida != null) {
            corAtual = escolhida;
            status.setText("Mensagem: novas figuras usarão a cor escolhida.");
        }
    }

    private void selecionar() {
        if (figuras.tamanho() == 0) {
            mensagem("Não há figuras para selecionar.");
            return;
        }
        modo = "selecionar";
        pontoInicial = null;
        status.setText("Mensagem: clique diretamente em uma figura para selecioná-la.");
    }

    private void mudarCorSelecionadas() {
        if (selecionadas.isEmpty()) { mensagem("Selecione uma ou mais figuras primeiro."); return; }
        Color escolhida = JColorChooser.showDialog(this, "Cor das figuras selecionadas", corAtual);
        if (escolhida == null) return;
        for (int indice : selecionadas) {
            try { figuras.getDado(indice).setCor(escolhida); }
            catch (Exception ex) { mensagem(ex.getMessage()); return; }
        }
        corAtual = escolhida;
        pnlDesenho.repaint();
    }

    private void deslocarSelecionadas() {
        if (selecionadas.isEmpty()) { mensagem("Selecione uma ou mais figuras primeiro."); return; }
        JTextField campoX = new JTextField("0", 6);
        JTextField campoY = new JTextField("0", 6);
        JPanel painel = new JPanel(new GridLayout(2, 2, 5, 5));
        painel.add(new JLabel("Deslocamento X:")); painel.add(campoX);
        painel.add(new JLabel("Deslocamento Y:")); painel.add(campoY);
        if (JOptionPane.showConfirmDialog(this, painel, "Deslocar figuras",
                JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            int dx = Integer.parseInt(campoX.getText().trim());
            int dy = Integer.parseInt(campoY.getText().trim());
            for (int indice : selecionadas) figuras.getDado(indice).deslocar(dx, dy);
            pnlDesenho.repaint();
        } catch (NumberFormatException ex) {
            mensagem("Digite valores inteiros para os deslocamentos.");
        } catch (Exception ex) {
            mensagem(ex.getMessage());
        }
    }

    private void removerSelecionadas() {
        if (selecionadas.isEmpty()) { mensagem("Não há figuras selecionadas."); return; }
        List<Integer> ordem = new ArrayList<>(selecionadas);
        ordem.sort(Collections.reverseOrder());
        try {
            for (int indice : ordem) figuras.excluir(indice);
            selecionadas.clear();
            pnlDesenho.repaint();
            status.setText("Mensagem: figuras removidas.");
        } catch (Exception ex) { mensagem(ex.getMessage()); }
    }

    private void limparSelecao() {
        selecionadas.clear();
        pnlDesenho.repaint();
        status.setText("Mensagem: seleção limpa.");
    }

    private void apagarTudo() {
        figuras.limpar();
        selecionadas.clear();
        modo = "";
        pontoInicial = null;
        pnlDesenho.iniciarPolilinha();
        pnlDesenho.repaint();
        status.setText("Mensagem: desenho apagado.");
    }

    private void salvar() {
        if (seletorArquivo.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File arquivo = seletorArquivo.getSelectedFile();
        if (!arquivo.getName().toLowerCase().endsWith(".txt"))
            arquivo = new File(arquivo.getParentFile(), arquivo.getName() + ".txt");
        try {
            figuras.gravarArquivo(arquivo.getAbsolutePath());
            status.setText("Mensagem: arquivo salvo em " + arquivo.getName());
        } catch (Exception ex) { mensagem("Não foi possível salvar: " + ex.getMessage()); }
    }

    private void abrir() {
        if (seletorArquivo.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File arquivo = seletorArquivo.getSelectedFile();
        try {
            figuras.lerArquivo(arquivo.getAbsolutePath());
            selecionadas.clear();
            modo = "";
            pnlDesenho.iniciarPolilinha();
            pnlDesenho.repaint();
            setTitle("Editor Gráfico - " + arquivo.getName());
            status.setText("Mensagem: arquivo aberto.");
        } catch (Exception ex) { mensagem("Não foi possível abrir o arquivo: " + ex.getMessage()); }
    }

    private void mensagem(String texto) {
        JOptionPane.showMessageDialog(this, texto, "Editor Gráfico", JOptionPane.INFORMATION_MESSAGE);
    }

    private int pedirInteiro(String pergunta, int minimo) {
        while (true) {
            String entrada = JOptionPane.showInputDialog(this, pergunta);
            if (entrada == null) return -1;
            try {
                int valor = Integer.parseInt(entrada.trim());
                if (valor >= minimo) return valor;
                mensagem("Digite um valor maior ou igual a " + minimo + ".");
            } catch (NumberFormatException ex) { mensagem("Digite um número inteiro válido."); }
        }
    }

    private class MeuJPanel extends JPanel {
        private final List<Ponto> pontosPolilinha = new ArrayList<>();

        MeuJPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(800, 500));
            addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(MouseEvent e) { tratarClique(e); }
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override public void mouseMoved(MouseEvent e) {
                    status.setText("Mensagem: " + (modo.isEmpty() ? "pronto" : modo)
                            + " | Coordenada: " + e.getX() + ", " + e.getY());
                    if ("polilinha".equals(modo)) repaint();
                }
            });
        }

        void iniciarPolilinha() { pontosPolilinha.clear(); }

        private void tratarClique(MouseEvent e) {
            int x = e.getX(), y = e.getY();
            try {
                if ("selecionar".equals(modo)) {
                    int indice = encontrarFigura(x, y);
                    if (indice < 0) {
                        status.setText("Mensagem: nenhuma figura encontrada nesse local.");
                    } else if (selecionadas.contains(indice)) {
                        selecionadas.remove(Integer.valueOf(indice));
                        status.setText("Mensagem: figura desmarcada.");
                    } else {
                        selecionadas.add(indice);
                        status.setText("Mensagem: figura " + (indice + 1) + " selecionada.");
                    }
                    repaint();
                    return;
                }
                switch (modo) {
                    case "ponto":
                        figuras.incluirAoFinal(new Ponto(x, y, corAtual));
                        finalizarModo();
                        break;
                    case "linha":
                        if (pontoInicial == null) {
                            pontoInicial = new Ponto(x, y, corAtual);
                            status.setText("Mensagem: clique no ponto final da linha.");
                        } else {
                            figuras.incluirAoFinal(new Linha(pontoInicial.getX(), pontoInicial.getY(), x, y, corAtual));
                            finalizarModo();
                        }
                        break;
                    case "circulo":
                        int raio = pedirInteiro("Digite o raio do círculo:", 1);
                        if (raio >= 1) figuras.incluirAoFinal(new Circulo(x, y, raio, corAtual));
                        finalizarModo();
                        break;
                    case "oval":
                        int raioA = pedirInteiro("Digite o raio horizontal do oval:", 1);
                        if (raioA < 1) { finalizarModo(); break; }
                        int raioB = pedirInteiro("Digite o raio vertical (diferente de " + raioA + "):", 1);
                        if (raioB < 1) { finalizarModo(); break; }
                        if (raioA == raioB) { mensagem("Os raios do oval devem ser diferentes."); break; }
                        figuras.incluirAoFinal(new Oval(x, y, raioA, raioB, corAtual));
                        finalizarModo();
                        break;
                    case "retangulo":
                        int largura = pedirInteiro("Digite a largura do retângulo:", 1);
                        if (largura < 1) { finalizarModo(); break; }
                        int altura = pedirInteiro("Digite a altura do retângulo:", 1);
                        if (altura >= 1) figuras.incluirAoFinal(new Retangulo(x, y, largura, altura, corAtual));
                        finalizarModo();
                        break;
                    case "polilinha":
                        if (e.getClickCount() >= 2) {
                            if (pontosPolilinha.isEmpty()
                                    || pontosPolilinha.get(pontosPolilinha.size() - 1).getX() != x
                                    || pontosPolilinha.get(pontosPolilinha.size() - 1).getY() != y) {
                                pontosPolilinha.add(new Ponto(x, y, corAtual));
                            }
                            if (pontosPolilinha.size() >= 2) {
                                figuras.incluirAoFinal(new Polilinha(pontosPolilinha, corAtual));
                                finalizarModo();
                            } else {
                                mensagem("Uma polilinha precisa de pelo menos dois pontos.");
                                status.setText("Mensagem: clique em mais pontos para continuar a polilinha.");
                            }
                        } else {
                            pontosPolilinha.add(new Ponto(x, y, corAtual));
                            status.setText("Mensagem: ponto adicionado à polilinha. Duplo clique para terminar.");
                            repaint();
                        }
                        break;
                    default:
                        break;
                }
                repaint();
            } catch (Exception ex) { mensagem("Não foi possível criar a figura: " + ex.getMessage()); }
        }

        private int encontrarFigura(int x, int y) {
            for (int i = figuras.tamanho() - 1; i >= 0; i--) {
                try {
                    Ponto f = figuras.getDado(i);
                    double tol = 7.0;
                    if (f instanceof Linha) {
                        Linha l = (Linha) f;
                        if (Line2D.ptSegDist(l.getX(), l.getY(), l.getXFinal(), l.getYFinal(), x, y) <= tol) return i;
                    } else if (f instanceof Circulo) {
                        Circulo c = (Circulo) f;
                        double d = Point2D.distance(x, y, f.getX(), f.getY());
                        if (Math.abs(d - c.getRaio()) <= tol) return i;
                    } else if (f instanceof Oval) {
                        Oval o = (Oval) f;
                        double nx = (x - f.getX()) / (double) o.getRaioA();
                        double ny = (y - f.getY()) / (double) o.getRaioB();
                        if (Math.abs(nx * nx + ny * ny - 1) <= 0.25) return i;
                    } else if (f instanceof Retangulo) {
                        Retangulo r = (Retangulo) f;
                        Rectangle2D area = new Rectangle2D.Double(f.getX() - tol, f.getY() - tol,
                                r.getLargura() + 2 * tol, r.getAltura() + 2 * tol);
                        if (area.contains(x, y)) return i;
                    } else if (f instanceof Polilinha) {
                        List<Ponto> ps = ((Polilinha) f).getPontos();
                        for (int j = 1; j < ps.size(); j++) {
                            Ponto a = ps.get(j - 1), b = ps.get(j);
                            if (Line2D.ptSegDist(a.getX(), a.getY(), b.getX(), b.getY(), x, y) <= tol) return i;
                        }
                    } else if (Point2D.distance(x, y, f.getX(), f.getY()) <= 9.0) return i;
                } catch (Exception ignored) { }
            }
            return -1;
        }

        private void finalizarModo() {
            modo = "";
            pontoInicial = null;
            pontosPolilinha.clear();
            status.setText("Mensagem: figura criada.");
        }

        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (int i = 0; i < figuras.tamanho(); i++) {
                try {
                    Ponto figura = figuras.getDado(i);
                    Graphics2D g2 = (Graphics2D) g.create();
                    if (selecionadas.contains(i)) g2.setStroke(new BasicStroke(3.0f));
                    figura.desenhar(figura.getCor(), g2);
                    g2.dispose();
                } catch (Exception ex) {
                    status.setText("Mensagem: erro ao desenhar figura.");
                }
            }
            if ("polilinha".equals(modo) && !pontosPolilinha.isEmpty()) {
                g.setColor(corAtual);
                for (int i = 1; i < pontosPolilinha.size(); i++) {
                    Ponto a = pontosPolilinha.get(i - 1), b = pontosPolilinha.get(i);
                    g.drawLine(a.getX(), a.getY(), b.getX(), b.getY());
                }
                Ponto ultimo = pontosPolilinha.get(pontosPolilinha.size() - 1);
                g.drawLine(ultimo.getX(), ultimo.getY(), getMousePosition() == null ? ultimo.getX() : getMousePosition().x,
                        getMousePosition() == null ? ultimo.getY() : getMousePosition().y);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Editor::new);
    }
}
