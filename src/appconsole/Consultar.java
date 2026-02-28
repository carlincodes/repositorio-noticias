package appconsole;

import java.util.List;

import modelo.Noticia;
import modelo.Assunto;
import requisito.Fachada;

public class Consultar {

    public Consultar() {
        consultar();
    }

    public void consultar() {

 
        String data = "29/10/2025";
        System.out.println("\n--- Notícias na data: " + data + " ---");

        List<Noticia> todas = Fachada.listarNoticias();

        for (Noticia n : todas) {
            if (n.getData().equals(data)) {
                System.out.println(n);
            }
        }

        String nomeAssunto = "Economia";
        System.out.println("\n--- Notícias do assunto: " + nomeAssunto + " ---");

        try {
            Assunto assunto = Fachada.localizarAssunto(nomeAssunto);

            for (Noticia n : assunto.getNoticias()) {
                System.out.println(n);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        final int nNoticias = 1;
        System.out.println("\n--- Assuntos com mais de " + nNoticias + " notícias ---");

        List<Assunto> listaAssuntos = Fachada.listarAssuntos();

        for (Assunto a : listaAssuntos) {
            if (a.getNoticias().size() > nNoticias) {
                System.out.println(a);
            }
        }
    }

    public static void main(String[] args) {
        new Consultar();
    }
}
