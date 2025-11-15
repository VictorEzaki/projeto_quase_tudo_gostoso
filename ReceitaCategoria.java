public class ReceitaCategoria {
    private Integer idReceita;
    private Integer idCategoria;

    public ReceitaCategoria(Integer idReceita, Integer idCategoria) {
        this.idReceita = idReceita;
        this.idCategoria = idCategoria;
    }

    public Integer getIdReceita() {
        return idReceita;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setReceita(Integer idReceita) {
        this.idReceita = idReceita;
    }

    public void satCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    @Override
    public String toString() {
        return "\n--- Receita x Categoria ---" +
                "\nID da Receita: " + this.getIdReceita() +
                "\nID da Categoria: " + this.getIdCategoria() +
                "\n---------------------------";
    }
}
