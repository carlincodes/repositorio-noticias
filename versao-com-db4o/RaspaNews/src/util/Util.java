package util;
/**********************************
 * IFPB - Curso Superior de Sistemas para Internet
 * Persist�ncia de Objetos
 * Prof. Fausto Maranh�o Ayres
 **********************************/

import java.util.Properties;

import javax.swing.JOptionPane;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ClientConfiguration;

import modelo.Noticia;
import modelo.Assunto;


public class Util {
	private static ObjectContainer manager;
	private static String ipservidor;

	public static void conectar() {
		try {
			//obter o ip do servidor ou localhost do arquivo de propriedades			
			Properties props = new Properties();
			props.load(Util.class.getResourceAsStream("/util/ip.properties"));//carrega o arquivo de propriedades
			ipservidor = props.getProperty("ipatual");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,	"ip incorreto=" + ipservidor + "\n" + e.getMessage());
			System.exit(0);
		}
		//abrir conexao com o banco
		if(ipservidor.equals("localhost"))
			conectarBancoLocal(); // banco local (pasta do projeto)
		else
			conectarBancoRemoto(); // banco remoto (precisa de um servidor ativo)
		
		// ativar controle de IDs autom�ticos
		ControleID.ativar(manager); // ativa gera��o de IDs autom�ticos para as classes com atributo "int id"
	}

	private static void  conectarBancoLocal() {
		if (manager != null)
			return ; // ja tem uma conexao

		// ---------------------------------------------------------------
		// configurar, criar e abrir banco local (pasta do projeto)
		// ---------------------------------------------------------------
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().messageLevel(0); // mensagens na tela 0(desliga),1,2,3...
		
		// habilitar cascata na altera��o, remo��o e leitura
		config.common().objectClass(Assunto.class).cascadeOnDelete(false);
		config.common().objectClass(Assunto.class).cascadeOnUpdate(true);
		config.common().objectClass(Assunto.class).cascadeOnActivate(true);
		config.common().objectClass(Noticia.class).cascadeOnDelete(false);
		config.common().objectClass(Noticia.class).cascadeOnUpdate(true);
		config.common().objectClass(Noticia.class).cascadeOnActivate(true);

		// conexao local
		//-------------- 
		try {
			manager = Db4oEmbedded.openFile(config, "banco.db4o");
			//System.out.println("conectado ao banco " + ipservidor);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Erro ao conectar ao banco local \n" + e.getMessage());
			System.exit(0);
		}
	}

	private static void conectarBancoRemoto() {
		if (manager != null)
			return ; // ja tem uma conexao

		// ---------------------------------------
		// configurar e conectar banco remoto
		// ---------------------------------------
		ClientConfiguration config = Db4oClientServer.newClientConfiguration();
		config.common().messageLevel(0); // 0,1,2,3...
		
		// habilitar cascata na altera��o, remo��o e leitura
		config.common().objectClass(Assunto.class).cascadeOnDelete(false);
		config.common().objectClass(Assunto.class).cascadeOnUpdate(true);
		config.common().objectClass(Assunto.class).cascadeOnActivate(true);
		config.common().objectClass(Noticia.class).cascadeOnUpdate(true);
		config.common().objectClass(Noticia.class).cascadeOnActivate(true);
		config.common().objectClass(Noticia.class).cascadeOnDelete(false);

		// **************************************
		// Conex�o client-server
		// **************************************
		try {
			manager = Db4oClientServer.openClient(config, ipservidor, 34000, "usuario1", "senha1");
			//System.out.println("conectado ao banco " + manager);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					"Erro ao conectar ao banco remoto ip=" + ipservidor + "\n" + e.getMessage());
			System.exit(0);
		}
	}

	public static void desconectar() {
		if (manager != null) {
			manager.close();
			manager = null;
		}
	}
	
	public static ObjectContainer getManager() {
		return manager;
	}

	public static String getIPservidor() {
		return ipservidor;
	}

}// fim da classe Util
