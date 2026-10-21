import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

public class Editor extends JFrame {

    static private MeuJPanel pnlDesenho;
    private static VetorPonto figuras;

    // informa ao programa o que ele deve esperar das ações do usuário
    // enquanto desenha figuras geométricas
    static private boolean esperaPonto, esperaInicioReta, esperaFimReta;

    Color corAtual = Color.black;   // cor em que se desenha no momento

    // armazenará o primeiro ponto base de uma figura geométrica
    private Ponto p1 = new Ponto(0,0,Color.black);

    static JLabel statusBar1, statusBar2;

    private JButton btnPonto, btnLinha, btnCirculo, btnOval, btnCor, btnAbrir,
            btnSalvar, btnApagar, btnLimpar, btnSair;

    private JPanel pnlBotoes;   // container dos botões

    static private JInternalFrame janelaFilha;

    public Editor() throws Exception    // construtor de Editor que criará o JFrame, colocará seu
    {			    // título, estabelecerá um tamanho para o formulário e o
                    // exibirá
        super("Editor Gráfico");	// cria o JFrame e coloca um título

        Icon imgAbrir = new ImageIcon("botoes\\abrir.png");
        btnAbrir = new JButton("Abrir", imgAbrir);
        // Define que o texto fica embaixo da imagem
        btnAbrir.setVerticalTextPosition(SwingConstants.BOTTOM);
        // Centraliza o texto em relação à imagem horizontalmente
        btnAbrir.setHorizontalTextPosition(SwingConstants.CENTER);

        btnSalvar = new JButton("Salvar", new ImageIcon("botoes\\salvar.png"));
        btnSalvar.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnSalvar.setHorizontalTextPosition(SwingConstants.CENTER);

        btnPonto = new JButton("Ponto", new ImageIcon("botoes\\ponto.png"));
        btnPonto.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnPonto.setHorizontalTextPosition(SwingConstants.CENTER);

        btnLinha = new JButton("Linha", new ImageIcon("botoes\\linha.png"));
        btnLinha.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnLinha.setHorizontalTextPosition(SwingConstants.CENTER);

        btnCirculo = new JButton("Circulo", new ImageIcon("botoes\\circulo.png"));
        btnCirculo.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnCirculo.setHorizontalTextPosition(SwingConstants.CENTER);

        btnOval = new JButton("Elipse", new ImageIcon("botoes\\oval.png"));
        btnOval.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnOval.setHorizontalTextPosition(SwingConstants.CENTER);

        btnCor = new JButton("Cores", new ImageIcon("botoes\\cores.png"));
        btnCor.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnCor.setHorizontalTextPosition(SwingConstants.CENTER);

        btnApagar = new JButton("Apagar", new ImageIcon("botoes\\apagar.png"));
        btnApagar.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnApagar.setHorizontalTextPosition(SwingConstants.CENTER);

        btnLimpar = new JButton("Limpar", new ImageIcon("botoes\\limpar.png"));
        btnLimpar.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnLimpar.setHorizontalTextPosition(SwingConstants.CENTER);

        btnSair = new JButton("Sair", new ImageIcon("botoes\\sair.png"));
        btnSair.setVerticalTextPosition(SwingConstants.BOTTOM);
        btnSair.setHorizontalTextPosition(SwingConstants.CENTER);

        // cria o JPanel que armazenará os botões
        pnlBotoes = new JPanel();
        // cria o layout usado para dispor fisicamente os botões
        FlowLayout flwBotoes = new FlowLayout();
        // informa que os componentes do pnlBotoes serão dispostos em forma livre
        pnlBotoes.setLayout(flwBotoes);

        // adiciona os controles visuais (botões) ao painel de botões, de cima
        // para baixo, da esquerda para direita.
        pnlBotoes.add(btnAbrir);
        pnlBotoes.add(btnSalvar);
        pnlBotoes.add(btnPonto);
        pnlBotoes.add(btnLinha);
        pnlBotoes.add(btnCirculo);
        pnlBotoes.add(btnOval);
        pnlBotoes.add(btnCor);
        pnlBotoes.add(btnApagar);
        pnlBotoes.add(btnSair);

        setSize(700,500);			// dimensões iniciais do formulário em pixels

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Para centralizar o formulário na tela em uso (2 ou mais monitores)
        // 1. Descobre a configuração de tela onde o mouse está atualmente
        PointerInfo pointerInfo = MouseInfo.getPointerInfo();
        GraphicsDevice monitorAtual = pointerInfo.getDevice();
        Rectangle limitesTela = monitorAtual.getDefaultConfiguration().getBounds();

        // 2. Calcula o centro dessa tela específica
        int x = limitesTela.x + (limitesTela.width - getWidth()) / 2;
        int y = limitesTela.y + (limitesTela.height - getHeight()) / 2;

        // 3. Define a posição do JFrame no monitor correto
        setLocation(x, y);

        Container cntForm = getContentPane(); // acessa o painel de conteúdo do frame
        cntForm.setLayout(new BorderLayout());
        cntForm.add(pnlBotoes, BorderLayout.NORTH);

        JDesktopPane panDesenho = new JDesktopPane();
        cntForm.add(panDesenho);

        janelaFilha = new JInternalFrame("Nenhum arquivo aberto", true, true, true, true);
        panDesenho.add(janelaFilha);
                            // largura           altura
        janelaFilha.setSize(this.getWidth() / 2, this.getHeight() / 2);
        janelaFilha.show();
        janelaFilha.setOpaque(true);

        pnlDesenho = new MeuJPanel();
        Container cntFrame = janelaFilha.getContentPane();
        cntFrame.add(pnlDesenho);

        figuras = new VetorPonto(5);

        btnAbrir.addActionListener(new FazAbertura());
        btnPonto.addActionListener(new DesenhaPonto());
        btnLinha.addActionListener(new DesenhaLinha());

        setVisible(true);	    // exibe o formulário na tela
    }

