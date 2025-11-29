import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Usuario implements HttpHandler {
    private Integer idUsuario;
    private String nome;
    private String email;
    private LocalDate data_nascimento;
    private Integer cep;
    private Integer genero;
    private String senha;
    private String salt;
    private LocalDateTime inscrito;
    private String uuid;

    private List<Comentario> comentarios = new ArrayList<>();
    private ArrayList<Receita> receitas = new ArrayList<>();

    private static ArrayList<Usuario> usuarios = new ArrayList<>();

    public Usuario() {

    }

    public Usuario(String nome, String email, LocalDate data_nascimento, Integer cep, Integer genero,
            String senha, LocalDateTime inscrito) {

        this.nome = nome;
        this.email = email;
        this.data_nascimento = data_nascimento;
        this.cep = cep;
        this.genero = genero;
        this.senha = senha;
        this.inscrito = inscrito;
        this.uuid = UUID.randomUUID().toString();
        this.salt = "10";

        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "INSERT INTO usuario (nome, email, data_nascimento, cep, genero, senha, salt, inscrito, uuid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            stmt.setString(1, this.getNome());
            stmt.setString(2, this.getEmail());
            stmt.setDate(3, java.sql.Date.valueOf(this.getDataNascimento()));
            stmt.setInt(4, this.getCep());
            stmt.setInt(5, this.getGenero());
            stmt.setString(6, this.getSenha());
            stmt.setString(7, this.getSalt());
            stmt.setTimestamp(8, java.sql.Timestamp.valueOf(this.getDataInscricao()));
            stmt.setString(9, this.getUuid());

            stmt.execute();
            DAO.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public Usuario(int idUsuario, String nome, String email, LocalDate data_nascimento, Integer cep, Integer genero,
            String senha, String salt, LocalDateTime inscrito, String uuid) {

        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.data_nascimento = data_nascimento;
        this.cep = cep;
        this.genero = genero;
        this.senha = senha;
        this.salt = salt;
        this.inscrito = inscrito;
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

    public void setDataNascimento(LocalDate data_nascimento) {
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

    public void setDataInscricao(LocalDateTime inscrito) {
        this.inscrito = inscrito;
    }

    public void setUuid(String uuid) {
        this.uuid = UUID.randomUUID().toString();
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

    public LocalDate getDataNascimento() {
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

    public LocalDateTime getDataInscricao() {
        return this.inscrito;
    }

    public String getUuid() {
        return this.uuid;
    }

    public static ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public static Usuario getUsuario(int id) {
        try {
            PreparedStatement stmt = DAO.createConnection()
                    .prepareStatement("SELECT * FROM usuario WHERE idusuario = ?");

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Usuario u = new Usuario(
                        rs.getInt("idusuario"),
                        rs.getString("nome"),
                        rs.getString("email"),
                        rs.getDate("data_nascimento").toLocalDate(),
                        rs.getInt("cep"),
                        rs.getInt("genero"),
                        rs.getString("senha"),
                        rs.getString("salt"),
                        rs.getTimestamp("inscrito").toLocalDateTime(),
                        rs.getString("uuid"));
                return u;
            }
            return null;

        } catch (Exception e) {
            System.out.println("Erro ao consultar usuário: " + e.getMessage());
            return null;
        }
    }

    public Receita getReceita(int id) {
        try {
            PreparedStatement stmt = DAO.createConnection()
                    .prepareStatement("SELECT * FROM receita WHERE idReceita = ?");

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                ArrayList<Integer> categoriasIds = new ArrayList<>();

                PreparedStatement stmtCat = DAO.createConnection()
                        .prepareStatement(
                                "SELECT categoria_idcategoria FROM categoria_receita WHERE receita_idreceita = ?");
                stmtCat.setInt(1, id);
                ResultSet rsCat = stmtCat.executeQuery();

                while (rsCat.next()) {
                    categoriasIds.add(rsCat.getInt("categoria_idcategoria"));
                }

                Receita r = new Receita(
                        rs.getInt("idreceita"),
                        rs.getString("titulo"),
                        rs.getString("descricao"),
                        rs.getString("imagem"),
                        rs.getInt("cadastro_idusuario"),
                        categoriasIds);

                return r;
            }

            return null;

        } catch (Exception e) {
            System.out.println("Erro ao consultar receita: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
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
        String query = "SELECT idUsuario, nome, email, data_nascimento, cep, genero, senha, salt, inscrito, uuid FROM usuario";

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
                        .append("}");
            }

            DAO.closeConnection();

        } catch (Exception e) {
            System.out.println("ERRO NO GET USUARIO: " + e);
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
         * exemplo de requisição
         * 
         * {
         * "nome": "{{$randomFirstName}}",
         * "email": "{{$randomEmail}}",
         * "dataNascimento": "2003-03-19",
         * "cep": "90124170",
         * "genero": "1",
         * "senha": "{{$randomPassword}}"
         * }
         * 
         */

        InputStream is = exchange.getRequestBody();
        String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        String nome = body.replaceAll("(?s).*\"nome\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String email = body.replaceAll("(?s).*\"email\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        LocalDate dataNascimento = LocalDate
                .parse(body.replaceAll("(?s).*\"dataNascimento\"\\s*:\\s*\"([^\"]+)\".*", "$1"));
        String cep = body.replaceAll("(?s).*\"cep\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String genero = body.replaceAll("(?s).*\"genero\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String senha = body.replaceAll("(?s).*\"senha\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        LocalDateTime dataInscricao = LocalDateTime.now();

        new Usuario(
                nome,
                email,
                dataNascimento,
                Integer.parseInt(cep),
                Integer.parseInt(genero),
                senha,
                dataInscricao);

        String response = "{\"message\": \"Usuário adicionado com sucesso\"}";
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(201, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void handlePut(HttpExchange exchange) throws IOException {
        /*
         * exemplo de requisição
         * 
         * URL: http://localhost:8089/usuario?id=1
         * 
         * {
         * "nome": "{{$randomFirstName}}",
         * "email": "{{$randomEmail}}",
         * "dataNascimento": "2003-03-19",
         * "cep": "90124170",
         * "genero": "1",
         * "senha": "{{$randomPassword}}"
         * }
         * 
         */

        String query = exchange.getRequestURI().getQuery();

        if (query == null || !query.contains("id=")) {
            String response = "{\"error\": \"ID do usuário não informado\"}";
            exchange.sendResponseHeaders(400, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
            return;
        }

        int id = Integer.parseInt(query.replaceAll(".*id=(\\d+).*", "$1"));

        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

        String nome = body.replaceAll("(?s).*\"nome\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String email = body.replaceAll("(?s).*\"email\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        LocalDate dataNascimento = LocalDate
                .parse(body.replaceAll("(?s).*\"dataNascimento\"\\s*:\\s*\"([^\"]+)\".*", "$1"));
        String cep = body.replaceAll("(?s).*\"cep\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String genero = body.replaceAll("(?s).*\"genero\"\\s*:\\s*\"([^\"]+)\".*", "$1");
        String senha = body.replaceAll("(?s).*\"senha\"\\s*:\\s*\"([^\"]+)\".*", "$1");

        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "UPDATE usuario SET nome = ?, email = ?, data_nascimento = ?, cep = ?, genero = ?, senha = ? WHERE idusuario = ?");

            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.setDate(3, java.sql.Date.valueOf(dataNascimento));
            stmt.setInt(4, Integer.parseInt(cep));
            stmt.setInt(5, Integer.parseInt(genero));
            stmt.setString(6, senha);
            stmt.setInt(7, id);

            int rows = stmt.executeUpdate();
            DAO.closeConnection();

            if (rows == 0) {
                String response = "{\"error\": \"Usuário não encontrado\"}";
                exchange.sendResponseHeaders(404, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            String response = "{\"message\": \"Usuário atualizada com sucesso\"}";
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();

        } catch (Exception e) {
            e.printStackTrace();
            String response = "{\"error\": \"Erro ao atualizar usuário\"}";
            exchange.sendResponseHeaders(500, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }

    private void handleDelete(HttpExchange exchange) throws IOException {
        /*
         * exemplo de requisição
         * 
         * URL: http://localhost:8089/usuario?id=1
         * 
         */

        String query = exchange.getRequestURI().getQuery();

        if (query == null || !query.contains("id=")) {
            String response = "{\"error\": \"ID do usuário não informado\"}";
            exchange.sendResponseHeaders(400, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
            return;
        }

        int id = Integer.parseInt(query.replaceAll(".*id=(\\d+).*", "$1"));

        try {
            PreparedStatement stmt = DAO.createConnection().prepareStatement(
                    "DELETE FROM usuario WHERE idusuario = ?");

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            DAO.closeConnection();

            if (rows == 0) {
                String response = "{\"error\": \"Usuário não encontrado\"}";
                exchange.sendResponseHeaders(404, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());
                exchange.getResponseBody().close();
                return;
            }

            String response = "{\"message\": \"Usuário removido com sucesso\"}";
            exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();

        } catch (java.sql.SQLIntegrityConstraintViolationException e) {
            String response = "{\"error\": \"Não é possível deletar: usuário está associado a uma receita\"}";
            exchange.sendResponseHeaders(409, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();

        } catch (Exception e) {
            e.printStackTrace();
            String response = "{\"error\": \"Erro ao excluir usuário\"}";
            exchange.sendResponseHeaders(500, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
        }
    }
}