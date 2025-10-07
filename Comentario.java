public class Comentario {
    private static int contador = 1;

    private Integer idComentario;
    private Receita receita;
    private Usuario usuario;
    private String comentario;
    private Integer nota;
    private String dataComentario;

    public Comentario(Receita receita, Usuario usuario, String comentario, Integer nota, String dataComentario) {
        this.idComentario = contador++;
        this.receita = receita;
        this.usuario = usuario;
        this.comentario = comentario;
        this.nota = nota;
        this.dataComentario = dataComentario;
    }

    public Integer getIdComentario() {
        return this.idComentario;
    }

    public Receita getReceita() {
        return this.receita;
    }

    public Usuario getUsuario() {
        return this.usuario;
    }

    public String getComentario() {
        return this.comentario;
    }

    public Integer getNota() {
        return this.nota;
    }

    public String getDataComentario() {
        return this.dataComentario;
    }

   
    public void setComentario(String comentario) {
        this.comentario = comentario;
    } 
    public void setidReceita(Receita receita) {
        this.receita = receita;
    } 

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    } 

    public void setidComentario(Integer idComentario) {
        this.idComentario = idComentario;
    } 

    public void setidNota(Integer nota) {
        this.nota = nota;
    } 

    public void setDataComentario(String dataComentario) {
        this.dataComentario = dataComentario;
    } 


}