import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ComentarioConexao {
    public static void main(String[] args) {
        try {
            Comentario comentario = new Comentario(1,1, "Comentario teste",5,"2025-03-10");
        criarComentario(comentario);


           
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void criarComentario(Comentario c) {
        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "INSERT INTO comentario (comentario, nota , dataComentario, idReceita, idUsuario) VALUES (? , ? , ? , ? , ?);");

            stmt.setString(1, c.getComentario());
            stmt.setInt(2, c.getNota());
            stmt.setString(3, c.getDataComentario());
            stmt.setInt(4, c.getIdReceita());
            stmt.setInt(5, c.getIdUsuario());
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
