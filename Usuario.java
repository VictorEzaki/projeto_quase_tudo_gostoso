import java.sql.Date;
import java.util.ArrayList;
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

    public static ArrayList<Usuario> usuarios = new ArrayList<>();

    public Usuario(String nome, String email, String data_nascimento, Integer cep, Integer genero,
            String senha, String salt, String inscrito, String uuid) {

        Random random = new Random();

        this.idUsuario = random.nextInt(100);
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