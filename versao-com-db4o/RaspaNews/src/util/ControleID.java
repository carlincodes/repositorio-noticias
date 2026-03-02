package util;
/**********************************
 * IFPB - Curso Superior de Sistemas para Internet
 * Persistência de Objetos
 * Prof. Fausto Maranhão Ayres
 **********************************/

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.EmbeddedObjectContainer;
import com.db4o.ObjectContainer;
import com.db4o.cs.Db4oClientServer;
import com.db4o.events.EventRegistry;
import com.db4o.events.EventRegistryFactory;
import com.db4o.query.Query;


public class ControleID {
	//classe que controla a geração de IDs sequenciais para 
	//as classes que possuem atributo id
	
	private static ObjectContainer sequencia; // bd auxiliar de sequencias DE IDs

	public static void ativar(ObjectContainer manager) {
		if (manager == null)
			throw new RuntimeException("Não pode ativar ControleID - manager desconectado"); // desativado

		if (manager instanceof EmbeddedObjectContainer) {
			// banco de sequencia local
			sequencia = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "sequencia.db4o");
		} else {
			// banco de sequencia remoto
			String ipservidor = Util.getIPservidor();
			sequencia = Db4oClientServer.openClient(Db4oClientServer.newClientConfiguration(), ipservidor, 35000,
					"usuario0", "senha0");
		}

		// REGISTRO DE EVENTOS (TRIGGERS) DO MANAGER
		EventRegistry eventRegistry = EventRegistryFactory.forObjectContainer(manager);

		// Resgistrar trigger "BEFORE PERSIST" disparado antes do manager.store(objeto)
		eventRegistry.creating().addListener((event, args) -> {
			try {
				Object objeto = args.object(); // objeto que esta sendo gravado
				//Field field = objeto.getClass().getDeclaredField("id");
				Class<?> classe = objeto.getClass();
				Field campo = null;
				for (Field f : getAllFieldsList(classe)) {
					if (f.getName().equals("id")) {
						campo = f;
						break;
					}
				}

				if (campo != null) { // tem campo id?
					Class<?> cls = objeto.getClass();
					String nomedaclasse = "";
					if(cls.getSuperclass()==Object.class)
						nomedaclasse = objeto.getClass().getName();
					else
						nomedaclasse = cls.getSuperclass().getName();
					
					RegistroID registro = lerRegistroID(nomedaclasse); // pega id da tabela
					registro.incrementarID(); // incrementa o id
					campo.setAccessible(true); // habilita acesso ao campo id do objeto
					campo.setInt(objeto, registro.getid()); // atualiza o id do objeto
					sequencia.store(registro);
					sequencia.commit();				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
			}
		});

		// Resgistrar trigger "AFTER COMMIT" disparado após manager.commit()
		eventRegistry.created().addListener((event, args) -> {
			manager.ext().purge(); // limpar cache do manager
			sequencia.ext().purge(); //
		});

		// Resgistrar trigger "BEFORE CLOSE" disparado antes do manager.close()
		eventRegistry.closing().addListener((event, args) -> {
			if (sequencia != null && !sequencia.ext().isClosed())
				sequencia.close(); // fecha o banco de sequencias
		});

		// Resgistrar trigger "AFTER QUERY" disparado pelo manager.query()
		eventRegistry.queryFinished().addListener((event, args) -> {
			// limpar o cache de objetos do manager apos cada query,
			// forcando a leitura direta do banco na proxima query
			manager.ext().purge();
			sequencia.ext().purge(); // limpar cache do manager
		});
	}

	private static RegistroID lerRegistroID(String nomeclasse) {
		Query q = sequencia.query();
		q.constrain(RegistroID.class);
		q.descend("nomedaclasse").constrain(nomeclasse);
		List<RegistroID> resultados = q.execute();
		if (resultados.size() > 0)
			return resultados.getFirst();
		else
			return new RegistroID(nomeclasse); // novo registro de id
	}

	public static void resetarRegistroID(Class<?> classe, int valorInicial) {
		String nomedaclasse = classe.getName();
		RegistroID registro = lerRegistroID(nomedaclasse); // pega registro da classe
		if (registro != null)
			sequencia.delete(registro); // apaga o registro atual da class
		registro = new RegistroID(nomedaclasse,valorInicial); // novo registro 
		sequencia.store(registro);
		sequencia.commit();
	}
	
	public static <X> List<Field> getAllFieldsList(final Class<X> cls) {
		// retorna uma lista com todos os campos do objeto
		final List<Field> allFields = new ArrayList<>();
		Class<?> currentClass = cls;
		while (currentClass != null) {
			final Field[] declaredFields = currentClass.getDeclaredFields();
			Collections.addAll(allFields, declaredFields);
			currentClass = currentClass.getSuperclass();
		}
		return allFields;
	}
} // fim classe ControleID

//*************************************************************
//classe interna
//Encapsula o ultimo ID gerado de uma classe (inicia com 0)
//*************************************************************
class RegistroID {
	private String nomedaclasse;
	private int ultimoid;

	public RegistroID(String nomedaclasse) {
		this.nomedaclasse = nomedaclasse;
		this.ultimoid = 0;
	}
	public RegistroID(String nomedaclasse, int valorInicial) {
		this.nomedaclasse = nomedaclasse;
		this.ultimoid = valorInicial;
	}

	public String getNomedaclasse() {
		return nomedaclasse;
	}

	public int getid() {
		return ultimoid;
	}

	public void incrementarID() {
		ultimoid++;
	}

	@Override
	public String toString() {
		return "RegistroID [nomedaclasse=" + nomedaclasse + ", ultimoid=" + ultimoid + "]";
	}

} // fim classe RegistroID
