package repositorio;

import java.util.List;

import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import modelo.Assunto;
import modelo.Noticia;
import util.Util;

public class AssuntoRepositorio extends CRUDRepositorio<Assunto> {

    @Override
    public Assunto ler(Object chave) {
        String nome = (String) chave;

        TypedQuery<Assunto> q = Util.getManager().createQuery(
            "select a from Assunto a where lower(a.nome) = lower(:nome)",
            Assunto.class
        );
        q.setParameter("nome", nome);

        List<Assunto> resultado = q.getResultList();
        return resultado.isEmpty() ? null : resultado.get(0);
    }

    @Override
    public List<Assunto> listar() {
        return Util.getManager()
            .createQuery("select a from Assunto a order by a.nome", Assunto.class)
            .getResultList();
    }

    public List<Noticia> noticiasDoAssunto(String nomeAssunto) {
        return Util.getManager()
            .createQuery("""
                select n from Noticia n
                join n.assuntos a
                where lower(a.nome) = lower(:nome)
            """, Noticia.class)
            .setParameter("nome", nomeAssunto)
            .getResultList();
    }
    


}



