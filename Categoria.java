import java.util.ArrayList;

public class Categoria {
    String categoria;
    Integer ativo;

    public static ArrayList<Categoria> categorias = new ArrayList<>();

    public Categoria(String categoria, Integer ativo) {
        this.categoria = categoria;
        this.ativo = ativo;

        categorias.add(this);
    }

    public static ArrayList<Categoria> listarCategorias() {
        return categorias;
    }
}
