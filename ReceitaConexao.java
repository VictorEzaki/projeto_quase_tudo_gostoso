import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReceitaConexao {
    public static void main(String[] args) {
        try {
            // Receita receita = new Receita("Bolo de cenoura", "Bolo muito gostoso", "bolo.png", 6);
            // criarReceita(receita);

            // Receita receitaEditada = new Receita("Bolo de chocolate", "Bolo muito gostoso", "bolo.png", 6);
            // editarReceita(receitaEditada, 4);

            // deletarReceita(4);

            imprimirReceitas();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void criarReceita(Receita r) {
        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "INSERT INTO receita (titulo, descricao, imagem, idUsuario) VALUES (?, ?, ?, ?);");

            stmt.setString(1, r.getTitulo());
            stmt.setString(2, r.getDescricao());
            stmt.setString(3, r.getImagem());
            stmt.setInt(4, r.getIdUsuario());
            stmt.execute();

            DAO.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void imprimirReceitas() throws Exception {
        Connection conexao = DAO.createConnection();

        ResultSet rs = conexao.createStatement().executeQuery(
                "SELECT * FROM receita;");
        while (rs.next()) {
            Receita receita2 = new Receita(
                    rs.getInt("idReceita"),
                    rs.getString("titulo"),
                    rs.getString("descricao"),
                    rs.getString("imagem"),
                    rs.getInt("idUsuario"));
            System.out.println(receita2);
        }

        DAO.closeConnection();
    }

    public static void editarReceita(Receita r, int id) {
        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement("UPDATE receita SET titulo = ?, descricao = ?, imagem = ?, idUsuario = ? WHERE idReceita = ?;");

            stmt.setString(1, r.getTitulo());
            stmt.setString(2, r.getDescricao());
            stmt.setString(3, r.getImagem());
            stmt.setInt(4, r.getIdUsuario());
            stmt.setInt(5, id);
            stmt.execute();

            DAO.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void deletarReceita(int id) {
        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "DELETE FROM receita WHERE idReceita = ?;");

            stmt.setInt(1, id);
            stmt.execute();

            DAO.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
