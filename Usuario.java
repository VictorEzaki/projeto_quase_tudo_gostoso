import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Usuario {
    private static int contador = 1;

    Integer idUsuario;
    String nome;
    String email;
    String data_nascimento;
    Integer cep;
    Integer genero;
    String senha;
    String salt;
    String inscrito;
    String uuid;

    public static List<Receita> receitas = new ArrayList<>();
    public List<Comentario> comentarios = new ArrayList<>();

    public Usuario(String nome, String email, String data_nascimento, Integer cep, Integer genero,
            String senha, String inscrito) {

        this.idUsuario = contador++;
        this.nome = nome;
        this.email = email;
        this.data_nascimento = data_nascimento;
        this.cep = cep;
        this.genero = genero;
        this.senha = senha;
        this.salt = "salt_random";
        this.inscrito = inscrito;
        this.uuid = UUID.randomUUID().toString();
        this.receitas = new ArrayList<>();
        this.comentarios = new ArrayList<>();
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