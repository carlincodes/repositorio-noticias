package appswing;

import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import modelo.Noticia;
import requisito.Fachada;

public class TelaConsulta {

    private JDialog frame;
    private JTable table;
    private JScrollPane scrollPane;
    private JButton button;
    private JLabel label;
    private JLabel labelTitulo;
    private JComboBox<String> comboBox;

    public TelaConsulta() {
        initialize();
        frame.setVisible(true);
    }

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

        table = new JTable();
        table.setGridColor(Color.BLACK);
        table.setBackground(Color.LIGHT_GRAY);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        scrollPane.setViewportView(table);

        label = new JLabel("");
        label.setForeground(Color.BLUE);
        label.setBounds(21, 321, 688, 14);
        frame.getContentPane().add(label);

        labelTitulo = new JLabel("resultados:");
        labelTitulo.setBounds(21, 190, 431, 14);
        frame.getContentPane().add(labelTitulo);

        comboBox = new JComboBox<>(new String[]{
                "notícias da data fixa",
                "notícias de um assunto fixo",
                "assuntos com mais de N notícias"
        });
        comboBox.setBounds(21, 10, 513, 22);
        frame.getContentPane().add(comboBox);

        button = new JButton("Consultar");
        button.setBounds(606, 10, 89, 23);
        button.addActionListener(e -> executarConsulta());
        frame.getContentPane().add(button);
    }

    private void executarConsulta() {
        label.setText("");

        try {
            switch (comboBox.getSelectedIndex()) {
                case 0 -> consultarPorData();
                case 1 -> consultarPorAssunto();
                case 2 -> consultarAssuntosMaisNoticias();
            }
        } catch (Exception e) {
            label.setText(e.getMessage());
        }
    }

    private void consultarPorData() {
        String entrada = JOptionPane.showInputDialog(frame, "Informe a data (yyyy-MM-dd):");
        if (entrada == null) return;

        LocalDate data = LocalDate.parse(entrada);
        labelTitulo.setText("Notícias da data: " + data);

        List<Noticia> lista = Fachada.consultarNoticiasPorData(data);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Título");
        model.addColumn("Data");
        model.addColumn("Resumo");

        for (Noticia n : lista) {
            model.addRow(new Object[]{
                    n.getId(),
                    n.getTitulo(),
                    n.getData(),
                    n.getResumo()
            });
        }

        table.setModel(model);
    }

    private void consultarPorAssunto() throws Exception {
        String assunto = JOptionPane.showInputDialog(frame, "Informe o assunto:");
        if (assunto == null) return;

        labelTitulo.setText("Notícias do assunto: " + assunto);

        List<Noticia> lista = Fachada.consultarNoticiasPorAssunto(assunto);

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Id");
        model.addColumn("Título");

        for (Noticia n : lista) {
            model.addRow(new Object[]{n.getId(), n.getTitulo()});
        }

        table.setModel(model);
    }

    private void consultarAssuntosMaisNoticias() throws Exception {
        String entrada = JOptionPane.showInputDialog(frame, "Quantidade mínima de notícias:");
        if (entrada == null) return;

        int n = Integer.parseInt(entrada);
        labelTitulo.setText("Assuntos com mais de " + n + " notícias");

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Assunto");
        model.addColumn("Qtd Notícias");

        for (Object[] linha : Fachada.consultarAssuntosComMaisDeNNoticias(n)) {
            model.addRow(linha);
        }

        table.setModel(model);
    }
}
