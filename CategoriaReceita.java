import java.util.ArrayList;

public class CategoriaReceita {
    Categoria categoria;
    Receita receita;

    public static ArrayList<CategoriaReceita> categoriaReceita = new ArrayList<>();

    public CategoriaReceita(Categoria categoria, Receita receita) {
        this.categoria = categoria;
        this.receita = receita;
    }
}
