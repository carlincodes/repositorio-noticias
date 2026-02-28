package appswing;

import java.awt.Color;
import java.awt.event.*;
import java.time.LocalDate;
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

    private JList<String> listAssuntos;
    private DefaultListModel<String> listModelAssuntos;

    private JLabel label;
    private JLabel labelInfo;

    public TelaNoticia() {
        initialize();
    }

    private void initialize() {

        frame = new JDialog();
        frame.getContentPane().setBackground(new Color(219, 183, 255));
        frame.setModal(true);
        frame.setTitle("Notícias");
        frame.setBounds(100, 100, 815, 480);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 40, 740, 150);
        frame.getContentPane().add(scrollPane);

        table = new JTable() {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                try {
                    if (table.getSelectedRow() >= 0) {
                        String titulo = (String) table.getValueAt(table.getSelectedRow(), 1);
                        Noticia n = Fachada.localizarNoticia(titulo);

                        textTitulo.setText(n.getTitulo());
                        textResumo.setText(n.getResumo());
                        textData.setText(n.getData().toString());
                        textLink.setText(n.getLinkWeb());

                        textAssuntos.setText(
                            Fachada.listarAssuntosComoTextoUnico(titulo)
                        );

                        label.setText("");
                    }
                } catch (Exception ex) {
                    label.setText(ex.getMessage());
                }
            }
        });

        scrollPane.setViewportView(table);

        JButton btnListar = new JButton("Listar");
        btnListar.setBounds(350, 10, 100, 25);
        btnListar.addActionListener(e -> {
            listagem();
            carregarAssuntos();
        });
        frame.getContentPane().add(btnListar);

        labelInfo = new JLabel("Selecione uma notícia para editar");
        labelInfo.setBounds(20, 193, 243, 15);
        frame.getContentPane().add(labelInfo);

        JLabel lblTitulo = new JLabel("Título:");
        lblTitulo.setBounds(20, 238, 80, 15);
        frame.getContentPane().add(lblTitulo);

        textTitulo = new JTextField();
        textTitulo.setBounds(100, 235, 300, 20);
        frame.getContentPane().add(textTitulo);

        JLabel lblResumo = new JLabel("Resumo:");
        lblResumo.setBounds(20, 266, 80, 15);
        frame.getContentPane().add(lblResumo);

        textResumo = new JTextField();
        textResumo.setBounds(100, 263, 300, 20);
        frame.getContentPane().add(textResumo);

        JLabel lblData = new JLabel("Data:");
        lblData.setBounds(20, 292, 80, 15);
        frame.getContentPane().add(lblData);

        textData = new JTextField();
        textData.setBounds(100, 289, 120, 20);
        frame.getContentPane().add(textData);

        JLabel lblLink = new JLabel("Link:");
        lblLink.setBounds(20, 320, 80, 15);
        frame.getContentPane().add(lblLink);

        textLink = new JTextField();
        textLink.setBounds(100, 317, 300, 20);
        frame.getContentPane().add(textLink);

        JLabel lblAssuntos = new JLabel("Assuntos associados:");
        lblAssuntos.setBounds(10, 348, 132, 15);
        frame.getContentPane().add(lblAssuntos);

        textAssuntos = new JTextField();
        textAssuntos.setEditable(false);
        textAssuntos.setBounds(148, 345, 252, 20);
        frame.getContentPane().add(textAssuntos);

        JLabel lblTodosAssuntos = new JLabel("Todos os assuntos:");
        lblTodosAssuntos.setBounds(430, 219, 174, 15);
        frame.getContentPane().add(lblTodosAssuntos);

        listModelAssuntos = new DefaultListModel<>();
        listAssuntos = new JList<>(listModelAssuntos);

        JScrollPane scrollAssuntos = new JScrollPane(listAssuntos);
        scrollAssuntos.setBounds(430, 245, 200, 120);
        frame.getContentPane().add(scrollAssuntos);

        JButton btnCriar = new JButton("Criar");
        btnCriar.setBounds(62, 376, 80, 25);
        btnCriar.addActionListener(e -> criar());
        frame.getContentPane().add(btnCriar);

        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBounds(151, 376, 100, 25);
        btnAtualizar.addActionListener(e -> atualizar());
        frame.getContentPane().add(btnAtualizar);

        JButton btnApagar = new JButton("Apagar");
        btnApagar.setBounds(264, 376, 80, 25);
        btnApagar.addActionListener(e -> apagar());
        frame.getContentPane().add(btnApagar);

        JButton btnAddAssunto = new JButton("Associar");
        btnAddAssunto.setBounds(417, 376, 100, 25);
        btnAddAssunto.addActionListener(e -> associarAssunto());
        frame.getContentPane().add(btnAddAssunto);

        JButton btnRemAssunto = new JButton("Desassociar");
        btnRemAssunto.setBounds(524, 376, 120, 25);
        btnRemAssunto.addActionListener(e -> removerAssunto());
        frame.getContentPane().add(btnRemAssunto);

        JButton btnCriarAssunto = new JButton("Criar Assunto");
        btnCriarAssunto.setBounds(649, 270, 140, 25);
        btnCriarAssunto.addActionListener(e -> criarAssunto());
        frame.getContentPane().add(btnCriarAssunto);

        JButton btnModificarAssunto = new JButton("Modificar Assunto");
        btnModificarAssunto.setBounds(649, 295, 140, 25);
        btnModificarAssunto.addActionListener(e -> modificarAssunto());
        frame.getContentPane().add(btnModificarAssunto);

        JButton btnExcluirAssunto = new JButton("Excluir Assunto");
        btnExcluirAssunto.setBounds(649, 320, 140, 25);
        btnExcluirAssunto.addActionListener(e -> excluirAssunto());
        frame.getContentPane().add(btnExcluirAssunto);

        label = new JLabel("");
        label.setForeground(Color.RED);
        label.setBounds(20, 412, 740, 15);
        frame.getContentPane().add(label);

        frame.setVisible(true);
    }


    private void carregarAssuntos() {
        try {
            listModelAssuntos.clear();
            for (Assunto a : Fachada.listarAssuntos())
                listModelAssuntos.addElement(a.getNome());
        } catch (Exception e) {
            label.setText(e.getMessage());
        }
    }

    private void listagem() {
        try {
            List<Noticia> lista = Fachada.listarNoticias();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("ID");
            model.addColumn("Título");
            model.addColumn("Data");
            model.addColumn("Resumo");

            for (Noticia n : lista)
                model.addRow(new Object[]{
                    n.getId(), n.getTitulo(), n.getData(), n.getResumo()
                });

            table.setModel(model);
            labelInfo.setText("Resultados: " + lista.size() + " notícias");

        } catch (Exception e) {
            label.setText(e.getMessage());
        }
    }

    
    
    private void criarAssunto() {
        try {
            String nome = JOptionPane.showInputDialog(frame, "Nome do assunto:");
            if (nome == null || nome.isBlank())
                return;

            Fachada.criarAssunto(nome);
            carregarAssuntos();
            label.setText("Assunto criado");

        } catch (Exception e) {
            label.setText(e.getMessage());
        }
    }

    private void modificarAssunto() {
        try {
            String nomeAtual = listAssuntos.getSelectedValue();
            if (nomeAtual == null)
                throw new Exception("Selecione um assunto");

            JTextField campo = new JTextField(nomeAtual);

            Object[] msg = {
                "Novo nome do assunto:", campo
            };

            int opcao = JOptionPane.showConfirmDialog(
                frame, msg, "Modificar Assunto",
                JOptionPane.OK_CANCEL_OPTION
            );

            if (opcao != JOptionPane.OK_OPTION)
                return;

            String novoNome = campo.getText();
            if (novoNome.isBlank())
                throw new Exception("Nome inválido");

            Fachada.alterarAssunto(nomeAtual, novoNome);
            carregarAssuntos();
            label.setText("Assunto modificado");

        } catch (Exception e) {
            label.setText(e.getMessage());
        }
    }

    private void excluirAssunto() {
        try {
            String nome = listAssuntos.getSelectedValue();
            if (nome == null)
                throw new Exception("Selecione um assunto");

            Fachada.apagarAssunto(nome);
            carregarAssuntos();
            label.setText("Assunto excluído");

        } catch (Exception e) {
            label.setText(e.getMessage());
        }
    }

    private void associarAssunto() {
        try {
            Fachada.adicionarAssuntoNaNoticia(
                textTitulo.getText(),
                listAssuntos.getSelectedValue()
            );

            textAssuntos.setText(
                Fachada.listarAssuntosComoTextoUnico(textTitulo.getText())
            );

            label.setText("Assunto associado");

        } catch (Exception e) {
            label.setText(e.getMessage());
        }
    }

    private void removerAssunto() {
        try {
            Fachada.removerAssuntoDaNoticia(
                textTitulo.getText(),
                listAssuntos.getSelectedValue()
            );

            textAssuntos.setText(
                Fachada.listarAssuntosComoTextoUnico(textTitulo.getText())
            );

            label.setText("Assunto removido");

        } catch (Exception e) {
            label.setText(e.getMessage());
        }
    }

    private void criar() {
        try {
            JTextField campoTitulo = new JTextField();
            JTextField campoResumo = new JTextField();
            JTextField campoData = new JTextField();
            JTextField campoLink = new JTextField();

            JComboBox<String> comboAssuntos = new JComboBox<>();
            for (Assunto a : Fachada.listarAssuntos()) {
                comboAssuntos.addItem(a.getNome());
            }

            Object[] msg = {
                "Título:", campoTitulo,
                "Resumo:", campoResumo,
                "Data (AAAA-MM-DD):", campoData,
                "Link:", campoLink,
                "Assunto:", comboAssuntos
            };

            int opcao = JOptionPane.showConfirmDialog(
                frame,
                msg,
                "Criar Nova Notícia",
                JOptionPane.OK_CANCEL_OPTION
            );

            if (opcao != JOptionPane.OK_OPTION)
                return;

            String titulo = campoTitulo.getText().trim();
            String resumo = campoResumo.getText().trim();
            String dataTexto = campoData.getText().trim();
            String link = campoLink.getText().trim();
            String assuntoSelecionado = (String) comboAssuntos.getSelectedItem();

            if (titulo.isBlank() || resumo.isBlank() || dataTexto.isBlank())
                throw new Exception("Título, resumo e data são obrigatórios");

            LocalDate data = LocalDate.parse(dataTexto);

            Fachada.criarNoticia(titulo, resumo, data, link);

            if (assuntoSelecionado != null) {
                Fachada.adicionarAssuntoNaNoticia(titulo, assuntoSelecionado);
            }

            listagem();
            label.setText("Notícia criada com sucesso");

        } catch (Exception e) {
            label.setText(e.getMessage());
        }
    }


    private void atualizar() {
        try {
            int linha = table.getSelectedRow();
            if (linha < 0)
                throw new Exception("Selecione uma notícia");

            String tituloAtual = (String) table.getValueAt(linha, 1);
            LocalDate data = LocalDate.parse(textData.getText().trim());

            Fachada.alterarNoticia(
                    tituloAtual,
                    textTitulo.getText().trim(),
                    textResumo.getText().trim(),
                    data,
                    textLink.getText().trim()
            );
            listagem();
            label.setText("Notícia atualizada com sucesso");
        } catch (Exception e) {
            label.setText(e.getMessage());
        }
    }

    private void apagar() {
        try {
            int linha = table.getSelectedRow();
            if (linha < 0)
                throw new Exception("Selecione uma notícia");

            String titulo = (String) table.getValueAt(linha, 1);
            Fachada.apagarNoticia(titulo);
            listagem();
            label.setText("Notícia apagada com sucesso");
        } catch (Exception e) {
            label.setText(e.getMessage());
        }
    }
}
