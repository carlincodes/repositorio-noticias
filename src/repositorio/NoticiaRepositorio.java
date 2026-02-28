package repositorio;

import java.util.List;

import jakarta.persistence.TypedQuery;
import modelo.Noticia;
import modelo.Assunto;
import util.Util;

public class NoticiaRepositorio extends CRUDRepositorio<Noticia> {

    @Override
    public Noticia ler(Object chave) {
        String titulo = (String) chave;

        TypedQuery<Noticia> q = Util.getManager().createQuery(
            "select n from Noticia n where lower(n.titulo) = lower(:titulo)",
            Noticia.class
        );
        q.setParameter("titulo", titulo);

        List<Noticia> resultado = q.getResultList();
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    @Override
    public List<Noticia> listar() {
        return Util.getManager()
            .createQuery("select n from Noticia n order by n.titulo", Noticia.class)
            .getResultList();
    }

    // 🔹 NOVA CONSULTA JPQL (substitui n.getAssuntos())
    public List<Assunto> assuntosDaNoticia(String titulo) {
        return Util.getManager()
            .createQuery("""
                select a from Assunto a
                join a.noticias n
                where lower(n.titulo) = lower(:titulo)
            """, Assunto.class)
            .setParameter("titulo", titulo)
            .getResultList();
    }
}
