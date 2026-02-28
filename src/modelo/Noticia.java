package modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "noticia20222370026")
public class Noticia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String titulo;
	private String resumo;
	private LocalDate data;
	private String linkWeb;

	@ManyToMany(
		cascade = { CascadeType.PERSIST, CascadeType.MERGE }
	)
	private Set<Assunto> assuntos = new HashSet<>();

	public Noticia() {}

	public Noticia(String titulo, String resumo, LocalDate data, String linkWeb) {
		this.titulo = titulo;
		this.resumo = resumo;
		this.data = data;
		this.linkWeb = linkWeb;
	}


	public int getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getResumo() {
		return resumo;
	}

	public void setResumo(String resumo) {
		this.resumo = resumo;
	}

	public LocalDate getData() {
		return data;
	}

	public String getDataFormatada() {
		return data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public String getLinkWeb() {
		return linkWeb;
	}

	public void setLinkWeb(String linkWeb) {
		this.linkWeb = linkWeb;
	}

	public Set<Assunto> getAssuntos() {
		return assuntos;
	}


	public void adicionar(Assunto a) throws Exception {
		if (a == null)
			throw new Exception("assunto inválido");

		assuntos.add(a);
		a.getNoticias().add(this);
	}

	public void remover(Assunto a) {
		if (a == null)
			return;

		assuntos.remove(a);
		a.getNoticias().remove(this);
	}

	public Assunto localizar(String nomeAssunto) {
		for (Assunto a : assuntos) {
			if (a.getNome().equalsIgnoreCase(nomeAssunto))
				return a;
		}
		return null;
	}

	@Override
	public String toString() {
		String texto = "Noticia: ";
		texto += "id=" + id +
				 ", titulo=" + titulo +
				 ", data=" + getDataFormatada() +
				 ", link=" + linkWeb +
				 ", assuntos=";

		if (assuntos.isEmpty())
			texto += "sem assuntos";
		else
			for (Assunto a : assuntos)
				texto += a.getNome() + ",";

		return texto;
	}
}
