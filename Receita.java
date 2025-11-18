import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
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
                    "INSERT INTO receita (titulo, descricao, imagem, idUsuario) VALUES (?, ?, ?, ?);",
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
                        "INSERT INTO receita_categoria (idReceita, idCategoria) VALUES (?, ?)");

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

    public Receita(int idReceita, String titulo, String descricao, String imagem, int idUsuario) {
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
        String query = "SELECT r.idReceita, r.titulo, r.descricao, r.imagem, u.nome AS autor " +
                "FROM receita r " +
                "INNER JOIN usuario u ON r.idUsuario = u.idUsuario;";

        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        try (Connection conn = DAO.createConnection();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                if (!first)
                    json.append(",");

                json.append(String.format(
                        "{\"idReceita\": \"%d\", \"titulo\": \"%s\", \"descricao\": \"%s\", \"imagem\": \"%s\", \"autor\": \"%s\"}",
                        rs.getInt("idReceita"),
                        rs.getString("titulo"),
                        rs.getString("descricao"),
                        rs.getString("imagem"),
                        rs.getString("autor")));

                first = false;
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
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        String titulo = body.replaceAll("(?s).*\"titulo\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String descricao = body.replaceAll("(?s).*\"descricao\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String imagem = body.replaceAll("(?s).*\"imagem\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String idUsuarioStr = body.replaceAll("(?s).*\"idUsuario\"\\s*:\\s*\"?([0-9]+)\"?.*", "$1");

        Usuario usuario = Usuario.getUsuario(Integer.parseInt(idUsuarioStr));
        if (usuario == null) {
            String response = "{\"error\": \"Usuário não encontrado\"}";
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

}
