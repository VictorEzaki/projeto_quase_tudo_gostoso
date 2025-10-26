public class ReceitaCategoria {
    private Receita receita;
    private Categoria categoria;

    public ReceitaCategoria(Receita receita, Categoria categoria) {
        this.receita = receita;
        this.categoria = categoria;
    }

    public Receita getReceita() {
        return receita;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setReceita(Receita receita) {
        this.receita = receita;
    }

    public void satCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