    public static void desenharObjetos(Graphics g)
    {
        pnlDesenho.paintComponent(g);
    }

    public static void main(String[] args) throws Exception
    {
        Editor aplicacao = new Editor();    // instancia a aplicação
        aplicacao.addWindowListener (
                new WindowAdapter() {   //  cria instância da interface
                    public void windowClosing(WindowEvent e){
                        System.exit(0);
                    }
                }
        );
    }

    // classe interna à classe Editor
    private class MeuJPanel extends JPanel
                            implements MouseMotionListener, MouseListener
    {
        JPanel pnlStatus = new JPanel();			// barra de status

        // construtor específico da classe MeuJPanel
        public MeuJPanel() {
            super();            // chama o construtor da classe JPanel
            pnlStatus.setLayout(new GridLayout(1, 2));	// painel com 2 colunas
            statusBar1 = new JLabel("Mensagem: ");
            statusBar2 = new JLabel("Coordenada: ");
            pnlStatus.add(statusBar1);					// label na coluna da esquerda
            pnlStatus.add(statusBar2); 					// label na coluna da direita
            getContentPane().add(pnlStatus, BorderLayout.SOUTH); // status no fundo do formulário

            // abaixo, this é o próprio objeto da classe MeuJPanel
            addMouseListener(this);		  // esta classe “ouve” eventos de mouse
            addMouseMotionListener(this); // e “ouve” também seus movimentos
        }

        public void mouseMoved(MouseEvent e)	{
            statusBar2.setText("Coordenada: "+e.getX()+","+e.getY());
        }
        public void mouseClicked (MouseEvent e)	{
            statusBar1.setText("Mensagem:");
        }
        public void mousePressed (MouseEvent e)	{
            if (esperaPonto)
            {
                Ponto novoPonto = new Ponto(e.getX(), e.getY(), corAtual);
                figuras.incluirAoFinal(novoPonto);
                novoPonto.desenhar(novoPonto.getCor(), pnlDesenho.getGraphics());
                esperaPonto = false;  // pois já digitalizou o ponto desejado
            }
            else
                if (esperaInicioReta)
                {
                    p1.setCor(corAtual);
                    try {
                        p1.setX(e.getX());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    try {
                        p1.setY(e.getY());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    esperaInicioReta = false;
                    esperaFimReta    = true;
                    statusBar1.setText("Mensagem: clique no ponto final da linha");
                }
                else
                if (esperaFimReta)
                {
                    limparEsperas();
                    Linha novaLinha = new Linha(p1.getX(), p1.getY(),
                            e.getX(), e.getY(), corAtual);
                    figuras.incluirAoFinal(novaLinha);
                    novaLinha.desenhar(novaLinha.getCor(), pnlDesenho.getGraphics());
                }
        }
        public void paintComponent(Graphics g)
        {
            for (int atual = 0; atual < figuras.tamanho(); atual++)
            {
                Ponto aFigura = null;
                try {
                    aFigura = figuras.getDado(atual);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                aFigura.desenhar(aFigura.getCor(), g);
            }
        }
        public void mouseDragged(MouseEvent e)	{
            // não faz nada por enquanto
        }
        public void mouseEntered (MouseEvent e)	{
            // não faz nada por enquanto
        }
        public void mouseExited (MouseEvent e)	{
            // não faz nada por enquanto
        }
        public void mouseReleased (MouseEvent e)	{
            // não faz nada por enquanto
        }
    }

    // classe interna que trata a ação de clicar no btnAbrir
    private class FazAbertura implements ActionListener {
        public void actionPerformed(ActionEvent e)	// código executado no evento
        {
            JFileChooser arqEscolhido = new JFileChooser ();
            arqEscolhido.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int result = arqEscolhido.showOpenDialog(Editor.this);
//… código de verificação se um arquivo foi selecionado e obtenção de seu nome
            if (result == JFileChooser.APPROVE_OPTION)
            {
                File arquivo = arqEscolhido.getSelectedFile();
                System.out.println("Processando "+arquivo.getName());
                try {
                        if (figuras.lerArquivo(arquivo.getName() ) )
                        {
                            janelaFilha.setTitle(arquivo.getName());
                            desenharObjetos(pnlDesenho.getGraphics());
                        }
                        else
                            janelaFilha.setTitle("Não foi possível ler o arquivo");
                }
                catch (Exception ioe){
                        System.out.println("Erro de leitura no arquivo");
                }
            }
        }
    }

    // classe interna que trata a ação de clicar no btnPonto
    // para que o programa saiba que se está esperando que o
    // usuário digitalize um ponto no pnlDesenho
    private class DesenhaPonto implements ActionListener {
        public void actionPerformed(ActionEvent e)    // código executado no evento
        {
            statusBar1.setText("Mensagem: clique no local desejado para o ponto");
            limparEsperas();
            esperaPonto = true;
        }
    }

    private class DesenhaLinha implements ActionListener {
        public void actionPerformed(ActionEvent e)	  {
            statusBar1.setText("Mensagem: clique no ponto inicial da reta");
            limparEsperas();
            esperaInicioReta = true;
        }
    }

    private static void limparEsperas()
    {
        esperaPonto = false;
        esperaInicioReta = false;
        esperaFimReta = false;
    }

}
