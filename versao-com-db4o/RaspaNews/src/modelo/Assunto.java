package modelo;

import java.util.ArrayList;
import java.util.List;

public class Assunto {
    private int id;
    private String nome;
    private List<Noticia> noticias = new ArrayList<>();

    public Assunto(String nome) {
        this.nome = nome;
    }

    public Assunto() {}

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Noticia> getNoticias() {
        return noticias;
    }

    public void adicionar(Noticia n) throws Exception {
        if (n == null)
            throw new Exception("notícia inválida");

        // evita duplicação no assunto
        for (Noticia existente : noticias) {
            if (existente.getTitulo().equalsIgnoreCase(n.getTitulo())) {
                throw new Exception(
                        "notícia '" + n.getTitulo() + "' já está associada ao assunto"
                );
            }
        }

        noticias.add(n);

        // sincroniza na notícia
        boolean existe = false;

        for (Assunto a : n.getAssuntos()) {
            if (a.getNome().equalsIgnoreCase(this.nome)) {
                existe = true;
                break;
            }
        }

        if (!existe) {
            n.getAssuntos().add(this);
        }
    }

    public void remover(Noticia n) {
        if (n == null)
            return;

        // remove da lista de notícias deste assunto
        for (int i = 0; i < noticias.size(); i++) {
            if (noticias.get(i).getTitulo().equalsIgnoreCase(n.getTitulo())) {
                noticias.remove(i);
                break;
            }
        }

        // remove da lista de assuntos da notícia
        for (int i = 0; i < n.getAssuntos().size(); i++) {
            if (n.getAssuntos().get(i).getNome().equalsIgnoreCase(this.nome)) {
                n.getAssuntos().remove(i);
                break;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder(
                id + " | nome: " + nome + " | notícias: "
        );

        if (noticias.isEmpty()) {
            texto.append("Sem notícias");
        } else {
            for (Noticia n : noticias) {
                texto.append(n.getTitulo()).append(", ");
            }
        }

        return texto.toString();
    }
}
