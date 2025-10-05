import java.util.ArrayList;
import java.util.List;

public class Categoria {
    private static int contador = 1;

    Integer id;
    String categoria;
    Integer ativo;

    public static ArrayList<Categoria> categorias = new ArrayList<>();
    public List<ReceitaCategoria> receitaCategorias = new ArrayList<>();

    public Categoria(String categoria, Integer ativo) {
        this.id = contador++;
        this.categoria = categoria;
        this.ativo = ativo;

        categorias.add(this);
    }

    public static ArrayList<Categoria> listarCategorias() {
        return categorias;
    }
}
