import java.sql.Date;
import java.util.ArrayList;

public class Comentario {
    Integer id_comentario;
    Receita receita;
    Usuario usuario;
    String comentario;
    Integer nota;
    Date data_comentario;

    public static ArrayList<Comentario> comentarios = new ArrayList<>();
    
    public Comentario(Integer idComentario, Receita receita, Usuario usuario, String comentario, Integer nota, Date dataComentario) {
        this.id_comentario = idComentario;
        this.receita = receita;
        this.usuario = usuario;
        this.comentario = comentario;
        this.nota = nota;
        this.data_comentario = dataComentario;

        comentarios.add(this);
    }
}
