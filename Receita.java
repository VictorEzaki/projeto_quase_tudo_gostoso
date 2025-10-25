import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class Receita implements HttpHandler {
    private static int contador = 1;

    private Integer idReceita;
    private String titulo;
    private String descricao;
    private String imagem;
    private Integer idUsuario;
    // private Usuario usuario;

    public List<ReceitaCategoria> receitaCategorias = new ArrayList<>();
    public List<Comentario> comentarios = new ArrayList<>();

    private static List<Receita> receitas = new ArrayList<>();

    public Receita() {
    }

    public Receita(String titulo, String descricao, String imagem, int idUsuario) {
        this.idReceita = contador++;
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagem = imagem;
        this.idUsuario = idUsuario;

        receitas.add(this);
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

    public void adicionarCategoria(Categoria categoria) {
        ReceitaCategoria associacao = new ReceitaCategoria(this, categoria);
        receitaCategorias.add(associacao);
        categoria.receitaCategorias.add(associacao);
    }

    public void listarCategoriasDaReceita() {
        if (receitaCategorias.isEmpty()) {
            System.out.println("Essa receita não tem categorias.");
            return;
        }

        System.out.println("Categorias:");
        for (ReceitaCategoria rc : receitaCategorias) {
            System.out.println(" - " + rc.categoria.getCategoria());
        }
    }

    public void adicionarComentario(Comentario comentario) {
        comentarios.add(comentario);
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
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < receitas.size(); i++) {
            Receita r = receitas.get(i);
            json.append(String.format(
                    "{\"idReceita\": \"%d\", \"titulo\": \"%s\", \"descricao\": \"%s\", \"imagem\": \"%s\", \"idUsuario\": \"%d\"}",
                    r.getIdReceita(), r.getTitulo(), r.getDescricao(), r.getImagem(), r.getIdUsuario()));
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
        String idUsuario = body.replaceAll("(?s).*\"idUsuario\"\\s*:\\s*\"([^\"]+)\".*", "$1");

        new Receita(
            titulo,
            descricao,
            imagem,
            Integer.parseInt(idUsuario)
        );

        String response = "{\"message\": \"Receita adicionada com sucesso\"}";
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(201, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    @Override
    public String toString() {
        return "" +
                "Id Receita: " + this.getIdReceita() +
                " | Título: " + this.getTitulo() +
                " | Descrição: " + this.getDescricao() +
                " | Imagem: " + this.getImagem() +
                " | Usuário: " + (this.idUsuario);

        // ReceitaCategoria e comentário não foram refatoradas ainda
        // " | Categorias: " + (this.receitaCategorias.isEmpty() ? "Nenhuma"
        // : this.receitaCategorias.stream()
        // .map(rc -> rc.getCategoria().getCategoria())
        // .reduce((a, b) -> a + ", " + b)
        // .orElse(""))
        // +
        // " | Comentários: "
        // + (this.comentarios.isEmpty() ? "Nenhum" : this.comentarios.size() + "
        // comentário(s)");
    }

}
