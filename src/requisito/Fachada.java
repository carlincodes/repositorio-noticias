package requisito;

import java.time.LocalDate;
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
        assuntoRep.desconectar();

        if (a == null)
            throw new Exception("assunto inexistente: " + nome);

        return a;
    }

    public static Noticia localizarNoticia(String titulo) throws Exception {
        noticiaRep.conectar();
        Noticia n = noticiaRep.ler(titulo);
        noticiaRep.desconectar();

        if (n == null)
            throw new Exception("notícia inexistente: " + titulo);

        return n;
    }

    public static void criarAssunto(String nome) throws Exception {
        assuntoRep.conectar();
        assuntoRep.begin();

        if (assuntoRep.ler(nome) != null) {
            assuntoRep.rollback();
            throw new Exception("assunto já existe: " + nome);
        }

        assuntoRep.criar(new Assunto(nome));
        assuntoRep.commit();
        assuntoRep.desconectar();
    }

    public static void criarNoticia(String titulo, String resumo, LocalDate data, String link) throws Exception {
        noticiaRep.conectar();
        noticiaRep.begin();

        LocalDate dataConvertida;

        if (noticiaRep.ler(titulo) != null) {
            noticiaRep.rollback();
            throw new Exception("notícia já existe: " + titulo);
        }

        noticiaRep.criar(new Noticia(titulo, resumo, data, link));
        noticiaRep.commit();
        noticiaRep.desconectar();
    }

    public static void adicionarAssuntoNaNoticia(String titulo, String nomeAssunto) throws Exception {
        noticiaRep.conectar();
        assuntoRep.conectar();
        noticiaRep.begin();

        Noticia n = noticiaRep.ler(titulo);
        Assunto a = assuntoRep.ler(nomeAssunto);

        if (n == null || a == null) {
            noticiaRep.rollback();
            throw new Exception("notícia ou assunto inexistente");
        }

        n.adicionar(a);

        noticiaRep.atualizar(n);
        assuntoRep.atualizar(a);

        noticiaRep.commit();
        noticiaRep.desconectar();
        assuntoRep.desconectar();
    }

    public static void apagarAssunto(String nome) throws Exception {
        assuntoRep.conectar();
        noticiaRep.conectar();
        assuntoRep.begin();

        Assunto a = assuntoRep.ler(nome);
        if (a == null) {
            assuntoRep.rollback();
            throw new Exception("assunto inexistente: " + nome);
        }

        for (Noticia n : assuntoRep.noticiasDoAssunto(nome)) {
            n.remover(a);
            noticiaRep.atualizar(n);
        }

        assuntoRep.apagar(a);
        assuntoRep.commit();
        assuntoRep.desconectar();
        noticiaRep.desconectar();
    }

    public static void apagarNoticia(String titulo) throws Exception {
        noticiaRep.conectar();
        assuntoRep.conectar();
        noticiaRep.begin();

        Noticia n = noticiaRep.ler(titulo);
        if (n == null) {
            noticiaRep.rollback();
            throw new Exception("notícia inexistente: " + titulo);
        }

        for (Assunto a : noticiaRep.assuntosDaNoticia(titulo)) {
            a.remover(n);
            assuntoRep.atualizar(a);
        }

        noticiaRep.apagar(n);
        noticiaRep.commit();
        noticiaRep.desconectar();
        assuntoRep.desconectar();
    }	
    
    public static void alterarAssunto(String nome, String novoNome) throws Exception {
        assuntoRep.conectar();
        assuntoRep.begin();

        Assunto a = assuntoRep.ler(nome);
        if (a == null) {
            assuntoRep.rollback();
            throw new Exception("assunto inexistente: " + nome);
        }

        // verifica duplicidade (regra de negócio)
        if (assuntoRep.ler(novoNome) != null) {
            assuntoRep.rollback();
            throw new Exception("assunto já existe: " + novoNome);
        }

        a.setNome(novoNome);
        assuntoRep.atualizar(a);

        assuntoRep.commit();
        assuntoRep.desconectar();
    }

    public static void alterarNoticia(
            String tituloAtual,
            String novoTitulo,
            String novoResumo,
            LocalDate novaData,
            String novoLink
    ) throws Exception {

        noticiaRep.conectar();
        noticiaRep.begin();

        Noticia n = noticiaRep.ler(tituloAtual);
        if (n == null) {
            noticiaRep.rollback();
            throw new Exception("notícia inexistente: " + tituloAtual);
        }

        // 🔹 regra de negócio: evitar título duplicado
        if (novoTitulo != null && !novoTitulo.equalsIgnoreCase(tituloAtual)) {
            if (noticiaRep.ler(novoTitulo) != null) {
                noticiaRep.rollback();
                throw new Exception("já existe notícia com o título: " + novoTitulo);
            }
            n.setTitulo(novoTitulo);
        }

        if (novoResumo != null)
            n.setResumo(novoResumo);

        if (novaData != null)
            n.setData(novaData);

        if (novoLink != null)
            n.setLinkWeb(novoLink);

        noticiaRep.atualizar(n);

        noticiaRep.commit();
        noticiaRep.desconectar();
    }

    public static void removerAssuntoDaNoticia(String titulo, String nomeAssunto) throws Exception {

        noticiaRep.conectar();
        assuntoRep.conectar();
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

        // 🔹 regra de negócio: remove associação
        n.remover(a);

        noticiaRep.atualizar(n);
        assuntoRep.atualizar(a);

        noticiaRep.commit();
        noticiaRep.desconectar();
        assuntoRep.desconectar();
    }
    
    public static String listarAssuntosComoTextoUnico(String titulo) throws Exception {

        noticiaRep.conectar();

        Noticia n = noticiaRep.ler(titulo);
        if (n == null) {
            noticiaRep.desconectar();
            throw new Exception("notícia inexistente: " + titulo);
        }

        List<Assunto> assuntos = noticiaRep.assuntosDaNoticia(titulo);

        String texto = "";
        for (Assunto a : assuntos) {
            if (!texto.isEmpty()) {
                texto += ", ";
            }
            texto += a.getNome();
        }

        if (texto.isEmpty())
            texto = "sem assuntos";

        noticiaRep.desconectar();
        return texto;
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
    
    public static List<Noticia> consultarNoticiasPorData(LocalDate data) {

        noticiaRep.conectar();
        List<Noticia> todas = noticiaRep.listar();
        noticiaRep.desconectar();

        return todas.stream()
                .filter(n -> n.getData().equals(data))
                .toList();
    }

    public static List<Noticia> consultarNoticiasPorAssunto(String nomeAssunto) throws Exception {

        noticiaRep.conectar();
        List<Noticia> noticias = noticiaRep.listar();
        noticiaRep.desconectar();

        List<Noticia> resultado = new java.util.ArrayList<>();

        for (Noticia n : noticias) {
            String assuntos = listarAssuntosComoTextoUnico(n.getTitulo());
            if (assuntos.contains(nomeAssunto)) {
                resultado.add(n);
            }
        }

        return resultado;
    }

    public static List<Object[]> consultarAssuntosComMaisDeNNoticias(int nNoticias) throws Exception {

        List<Assunto> assuntos = listarAssuntos();
        List<Noticia> noticias = listarNoticias();

        List<Object[]> resultado = new java.util.ArrayList<>();

        for (Assunto a : assuntos) {
            int contador = 0;

            for (Noticia n : noticias) {
                String texto = listarAssuntosComoTextoUnico(n.getTitulo());
                if (texto.contains(a.getNome())) {
                    contador++;
                }
            }

            if (contador > nNoticias) {
                resultado.add(new Object[]{a.getNome(), contador});
            }
        }

        return resultado;
    }
}
