/**********************************
 * IFPB - Curso Superior de Tec. em Sist. para Internet
 * POB - Persistencia de Objetos
 * Prof. Fausto Ayres
 *
 */
package util;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class Util {
	private static EntityManager manager;
	private static EntityManagerFactory factory;
	// gravar no arquivo log4j.log
	private static final Logger logger = LogManager.getLogger(Util.class);


	public static EntityManager conectar() {
		if (manager == null) {
			try {
				logger.info("----conectar banco - lendo arquivo de propriedades ");
				Properties dados = new Properties();
				dados.load(Util.class.getResourceAsStream("/util/util.properties")); // dentro de src
				String sgbd = dados.getProperty("sgbd");
				String banco = dados.getProperty("banco");
				String ip = dados.getProperty("ipatual");
				String usuario = dados.getProperty("usuario");
				String senha = dados.getProperty("senha");

				logger.info("----sgbd => " + sgbd + ", banco => " + banco + ", ip => " + ip);

				// substituir algumas propriedades do persistence.xml
				Properties propriedades = new Properties();
				if (sgbd.equals("postgresql")) {
					//logger.info("----configurando postgresql");
					propriedades.setProperty("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");
					propriedades.setProperty("jakarta.persistence.jdbc.url",
							"jdbc:postgresql://" + ip + ":5432/" + banco);
					propriedades.setProperty("jakarta.persistence.jdbc.user", usuario);
					propriedades.setProperty("jakarta.persistence.jdbc.password", senha);
				}
				if (sgbd.equals("mysql")) {
					//logger.info("----configurando mysql");
					propriedades.setProperty("jakarta.persistence.jdbc.driver", "com.mysql.cj.jdbc.Driver");
					propriedades.setProperty("jakarta.persistence.jdbc.url",
							"jdbc:mysql://" + ip + ":3306/" + banco + "?createDatabaseIfNotExist=true");
					propriedades.setProperty("jakarta.persistence.jdbc.user",usuario);
					propriedades.setProperty("jakarta.persistence.jdbc.password", senha);
				}
				// -----------------------------------------------------------------------------------
				String unit_name = "hibernate" + "-" + sgbd;
				factory = Persistence.createEntityManagerFactory(unit_name, propriedades);
				manager = factory.createEntityManager();

			} catch (Exception e) {
				logger.info("problema de configuracao Util.java: " + e.getMessage());
				throw new RuntimeException("nao conectou ao bd");
			}
		}
		return manager;
	}

	public static void desconectar() {
		logger.info("----desconectar banco");
		if (manager != null && manager.isOpen()) {
			manager.close();
			factory.close();
			manager = null;
		}
	}
	
	public static EntityManager getManager() {
	    if (manager == null) {
	        conectar();
	    }
	    return manager;
	}


}
