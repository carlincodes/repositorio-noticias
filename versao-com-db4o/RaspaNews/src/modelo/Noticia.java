package modelo;

import java.util.ArrayList;
import java.util.List;

public class Noticia {
    private int id;
    private String titulo;
    private String resumo;
    private String data;
    private String linkWeb;
    private List<Assunto> assuntos = new ArrayList<>();

    public Noticia(String titulo, String resumo, String data, String linkWeb) {
        this.titulo = titulo;
        this.resumo = resumo;
        this.data = data;
        this.linkWeb = linkWeb;
    }

    public Noticia() {}

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getResumo() {
        return resumo;
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getLinkWeb() {
        return linkWeb;
    }

    public void setLinkWeb(String linkWeb) {
        this.linkWeb = linkWeb;
    }

    public List<Assunto> getAssuntos() {
        return assuntos;
    }

    public void adicionar(Assunto a) throws Exception {
        if (a == null)
            throw new Exception("assunto inválido");

        // evita duplicação na notícia
        for (Assunto existente : assuntos) {
            if (existente.getNome().equalsIgnoreCase(a.getNome())) {
                throw new Exception(
                        "assunto '" + a.getNome() + "' já está associado à notícia"
                );
            }
        }

        assuntos.add(a);

        // sincroniza no assunto
        boolean existe = false;

        for (Noticia n : a.getNoticias()) {
            if (n.getTitulo().equalsIgnoreCase(this.titulo)) {
                existe = true;
                break;
            }
        }

        if (!existe) {
            a.getNoticias().add(this);
        }
    }

    public void remover(Assunto a) {
        if (a == null)
            return;

        for (int i = 0; i < assuntos.size(); i++) {
            if (assuntos.get(i).getNome().equalsIgnoreCase(a.getNome())) {
                assuntos.remove(i);
                break;
            }
        }

        for (int i = 0; i < a.getNoticias().size(); i++) {
            if (a.getNoticias().get(i).getTitulo().equalsIgnoreCase(this.titulo)) {
                a.getNoticias().remove(i);
                break;
            }
        }
    }

    public Assunto localizar(String nomeAssunto) {
        for (Assunto a : assuntos) {
            if (a.getNome().equalsIgnoreCase(nomeAssunto)) {
                return a;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder texto = new StringBuilder(
                id +
                " | título: " + titulo +
                " | data: " + data +
                " | link: " + linkWeb +
                " | assuntos: "
        );

        if (assuntos.isEmpty()) {
            texto.append("Sem assuntos");
        } else {
            for (Assunto a : assuntos) {
                texto.append(a.getNome()).append(", ");
            }
        }

        return texto.toString();
    }
}
