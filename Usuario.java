import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class Usuario implements HttpHandler {
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

    private List<Comentario> comentarios = new ArrayList<>();
    private ArrayList<Receita> receitas = new ArrayList<>();

    private static ArrayList<Usuario> usuarios = new ArrayList<>();

    public Usuario() {

    }

    public Usuario(String nome, String email, String data_nascimento, Integer cep, Integer genero,
            String senha, String inscrito, Integer ativo) {

        this.nome = nome;
        this.email = email;
        this.data_nascimento = data_nascimento;
        this.cep = cep;
        this.genero = genero;
        this.senha = senha;
        this.inscrito = inscrito;
        this.ativo = ativo;
        this.uuid = UUID.randomUUID().toString();

        this.salt = "10";

        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "INSERT INTO usuario (nome, email, data_nascimento, cep, genero, senha, salt, inscrito, uuid, ativo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");

            stmt.setString(1, this.getNome());
            stmt.setString(2, this.getEmail());
            stmt.setString(3, this.getDataNascimento());
            stmt.setInt(4, this.getCep());
            stmt.setInt(5, this.getGenero());
            stmt.setString(6, this.getSenha());
            stmt.setString(7, this.getSalt());
            stmt.setString(8, this.getDataInscricao());
            stmt.setString(9, this.getUuid());
            stmt.setInt(10, this.getAtivo());
            stmt.execute();

            DAO.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public Usuario(int idUsuario, String nome, String email, String data_nascimento, Integer cep, Integer genero,
            String senha, String salt, String inscrito, String uuid, Integer ativo) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.data_nascimento = data_nascimento;
        this.cep = cep;
        this.genero = genero;
        this.senha = senha;
        this.salt = salt;
        this.inscrito = inscrito;
        this.ativo = ativo;
        this.uuid = uuid;
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
        this.salt = "10";
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

    public Integer getId() {
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

    public static ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public static Usuario getUsuario(int id) {
    try {
        PreparedStatement stmt = DAO.createConnection()
            .prepareStatement("SELECT * FROM usuario WHERE idUsuario = ?");
        
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Usuario u = new Usuario(
                rs.getInt("idUsuario"),
                rs.getString("nome"),
                rs.getString("email"),
                rs.getString("data_nascimento"),
                rs.getInt("cep"),
                rs.getInt("genero"),
                rs.getString("senha"),
                rs.getString("salt"),
                rs.getString("inscrito"),
                rs.getString("uuid"),
                rs.getInt("ativo")
            );
            return u;
        }
        return null;

    } catch (Exception e) {
        System.out.println("Erro ao consultar usuário: " + e.getMessage());
        return null;
    }
}


    public Receita getReceita(int id) {
        for (Receita r : receitas) {
            if (r.getIdReceita().equals(id)) {
                return r;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String status = (this.getAtivo() == 1) ? "Ativo" : "Inativo";

        return "\n=== Usuário ==========================\n" +
                "ID:               " + this.getId() + "\n" +
                "Nome:             " + this.getNome() + "\n" +
                "Email:            " + this.getEmail() + "\n" +
                "Data Nascimento:  " + this.getDataNascimento() + "\n" +
                "CEP:              " + this.getCep() + "\n" +
                "Gênero:           " + this.getGenero() + "\n" +
                "Senha:            " + this.getSenha() + "\n" +
                "Salt:             " + this.getSalt() + "\n" +
                "Data Inscrição:   " + this.getDataInscricao() + "\n" +
                "Status:           " + status + "\n" +
                "UUID:             " + this.getUuid() + "\n" +
                "======================================\n";
    }

    public ArrayList<Receita> listarReceitas() {
        return receitas;
    }

    public void adicionarReceita(Receita receita) {
        receitas.add(receita);
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
        String query = "SELECT idUsuario, nome, email, data_nascimento, cep, genero, senha, salt, inscrito, uuid, ativo FROM usuario";

        StringBuilder json = new StringBuilder("[");
        boolean first = true;

        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(query);
            var rs = stmt.executeQuery();

            while (rs.next()) {
                if (!first) {
                    json.append(",");
                }
                first = false;

                json.append("{")
                        .append("\"id\": \"").append(rs.getInt("idUsuario")).append("\",")
                        .append("\"nome\": \"").append(rs.getString("nome")).append("\",")
                        .append("\"email\": \"").append(rs.getString("email")).append("\",")
                        .append("\"dataNascimento\": \"").append(rs.getString("data_nascimento")).append("\",")
                        .append("\"cep\": \"").append(rs.getInt("cep")).append("\",")
                        .append("\"genero\": \"").append(rs.getInt("genero")).append("\",")
                        .append("\"senha\": \"").append(rs.getString("senha")).append("\",")
                        .append("\"salt\": \"").append(rs.getString("salt")).append("\",")
                        .append("\"dataInscricao\": \"").append(rs.getString("inscrito")).append("\",")
                        .append("\"uuid\": \"").append(rs.getString("uuid")).append("\",")
                        .append("\"ativo\": \"").append(rs.getInt("ativo")).append("\"")
                        .append("}");
            }

            DAO.closeConnection();

        } catch (Exception e) {
            System.out.println("ERRO NO GET USUARIO: " + e);
        }

        json.append("]");

        // Resposta
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

        new Usuario(
                nome,
                email,
                dataNascimento,
                Integer.parseInt(cep),
                Integer.parseInt(genero),
                senha,
                dataInscricao,
                Integer.parseInt(ativo));

        String response = "{\"message\": \"Usuário adicionado com sucesso\"}";
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(201, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}