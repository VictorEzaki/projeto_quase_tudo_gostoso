import java.util.ArrayList;

public class Categoria {
    Integer id_categoria;
    String categoria;
    Integer ativo;

    public static ArrayList<Categoria> categorias = new ArrayList<>();

    public Categoria(Integer idCategoria, String categoria, Integer ativo) {
        this.id_categoria = idCategoria;
        this.categoria = categoria;
        this.ativo = ativo;

        categorias.add(this);
    }
}
