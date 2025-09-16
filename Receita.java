import java.util.ArrayList;

public class Receita {
    Integer id_receita;
    String titulo;
    String descricao;
    String imagem;
    Usuario usuario;

    public static ArrayList<Receita> receitas = new ArrayList<>();

    public Receita(Integer idReceita, String titulo, String descricao, String imagem, Usuario usuario) {
        this.id_receita = idReceita;
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagem = imagem;
        this.usuario = usuario;

        receitas.add(this);
    }
}
