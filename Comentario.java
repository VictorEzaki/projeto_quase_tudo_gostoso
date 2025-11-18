import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Comentario implements HttpHandler{
    private Integer idComentario;
    private Integer idReceita;
    private Integer idUsuario;
    private String comentario;
    private Integer nota;
    private String dataComentario;

    public Comentario() {

    }

    public Comentario(int idReceita, int idUsuario, String comentario, Integer nota,
            String dataComentario) {
        this.idReceita = idReceita;
        this.idUsuario = idUsuario;
        this.comentario = comentario;
        this.nota = nota;
        this.dataComentario = dataComentario;
    }

    public Comentario(int idComentario, int idReceita, int idUsuario, String comentario, Integer nota,
            String dataComentario) {
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

    public String getDataComentario() {
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

    public void setDataComentario(String dataComentario) {
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
        // StringBuilder json = new StringBuilder("[");
        // boolean first = true;

        // int idReceita = 1;

        // for (Comentario c : Receita.listarComentariosPorReceita(idReceita)) {
        //     if (!first)
        //         json.append(",");
        //     json.append(String.format(
        //             "{\"idComentario\": \"%d\", \"idReceita\": \"%d\", \"autor_comentario\": \"%s\", \"comentario\": \"%s\", \"nota\": \"%s\", \"data_comentario\": \"%s\"}",
        //             c.getIdComentario(), c.receita.getIdReceita(), c.usuario.getNome(), c.getComentario(), c.getNota(),
        //             c.dataComentario));
        //     first = false;
        // }

        // json.append("]");

        // byte[] bytes = json.toString().getBytes(StandardCharsets.UTF_8);
        // exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        // exchange.sendResponseHeaders(200, bytes.length);
        // try (OutputStream os = exchange.getResponseBody()) {
        //     os.write(bytes);
        // }
    }

    private void handlePost(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        String idReceita = body.replaceAll("(?s).*\"idReceita\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String idUsuario = body.replaceAll("(?s).*\"idUsuario\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String comentario = body.replaceAll("(?s).*\"comentario\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String nota = body.replaceAll("(?s).*\"nota\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String dataComentario = LocalDate.now().toString();

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

        String response = "{\"message\": \"Receita adicionada com sucesso\"}";
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(201, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
