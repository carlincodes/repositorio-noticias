package appswing;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Assunto;
import requisito.Fachada;

public class TelaAssunto {
    private JDialog frame;
    private JTable table;
    private JScrollPane scrollPane;
    private JLabel label;
    private JLabel label_2;

    private JLabel lblNome;
    private JTextField txtNome;

    private JLabel lblNovoNome;
    private JTextField txtNovoNome;

    private JButton btnCriar;
    private JButton btnAtualizar;
    private JButton btnApagar;
    private JButton btnLimpar;

    public TelaAssunto() {
        initialize();
    }

    @SuppressWarnings("serial")
	private void initialize() {

        frame = new JDialog();
        frame.setResizable(false);
        frame.setModal(true);
        frame.setTitle("Assunto");
        frame.setBounds(100, 100, 600, 420);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent arg0) {
                listagem();
            }
        });

        // PAINEL DA TABELA --------------------------------------------
        scrollPane = new JScrollPane();
        scrollPane.setBounds(21, 39, 540, 147);
        frame.getContentPane().add(scrollPane);

        table = new JTable() {
            public boolean isCellEditable(int rowIndex, int vColIndex) {
                return false;
            }
        };
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (table.getSelectedRow() >= 0) {
                        String nome = (String) table.getValueAt(table.getSelectedRow(), 1);
                        Assunto a = Fachada.localizarAssunto(nome);
                        txtNome.setText(a.getNome());
                    }
                } catch (Exception erro) {
                    label.setText(erro.getMessage());
                }
            }
        });

        table.setGridColor(Color.BLACK);
        table.setRequestFocusEnabled(false);
        table.setFocusable(false);
        table.setBackground(Color.WHITE);
        table.setFillsViewportHeight(true);
        table.setRowSelectionAllowed(true);
        table.setFont(new Font("Tahoma", Font.PLAIN, 14));
        scrollPane.setViewportView(table);
        table.setBorder(new LineBorder(new Color(0, 0, 0)));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setShowGrid(true);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // LABEL DE ERRO -----------------------------------------------
        label = new JLabel("");
        label.setForeground(Color.RED);
        label.setBounds(21, 350, 540, 14);
        frame.getContentPane().add(label);

        label_2 = new JLabel("selecione um assunto para editar");
        label_2.setBounds(21, 187, 394, 14);
        frame.getContentPane().add(label_2);

        // CAMPOS -------------------------------------------------------

        lblNome = new JLabel("Nome atual:");
        lblNome.setHorizontalAlignment(SwingConstants.RIGHT);
        lblNome.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblNome.setBounds(21, 216, 80, 14);
        frame.getContentPane().add(lblNome);

        txtNome = new JTextField();
        txtNome.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtNome.setColumns(10);
        txtNome.setBounds(110, 213, 350, 20);
        frame.getContentPane().add(txtNome);

        // CAMPO NOVO NOME ---------------------------------------------

        lblNovoNome = new JLabel("Novo nome:");
        lblNovoNome.setHorizontalAlignment(SwingConstants.RIGHT);
        lblNovoNome.setFont(new Font("Tahoma", Font.PLAIN, 11));
        lblNovoNome.setBounds(21, 246, 80, 14);
        frame.getContentPane().add(lblNovoNome);

        txtNovoNome = new JTextField();
        txtNovoNome.setFont(new Font("Tahoma", Font.PLAIN, 12));
        txtNovoNome.setColumns(10);
        txtNovoNome.setBounds(110, 243, 350, 20);
        frame.getContentPane().add(txtNovoNome);

        // BOTÕES -------------------------------------------------------
        btnCriar = new JButton("Criar");
        btnCriar.setToolTipText("cadastrar novo assunto");
        btnCriar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtNome.getText().isEmpty())
                    label.setText("nome vazio");
                else
                    criarAssunto();
            }
        });
        btnCriar.setBounds(70, 290, 95, 23);
        frame.getContentPane().add(btnCriar);

        btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setToolTipText("atualizar assunto");
        btnAtualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtNome.getText().isEmpty() || txtNovoNome.getText().isEmpty())
                    label.setText("preencha nome atual e novo nome");
                else
                    atualizarAssunto();
            }
        });
        btnAtualizar.setBounds(180, 290, 95, 23);
        frame.getContentPane().add(btnAtualizar);

        btnApagar = new JButton("Apagar");
        btnApagar.setToolTipText("apagar assunto");
        btnApagar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (txtNome.getText().isEmpty())
                    label.setText("nome vazio");
                else
                    apagarAssunto();
            }
        });
        btnApagar.setBounds(290, 290, 95, 23);
        frame.getContentPane().add(btnApagar);

        btnLimpar = new JButton("Limpar");
        btnLimpar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                txtNome.setText("");
                txtNovoNome.setText("");
            }
        });
        btnLimpar.setBounds(400, 290, 95, 23);
        frame.getContentPane().add(btnLimpar);

        frame.setVisible(true);
    }

    // =========================================================
    // LISTAGEM
    // =========================================================
    public void listagem() {
        try {
            DefaultTableModel model = new DefaultTableModel();
            table.setModel(model);

            model.addColumn("Id");
            model.addColumn("Nome");

            List<Assunto> lista = Fachada.listarAssuntos();
            for (Assunto a : lista)
                model.addRow(new Object[]{a.getId(), a.getNome()});

            label_2.setText("resultados: " + lista.size() + " assuntos   - selecione uma linha para editar");
        } catch (Exception erro) {
            label.setText(erro.getMessage());
        }
    }

    // =========================================================
    // CRUD
    // =========================================================

    public void criarAssunto() {
        try {
            label.setText("");
            String nome = txtNome.getText().trim();

            Fachada.criarAssunto(nome);
            label.setText("assunto criado");
            listagem();
        } catch (Exception ex) {
            label.setText(ex.getMessage());
        }
    }

    public void atualizarAssunto() {
        try {
            label.setText("");

            String nome = txtNome.getText().trim();
            String novoNome = txtNovoNome.getText().trim();

            Fachada.alterarAssunto(nome, novoNome);

            label.setText("assunto atualizado");
            listagem();
        } catch (Exception ex) {
            label.setText(ex.getMessage());
        }
    }

    public void apagarAssunto() {
        try {
            label.setText("");
            String nome = txtNome.getText();

            Object[] options = {"Confirmar", "Cancelar"};
            int escolha = JOptionPane.showOptionDialog(null,
                    "Esta operação apagará o assunto " + nome,
                    "Alerta",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[1]);

            if (escolha == 0) {
                Fachada.apagarAssunto(nome);
                label.setText("assunto excluído");
                listagem();
            } else {
                label.setText("exclusão cancelada");
            }

        } catch (Exception erro) {
            label.setText(erro.getMessage());
        }
    }
}
