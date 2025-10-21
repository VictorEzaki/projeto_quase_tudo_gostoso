import java.util.ArrayList;
import java.util.List;
import java.util.ArrayList;

// Importações para o servidor HTTP
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Categoria implements HttpHandler {
    private static int contador = 1;

    private Integer id;
    private String categoria;
    private Boolean ativo;

    public static ArrayList<Categoria> categorias = new ArrayList<>();
    public List<ReceitaCategoria> receitaCategorias = new ArrayList<>();

    public Categoria(){}

    public Categoria(String categoria, Boolean ativo) {
        this.id = contador++;
        this.categoria = categoria;
        this.ativo = ativo;

        categorias.add(this);
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

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }
    
    public void setAtivo(Boolean ativo) {
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
        for (int i = 0; i < categorias.size(); i++) {
            Categoria c = categorias.get(i);
            json.append(String.format("{\"id\": \"%s\",\"categoria\": \"%s\",\"ativo\": \"%s\"}",
                    c.getId(), c.getCategoria(), c.getAtivo()));
            if (i < categorias.size() - 1) json.append(",");
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

        // Parse simples (sem Gson)
        // Exemplo de entrada: {"nome":"Italiana", "ativo":"true"}
        String nome = body.replaceAll(".*\"categoria\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String sAtivo = body.replaceAll(".*\"ativo\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        boolean ativo = sAtivo.toLowerCase().equals("true");
    
        new Categoria(nome, ativo);

        String response = "{\"message\": \"Categoria adicionada com sucesso\"}";
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(201, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
