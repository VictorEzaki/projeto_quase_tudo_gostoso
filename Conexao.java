import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Conexao {
    public static void main(String[] args) {
        try{
            Connection conexao = DAO.createConnection();
            Categoria categoria = new Categoria("teste", true);

            /* INSERT Usuário */
            // PreparedStatement stmt = conexao.prepareStatement(
            //     "INSERT INTO categoria (categoria, ativo) VALUES (?, ?);"
            // );
            // stmt.setString(1, categoria.getCategoria());
            // stmt.setBoolean(2, categoria.getAtivo());
            // stmt.execute();

            /* SELECT ALL USERS */
            imprimirCategorias(conexao);

            /* DELETE id = 2 */
            // PreparedStatement stmt = conexao.prepareStatement(
            //     "DELETE FROM usuario WHERE id = ?;"
            // );
            // stmt.setInt(1, 2);
            // stmt.execute();
            // imprimirUsuarios(conexao);

            /* UPDATE id = 1 */
            // stmt = conexao.prepareStatement(
            //     "UPDATE usuario SET user_name = ?, name = ?, password = ? WHERE id = ?;"
            // );
            // stmt.setString(1, "maria.dores");
            // stmt.setString(2, "Maria das Dores");
            // stmt.setString(3, "123457");
            // stmt.setInt(4, 1);
            // stmt.execute();
            // imprimirUsuarios(conexao);
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

    public static void imprimirCategorias(Connection conexao) throws Exception {
        ResultSet rs = conexao.createStatement().executeQuery(
            "SELECT * FROM categoria;"
        );
        while(rs.next()){
            Categoria categoria2 = new Categoria(
                rs.getInt("id"), 
                rs.getString("categoria"),
                rs.getBoolean("ativo")
            );
            System.out.println(categoria2);
            System.out.println("===================================");
        }
    }
}
