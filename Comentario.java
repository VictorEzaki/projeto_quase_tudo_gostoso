import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Comentario implements HttpHandler {
    private Integer idComentario;
    private Integer idReceita;
    private Integer idUsuario;
    private String comentario;
    private Integer nota;
    private LocalDateTime dataComentario;

    public Comentario() {

    }

    public Comentario(int idReceita, int idUsuario, String comentario, Integer nota,
            LocalDateTime dataComentario) {
        this.idReceita = idReceita;
        this.idUsuario = idUsuario;
        this.comentario = comentario;
        this.nota = nota;
        this.dataComentario = dataComentario;

        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "INSERT INTO comentario (receita_idreceita, usuario_idusuario, comentario, nota, datacomentario) VALUES (?, ?, ?, ?, ?);");

            stmt.setInt(1, this.getIdReceita());
            stmt.setInt(2, this.getIdUsuario());
            stmt.setString(3, this.getComentario());
            stmt.setInt(4, this.getNota());
            stmt.setTimestamp(5, java.sql.Timestamp.valueOf(this.getDataComentario()));
            stmt.execute();
            DAO.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Comentario(int idComentario, int idReceita, int idUsuario, String comentario, Integer nota,
            LocalDateTime dataComentario) {
        this.idComentario = idComentario;
        this.idReceita = idReceita;
        this.idUsuario = idUsuario;
        this.comentario = comentario;
        this.nota = nota;
        this.dataComentario = dataComentario;
    }

    public Integer getIdComentario() {
        return this.idComentario;
    }

    public String getComentario() {
        return this.comentario;
    }

    public Integer getNota() {
        return this.nota;
    }

    public LocalDateTime getDataComentario() {
        return this.dataComentario;
    }

    public Integer getIdReceita() {
        return this.idReceita;
    }

    public Integer getIdUsuario() {
        return this.idUsuario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public void setDataComentario(LocalDateTime dataComentario) {
        this.dataComentario = dataComentario;
    }

    public void setIdReceita(int idReceita) {
        this.idReceita = idReceita;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public String toString() {
        return "\n----------- Comentário -----------" +
                "\nID do Comentário: " + this.idComentario +
                "\nID da Receita: " + this.idReceita +
                "\nID do Usuário: " + this.idUsuario +
                "\nComentário: " + this.comentario +
                "\nNota: " + this.nota +
                "\nData do Comentário: " + this.dataComentario +
                "\n----------------------------------";
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
        String query = exchange.getRequestURI().getQuery();
        Integer filtroReceita = null;

        if (query != null && query.contains("receita=")) {
            filtroReceita = Integer.parseInt(query.replaceAll(".*receita=(\\d+).*", "$1"));
        }

        String sql = "SELECT c.idcomentario, c.receita_idreceita, c.usuario_idusuario, " +
                "c.comentario, c.nota, c.datacomentario, u.nome AS autor " +
                "FROM comentario c " +
                "JOIN usuario u ON u.idusuario = c.usuario_idusuario";

        if (filtroReceita != null) {
            sql += " WHERE c.receita_idreceita = ?";
        }

        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(sql);

            if (filtroReceita != null) {
                stmt.setInt(1, filtroReceita);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                if (!first) {
                    json.append(",");
                }

                json.append(String.format(
                        "{\"idComentario\": %d, \"idReceita\": %d, \"idUsuario\": %d, " +
                                "\"autor\": \"%s\", \"comentario\": \"%s\", \"nota\": %d, " +
                                "\"dataComentario\": \"%s\"}",
                        rs.getInt("idcomentario"),
                        rs.getInt("receita_idreceita"),
                        rs.getInt("usuario_idusuario"),
                        rs.getString("autor"),
                        rs.getString("comentario").replace("\"", "\\\""),
                        rs.getInt("nota"),
                        rs.getTimestamp("datacomentario").toLocalDateTime()));

                first = false;
            }

            DAO.closeConnection();

        } catch (Exception e) {
            System.out.println("Erro ao buscar comentários: " + e.getMessage());
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
        Exemplo de JSON
        
        {
        "receita_idreceita": 1,
        "usuario_idusuario": 1,
        "comentario": "Muito boa receita",
        "nota": 10
        }
        
        */

        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        String idReceita = body.replaceAll("(?s).*\"receita_idreceita\"\\s*:\\s*(?:\"([^\"]+)\"|(\\d+)).*", "$1$2");
        String idUsuario = body.replaceAll("(?s).*\"usuario_idusuario\"\\s*:\\s*(?:\"([^\"]+)\"|(\\d+)).*", "$1$2");
        String comentario = body.replaceAll("(?s).*\"comentario\"\\s*:\\s*(?:\"([^\"]+)\"|(\\d+)).*", "$1$2");
        String nota = body.replaceAll("(?s).*\"nota\"\\s*:\\s*(?:\"([^\"]+)\"|(\\d+)).*", "$1$2");

        LocalDateTime dataComentario = LocalDateTime.now();

        Usuario usuario = Usuario.getUsuario(Integer.parseInt(idUsuario));

        if (usuario == null) {
            String response = "{\"error\": \"Usuário não encontrado\"}";
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(404, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
            return;
        }

        Receita receita = usuario.getReceita(Integer.parseInt(idReceita));

        if (receita == null) {
            String response = "{\"error\": \"Receita não encontrada\"}";
            byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(404, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
            return;
        }

        new Comentario(
                receita.getIdReceita(),
                usuario.getId(),
                comentario,
                Integer.parseInt(nota),
                dataComentario);

        String response = "{\"message\": \"Comentário adicionada com sucesso\"}";
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(201, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
