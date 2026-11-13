package dao;

import persistencia.ConexaoBD;
import excecoes.PersistenciaExcecao;
import modelo.*;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BilheteDAO {

    public void inserir(Bilhete bilhete) throws PersistenciaExcecao {
        String sql = "INSERT INTO ticket (matricula, tipo_veiculo, marca, modelo, numero_vaga, hora_entrada) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection c = ConexaoBD.obter();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, bilhete.getVeiculo().getMatricula());
            ps.setString(2, bilhete.getVeiculo().getTipo().name());
            ps.setString(3, bilhete.getVeiculo().getMarca());
            ps.setString(4, bilhete.getVeiculo().getModelo());
            ps.setInt(5, bilhete.getVaga().getNumero());
            ps.setTimestamp(6, Timestamp.valueOf(bilhete.getHoraEntrada()));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaExcecao("Erro ao gravar o bilhete", e);
        }
    }

    public void registarSaida(String matricula, LocalDateTime saida, double valor)
            throws PersistenciaExcecao {
        String sql = "UPDATE ticket SET hora_saida = ?, valor_pago = ? "
                   + "WHERE matricula = ? AND hora_saida IS NULL";
        try (Connection c = ConexaoBD.obter();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setTimestamp(1, Timestamp.valueOf(saida));
            ps.setBigDecimal(2, BigDecimal.valueOf(valor));
            ps.setString(3, matricula);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaExcecao("Erro ao registar a saída", e);
        }
    }

    public int atualizarDadosVeiculo(int id, String novaMatricula, String marca, String modelo)
            throws PersistenciaExcecao {
        String sql = "UPDATE ticket SET matricula = ?, marca = ?, modelo = ? WHERE id = ?";
        try (Connection c = ConexaoBD.obter();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, novaMatricula);
            ps.setString(2, marca);
            ps.setString(3, modelo);
            ps.setInt(4, id);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaExcecao("Erro ao atualizar o veículo", e);
        }
    }

    public boolean veiculoEstaDentro(String matricula) throws PersistenciaExcecao {
        String sql = "SELECT 1 FROM ticket WHERE matricula = ? AND hora_saida IS NULL";
        try (Connection c = ConexaoBD.obter();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, matricula);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new PersistenciaExcecao("Erro ao verificar o veículo", e);
        }
    }

    public Bilhete procurarAtivoPorMatricula(String matricula) throws PersistenciaExcecao {
        List<Bilhete> r = consultar(
            "SELECT * FROM ticket WHERE matricula = ? AND hora_saida IS NULL", matricula);
        return r.isEmpty() ? null : r.get(0);
    }

    public List<Bilhete> listarTodos() throws PersistenciaExcecao {
        return consultar("SELECT * FROM ticket ORDER BY hora_entrada DESC", null);
    }

    public List<Bilhete> listarAtivos() throws PersistenciaExcecao {
        return consultar("SELECT * FROM ticket WHERE hora_saida IS NULL ORDER BY hora_entrada", null);
    }

    public List<Bilhete> pesquisarPorMatricula(String matricula) throws PersistenciaExcecao {
        return consultar(
            "SELECT * FROM ticket WHERE matricula = ? ORDER BY hora_entrada DESC", matricula);
    }

    public List<Bilhete> pesquisarPorTexto(String texto) throws PersistenciaExcecao {
        List<Bilhete> lista = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE matricula LIKE ? OR marca LIKE ? OR modelo LIKE ? ORDER BY hora_entrada DESC";
        String padrao = "%" + texto + "%";

        try (Connection c = ConexaoBD.obter();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, padrao);
            ps.setString(2, padrao);
            ps.setString(3, padrao);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaExcecao("Erro ao pesquisar veículos", e);
        }
        return lista;
    }

    public List<Bilhete> listarPorData(LocalDate data) throws PersistenciaExcecao {
        return consultar(
            "SELECT * FROM ticket WHERE DATE(hora_entrada) = ? ORDER BY hora_entrada",
            data.toString());
    }

    public List<Bilhete> pesquisarAtivos(String texto) throws PersistenciaExcecao {
        List<Bilhete> lista = new ArrayList<>();
        String sql = "SELECT * FROM ticket WHERE hora_saida IS NULL "
                   + "AND (matricula LIKE ? OR marca LIKE ? OR modelo LIKE ?) "
                   + "ORDER BY numero_vaga";
        String padrao = "%" + texto + "%";

        try (Connection c = ConexaoBD.obter();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, padrao);
            ps.setString(2, padrao);
            ps.setString(3, padrao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaExcecao("Erro ao pesquisar veículos", e);
        }
        return lista;
    }

    public double totalFaturado(LocalDate data) throws PersistenciaExcecao {
        String sql = "SELECT COALESCE(SUM(valor_pago), 0) FROM ticket WHERE DATE(hora_saida) = ?";
        try (Connection c = ConexaoBD.obter();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, data.toString());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            throw new PersistenciaExcecao("Erro ao calcular o total faturado", e);
        }
    }

    public List<Bilhete> filtrar(String texto, String tipoVeiculo, String estado, java.time.LocalDate data) throws PersistenciaExcecao {
        List<Bilhete> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM ticket WHERE 1=1 ");
        List<String> parametros = new ArrayList<>();

        if (texto != null && !texto.trim().isEmpty()) {
            sql.append("AND (matricula LIKE ? OR marca LIKE ? OR modelo LIKE ?) ");
            String p = "%" + texto.trim() + "%";
            parametros.add(p);
            parametros.add(p);
            parametros.add(p);
        }

        if (tipoVeiculo != null && !tipoVeiculo.equals("Todos")) {
            sql.append("AND tipo_veiculo = ? ");
            parametros.add(tipoVeiculo.toUpperCase());
        }

        if (estado != null && !estado.equals("Todos")) {
            if (estado.equals("No Parque")) {
                sql.append("AND hora_saida IS NULL ");
            } else if (estado.equals("Finalizados")) {
                sql.append("AND hora_saida IS NOT NULL ");
            }
        }

        if (data != null) {
            sql.append("AND DATE(hora_entrada) = ? ");
            parametros.add(data.toString()); 
        }

        sql.append("ORDER BY hora_entrada DESC");

        try (Connection c = ConexaoBD.obter();
             PreparedStatement ps = c.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {
                ps.setString(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaExcecao("Erro ao filtrar veículos", e);
        }
        return lista;
    }

    private List<Bilhete> consultar(String sql, String parametro) throws PersistenciaExcecao {
        List<Bilhete> lista = new ArrayList<>();
        try (Connection c = ConexaoBD.obter();
             PreparedStatement ps = c.prepareStatement(sql)) {

            if (parametro != null) {
                ps.setString(1, parametro);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaExcecao("Erro ao consultar bilhetes", e);
        }
        return lista;
    }

    private Bilhete mapear(ResultSet rs) throws SQLException {
        String matricula = rs.getString("matricula");
        String marca = rs.getString("marca");
        String modelo = rs.getString("modelo");
        TipoVaga tipo = TipoVaga.valueOf(rs.getString("tipo_veiculo"));

        Veiculo veiculo = switch (tipo) {
            case CARRO  -> new Carro(matricula, marca, modelo);
            case MOTA   -> new Mota(matricula, marca, modelo);
            case CAMIAO -> new Camiao(matricula, marca, modelo);
        };

        Vaga vaga = new Vaga(rs.getInt("numero_vaga"), tipo);

        Timestamp ts = rs.getTimestamp("hora_saida");
        LocalDateTime saida = (ts != null) ? ts.toLocalDateTime() : null;
        if (saida == null) {
            vaga.ocupar();
        }

        BigDecimal valor = rs.getBigDecimal("valor_pago");

        return new Bilhete(rs.getInt("id"), veiculo, vaga,
                rs.getTimestamp("hora_entrada").toLocalDateTime(),
                saida,
                valor != null ? valor.doubleValue() : null);
    }
    
    public Bilhete procurarAtivoPorVaga(int numeroVaga) throws PersistenciaExcecao {
        List<Bilhete> r = consultar(
            "SELECT * FROM ticket WHERE numero_vaga = ? AND hora_saida IS NULL", String.valueOf(numeroVaga));
        return r.isEmpty() ? null : r.get(0);
    }
}