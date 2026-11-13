package GUI;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import modelo.Bilhete;
import modelo.Veiculo;
import modelo.Vaga;
import modelo.Carro;
import modelo.Mota;
import modelo.Camiao;
import dao.BilheteDAO;
import dao.VagaDAO;
import controle.EntradaControle;
import controle.SaidaControle;
import controle.VagaControle;
import controle.HistoricoControle;
import controle.VeiculoControle;

public class TelaPrincipal extends JFrame {

    private JTable tabelaAtivos;
    private DefaultTableModel modeloAtivos;
    private JTextField txtPesquisaAtivos;

    // Componentes da Tabela de Histórico
    private JTable tabelaHistorico;
    private DefaultTableModel modeloHistorico;
    private JTextField txtPesquisaHistorico;
    private JTextField txtFiltroData;
    private JComboBox<String> cbFiltroTipo;
    private JComboBox<String> cbFiltroEstado;
    
    // Componentes do Mapa de Vagas
    private JPanel painelGradeVagas;
    private JLabel lblResumoVagas;
    
    // Cartões do Dashboard
    private JLabel lblDashVeiculos;
    private JLabel lblDashVagas;
    private JLabel lblDashFaturamento;
    
    private CardLayout cardLayout;
    private JPanel painelCartoes;

    private DateTimeFormatter fmtDataTela = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private DateTimeFormatter fmtDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private BilheteDAO bilheteDAO = new BilheteDAO();
    private VagaDAO vagaDAO = new VagaDAO();
    private VagaControle vagaControle = new VagaControle(vagaDAO);
    private EntradaControle entradaControle = new EntradaControle(bilheteDAO, vagaControle);
    private SaidaControle saidaControle = new SaidaControle(bilheteDAO, vagaControle);
    private HistoricoControle historicoControle = new HistoricoControle(bilheteDAO);
    private VeiculoControle veiculoControle = new VeiculoControle(bilheteDAO);

