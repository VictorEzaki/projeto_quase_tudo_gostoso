import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioConexao {
    public static void main(String[] args) {
        try {
            Usuario usuario = new Usuario("Victor", "victor@victor.com", "2003-03-19", 12312312, 1, "senhaMuitoSegura", "10", "2025-11-15", "12312312dsdawds", 1);
            criarUsuario(usuario);

            Usuario usuarioEditado = new Usuario("Bruno", "bruno@example.com", "2003-03-19", 12312312, 1, "senhaMuitoSegura", "10", "2025-11-15", "12312312dsdawds", 1);
            editarUsuario(usuarioEditado, 1);

            deletarUsuario(1);

            imprimirUsuarios();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void criarUsuario(Usuario u) {
        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "INSERT INTO usuario (nome, email, data_nascimento, cep, genero, senha, salt, inscrito, uuid, ativo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getDataNascimento());
            stmt.setInt(4, u.getCep());
            stmt.setInt(5, u.getGenero());
            stmt.setString(6, u.getSenha());
            stmt.setString(7, u.getDataInscricao());
            stmt.setString(8, u.getUuid());
            stmt.setString(9, u.getUuid());
            stmt.setInt(10, u.getAtivo());
            stmt.execute();

            DAO.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void imprimirUsuarios() throws Exception {
        Connection conexao = DAO.createConnection();

        ResultSet rs = conexao.createStatement().executeQuery(
                "SELECT * FROM usuario;");
        while (rs.next()) {
            Usuario usuario2 = new Usuario(
                    rs.getInt("idUsuario"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("data_nascimento"),
                    rs.getInt("cep"),
                    rs.getInt("genero"),
                    rs.getString("senha"),
                    rs.getString("salt"),
                    rs.getString("inscrito"),
                    rs.getString("uuid"),
                    rs.getInt("ativo"));
            System.out.println(usuario2);
        }

        DAO.closeConnection();
    }

    public static void editarUsuario(Usuario u, int id) {
        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "UPDATE usuario SET nome = ?, email = ?, data_nascimento = ?, cep = ?, genero = ?, senha = ?, salt = ?, inscrito = ?, uuid = ?, ativo = ? WHERE idUsuario = ?;");

            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getEmail());
            stmt.setString(3, u.getDataNascimento());
            stmt.setInt(4, u.getCep());
            stmt.setInt(5, u.getGenero());
            stmt.setString(6, u.getSenha());
            stmt.setString(7, u.getDataInscricao());
            stmt.setString(8, u.getUuid());
            stmt.setString(9, u.getUuid());
            stmt.setInt(10, u.getAtivo());
            stmt.setInt(11, id);
            stmt.execute();

            DAO.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void deletarUsuario(int id) {
        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "DELETE FROM usuario WHERE idUsuario = ?;");

            stmt.setInt(1, id);
            stmt.execute();

            DAO.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}