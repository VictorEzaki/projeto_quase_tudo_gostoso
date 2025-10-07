import java.util.ArrayList;
import java.util.List;

public class Categoria {
    private static int contador = 1;

    private Integer id;
    private String categoria;
    private Integer ativo;

    public static ArrayList<Categoria> categorias = new ArrayList<>();
    public List<ReceitaCategoria> receitaCategorias = new ArrayList<>();

    public Categoria(String categoria, Integer ativo) {
        this.id = contador++;
        this.categoria = categoria;
        this.ativo = ativo;
    }

    public static ArrayList<Categoria> listarCategorias() {
        return categorias;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoria() {
        return this.categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Integer getAtivo() {
        return this.ativo;
    }
    
    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }

    public String toString() {
        return "id: " + id +
               "\nCategoria: " + categoria +
               "\nStatus: " + ativo;
    }
}