    public TelaPrincipal() {
        setTitle("Sistema de Gestão de Estacionamento");
        setSize(1350, 800); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        //cabecalho
        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(new Color(0, 0, 128));
        painelTopo.setLayout(new BoxLayout(painelTopo, BoxLayout.Y_AXIS));
        painelTopo.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("Sistema de Gestão de Estacionamento");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel lblSubtitulo = new JLabel("Controle e organização de veículos e vagas");
        lblSubtitulo.setForeground(Color.WHITE);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        painelTopo.add(lblTitulo);
        painelTopo.add(Box.createRigidArea(new Dimension(0, 5)));
        painelTopo.add(lblSubtitulo);
        add(painelTopo, BorderLayout.NORTH);

        //Menu lateral
        JPanel painelMenu = new JPanel();
        painelMenu.setBackground(new Color(0, 0, 128));
        painelMenu.setLayout(new GridLayout(5, 1, 0, 10)); 
        painelMenu.setPreferredSize(new Dimension(200, 0));
        painelMenu.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JButton btnInicio = criarBotaoMenu("Inicio");
        JButton btnVeiculos = criarBotaoMenu("Veículos no Parque");
        JButton btnHistorico = criarBotaoMenu("Histórico Completo");
        JButton btnSair = criarBotaoMenu("Sair");

        painelMenu.add(btnInicio);
        painelMenu.add(btnVeiculos);
        painelMenu.add(btnHistorico);
        painelMenu.add(btnSair);
        add(painelMenu, BorderLayout.WEST);

        // Area central
        cardLayout = new CardLayout();
        painelCartoes = new JPanel(cardLayout);

        //veiculos no parque
        JPanel painelAtivos = new JPanel(new BorderLayout());
        painelAtivos.setBackground(new Color(245, 245, 245));
        painelAtivos.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTituloAtivos = new JLabel("Veículos no Parque");
        lblTituloAtivos.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloAtivos.setForeground(new Color(50, 50, 50));
        lblTituloAtivos.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        painelAtivos.add(lblTituloAtivos, BorderLayout.NORTH);

        JPanel painelCentroAtivos = new JPanel(new BorderLayout());
        painelCentroAtivos.setOpaque(false);

        // Barra de Pesquisa Simples
        JPanel painelPesquisaAtivos = new JPanel(new BorderLayout(10, 0));
        painelPesquisaAtivos.setOpaque(false);
        painelPesquisaAtivos.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        txtPesquisaAtivos = new JTextField();
        txtPesquisaAtivos.setPreferredSize(new Dimension(0, 35));
        txtPesquisaAtivos.putClientProperty("JTextField.placeholderText", "Pesquisar por matrícula, marca ou modelo...");
        txtPesquisaAtivos.putClientProperty("JTextField.showClearButton", true);

        JButton btnPesquisarAtivos = new JButton("Pesquisar");
        btnPesquisarAtivos.setBackground(new Color(0, 0, 128));
        btnPesquisarAtivos.setForeground(Color.WHITE);
        btnPesquisarAtivos.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPesquisarAtivos.setPreferredSize(new Dimension(120, 35));

        painelPesquisaAtivos.add(txtPesquisaAtivos, BorderLayout.CENTER);
        painelPesquisaAtivos.add(btnPesquisarAtivos, BorderLayout.EAST);
        painelCentroAtivos.add(painelPesquisaAtivos, BorderLayout.NORTH);

        // Tabela de Ativos
        modeloAtivos = new DefaultTableModel(new String[]{"ID", "Matrícula", "Tipo", "Marca", "Modelo", "Nº Vaga", "Hora Entrada"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaAtivos = new JTable(modeloAtivos);
        configurarTabela(tabelaAtivos);
        painelCentroAtivos.add(new JScrollPane(tabelaAtivos), BorderLayout.CENTER);
        painelAtivos.add(painelCentroAtivos, BorderLayout.CENTER);

        // Botões de Ação (Ativos)
        JPanel painelBotoesAtivos = criarPainelBotoesAcao();
        painelAtivos.add(painelBotoesAtivos, BorderLayout.SOUTH);

        // Historico
        JPanel painelHistorico = new JPanel(new BorderLayout());
        painelHistorico.setBackground(new Color(245, 245, 245));
        painelHistorico.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel painelTopoHistorico = new JPanel(new BorderLayout());
        painelTopoHistorico.setOpaque(false);
        painelTopoHistorico.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Dashboard 
        JPanel painelStats = new JPanel(new GridLayout(1, 3, 20, 0));
        painelStats.setOpaque(false);
        painelStats.setPreferredSize(new Dimension(0, 100)); 

        JPanel cardVeiculos = new JPanel(new BorderLayout());
        cardVeiculos.setBackground(Color.WHITE);
        cardVeiculos.putClientProperty("FlatLaf.style", "arc: 15");
        cardVeiculos.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        lblDashVeiculos = new JLabel("0");
        lblDashVeiculos.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblDashVeiculos.setForeground(new Color(0, 123, 255));
        cardVeiculos.add(new JLabel("Veículos no Parque"), BorderLayout.NORTH);
        cardVeiculos.add(lblDashVeiculos, BorderLayout.CENTER);

        JPanel cardVagas = new JPanel(new BorderLayout());
        cardVagas.setBackground(Color.WHITE);
        cardVagas.putClientProperty("FlatLaf.style", "arc: 15");
        cardVagas.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        lblDashVagas = new JLabel("0");
        lblDashVagas.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblDashVagas.setForeground(new Color(40, 167, 69));
        cardVagas.add(new JLabel("Vagas Livres"), BorderLayout.NORTH);
        cardVagas.add(lblDashVagas, BorderLayout.CENTER);

        JPanel cardFaturamento = new JPanel(new BorderLayout());
        cardFaturamento.setBackground(Color.WHITE);
        cardFaturamento.putClientProperty("FlatLaf.style", "arc: 15");
        cardFaturamento.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        lblDashFaturamento = new JLabel("0.00 MT");
        lblDashFaturamento.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblDashFaturamento.setForeground(new Color(0, 0, 128));
        cardFaturamento.add(new JLabel("Faturamentos"), BorderLayout.NORTH);
        cardFaturamento.add(lblDashFaturamento, BorderLayout.CENTER);

        painelStats.add(cardVeiculos);
        painelStats.add(cardVagas);
        painelStats.add(cardFaturamento);

        JLabel lblTituloHistorico = new JLabel("Histórico Completo");
        lblTituloHistorico.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloHistorico.setForeground(new Color(50, 50, 50));
        lblTituloHistorico.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));

        JPanel painelTopoCompleto = new JPanel(new BorderLayout());
        painelTopoCompleto.setOpaque(false);
        painelTopoCompleto.add(painelStats, BorderLayout.CENTER);
        painelTopoCompleto.add(lblTituloHistorico, BorderLayout.SOUTH);

        painelHistorico.add(painelTopoCompleto, BorderLayout.NORTH);

        // Centro do Histórico
        JPanel painelCentroHistorico = new JPanel(new BorderLayout());
        painelCentroHistorico.setOpaque(false);

        JPanel painelFiltros = new JPanel(new BorderLayout(10, 0)); 
        painelFiltros.setOpaque(false);
        painelFiltros.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JPanel painelInputs = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        painelInputs.setOpaque(false);

        txtPesquisaHistorico = new JTextField();
        txtPesquisaHistorico.setPreferredSize(new Dimension(180, 35));
        txtPesquisaHistorico.putClientProperty("JTextField.placeholderText", "Matrícula, marca...");
        txtPesquisaHistorico.putClientProperty("JTextField.showClearButton", true);

        txtFiltroData = new JTextField();
        txtFiltroData.setPreferredSize(new Dimension(110, 35));
        txtFiltroData.putClientProperty("JTextField.placeholderText", "dd/mm/aaaa");
        txtFiltroData.putClientProperty("JTextField.showClearButton", true);

        cbFiltroTipo = new JComboBox<>(new String[]{"Todos", "Carro", "Mota", "Camião"});
        cbFiltroTipo.setPreferredSize(new Dimension(100, 35));
        
        cbFiltroEstado = new JComboBox<>(new String[]{"Todos", "No Parque", "Finalizados"});
        cbFiltroEstado.setPreferredSize(new Dimension(120, 35));

        JButton btnPesquisarHistorico = new JButton("Pesquisar");
        btnPesquisarHistorico.setBackground(new Color(0, 0, 128));
        btnPesquisarHistorico.setForeground(Color.WHITE);
        btnPesquisarHistorico.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPesquisarHistorico.setPreferredSize(new Dimension(120, 35));

        painelInputs.add(txtPesquisaHistorico);
        painelInputs.add(txtFiltroData);
        painelInputs.add(cbFiltroTipo);
        painelInputs.add(cbFiltroEstado);

        painelFiltros.add(painelInputs, BorderLayout.CENTER);
        painelFiltros.add(btnPesquisarHistorico, BorderLayout.EAST);
        painelCentroHistorico.add(painelFiltros, BorderLayout.NORTH);

        // Tabela de Histórico
        modeloHistorico = new DefaultTableModel(new String[]{"ID", "Matrícula", "Tipo", "Marca", "Modelo", "Nº Vaga", "Hora Entrada", "Hora Saída", "Valor Pago"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaHistorico = new JTable(modeloHistorico);
        configurarTabela(tabelaHistorico);
        painelCentroHistorico.add(new JScrollPane(tabelaHistorico), BorderLayout.CENTER);
        painelHistorico.add(painelCentroHistorico, BorderLayout.CENTER);

        // Botões de Ação 
        JPanel painelBotoesHistorico = criarPainelBotoesAcao();
        painelHistorico.add(painelBotoesHistorico, BorderLayout.SOUTH);

        //mapa de vagas
        JPanel painelVagas = new JPanel(new BorderLayout());
        painelVagas.setBackground(new Color(245, 245, 245));
        painelVagas.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel painelTopoVagas = new JPanel(new BorderLayout());
        painelTopoVagas.setOpaque(false);
        
        JLabel lblTituloVagas = new JLabel("Mapa de Vagas do Estacionamento");
        lblTituloVagas.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTituloVagas.setForeground(new Color(50, 50, 50));
        
        lblResumoVagas = new JLabel(""); 
        lblResumoVagas.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblResumoVagas.setHorizontalAlignment(SwingConstants.RIGHT);

        painelTopoVagas.add(lblTituloVagas, BorderLayout.WEST);
        painelTopoVagas.add(lblResumoVagas, BorderLayout.EAST);
        painelTopoVagas.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        painelVagas.add(painelTopoVagas, BorderLayout.NORTH);

        painelGradeVagas = new JPanel(new GridLayout(0, 5, 15, 15));
        painelGradeVagas.setOpaque(false);
        
        JScrollPane scrollVagas = new JScrollPane(painelGradeVagas);
        scrollVagas.setBorder(null);
        scrollVagas.setOpaque(false);
        scrollVagas.getViewport().setOpaque(false);
        scrollVagas.getVerticalScrollBar().setUnitIncrement(16);
        painelVagas.add(scrollVagas, BorderLayout.CENTER);

        // Adiciona os cartões ao painel principal
        painelCartoes.add(painelAtivos, "TELA_ATIVOS");
        painelCartoes.add(painelHistorico, "TELA_HISTORICO");
        painelCartoes.add(painelVagas, "TELA_VAGAS");
        add(painelCartoes, BorderLayout.CENTER);

        // eventos
        btnInicio.addActionListener(e -> {
            cardLayout.show(painelCartoes, "TELA_VAGAS");
            atualizarMapaVagas();
        });

        //Veículos no Parque
        btnVeiculos.addActionListener(e -> {
            cardLayout.show(painelCartoes, "TELA_ATIVOS");
            txtPesquisaAtivos.setText("");
            carregarTabelaAtivos("");
        });

        //Histórico
        btnHistorico.addActionListener(e -> {
            cardLayout.show(painelCartoes, "TELA_HISTORICO");
            txtPesquisaHistorico.setText("");
            txtFiltroData.setText("");
            cbFiltroTipo.setSelectedItem("Todos");
            cbFiltroEstado.setSelectedItem("Todos");
            carregarTabelaHistorico();
        });

        btnSair.addActionListener(e -> System.exit(0));

        btnPesquisarAtivos.addActionListener(e -> carregarTabelaAtivos(txtPesquisaAtivos.getText().trim()));
        btnPesquisarHistorico.addActionListener(e -> carregarTabelaHistorico());

        // INICIA O SISTEMA NO MAPA DE VAGAS
        btnInicio.doClick();
    }

    private JPanel criarPainelBotoesAcao() {
        JPanel painel = new JPanel(new GridLayout(1, 4, 10, 0));
        painel.setOpaque(false);
        painel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        JButton btnCadastrar = criarBotaoAcao("Cadastrar", new Color(40, 167, 69)); 
        JButton btnAtualizar = criarBotaoAcao("Atualizar", new Color(0, 123, 255)); 
        JButton btnEliminar = criarBotaoAcao("Registar Saída", new Color(220, 53, 69)); 
        JButton btnDetalhes = criarBotaoAcao("Ver Bilhete", new Color(108, 117, 125)); 

        btnCadastrar.addActionListener(e -> abrirDialogoCadastro());
        btnAtualizar.addActionListener(e -> abrirDialogoAtualizar());
        btnEliminar.addActionListener(e -> registarSaida());
        btnDetalhes.addActionListener(e -> mostrarDetalhes());

        painel.add(btnCadastrar);
        painel.add(btnAtualizar);
        painel.add(btnEliminar);
        painel.add(btnDetalhes);
        return painel;
    }

    private void configurarTabela(JTable tabela) {
        tabela.setRowHeight(28); 
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabela.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!isSelected) {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                }
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(200, 200, 200)));
                return this;
            }
        };
        
        for (int i = 0; i < tabela.getColumnCount(); i++) {
            tabela.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    // Retorna a tabela que está atualmente visível
    private JTable getTabelaAtual() {
        return tabelaAtivos.isShowing() ? tabelaAtivos : tabelaHistorico;
    }

    private void carregarTabelaAtivos(String texto) {
        try {
            modeloAtivos.setRowCount(0);
            List<Bilhete> lista = historicoControle.filtrar(texto, "Todos", "No Parque", null);
            for (Bilhete b : lista) {
                modeloAtivos.addRow(new Object[]{
                    b.getId(), b.getVeiculo().getMatricula(), b.getVeiculo().getTipo().getDescricao(),
                    b.getVeiculo().getMarca(), b.getVeiculo().getModelo(), b.getVaga().getNumero(),
                    b.getHoraEntrada().format(fmtDataHora)
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    private void carregarTabelaHistorico() {
        try {
            String texto = txtPesquisaHistorico.getText().trim();
            String tipo = cbFiltroTipo.getSelectedItem().toString();
            String estado = cbFiltroEstado.getSelectedItem().toString();
            
            LocalDate data = null;
            String dataStr = txtFiltroData.getText().trim();
            if (!dataStr.isEmpty()) {
                data = LocalDate.parse(dataStr, fmtDataTela);
            }

            modeloHistorico.setRowCount(0);
            List<Bilhete> lista = historicoControle.filtrar(texto, tipo, estado, data);
            for (Bilhete b : lista) {
                modeloHistorico.addRow(new Object[]{
                    b.getId(), b.getVeiculo().getMatricula(), b.getVeiculo().getTipo().getDescricao(),
                    b.getVeiculo().getMarca(), b.getVeiculo().getModelo(), b.getVaga().getNumero(),
                    b.getHoraEntrada().format(fmtDataHora),
                    b.getHoraSaida() != null ? b.getHoraSaida().format(fmtDataHora) : "No Parque",
                    b.getValorPago() != null ? String.format("%.2f MT", b.getValorPago()) : "-"
                });
            }
            atualizarEstatisticas(tipo, data);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro na pesquisa. Verifique a data (dd/mm/aaaa).");
        }
    }

    private void atualizarEstatisticas(String tipo, LocalDate data) {
        try {
            int ativos = historicoControle.filtrar("", tipo, "No Parque", data).size();
            lblDashVeiculos.setText(String.valueOf(ativos));

            int vagasLivres = 0;
            for (Vaga v : vagaControle.listarTodas()) {
                if (!v.isOcupada() && (tipo.equals("Todos") || v.getTipo().getDescricao().equals(tipo))) {
                    vagasLivres++;
                }
            }
            lblDashVagas.setText(String.valueOf(vagasLivres));

            double faturamento = 0;
            for (Bilhete b : historicoControle.filtrar("", tipo, "Finalizados", data)) {
                if (b.getValorPago() != null) faturamento += b.getValorPago();
            }
            lblDashFaturamento.setText(String.format("%.2f MT", faturamento));
            lblDashFaturamento.setForeground(faturamento > 0 ? new Color(0, 0, 128) : new Color(150, 150, 150));
        } catch (Exception ex) {}
    }

    private void atualizarMapaVagas() {
        painelGradeVagas.removeAll(); 
        try {
            List<Vaga> vagas = vagaControle.listarTodas();
            int livres = 0, ocupadas = 0;

            for (Vaga v : vagas) {
                JPanel cartaoVaga = new JPanel(new BorderLayout());
                cartaoVaga.putClientProperty("FlatLaf.style", "arc: 15"); 
                
                if (v.isOcupada()) {
                    cartaoVaga.setBackground(new Color(220, 53, 69)); 
                    cartaoVaga.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
                    ocupadas++;
                } else {
                    cartaoVaga.setBackground(new Color(40, 167, 69)); 
                    livres++;
                }

                String texto = "<html><center>Vaga " + v.getNumero() + "<br>"
                             + v.getTipo().getDescricao() + "<br>"
                             + (v.isOcupada() ? "OCUPADA" : "LIVRE") + "</center></html>";
                
                JLabel lblVaga = new JLabel(texto);
                lblVaga.setHorizontalAlignment(SwingConstants.CENTER);
                lblVaga.setForeground(Color.WHITE);
                lblVaga.setFont(new Font("Segoe UI", Font.BOLD, 14));
                cartaoVaga.add(lblVaga, BorderLayout.CENTER);

                cartaoVaga.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (v.isOcupada()) {
                            try {
                                Bilhete b = historicoControle.procurarAtivoPorVaga(v.getNumero());
                                if (b != null) mostrarDetalhesBilhete(b); 
                            } catch (Exception ex) {}
                        } else {
                            JOptionPane.showMessageDialog(TelaPrincipal.this, "A vaga " + v.getNumero() + " está livre.");
                        }
                    }
                });
                painelGradeVagas.add(cartaoVaga);
            }
            lblResumoVagas.setText("<html>Total: <b>" + vagas.size() + "</b> | <font color='#28a745'>Livres: <b>" + livres + "</b></font> | <font color='#dc3545'>Ocupadas: <b>" + ocupadas + "</b></font></html>");
            painelGradeVagas.revalidate();
            painelGradeVagas.repaint();
        } catch (Exception ex) {}
    }

    private void abrirDialogoCadastro() {
        JTextField txtMatricula = new JTextField();
        JTextField txtMarca = new JTextField();
        JTextField txtModelo = new JTextField();
        String[] tipos = {"Carro", "Mota", "Camião"};
        JComboBox<String> cbTipo = new JComboBox<>(tipos);

        Object[] message = {"Matrícula:", txtMatricula, "Marca:", txtMarca, "Modelo:", txtModelo, "Tipo:", cbTipo};
        if (JOptionPane.showConfirmDialog(this, message, "Entrada de Veículo", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                String t = cbTipo.getSelectedItem().toString();
                Veiculo v = t.equals("Carro") ? new Carro(txtMatricula.getText(), txtMarca.getText(), txtModelo.getText()) :
                            t.equals("Mota") ? new Mota(txtMatricula.getText(), txtMarca.getText(), txtModelo.getText()) :
                            new Camiao(txtMatricula.getText(), txtMarca.getText(), txtModelo.getText());
                
                Bilhete b = entradaControle.registarEntrada(v);
                JOptionPane.showMessageDialog(this, "Entrada OK!\nVaga: " + b.getVaga().getNumero());
                
                carregarTabelaAtivos(txtPesquisaAtivos.getText());
                carregarTabelaHistorico();
                atualizarMapaVagas();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void abrirDialogoAtualizar() {
        JTable tabela = getTabelaAtual();
        int linha = tabela.getSelectedRow();
        if (linha == -1) { JOptionPane.showMessageDialog(this, "Selecione um veículo."); return; }

        int colSaida = tabela == tabelaAtivos ? -1 : 7; 
        if (colSaida != -1 && !tabela.getValueAt(linha, colSaida).equals("No Parque")) {
            JOptionPane.showMessageDialog(this, "Veículo já saiu."); return;
        }

        int id = Integer.parseInt(tabela.getValueAt(linha, 0).toString());
        String matricula = tabela.getValueAt(linha, 1).toString();
        String marca = tabela.getValueAt(linha, 3).toString();
        String modelo = tabela.getValueAt(linha, 4).toString();

        JTextField txtNovaMatricula = new JTextField(matricula);
        JTextField txtNovaMarca = new JTextField(marca);
        JTextField txtNovoModelo = new JTextField(modelo);

        Object[] msg = {"Nova Matrícula:", txtNovaMatricula, "Nova Marca:", txtNovaMarca, "Novo Modelo:", txtNovoModelo};
        if (JOptionPane.showConfirmDialog(this, msg, "Atualizar", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                veiculoControle.atualizarDados(id, matricula, txtNovaMatricula.getText(), txtNovaMarca.getText(), txtNovoModelo.getText());
                JOptionPane.showMessageDialog(this, "Atualizado!");
                carregarTabelaAtivos(txtPesquisaAtivos.getText());
                carregarTabelaHistorico();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        }
    }

    private void registarSaida() {
        JTable tabela = getTabelaAtual();
        int linha = tabela.getSelectedRow();
        if (linha == -1) { JOptionPane.showMessageDialog(this, "Selecione um veículo."); return; }

        int colSaida = tabela == tabelaAtivos ? -1 : 7;
        if (colSaida != -1 && !tabela.getValueAt(linha, colSaida).equals("No Parque")) {
            JOptionPane.showMessageDialog(this, "Veículo já saiu."); return;
        }

        String matricula = tabela.getValueAt(linha, 1).toString();
        try {
            double valor = saidaControle.calcularValor(matricula);
            if (JOptionPane.showConfirmDialog(this, "Pagar: " + String.format("%.2f MT", valor) + "?", "Saída", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                saidaControle.registarSaida(matricula);
                JOptionPane.showMessageDialog(this, "Saída OK!");
                carregarTabelaAtivos(txtPesquisaAtivos.getText());
                carregarTabelaHistorico();
                atualizarMapaVagas();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void mostrarDetalhes() {
        JTable tabela = getTabelaAtual();
        int linha = tabela.getSelectedRow();
        if (linha == -1) { JOptionPane.showMessageDialog(this, "Selecione um veículo."); return; }

        String id = tabela.getValueAt(linha, 0).toString();
        String matricula = tabela.getValueAt(linha, 1).toString();
        String tipo = tabela.getValueAt(linha, 2).toString();
        String marca = tabela.getValueAt(linha, 3).toString();
        String modelo = tabela.getValueAt(linha, 4).toString();
        String vaga = tabela.getValueAt(linha, 5).toString();
        String entrada = tabela.getValueAt(linha, 6).toString();
        String saida = tabela == tabelaAtivos ? "No Parque" : tabela.getValueAt(linha, 7).toString();
        String valor = tabela == tabelaAtivos ? "-" : tabela.getValueAt(linha, 8).toString();

        mostrarPainelBilhete(id, matricula, tipo, marca, modelo, vaga, entrada, saida, valor);
    }

    private void mostrarDetalhesBilhete(Bilhete b) {
        mostrarPainelBilhete(String.valueOf(b.getId()), b.getVeiculo().getMatricula(), b.getVeiculo().getTipo().getDescricao(),
                b.getVeiculo().getMarca(), b.getVeiculo().getModelo(), String.valueOf(b.getVaga().getNumero()),
                b.getHoraEntrada().format(fmtDataHora), b.getHoraSaida() != null ? b.getHoraSaida().format(fmtDataHora) : "No Parque",
                b.getValorPago() != null ? String.format("%.2f MT", b.getValorPago()) : "-");
    }

    private void mostrarPainelBilhete(String id, String matricula, String tipo, String marca, String modelo, String vaga, String entradaStr, String saidaStr, String valor) {
        String tempo = "-";
        try {
            LocalDateTime ent = LocalDateTime.parse(entradaStr, fmtDataHora);
            LocalDateTime sai = saidaStr.equals("No Parque") ? LocalDateTime.now() : LocalDateTime.parse(saidaStr, fmtDataHora);
            Duration d = Duration.between(ent, sai);
            tempo = d.toHours() + "h e " + d.toMinutesPart() + "min";
        } catch (Exception e) {}

        String info = "<html><body style='width: 280px; font-family: Segoe UI;'>"
                + "<h2 style='color: #000080;'>Bilhete #" + id + "</h2>"
                + "<b>Matrícula:</b> " + matricula + "<br>"
                + "<b>Tipo:</b> " + tipo + "<br>"
                + "<b>Marca:</b> " + marca + "<br>"
                + "<b>Modelo:</b> " + modelo + "<br><hr>"
                + "<b>Vaga:</b> " + vaga + "<br>"
                + "<b>Entrada:</b> " + entradaStr + "<br>"
                + "<b>Saída:</b> " + (saidaStr.equals("No Parque") ? "<font color='green'><b>AINDA NO PARQUE</b></font>" : saidaStr) + "<br>"
                + "<b>Tempo:</b> <font color='blue'><b>" + tempo + "</b></font><br>" 
                + "<b>Valor:</b> " + (valor.equals("-") ? "Ainda não pagou" : valor)
                + "</body></html>";
        JOptionPane.showMessageDialog(this, info, "Detalhes", JOptionPane.INFORMATION_MESSAGE);
    }

    private JButton criarBotaoMenu(String texto) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(0, 0, 128));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        return btn;
    }

    private JButton criarBotaoAcao(String texto, Color corFundo) {
        JButton btn = new JButton(texto);
        btn.setBackground(corFundo);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        return btn;
    }    
} 