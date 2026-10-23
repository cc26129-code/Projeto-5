import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;

public class VetorPontoSmokeTest {
    public static void main(String[] args) throws Exception {
        VetorPonto vetor = new VetorPonto(1);
        Color azul = Color.BLUE;

        vetor.incluirAoFinal(new Ponto(10, 20, azul));
        vetor.incluirAoFinal(new Linha(1, 2, 30, 40, Color.RED));
        vetor.incluirAoFinal(new Circulo(50, 60, 12, Color.GREEN));
        vetor.incluirAoFinal(new Oval(70, 80, 15, 9, Color.BLACK));
        vetor.incluirAoFinal(new Retangulo(90, 100, 25, 18, Color.ORANGE));
        vetor.incluirAoFinal(new Polilinha(java.util.Arrays.asList(
                new Ponto(1, 1, azul), new Ponto(5, 5, azul), new Ponto(10, 2, azul)), azul));

        File arquivo = File.createTempFile("figuras", ".txt");
        File invalido = File.createTempFile("figuras-invalidas", ".txt");
        try {
            vetor.gravarArquivo(arquivo.getAbsolutePath());
            VetorPonto recuperado = new VetorPonto(1);
            recuperado.lerArquivo(arquivo.getAbsolutePath());
            if (recuperado.tamanho() != 6) throw new AssertionError("Não recuperou todas as figuras.");
            if (!(recuperado.getDado(1) instanceof Linha)
                    || !(recuperado.getDado(2) instanceof Circulo)
                    || !(recuperado.getDado(3) instanceof Oval)
                    || !(recuperado.getDado(4) instanceof Retangulo)
                    || !(recuperado.getDado(5) instanceof Polilinha))
                throw new AssertionError("Tipo de figura incorreto após recuperar arquivo.");

            recuperado.excluir(1);
            if (recuperado.tamanho() != 5) throw new AssertionError("Falha ao excluir figura.");
            recuperado.incluirEmOrdem(1, new Ponto(2, 3, Color.MAGENTA));
            if (recuperado.tamanho() != 6) throw new AssertionError("Falha ao inserir em posição.");
            recuperado.irParaOFim();
            if (recuperado.getPosicaoAtual() != recuperado.tamanho() - 1)
                throw new AssertionError("Falha na navegação até o fim.");

            try (FileWriter writer = new FileWriter(invalido)) {
                writer.write("p;1;2;0;0;0\n");
                writer.write("c;1;2;0;0;0\n"); // falta o raio
            }
            int antes = recuperado.tamanho();
            boolean falhouComoEsperado = false;
            try {
                recuperado.lerArquivo(invalido.getAbsolutePath());
            } catch (Exception esperado) {
                falhouComoEsperado = true;
            }
            if (!falhouComoEsperado) throw new AssertionError("Arquivo inválido foi aceito.");
            if (recuperado.tamanho() != antes)
                throw new AssertionError("Arquivo inválido apagou o desenho existente.");

            recuperado.limpar();
            if (recuperado.tamanho() != 0) throw new AssertionError("Falha ao limpar vetor.");
            System.out.println("OK: desenho, persistência, validação, inserção, exclusão, navegação e limpeza.");
        } finally {
            Files.deleteIfExists(arquivo.toPath());
            Files.deleteIfExists(invalido.toPath());
        }
    }
}