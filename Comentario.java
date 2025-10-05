public class Comentario {
    private static int contador = 1;

    Integer idComentario;
    Receita receita;
    Usuario usuario;
    String comentario;
    Integer nota;
    String dataComentario;
    
    public Comentario(Receita receita, Usuario usuario, String comentario, Integer nota, String dataComentario) {
        this.idComentario = contador++;
        this.receita = receita;
        this.usuario = usuario;
        this.comentario = comentario;
        this.nota = nota;
        this.dataComentario = dataComentario;
    }
}
