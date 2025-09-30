
import java.util.ArrayList;
import java.util.Random;

public class Usuario {
    String nome;
    String email;
    String data_nascimento;
    Integer cep;
    Integer genero;
    String senha;
    String salt;
    String inscrito;
    String uuid;

    public static ArrayList<Usuario> usuarios = new ArrayList<>();

    public Usuario(String nome, String email, String data_nascimento, Integer cep, Integer genero,
            String senha, String salt, String inscrito, String uuid) {

        this.nome = nome;
        this.email = email;
        this.data_nascimento = data_nascimento;
        this.cep = cep;
        this.genero = genero;
        this.senha = senha;
        this.salt = salt;
        this.inscrito = inscrito;
        this.uuid = uuid;

        usuarios.add(this);
    }

}