import java.sql.Date;
import java.util.ArrayList;

public class Usuario {
    Integer id_usuario;
    String nome;
    String email;
    Date data_nascimento;
    Integer cep;
    Integer genero;
    String senha;
    String salt;
    Date inscrito;
    String uuid;

public static ArrayList<Usuario> usuarios = new ArrayList<>();

public Usuario(Integer id_usuario, String nome, String email, Date data_nascimento, Integer cep, Integer genero, String senha, String salt, Date inscrito, String uuid){
    this.id_usuario = id_usuario;
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