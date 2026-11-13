package dao;

import excecoes.*;
import modelo.TipoVaga;
import modelo.Vaga;
import conexao.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VagaDAO {

    public List<Vaga> listarTodas() throws PersistenciaExcecao {
        return consultar("SELECT numero, tipo, ocupada FROM vaga ORDER BY numero");
    }

    public List<Vaga> listarLivres() throws PersistenciaExcecao {
        return consultar("SELECT numero, tipo, ocupada FROM vaga WHERE ocupada = FALSE ORDER BY numero");
    }

    public List<Vaga> listarOcupadas() throws PersistenciaExcecao {
        return consultar("SELECT numero, tipo, ocupada FROM vaga WHERE ocupada = TRUE ORDER BY numero");
    }

    private List<Vaga> consultar(String sql) throws PersistenciaExcecao {
        List<Vaga> lista = new ArrayList<>();
        try (Connection c = ConexaoBD.obter();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vaga v = new Vaga(rs.getInt("numero"), TipoVaga.valueOf(rs.getString("tipo")));
                if (rs.getBoolean("ocupada")) {
                    v.ocupar();
                }
                lista.add(v);
            }
        } catch (SQLException e) {
            throw new PersistenciaExcecao("Erro ao listar vagas", e);
        }
        return lista;
    }

    public Vaga procurarLivrePorTipo(TipoVaga tipo) throws PersistenciaExcecao {
        String sql = "SELECT numero, tipo FROM vaga WHERE ocupada = FALSE AND tipo = ? ORDER BY numero LIMIT 1";
        try (Connection c = ConexaoBD.obter();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, tipo.name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Vaga(rs.getInt("numero"), TipoVaga.valueOf(rs.getString("tipo")));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new PersistenciaExcecao("Erro ao procurar vaga livre", e);
        }
    }

    public void atualizarEstado(int numero, boolean ocupada) throws PersistenciaExcecao {
        String sql = "UPDATE vaga SET ocupada = ? WHERE numero = ?";
        try (Connection c = ConexaoBD.obter();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setBoolean(1, ocupada);
            ps.setInt(2, numero);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaExcecao("Erro ao atualizar vaga", e);
        }
    }

    public int contarLivresPorTipo(TipoVaga tipo) throws PersistenciaExcecao {
        String sql = "SELECT COUNT(*) FROM vaga WHERE ocupada = FALSE AND tipo = ?";
        try (Connection c = ConexaoBD.obter();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, tipo.name());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            throw new PersistenciaExcecao("Erro ao contar vagas livres", e);
        }
    }
}