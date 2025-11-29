import java.util.ArrayList;
import java.util.List;

// Importações para o servidor HTTP
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

// Importações para o servidor HTTP

public class Categoria implements HttpHandler {
    private Integer id;
    private String categoria;
    private Integer ativo;

    private static ArrayList<Categoria> categorias = new ArrayList<>();
    public List<ReceitaCategoria> receitaCategorias = new ArrayList<>();

    public Categoria() {

    }

    public Categoria(String categoria, Integer ativo) {
        this.categoria = categoria;
        this.ativo = ativo;

        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "INSERT INTO categoria (categoria, ativo) VALUES (?, ?);");

            stmt.setString(1, this.getCategoria());
            stmt.setInt(2, this.getAtivo());
            stmt.execute();
            DAO.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Categoria(Integer id, String categoria, Integer ativo) {
        this.id = id;
        this.categoria = categoria;
        this.ativo = ativo;
    }

    public static ArrayList<Categoria> listarCategorias() {
        return categorias;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public static Categoria getCategoriaPorID(int id) {
        for (Categoria categoria : categorias) {
            if (categoria.getId().equals(id)) {
                return categoria;
            }
        }
        return null;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getAtivo() {
        return this.ativo;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "ID: " + this.getId() + " - Categoria: " + this.getCategoria() + " - Ativo: " + this.getAtivo();
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
        String query = "SELECT idcategoria, categoria, ativo FROM categoria";

        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (!first)
                    json.append(",");

                json.append("{")
                        .append("\"idcategoria\": ").append(rs.getInt("idcategoria")).append(",")
                        .append("\"categoria\": \"").append(rs.getString("categoria")).append("\",")
                        .append("\"ativo\": ").append(rs.getBoolean("ativo"))
                        .append("}");

                first = false;
            }
            DAO.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
            String errorResponse = "{\"error\": \"Erro ao buscar categorias\"}";
            exchange.sendResponseHeaders(500, errorResponse.getBytes().length);
            exchange.getResponseBody().write(errorResponse.getBytes());
            exchange.getResponseBody().close();
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
         * "categoria": "sobremesa",
         * "ativo": 1
         * }
         * 
         */

        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        String nome = body.replaceAll("(?s).*\"categoria\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String sAtivo = body.replaceAll("(?s).*\"ativo\"\\s*:\\s*(\\d+).*", "$1");
        int ativo = Integer.parseInt(sAtivo);

        new Categoria(nome, ativo);

        String response = "{\"message\": \"Categoria adicionada com sucesso\"}";
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
         * URL: http://localhost:8089/categorias?id=1
         * 
         * {
         * "categoria": "sobremesa",
         * "ativo": 1
         * }
         * 
         */

        String query = exchange.getRequestURI().getQuery();

        if (query == null || !query.contains("id=")) {
            String response = "{\"error\": \"ID da categoria não informado\"}";
            exchange.sendResponseHeaders(400, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
            return;
        }

        int id = Integer.parseInt(query.replaceAll(".*id=(\\d+).*", "$1"));

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        String nome = body.replaceAll("(?s).*\"categoria\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String sAtivo = body.replaceAll("(?s).*\"ativo\"\\s*:\\s*(\\d+).*", "$1");
        int ativo = Integer.parseInt(sAtivo);

        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "UPDATE categoria SET categoria = ?, ativo = ? WHERE idcategoria = ?");

            stmt.setString(1, nome);
            stmt.setInt(2, ativo);
            stmt.setInt(3, id);

            int rows = stmt.executeUpdate();
            DAO.closeConnection();

            if (rows == 0) {
                String response = "{\"error\": \"Categoria não encontrada\"}";
                exchange.sendResponseHeaders(404, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            String response = "{\"message\": \"Categoria atualizada com sucesso\"}";
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();

        } catch (Exception e) {
            e.printStackTrace();
            String response = "{\"error\": \"Erro ao atualizar categoria\"}";
            exchange.sendResponseHeaders(500, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        /*
         * Exemplo de requisição
         * 
         * URL: http://localhost:8089/categorias?id=1
         * 
         */

        String query = exchange.getRequestURI().getQuery();
        if (query == null || !query.contains("id=")) {
            String response = "{\"error\": \"ID da categoria não informado\"}";
            exchange.sendResponseHeaders(400, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
            return;
        }

        int id = Integer.parseInt(query.replaceAll(".*id=(\\d+).*", "$1"));

        try {
            PreparedStatement stmt = DAO.createConnection()
                    .prepareStatement("DELETE FROM categoria WHERE idcategoria = ?");
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            DAO.closeConnection();

            if (rows == 0) {
                String response = "{\"error\": \"Categoria não encontrada\"}";
                exchange.sendResponseHeaders(404, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            String response = "{\"message\": \"Categoria deletada com sucesso\"}";
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            String response = "{\"error\": \"Não é possível deletar: categoria está associada a uma receita\"}";
            exchange.sendResponseHeaders(409, response.getBytes().length); // 409 Conflict
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();

        } catch (Exception e) {

            e.printStackTrace();
            String response = "{\"error\": \"Erro interno ao deletar a categoria\"}";
            exchange.sendResponseHeaders(500, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }

}