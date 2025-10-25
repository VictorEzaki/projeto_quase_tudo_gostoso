import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

public class Usuario implements HttpHandler {
    private static int contador = 1;

    private Integer idUsuario;
    private String nome;
    private String email;
    private String data_nascimento;
    private Integer cep;
    private Integer genero;
    private String senha;
    private String salt;
    private String inscrito;
    private String uuid;
    private Integer ativo;

    public static List<Receita> receitas = new ArrayList<>();
    public List<Comentario> comentarios = new ArrayList<>();
    private static ArrayList<Usuario> usuarios = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(String nome, String email, String data_nascimento, Integer cep, Integer genero,
            String senha, String inscrito, Integer ativo) {
        this.idUsuario = contador++;
        this.nome = nome;
        this.email = email;
        this.data_nascimento = data_nascimento;
        this.cep = cep;
        this.genero = genero;
        this.senha = senha;
        this.salt = "salt_random";
        this.inscrito = inscrito;
        this.ativo = ativo;
        this.uuid = UUID.randomUUID().toString();
        // this.receitas = new ArrayList<>();
        // this.comentarios = new ArrayList<>();

        usuarios.add(this);
    }

    public void setId(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDataNascimento(String data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public void setCep(int cep) {
        this.cep = cep;
    }

    public void setGenero(Integer genero) {
        this.genero = genero;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setSalt(int salt) {
        this.salt = "salt_random";
    }

    public void setDataInscricao(String inscrito) {
        this.inscrito = inscrito;
    }

    public void setUuid(String uuid) {
        this.uuid = UUID.randomUUID().toString();
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }

    public int getId() {
        return this.idUsuario;
    }

    public String getNome() {
        return this.nome;
    }

    public String getEmail() {
        return this.email;
    }

    public String getDataNascimento() {
        return this.data_nascimento;
    }

    public int getCep() {
        return this.cep;
    }

    public Integer getGenero() {
        return this.genero;
    }

    public String getSenha() {
        return this.senha;
    }

    public String getSalt() {
        return this.salt;
    }

    public String getDataInscricao() {
        return this.inscrito;
    }

    public String getUuid() {
        return this.uuid;
    }

    public Integer getAtivo() {
        return this.ativo;
    }

    @Override
    public String toString() {
        String status = (this.getAtivo() == 1) ? "Ativo" : "Inativo";
        return "" +
                "Id:" + this.getId() +
                "Nome:" + this.getNome() +
                "Email:" + this.getEmail() +
                "Data Nascimento:" + this.getDataNascimento() +
                "CEP:" + this.getCep() +
                "Gênero:" + this.getGenero() +
                "Senha:" + this.getSenha() +
                "Salt:" + this.getSalt() +
                "Data Inscrição:" + this.getDataInscricao() +
                "Status:" + status +
                "UUID:" + this.getUuid();
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
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            json.append(String.format(
                    "{\"id\": \"%d\", \"nome\": \"%s\", \"email\": \"%s\", \"cep\": \"%d\", \"genero\": \"%d\", \"senha\": \"%s\", \"dataNascimento\": \"%s\", \"salt\": \"%s\", \"dataInscricao\": \"%s\", \"uuid\": \"%s\", \"ativo\": \"%d\"}",
                    u.getId(), u.getNome(), u.getEmail(), u.getCep(), u.getGenero(), u.getSenha(),
                    u.getDataNascimento(), u.getSalt(), u.getDataInscricao(), u.getUuid(), u.getAtivo()));

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
        // Exemplo: {"nome": "Tadeu", "email": "tadeu@mail.com", "dataNascimento":
        // "01/01/1990", "cep": "89205035", "genero": "Masculino", "senha": "123456",
        // "salt": 64, "dataInscricao": "20/10/2025", "uuid": "1234-5678-90AB"}
        String nome = body.replaceAll("(?s).*\"nome\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String email = body.replaceAll("(?s).*\"email\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String dataNascimento = body.replaceAll("(?s).*\"dataNascimento\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String cep = body.replaceAll("(?s).*\"cep\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String genero = body.replaceAll("(?s).*\"genero\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String senha = body.replaceAll("(?s).*\"senha\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String dataInscricao = LocalDate.now().toString();
        String ativo = "1";

        new Usuario(nome, email, dataNascimento, Integer.parseInt(cep), Integer.parseInt(genero),
                senha, dataInscricao, Integer.parseInt(ativo));

        String response = "{\"message\": \"Usuário adicionado com sucesso\"}";
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(201, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public List<Receita> listarReceitas() {
        return receitas;
    }

    public void adicionarReceita(Receita receita) {
        receitas.add(receita);
    }

    public void adicionarComentario(Comentario comentario) {
        comentarios.add(comentario);
    }
}