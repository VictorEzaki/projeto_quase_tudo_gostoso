import java.util.ArrayList;
import java.util.List;

public class Receita {
    private static int contador = 1;

    Integer idReceita;
    String titulo;
    String descricao;
    String imagem;
    Usuario usuario;

    public List<ReceitaCategoria> receitaCategorias = new ArrayList<>();
    public List<Comentario> comentarios = new ArrayList<>();

    public Receita(String titulo, String descricao, String imagem, Usuario usuario) {
        this.idReceita = contador++;
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagem = imagem;
        this.usuario = usuario;
    }

    public void adicionarCategoria(Categoria categoria) {
        ReceitaCategoria associacao = new ReceitaCategoria(this, categoria);
        receitaCategorias.add(associacao);
        categoria.receitaCategorias.add(associacao);
    }

    public void listarCategoriasDaReceita() {
        if (receitaCategorias.isEmpty()) {
            System.out.println("Essa receita não tem categorias.");
            return;
        }

        System.out.println("Categorias:");
        for (ReceitaCategoria rc : receitaCategorias) {
            System.out.println(" - " + rc.categoria.getCategoria());
        }
    }
    
    public void adicionarComentario(Comentario comentario) {
        comentarios.add(comentario);
    }
}
