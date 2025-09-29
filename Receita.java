import java.util.ArrayList;

public class Receita {
    String titulo;
    String descricao;
    String imagem;
    Usuario usuario;

    public static ArrayList<Receita> receitas = new ArrayList<>();

    public Receita(String titulo, String descricao, String imagem, Usuario usuario) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagem = imagem;
        this.usuario = usuario;

        receitas.add(this);
    }
}
