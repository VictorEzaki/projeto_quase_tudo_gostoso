
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Usuario {
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

    public Usuario(Integer idUsuario, String nome, String email, String data_nascimento, Integer cep, Integer genero,
            String senha, String salt, String inscrito, String uuid) {

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
        this.receitas = new ArrayList<>();
    }

    public List<Receita> listarReceitas() {
        return receitas;
    }

    public void adicionarReceita(Receita receita) {
        receitas.add(receita);
    }
}