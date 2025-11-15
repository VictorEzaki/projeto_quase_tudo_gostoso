import java.util.ArrayList;

public class Receita {
    private Integer idReceita;
    private String titulo;
    private String descricao;
    private String imagem;
    private Integer idUsuario;

    private ArrayList<ReceitaCategoria> receitaCategorias = new ArrayList<>();
    private static ArrayList<Comentario> comentarios = new ArrayList<>();

    public Receita(String titulo, String descricao, String imagem, int idUsuario) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagem = imagem;
        this.idUsuario = idUsuario;
    }

    public Receita(int idReceita, String titulo, String descricao, String imagem, int idUsuario) {
        this.idReceita = idReceita;
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagem = imagem;
        this.idUsuario = idUsuario;
    }

    public Integer getIdReceita() {
        return this.idReceita;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public String getImagem() {
        return this.imagem;
    }

    public Integer getIdUsuario() {
        return this.idUsuario;
    }

    public void setIdReceita(int idReceita) {
        this.idReceita = idReceita;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void adicionarComentario(Comentario comentario) {
        comentarios.add(comentario);
    }

    public static ArrayList<Comentario> listarComentarios() {
        return comentarios;
    }

    @Override
    public String toString() {
        return "\n========== Receita ==========\n" +
                "ID Receita:      " + this.getIdReceita() + "\n" +
                "Título:          " + this.getTitulo() + "\n" +
                "Descrição:       " + this.getDescricao() + "\n" +
                "Imagem:          " + this.getImagem() + "\n" +
                "ID do Autor:     " + this.getIdUsuario() + "\n" +
                "=============================\n";
    }
}
