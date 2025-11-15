public class Comentario {
    private Integer idComentario;
    private Integer idReceita;
    private Integer idUsuario;
    private String comentario;
    private Integer nota;
    private String dataComentario;

    public Comentario() {
    }

    public Comentario(int idComentario, int idReceita, int idUsuario, String comentario, Integer nota,
            String dataComentario) {
        this.idComentario = idComentario;
        this.idReceita = idReceita;
        this.idUsuario = idUsuario;
        this.comentario = comentario;
        this.nota = nota;
        this.dataComentario = dataComentario;
    }

    public Integer getIdComentario() {
        return this.idComentario;
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

    public Integer getIdReceita() {
        return this.idReceita;
    }

    public Integer getIdUsuario() {
        return this.idUsuario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public void setDataComentario(String dataComentario) {
        this.dataComentario = dataComentario;
    }

    public void setIdReceita(int idReceita) {
        this.idReceita = idReceita;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
