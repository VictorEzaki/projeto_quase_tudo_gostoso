import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Receita implements HttpHandler {
    private Integer idReceita;
    private String titulo;
    private String descricao;
    private String imagem;
    private Integer idUsuario;

    private ArrayList<ReceitaCategoria> receitaCategorias = new ArrayList<>();
    private static ArrayList<Comentario> comentarios = new ArrayList<>();

    public Receita() {

    }

    public Receita(String titulo, String descricao, String imagem, int idUsuario, ArrayList<Integer> categoriasIds) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagem = imagem;
        this.idUsuario = idUsuario;

        try {

            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "INSERT INTO receita (titulo, descricao, imagem, cadastro_idusuario) VALUES (?, ?, ?, ?);",
                    PreparedStatement.RETURN_GENERATED_KEYS);

            stmt.setString(1, this.getTitulo());
            stmt.setString(2, this.getDescricao());
            stmt.setString(3, this.getImagem());
            stmt.setInt(4, this.getIdUsuario());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            int idReceitaGerada = 0;
            if (rs.next()) {
                idReceitaGerada = rs.getInt(1);
            }
            rs.close();
            stmt.close();

            for (Integer idCategoria : categoriasIds) {
                PreparedStatement stmtRel = DAO.createConnection().prepareStatement(
                        "INSERT INTO categoria_receita (receita_idreceita, categoria_idcategoria) VALUES (?, ?)");

                stmtRel.setInt(1, idReceitaGerada);
                stmtRel.setInt(2, idCategoria);
                stmtRel.executeUpdate();
                stmtRel.close();
            }

            DAO.closeConnection();

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar receita com categorias: " + e.getMessage());
        }
    }

    public Receita(int idReceita, String titulo, String descricao, String imagem, int idUsuario,
            ArrayList<Integer> categoriasIds) {
        this.idReceita = idReceita;
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagem = imagem;
        this.idUsuario = idUsuario;
    }

    public Integer getIdReceita() {
        return this.idReceita;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public String getImagem() {
        return this.imagem;
    }

    public Integer getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdReceita(int idReceita) {
        this.idReceita = idReceita;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void adicionarComentario(Comentario comentario) {
        comentarios.add(comentario);
    }

    public static ArrayList<Comentario> listarComentarios() {
        return comentarios;
    }

    @Override
    public String toString() {
        return "\n========== Receita ==========\n" +
                "ID Receita:      " + this.getIdReceita() + "\n" +
                "Título:          " + this.getTitulo() + "\n" +
                "Descrição:       " + this.getDescricao() + "\n" +
                "Imagem:          " + this.getImagem() + "\n" +
                "ID do Autor:     " + this.getIdUsuario() + "\n" +
                "=============================\n";
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equalsIgnoreCase("GET")) {
            handleGet(exchange);
        } else if (method.equalsIgnoreCase("POST")) {
            handlePost(exchange);
        } else if (method.equalsIgnoreCase("PUT")) {
            handlePut(exchange);
        } else if (method.equalsIgnoreCase("DELETE")) {
            handleDelete(exchange);
        } else {
            String response = "Método não suportado";
            byte[] bytes = response.getBytes("UTF-8");
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=UTF-8");
            exchange.sendResponseHeaders(405, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }
    }

    private void handleGet(HttpExchange exchange) throws IOException {
        String query = "SELECT r.idreceita, r.titulo, r.descricao, r.imagem, u.nome AS autor " +
                "FROM receita r " +
                "INNER JOIN usuario u ON r.cadastro_idusuario = u.idusuario;";

        StringBuilder json = new StringBuilder("[");
        boolean firstReceita = true;

        try (Connection conn = DAO.createConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                if (!firstReceita)
                    json.append(",");

                int idReceita = rs.getInt("idreceita");

                String queryCategorias = "SELECT c.idcategoria, c.categoria " +
                        "FROM categoria_receita cr " +
                        "INNER JOIN categoria c ON c.idcategoria = cr.categoria_idcategoria " +
                        "WHERE cr.receita_idreceita = ?";

                PreparedStatement stmtCat = conn.prepareStatement(queryCategorias);
                stmtCat.setInt(1, idReceita);
                ResultSet rsCat = stmtCat.executeQuery();

                StringBuilder categoriasJson = new StringBuilder("[");
                boolean firstCat = true;

                while (rsCat.next()) {
                    if (!firstCat)
                        categoriasJson.append(",");

                    categoriasJson.append(String.format(
                            "{\"id\": %d, \"categoria\": \"%s\"}",
                            rsCat.getInt("idcategoria"),
                            rsCat.getString("categoria")));

                    firstCat = false;
                }
                categoriasJson.append("]");

                rsCat.close();
                stmtCat.close();

                json.append("{")
                        .append("\"idReceita\": ").append(idReceita).append(",")
                        .append("\"titulo\": \"").append(rs.getString("titulo")).append("\",")
                        .append("\"descricao\": \"").append(rs.getString("descricao")).append("\",")
                        .append("\"imagem\": \"").append(rs.getString("imagem")).append("\",")
                        .append("\"autor\": \"").append(rs.getString("autor")).append("\",")
                        .append("\"categorias\": ").append(categoriasJson)
                        .append("}");

                firstReceita = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            String response = "{\"error\": \"Erro ao buscar receitas\"}";
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(500, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
            return;
        }

        json.append("]");

        byte[] bytes = json.toString().getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        /*
         * Exemplo de requisição
         * 
         * {
         * "titulo": "Bolo de cenoura",
         * "descricao": "Bolo de cenoura com muita cobertura de chocolate",
         * "imagem": "Imagem do bolo",
         * "cadastro_idusuario": 1,
         * "categorias": [1, 2]
         * }
         * 
         */

        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        String titulo = body.replaceAll("(?s).*\"titulo\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String descricao = body.replaceAll("(?s).*\"descricao\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String imagem = body.replaceAll("(?s).*\"imagem\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String idUsuarioStr = body.replaceAll("(?s).*\"cadastro_idusuario\"\\s*:\\s*\"?([0-9]+)\"?.*", "$1");

        Usuario usuario = Usuario.getUsuario(Integer.parseInt(idUsuarioStr));
        if (usuario == null) {
            String response = "{\"error\": \"Usuário não encontrada\"}";
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(404, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
            return;
        }

        String categoriasStr = body.replaceAll("(?s).*\"categorias\"\\s*:\\s*\\[([^\\]]*)\\].*", "$1");

        ArrayList<Integer> idsCategorias = new ArrayList<>();

        if (!categoriasStr.trim().isEmpty()) {
            String[] ids = categoriasStr.split(",");
            for (String id : ids) {
                try {
                    idsCategorias.add(Integer.parseInt(id.trim()));
                } catch (NumberFormatException e) {

                }
            }
        }

        new Receita(
                titulo,
                descricao,
                imagem,
                usuario.getId(),
                idsCategorias);

        String response = "{\"message\": \"Receita adicionada com sucesso\"}";
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(201, bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        /*
         * Exemplo de requisição
         * 
         * http://localhost:8089/receita?id=1
         * 
         * {
         * "titulo": "Bolo de cenoura",
         * "descricao": "Bolo de cenoura com muita cobertura de chocolate",
         * "imagem": "Imagem do bolo",
         * "cadastro_idusuario": 1,
         * "categorias": [1, 2]
         * }
         * 
         */

        String query = exchange.getRequestURI().getQuery();

        if (query == null || !query.contains("id=")) {
            String response = "{\"error\": \"ID da receita não informada\"}";
            exchange.sendResponseHeaders(400, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
            return;
        }

        int idReceita = Integer.parseInt(query.replaceAll(".*id=(\\d+).*", "$1"));

        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        String titulo = body.replaceAll("(?s).*\"titulo\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String descricao = body.replaceAll("(?s).*\"descricao\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String imagem = body.replaceAll("(?s).*\"imagem\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String idUsuarioStr = body.replaceAll("(?s).*\"cadastro_idusuario\"\\s*:\\s*\"?([0-9]+)\"?.*", "$1");

        Usuario usuario = Usuario.getUsuario(Integer.parseInt(idUsuarioStr));
        if (usuario == null) {
            String response = "{\"error\": \"Usuário não encontrada\"}";
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(404, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
            return;
        }

        String categoriasStr = body.replaceAll("(?s).*\"categorias\"\\s*:\\s*\\[([^\\]]*)\\].*", "$1");

        ArrayList<Integer> idsCategorias = new ArrayList<>();

        if (!categoriasStr.trim().isEmpty()) {
            String[] ids = categoriasStr.split(",");
            for (String id : ids) {
                try {
                    idsCategorias.add(Integer.parseInt(id.trim()));
                } catch (NumberFormatException e) {

                }
            }
        }

        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "UPDATE receita SET titulo = ?, descricao = ?, imagem = ?, cadastro_idusuario = ? WHERE idreceita = ?");

            stmt.setString(1, titulo);
            stmt.setString(2, descricao);
            stmt.setString(3, imagem);
            stmt.setInt(4, Integer.parseInt(idUsuarioStr));
            stmt.setInt(5, idReceita);

            int rows = stmt.executeUpdate();
            stmt.close();

            PreparedStatement stmtDel = DAO.createConnection().prepareStatement(
                    "DELETE FROM categoria_receita WHERE receita_idreceita = ?");
            stmtDel.setInt(1, idReceita);
            stmtDel.executeUpdate();
            stmtDel.close();

            for (Integer idCategoria : idsCategorias) {
                PreparedStatement stmtRel = DAO.createConnection().prepareStatement(
                        "INSERT INTO categoria_receita (receita_idreceita, categoria_idcategoria) VALUES (?, ?)");

                stmtRel.setInt(1, idReceita);
                stmtRel.setInt(2, idCategoria);
                stmtRel.executeUpdate();
                stmtRel.close();
            }
            DAO.closeConnection();

            if (rows == 0) {
                String response = "{\"error\": \"Receita não encontrada\"}";
                exchange.sendResponseHeaders(404, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            String response = "{\"message\": \"Receita atualizada com sucesso\"}";
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();

        } catch (Exception e) {
            e.printStackTrace();
            String response = "{\"error\": \"Erro ao atualizar receita\"}";
            exchange.sendResponseHeaders(500, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        /*
         * Exemplo de requisição
         * 
         * http://localhost:8089/receita?id=1
         */

        String query = exchange.getRequestURI().getQuery();

        if (query == null || !query.contains("id=")) {
            String response = "{\"error\": \"ID da receita não informada\"}";
            exchange.sendResponseHeaders(400, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
            return;
        }

        int idReceita = Integer.parseInt(query.replaceAll(".*id=(\\d+).*", "$1"));

        try {
            Connection conn = DAO.createConnection();

            PreparedStatement stmtRel = conn.prepareStatement(
                    "DELETE FROM categoria_receita WHERE receita_idreceita = ?");
            stmtRel.setInt(1, idReceita);
            stmtRel.executeUpdate();
            stmtRel.close();

            PreparedStatement stmt = conn.prepareStatement(
                    "DELETE FROM receita WHERE idreceita = ?");
            stmt.setInt(1, idReceita);
            int rows = stmt.executeUpdate();
            stmt.close();

            DAO.closeConnection();

            if (rows == 0) {
                String response = "{\"error\": \"Receita não encontrada\"}";
                exchange.sendResponseHeaders(404, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            String response = "{\"message\": \"Receita removida com sucesso\"}";
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {

            String response = "{\"error\": \"Não é possível deletar: receita está associada a outros registros\"}";
            exchange.sendResponseHeaders(409, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();

        } catch (Exception e) {
            e.printStackTrace();
            String response = "{\"error\": \"Erro ao excluir receita\"}";
            exchange.sendResponseHeaders(500, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }

}
