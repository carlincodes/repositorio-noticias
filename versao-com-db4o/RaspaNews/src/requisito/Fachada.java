package requisito;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import modelo.Assunto;
import modelo.Noticia;
import repositorio.AssuntoRepositorio;
import repositorio.NoticiaRepositorio;

public class Fachada {
    private Fachada() {}

    private static AssuntoRepositorio assuntoRep = new AssuntoRepositorio();
    private static NoticiaRepositorio noticiaRep = new NoticiaRepositorio();

    public static Assunto localizarAssunto(String nome) throws Exception {
        assuntoRep.conectar();
        Assunto a = assuntoRep.ler(nome);
        if (a == null) {
            assuntoRep.desconectar();
            throw new Exception("assunto inexistente: " + nome);
        }
        assuntoRep.desconectar();
        return a;
    }

    public static Noticia localizarNoticia(String titulo) throws Exception {
        noticiaRep.conectar();
        Noticia n = noticiaRep.ler(titulo);
        if (n == null) {
            noticiaRep.desconectar();
            throw new Exception("notícia inexistente: " + titulo);
        }
        noticiaRep.desconectar();
        return n;
    }

    public static void criarAssunto(String nome) throws Exception {
        assuntoRep.conectar();
        assuntoRep.begin();

        Assunto a = assuntoRep.ler(nome);
        if (a != null) {
            assuntoRep.rollback();
            throw new Exception("assunto já existe: " + nome);
        }

        a = new Assunto(nome);
        assuntoRep.criar(a);

        assuntoRep.commit();
        assuntoRep.desconectar();
    }

    public static void criarNoticia(String titulo, String resumo, String data, String link) throws Exception {
        noticiaRep.conectar();
        noticiaRep.begin();

        try {
            LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (DateTimeParseException e) {
            noticiaRep.rollback();
            throw new Exception("data inválida: " + data);
        }

        Noticia n = noticiaRep.ler(titulo);
        if (n != null) {
            noticiaRep.rollback();
            throw new Exception("notícia já existe: " + titulo);
        }

        n = new Noticia(titulo, resumo, data, link);
        noticiaRep.criar(n);

        noticiaRep.commit();
        noticiaRep.desconectar();
    }


    public static void alterarAssunto(String nome, String novoNome) throws Exception {
        assuntoRep.conectar();
        assuntoRep.begin();

        Assunto a = assuntoRep.ler(nome);
        if (a == null) {
            assuntoRep.rollback();
            throw new Exception("assunto inexistente: " + nome);
        }

        a.setNome(novoNome);
        assuntoRep.atualizar(a);

        assuntoRep.commit();
        assuntoRep.desconectar();
    }

    public static void alterarNoticia(String titulo, String novoTitulo, String resumo,
                                      String data, String link) throws Exception {
        noticiaRep.conectar();
        noticiaRep.begin();

        Noticia n = noticiaRep.ler(titulo);
        if (n == null) {
            noticiaRep.rollback();
            throw new Exception("notícia inexistente: " + titulo);
        }

        if (data != null) {
            try {
                LocalDate.parse(data, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                n.setData(data);
            } catch (DateTimeParseException e) {
                noticiaRep.rollback();
                throw new Exception("data inválida: " + data);
            }
        }

        if (novoTitulo != null)
            n.setTitulo(novoTitulo);

        if (resumo != null)
            n.setResumo(resumo);

        if (link != null)
            n.setLinkWeb(link);

        noticiaRep.atualizar(n);

        noticiaRep.commit();
        noticiaRep.desconectar();
    }

    public static void adicionarAssuntoNaNoticia(String titulo, String nomeAssunto) throws Exception {
        noticiaRep.conectar();
        noticiaRep.begin();

        Noticia n = noticiaRep.ler(titulo);
        Assunto a = assuntoRep.ler(nomeAssunto);

        if (n == null) {
            noticiaRep.rollback();
            throw new Exception("notícia inexistente: " + titulo);
        }

        if (a == null) {
            noticiaRep.rollback();
            throw new Exception("assunto inexistente: " + nomeAssunto);
        }

        n.adicionar(a);

        noticiaRep.atualizar(n);
        assuntoRep.atualizar(a);

        noticiaRep.commit();
        noticiaRep.desconectar();
    }

    public static void removerAssuntoDaNoticia(String titulo, String nomeAssunto) throws Exception {
        noticiaRep.conectar();
        noticiaRep.begin();

        Noticia n = noticiaRep.ler(titulo);
        Assunto a = assuntoRep.ler(nomeAssunto);

        if (n == null) {
            noticiaRep.rollback();
            throw new Exception("notícia inexistente: " + titulo);
        }

        if (a == null) {
            noticiaRep.rollback();
            throw new Exception("assunto inexistente: " + nomeAssunto);
        }

        n.remover(a);

        noticiaRep.atualizar(n);
        assuntoRep.atualizar(a);

        noticiaRep.commit();
        noticiaRep.desconectar();
    }

    public static void apagarAssunto(String nome) throws Exception {
        assuntoRep.conectar();
        assuntoRep.begin();

        Assunto a = assuntoRep.ler(nome);
        if (a == null) {
            assuntoRep.rollback();
            throw new Exception("assunto inexistente: " + nome);
        }

        for (Noticia n : a.getNoticias()) {
            n.remover(a);
            noticiaRep.atualizar(n);
        }

        assuntoRep.apagar(a);
        assuntoRep.commit();
        assuntoRep.desconectar();
    }

    public static void apagarNoticia(String titulo) throws Exception {
        noticiaRep.conectar();
        noticiaRep.begin();

        Noticia n = noticiaRep.ler(titulo);
        if (n == null) {
            noticiaRep.rollback();
            throw new Exception("notícia inexistente: " + titulo);
        }

        for (Assunto a : n.getAssuntos()) {
            a.remover(n);
            assuntoRep.atualizar(a);
        }

        noticiaRep.apagar(n);

        noticiaRep.commit();
        noticiaRep.desconectar();
    }

    public static List<Assunto> listarAssuntos() {
        assuntoRep.conectar();
        List<Assunto> lista = assuntoRep.listar();
        assuntoRep.desconectar();
        return lista;
    }

    public static List<Noticia> listarNoticias() {
        noticiaRep.conectar();
        List<Noticia> lista = noticiaRep.listar();
        noticiaRep.desconectar();
        return lista;
    }
    
}
