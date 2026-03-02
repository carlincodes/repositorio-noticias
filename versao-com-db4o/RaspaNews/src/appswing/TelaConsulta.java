package appswing;

import java.awt.Color;
import java.awt.Font;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Noticia;
import modelo.Assunto;
import requisito.Fachada;

public class TelaConsulta {
	private JDialog frame;
	private JTable table;
	private JScrollPane scrollPane;
	private JButton button;
	private JLabel label;
	private JLabel label_4;

	private JComboBox<String> comboBox;

	public TelaConsulta() {
		initialize();
		frame.setVisible(true);
	}

	@SuppressWarnings("serial")
	private void initialize() {
		frame = new JDialog();
        frame.setModal(true);

		frame.setResizable(false);
		frame.setTitle("Consultas");
		frame.setBounds(100, 100, 729, 385);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 43, 674, 148);
		frame.getContentPane().add(scrollPane);

		table = new JTable() {
			public boolean isCellEditable(int rowIndex, int vColIndex) {
				return false;
			}
		};

		table.setGridColor(Color.BLACK);
		table.setRequestFocusEnabled(false);
		table.setFocusable(false);
		table.setBackground(Color.LIGHT_GRAY);
		table.setFillsViewportHeight(true);
		table.setRowSelectionAllowed(true);
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		scrollPane.setViewportView(table);
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setShowGrid(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		label = new JLabel(""); 
		label.setForeground(Color.BLUE);
		label.setBounds(21, 321, 688, 14);
		frame.getContentPane().add(label);

		label_4 = new JLabel("resultados:");
        label_4.setBounds(21, 190, 431, 14);
        frame.getContentPane().add(label_4);

		button = new JButton("Consultar");
		button.setFont(new Font("Tahoma", Font.PLAIN, 12));
		button.addActionListener(e -> {
			int index = comboBox.getSelectedIndex();

			switch(index) {
			case 0:
				consultaPorData();
				break;
			case 1:
				consultaPorAssunto();
				break;
			case 2:
				consultaAssuntosMaisNoticias();
				break;
			default:
				label_4.setText("consulta não selecionada");
			}

		});
		button.setBounds(606, 10, 89, 23);
		frame.getContentPane().add(button);

		comboBox = new JComboBox<String>();
		comboBox.setToolTipText("selecione a consulta");
		comboBox.setModel(new DefaultComboBoxModel<>(
				new String[] {
						"notícias da data fixa",
						"notícias de um assunto fixo",
						"assuntos com mais de N notícias"
				}));
		comboBox.setBounds(21, 10, 513, 22);
		frame.getContentPane().add(comboBox);
	}


	private void consultaPorData() {
		String data = "29/10/2025";
		label_4.setText("Notícias da data: " + data);

        List<Noticia> todas = Fachada.listarNoticias();

		DefaultTableModel model = new DefaultTableModel();
		table.setModel(model);

		model.addColumn("Id");
		model.addColumn("Título");
		model.addColumn("Data");
		model.addColumn("Texto");

		for (Noticia n : todas) {
			if (n.getData().equals(data)) {
				model.addRow(new Object[] { n.getId(), n.getTitulo(), n.getData(), n.getResumo() });
			}
		}
	}


	private void consultaPorAssunto() {
		String nomeAssunto = "Economia";
		label_4.setText("Notícias do assunto: " + nomeAssunto);

		DefaultTableModel model = new DefaultTableModel();
		table.setModel(model);

		model.addColumn("Id");
		model.addColumn("Título");

		try {
			Assunto assunto = Fachada.localizarAssunto(nomeAssunto);

			for (Noticia n : assunto.getNoticias()) {
				model.addRow(new Object[] { n.getId(), n.getTitulo() });
			}

		} catch (Exception e) {
			label.setText(e.getMessage());
		}
	}


	private void consultaAssuntosMaisNoticias() {
		int nNoticias = 1;
		label_4.setText("Assuntos com mais de " + nNoticias + " notícias");

		List<Assunto> lista = Fachada.listarAssuntos();

		DefaultTableModel model = new DefaultTableModel();
		table.setModel(model);

		model.addColumn("Assunto");
		model.addColumn("Qtd Notícias");

		for (Assunto a : lista) {
			if (a.getNoticias().size() > nNoticias) {
				model.addRow(new Object[] { a.getNome(), a.getNoticias().size() });
			}
		}
	}
}
