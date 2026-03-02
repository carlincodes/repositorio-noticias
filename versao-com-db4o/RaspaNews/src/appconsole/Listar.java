package appconsole;

import java.util.List;

import modelo.Noticia;
import modelo.Assunto;
import requisito.Fachada;

public class Listar {

    public Listar() {
        listar();
    }

    public void listar() {

        System.out.println("------- LISTA DE NOTÍCIAS -------");

        List<Noticia> noticias = Fachada.listarNoticias();

        if (noticias.isEmpty()) {
            System.out.println("Nenhuma notícia cadastrada.");
        } else {
            for (Noticia n : noticias) {
                System.out.println("\n----------------------------------------");
                System.out.println("Título : " + n.getTitulo());
                System.out.println("Resumo : " + n.getResumo());
                System.out.println("Data   : " + n.getData());
                System.out.println("Link   : " + n.getLinkWeb());

                System.out.print("Assuntos: ");
                if (n.getAssuntos() == null || n.getAssuntos().isEmpty()) {
                    System.out.println("Sem assuntos relacionados.");
                } else {
                    n.getAssuntos().forEach(a -> System.out.print(a.getNome() + "  "));
                    System.out.println();
                }
            }
        }


        System.out.println("\n------- LISTA DE ASSUNTOS -------");

        List<Assunto> assuntos = Fachada.listarAssuntos();

        if (assuntos.isEmpty()) {
            System.out.println("Nenhum assunto cadastrado.");
        } else {
            for (Assunto a : assuntos) {
                System.out.println("\n----------------------------------------");
                System.out.println("Assunto: " + a.getNome());
                System.out.print("Notícias: ");

                if (a.getNoticias() == null || a.getNoticias().isEmpty()) {
                    System.out.println("Sem notícias associadas.");
                } else {
                    a.getNoticias().forEach(n -> System.out.print(n.getTitulo() + " | "));
                    System.out.println();
                }
            }
        }
    }

    public static void main(String[] args) {
        new Listar();
    }
}
