package appconsole;

import modelo.Noticia;
import requisito.Fachada;

public class Deletar {

    public Deletar() {
        apagar();
    }

    public void apagar() {

        String titulo = "Safra de uva está no final na região de Jales";

        try {
            System.out.println("\n--- Tentando localizar notícia ---");

            Noticia noticia = Fachada.localizarNoticia(titulo);
            System.out.println("Notícia encontrada: " + noticia);

            System.out.println("\n--- Removendo notícia e atualizando relacionamentos ---");

            Fachada.apagarNoticia(titulo);

            System.out.println("\n Notícia removida com sucesso.");

        } catch (Exception e) {
            System.out.println(" Erro: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Deletar();
    }
}
