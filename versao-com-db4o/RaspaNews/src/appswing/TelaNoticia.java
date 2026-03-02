package appswing;

import java.awt.Color;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import modelo.Noticia;
import modelo.Assunto;
import requisito.Fachada;

public class TelaNoticia {

    private JDialog frame;
    private JTable table;
    private JScrollPane scrollPane;

    private JTextField textTitulo;
    private JTextField textResumo;
    private JTextField textData;
    private JTextField textLink;
    private JTextField textAssuntos;
    private JTextField textNovoAssunto;

    private JLabel label;
    private JLabel labelInfo;

    public TelaNoticia() {
        initialize();
    }

    @SuppressWarnings("serial")
	private void initialize() {

        frame = new JDialog();
        frame.setModal(true);
        frame.setTitle("Notícias");
        frame.setBounds(100, 100, 700, 430);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 40, 640, 160);
        frame.getContentPane().add(scrollPane);

        table = new JTable(){
            public boolean isCellEditable(int r, int c){ return false; }
        };

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    if(table.getSelectedRow() >= 0) {
                        String titulo = (String) table.getValueAt(table.getSelectedRow(), 1);
                        Noticia n = Fachada.localizarNoticia(titulo);

                        textTitulo.setText(n.getTitulo());
                        textResumo.setText(n.getResumo());
                        textData.setText(n.getData());
                        textLink.setText(n.getLinkWeb());

                        String assuntos = "";
                        for(Assunto a : n.getAssuntos())
                            assuntos += a.getNome() + ", ";
                        if(!assuntos.isEmpty())
                            assuntos = assuntos.substring(0, assuntos.length()-2);

                        textAssuntos.setText(assuntos);
                        label.setText("");
                    }
                } catch(Exception ex) {
                    label.setText(ex.getMessage());
                }
            }
        });

        scrollPane.setViewportView(table);

        JButton btnListar = new JButton("Listar");
        btnListar.setBounds(300, 10, 100, 25);
        btnListar.addActionListener(e -> listagem());
        frame.getContentPane().add(btnListar);

        labelInfo = new JLabel("Selecione uma notícia para editar");
        labelInfo.setBounds(20, 200, 400, 15);
        frame.getContentPane().add(labelInfo);

        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setBounds(20, 225, 80, 15);
        frame.getContentPane().add(lblTitulo);

        textTitulo = new JTextField();
        textTitulo.setBounds(100, 222, 250, 20);
        frame.getContentPane().add(textTitulo);

        JLabel lblResumo = new JLabel("Resumo:");
        lblResumo.setBounds(20, 250, 80, 15);
        frame.getContentPane().add(lblResumo);

        textResumo = new JTextField();
        textResumo.setBounds(100, 247, 250, 20);
        frame.getContentPane().add(textResumo);

        JLabel lblData = new JLabel("Data:");
        lblData.setBounds(20, 275, 80, 15);
        frame.getContentPane().add(lblData);

        textData = new JTextField();
        textData.setBounds(100, 272, 120, 20);
        frame.getContentPane().add(textData);

        JLabel lblLink = new JLabel("Link:");
        lblLink.setBounds(20, 300, 80, 15);
        frame.getContentPane().add(lblLink);

        textLink = new JTextField();
        textLink.setBounds(100, 297, 250, 20);
        frame.getContentPane().add(textLink);

        JLabel lblAssuntos = new JLabel("Assuntos:");
        lblAssuntos.setBounds(20, 325, 80, 15);
        frame.getContentPane().add(lblAssuntos);

        textAssuntos = new JTextField();
        textAssuntos.setEditable(false);
        textAssuntos.setBounds(100, 322, 250, 20);
        frame.getContentPane().add(textAssuntos);

        JLabel lblNovoAssunto = new JLabel("Novo assunto:");
        lblNovoAssunto.setBounds(380, 225, 100, 15);
        frame.getContentPane().add(lblNovoAssunto);

        textNovoAssunto = new JTextField();
        textNovoAssunto.setBounds(480, 222, 150, 20);
        frame.getContentPane().add(textNovoAssunto);

        JButton btnCriar = new JButton("Criar");
        btnCriar.setBounds(20, 355, 80, 25);
        btnCriar.addActionListener(e -> criar());
        frame.getContentPane().add(btnCriar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(110, 355, 100, 25);
        btnAtualizar.addActionListener(e -> atualizar());
        frame.getContentPane().add(btnAtualizar);

        JButton btnApagar = new JButton("Apagar");
        btnApagar.setBounds(220, 355, 80, 25);
        btnApagar.addActionListener(e -> apagar());
        frame.getContentPane().add(btnApagar);

        JButton btnLimpar = new JButton("Limpar");
        btnLimpar.setBounds(310, 355, 80, 25);
        btnLimpar.addActionListener(e -> limpar());
        frame.getContentPane().add(btnLimpar);

        JButton btnAddAssunto = new JButton("Adicionar Assunto");
        btnAddAssunto.setBounds(380, 255, 150, 25);
        btnAddAssunto.addActionListener(e -> adicionarAssunto());
        frame.getContentPane().add(btnAddAssunto);

        JButton btnRemAssunto = new JButton("Remover Assunto");
        btnRemAssunto.setBounds(380, 285, 150, 25);
        btnRemAssunto.addActionListener(e -> removerAssunto());
        frame.getContentPane().add(btnRemAssunto);

        label = new JLabel("");
        label.setForeground(Color.RED);
        label.setBounds(20, 385, 650, 15);
        frame.getContentPane().add(label);

        frame.setVisible(true);
    }


    public void listagem() {
        try {
            List<Noticia> lista = Fachada.listarNoticias();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Título");
            model.addColumn("Data");
            model.addColumn("Resumo");

            for(Noticia n : lista)
                model.addRow(new Object[]{ n.getId(), n.getTitulo(), n.getData(), n.getResumo() });

            table.setModel(model);

            labelInfo.setText("Resultados: " + lista.size() + " notícias");

        } catch(Exception e){
            label.setText(e.getMessage());
        }
    }


    private void criar(){
        try{
            Fachada.criarNoticia(
                textTitulo.getText(),
                textResumo.getText(),
                textData.getText(),
                textLink.getText()
            );
            label.setText("Notícia criada");
            listagem();
        }catch(Exception e){
            label.setText(e.getMessage());
        }
    }


    private void atualizar(){
        try{
            Fachada.alterarNoticia(
                textTitulo.getText(),
                textTitulo.getText(),
                textResumo.getText(),
                textData.getText(),
                textLink.getText()
            );
            label.setText("Notícia atualizada");
            listagem();
        }catch(Exception e){
            label.setText(e.getMessage());
        }
    }


    private void apagar(){
        try{
            Fachada.apagarNoticia(textTitulo.getText());
            label.setText("Notícia apagada");
            listagem();
        }catch(Exception e){
            label.setText(e.getMessage());
        }
    }

    private void limpar(){
        textTitulo.setText("");
        textResumo.setText("");
        textData.setText("");
        textLink.setText("");
        textAssuntos.setText("");
        textNovoAssunto.setText("");
    }

    private void adicionarAssunto(){
        try{
            Fachada.adicionarAssuntoNaNoticia(
                textTitulo.getText(),
                textNovoAssunto.getText()
            );
            label.setText("Assunto adicionado");
            listagem();
        }catch(Exception e){
            label.setText(e.getMessage());
        }
    }

    private void removerAssunto(){
        try{
            Fachada.removerAssuntoDaNoticia(
                textTitulo.getText(),
                textNovoAssunto.getText()
            );
            label.setText("Assunto removido");
            listagem();
        }catch(Exception e){
            label.setText(e.getMessage());
        }
    }
}
