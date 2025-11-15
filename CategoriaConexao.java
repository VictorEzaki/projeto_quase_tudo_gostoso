import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CategoriaConexao {
    public static void main(String[] args) {
        try {
            Categoria categoria = new Categoria("Japonesa", true);
            criarCategoria(categoria);

            Categoria categoria2 = new Categoria("Italiana", true);
            editarCategoria(categoria2, 1);

            deletarCategoria(1);

            imprimirCategorias();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void criarCategoria(Categoria c) {
        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "INSERT INTO categoria (categoria, ativo) VALUES (?, ?);");

            stmt.setString(1, c.getCategoria());
            stmt.setBoolean(2, c.getAtivo());
            stmt.execute();

            DAO.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void imprimirCategorias() throws Exception {
        Connection conexao = DAO.createConnection();

        ResultSet rs = conexao.createStatement().executeQuery(
                "SELECT * FROM categoria;");
        while (rs.next()) {
            Categoria categoria2 = new Categoria(
                    rs.getInt("id"),
                    rs.getString("categoria"),
                    rs.getBoolean("ativo"));
            System.out.println(categoria2);
            System.out.println("===================================");
        }

        DAO.closeConnection();
    }

    public static void editarCategoria(Categoria c, int id) {
        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "UPDATE categoria SET categoria = ?, ativo = ? WHERE id = ?;");

            stmt.setString(1, c.getCategoria());
            stmt.setBoolean(2, c.getAtivo());
            stmt.setInt(3, id);
            stmt.execute();

            DAO.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void deletarCategoria(int id) {
        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "DELETE FROM categoria WHERE id = ?;");

            stmt.setInt(1, id);
            stmt.execute();

            DAO.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
