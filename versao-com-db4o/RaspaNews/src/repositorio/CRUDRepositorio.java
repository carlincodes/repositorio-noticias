/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * Persistência de Objetos
 * Prof. Fausto Maranhão Ayres
 **********************************/
package repositorio;

import java.util.ArrayList;
import java.util.List;

import com.db4o.query.Query;

import util.ControleID;
import util.Util;

public abstract class CRUDRepositorio<T> {
	// operações CRUD para persistir qualquer tipo de objeto T

	public void conectar() {
		Util.conectar();
	}

	public void desconectar() {
		Util.desconectar();
	}

	public void criar(T objeto) {
		Util.getManager().store(objeto);
	}

	public void atualizar(T objeto) {
		Util.getManager().store(objeto);
	}

	public abstract T ler(Object chave);

	public void apagar(T objeto) {
		Util.getManager().delete(objeto);
	}

	public List<T> listar() {
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];

		Query q = Util.getManager().query();
		q.constrain(type);
		List<T> resultado = q.execute();		return new ArrayList<>(resultado);
	}

	public void begin() {
		// nao faz nada, pois o db4o inicia uma transação automaticamente
	}

	public void commit() {
		Util.getManager().commit();
	}

	public void rollback() {
		Util.getManager().rollback();
	}

	public void resetarID(int valorInicial) {
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) ((java.lang.reflect.ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];

		ControleID.resetarRegistroID(type, valorInicial);
	}
}
