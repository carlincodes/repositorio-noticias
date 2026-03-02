package repositorio;

import java.util.List;

import com.db4o.query.Query;
import modelo.Assunto;
import util.Util;

public class AssuntoRepositorio extends CRUDRepositorio<Assunto> {

    @Override
    public Assunto ler(Object chave) {
        String nome = (String) chave;

        Query q = Util.getManager().query();
        q.constrain(Assunto.class);
        q.descend("nome").constrain(nome);

        List<Assunto> resultado = q.execute();

        if (!resultado.isEmpty())
            return resultado.getFirst();
        else
            return null;
    }
}
