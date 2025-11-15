import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Usuario {
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

    public Usuario(String nome, String email, String data_nascimento, Integer cep, Integer genero,
            String senha, String salt, String inscrito, String uuid, Integer ativo) {
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
        for (Usuario u : usuarios) {
            if (u.getId().equals(id)) {
                return u;
            }
        }
        return null;
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
}