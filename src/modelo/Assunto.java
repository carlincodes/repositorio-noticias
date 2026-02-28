package modelo;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "assunto20222370026")
public class Assunto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String nome;

	@ManyToMany(mappedBy = "assuntos")
	private Set<Noticia> noticias = new HashSet<>();

	public Assunto() {}

	public Assunto(String nome) {
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Set<Noticia> getNoticias() {
		return noticias;
	}


	public void adicionar(Noticia n) throws Exception {
		if (n == null)
			throw new Exception("notícia inválida");

		noticias.add(n);
		n.getAssuntos().add(this);
	}

	public void remover(Noticia n) {
		if (n == null)
			return;

		noticias.remove(n);
		n.getAssuntos().remove(this);
	}

	@Override
	public String toString() {
		String texto = "Assunto: ";
		texto += "id=" + id + ", nome=" + nome + ", notícias=";

		if (noticias.isEmpty())
			texto += "sem notícias";
		else
			for (Noticia n : noticias)
				texto += n.getTitulo() + ",";

		return texto;
	}
}
