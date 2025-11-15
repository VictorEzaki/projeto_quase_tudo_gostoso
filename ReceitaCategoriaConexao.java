import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ReceitaCategoriaConexao {
    public static void main(String[] args) {
        try {
            // ReceitaCategoria rc = new ReceitaCategoria(5, 3);
            // associarReceitaCategoria(rc);

            imprimirReceitasCategorias();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void associarReceitaCategoria(ReceitaCategoria rc) {
        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "INSERT INTO receita_categoria (idReceita, idCategoria) VALUES (?, ?);");

            stmt.setInt(1, rc.getIdReceita());
            stmt.setInt(2, rc.getIdCategoria());
            stmt.execute();

            DAO.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void imprimirReceitasCategorias() throws Exception {
        Connection conexao = DAO.createConnection();

        ResultSet rs = conexao.createStatement().executeQuery(
                "SELECT * FROM receita_categoria;");
        while (rs.next()) {
            ReceitaCategoria rc2 = new ReceitaCategoria(
                    rs.getInt("idReceita"),
                    rs.getInt("idCategoria"));
            System.out.println(rc2);
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
