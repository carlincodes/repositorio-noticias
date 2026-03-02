package repositorio;

import java.util.List;

import com.db4o.query.Query;
import modelo.Noticia;
import util.Util;

public class NoticiaRepositorio extends CRUDRepositorio<Noticia> {

    @Override
    public Noticia ler(Object chave) {
        String titulo = (String) chave;

        Query q = Util.getManager().query();
        q.constrain(Noticia.class);
        q.descend("titulo").constrain(titulo);
        List<Noticia> resultado = q.execute();

        if (!resultado.isEmpty())
            return resultado.get(0);
        return null;
    }
}
