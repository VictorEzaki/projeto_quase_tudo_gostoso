import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CategoriaConexao {
    public static void main(String[] args) {
        try {
            Connection conexao = DAO.createConnection();

            /*
                Lembrem-se de descomentar ou comentar determinada função para executar ou não executar respectivamente quando clicar em "Run"
            */

            // Chama a função resposável por criar uma categoria (como não tem menu, para criar precisa alterar os parâmetros na chamada manualmente)
            // criarCategoria("Brasileira", true);

            // Chama a função de editar uma categoria por ID (como não tem menu, para deletar precisa alterar os parâmetros na chamada manualmente)
            // editarCategoria(conexao, "italiana", true, 2);

            // Chama a função de deletar uma categoria por ID (como não tem menu, para deletar precisa alterar os parâmetros na chamada manualmente)
            // deletarCategoria(conexao, 1);

            // Função que exibe todas as categorias
            // imprimirCategorias(conexao);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Funções que manipulam os dados no banco
    public static void criarCategoria(String categoria, Boolean ativo) {
        try {
            Categoria novaCategoria = new Categoria(categoria, ativo);

            System.out.printf("=== Categoria criada ===\n");
            System.out.printf("Nome da categoria: %s\n", novaCategoria.getCategoria());
            System.out.printf("Status: %s\n", (novaCategoria.getAtivo().equals(true)) ? "Ativo" : "Inativo");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Função responsável por exibir todas as categorias
    public static void imprimirCategorias(Connection conexao) throws Exception {
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
    }

    // Função responsável por editar uma categoria existente
    public static void editarCategoria(Connection conexao, String categoria, Boolean ativo, int id) {
        try {
            PreparedStatement stmt = conexao.prepareStatement(
                    "UPDATE categoria SET categoria = ?, ativo = ? WHERE id = ?;");
            stmt.setString(1, categoria);
            stmt.setBoolean(2, ativo);
            stmt.setInt(3, id);
            stmt.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // Função responsável por deletar uma categoria por ID existente
    public static void deletarCategoria(Connection conexao, int id) {
        try {
            PreparedStatement stmt = conexao.prepareStatement(
                    "DELETE FROM categoria WHERE id = ?;");
            stmt.setInt(1, id);
            stmt.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
