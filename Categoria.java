import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class Categoria {
    private Integer id;
    private String categoria;
    private Boolean ativo;

    private static ArrayList<Categoria> categorias = new ArrayList<>();
    public List<ReceitaCategoria> receitaCategorias = new ArrayList<>();

    public Categoria(String categoria, Boolean ativo) throws Exception {
        this.categoria = categoria;
        this.ativo = ativo;
    }

    public Categoria(Integer id, String categoria, Boolean ativo) {
        this.id = id;
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

    public static Categoria getCategoriaPorID(int id) {
        for (Categoria categoria : categorias) {
            if (categoria.getId().equals(id)) {
                return categoria;
            }
        }
        return null;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Boolean getAtivo() {
        return this.ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "ID: " + this.getId() + " - Categoria: " + this.getCategoria() + " - Ativo: " + this.getAtivo();
    }
}
