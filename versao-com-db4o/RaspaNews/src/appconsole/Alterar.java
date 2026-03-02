package appconsole;

import requisito.Fachada;

public class Alterar {

    public Alterar() {
        atualizar();
    }

    public void atualizar() {
        System.out.println("=== ALTERAÇÃO DE NOTÍCIA ===");

        String tituloNoticia = "Safra de uva está no final na região de Jales";
        String assuntoParaRemover = "Economia";

        try {
            var noticia = Fachada.localizarNoticia(tituloNoticia);
            System.out.println("Notícia encontrada: " + noticia);

            Fachada.removerAssuntoDaNoticia(tituloNoticia, assuntoParaRemover);
            System.out.println("Assunto removido com sucesso.");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new Alterar();
    }
}
