import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static ArrayList<Usuario> usuarios = new ArrayList<>();
    public static ArrayList<Receita> receitas = new ArrayList<>();
    public static ArrayList<Categoria> categorias = new ArrayList<>();
    public static ArrayList<Comentario> comentarios = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {

            int escolha = 0;

            while (escolha != 9) {

                System.out.printf("+-----------------------------------+\n");
                System.out.printf("|          Quase tudo gostoso       |\n");
                System.out.printf("+-----------------------------------+\n");
                System.out.printf("|                                   |\n");
                System.out.printf("|  [1] Adicionar categoria          |\n");
                System.out.printf("|  [2] Listar categorias            |\n");
                System.out.printf("|                                   |\n");
                System.out.printf("|-----------------------------------|\n");
                System.out.printf("|                                   |\n");
                System.out.printf("|  [3] Adicionar comentário         |\n");
                System.out.printf("|  [4] Listar comentários           |\n");
                System.out.printf("|                                   |\n");
                System.out.printf("|-----------------------------------|\n");
                System.out.printf("|                                   |\n");
                System.out.printf("|  [5] Adicionar usuário            |\n");
                System.out.printf("|  [6] Listar usuário               |\n");
                System.out.printf("|                                   |\n");
                System.out.printf("|-----------------------------------|\n");
                System.out.printf("|                                   |\n");
                System.out.printf("|  [7] Adicionar receita            |\n");
                System.out.printf("|  [8] Listar receitas              |\n");
                System.out.printf("|                                   |\n");
                System.out.printf("|-----------------------------------|\n");
                System.out.printf("|  [9] Encerrar programa            |\n");
                System.out.printf("+-----------------------------------+\n");

                escolha = Integer.parseInt(scanner.nextLine());

                switch (escolha) {
                    case 1:

                        criarCategoria(scanner);

                        break;

                    case 2:

                        for (Categoria c : categorias) {
                            System.out.printf("Descrição da categoria: %s\n", c.categoria);
                            System.out.printf("Status da categoria: %s\n", (c.ativo == 1) ? "Ativo" : "Inativo");
                        }

                        break;

                    case 3:

                        criarComentario(scanner);

                        break;

                    case 4:

                        for (Comentario c : comentarios) {
                            System.out.printf("Receita: %s\n", c.receita.descricao);
                            System.out.printf("Autor da receita: %s\n", c.receita.usuario.nome);
                            System.out.printf("Usuário do comentário: %s\n", c.usuario.nome);
                            System.out.printf("Comentário: %s\n", c.comentario);
                            System.out.printf("Data do Comentário: %s\n", c.dataComentario);
                        }

                        break;

                    case 5:

                        criarUsuario(scanner);

                        break;

                    case 6:

                        for (Usuario u : usuarios) {
                            System.out.printf("Nome: %s\n", u.nome);
                            System.out.printf("Data de nascimento: %s\n", u.data_nascimento);
                            System.out.printf("Gênero: %s\n", (u.genero == 1) ? "Masculino" : "Feminino");
                            System.out.printf("Data de inscrição: %s\n", u.inscrito);
                        }

                        break;

                    case 7:

                        criarReceita(scanner);

                        break;

                    case 8:

                        for (Receita r : receitas) {
                            System.out.printf("Título: %s\n", r.titulo);
                            System.out.printf("Descrição: %s\n", r.descricao);
                            r.listarCategoriasDaReceita();
                            System.out.printf("Imagem: %s\n", r.imagem);
                            System.out.printf("Autor: %s\n", r.usuario.nome);
                        }

                        break;

                    case 9:

                        System.out.println("Encerrando...");

                        break;

                    default:
                        break;
                }

            }

        } catch (Error e) {
            System.out.println(e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static void criarCategoria(Scanner scanner) {

        System.out.printf("Digite a descrição da cateogira: ");
        String nomeCategoria = scanner.nextLine();
        System.out.printf("Digite o status da cateogira(1 = ativo / 0 = inativo): ");
        Integer ativo = Integer.parseInt(scanner.nextLine());

        Categoria categoria = new Categoria(nomeCategoria, ativo);
        categorias.add(categoria);
    }

    public static void criarComentario(Scanner scanner) {

        if (usuarios.isEmpty()) {
            throw new Error("É necessário criar ao menos um usuário antes.");
        }

        if (receitas.isEmpty()) {
            throw new Error("É necessário criar ao menos uma receita antes.");
        }

        System.out.printf("Escolha a receita pelo ID que deseja fazer um comentário:\n");
        for (Receita r : receitas) {
            System.out.printf("[%d] - Nome da receita: %s\n", r.idReceita, r.titulo);
            System.out.printf("Autor da receita: %s\n\n", r.usuario.nome);
        }
        Integer idReceita = Integer.parseInt(scanner.nextLine());

        Receita receitaEncontrada = buscarReceitaPorId(idReceita);
        if (receitaEncontrada == null) {
            throw new Error("Receita não encontrada.");
        }

        System.out.printf("Escolha o usuário pelo ID que deseja fazer um comentário:\n");
        for (Usuario u : usuarios) {
            System.out.printf("[%d] - Nome do usuário: %s\n", u.idUsuario, u.nome);
        }
        Integer idUsuario = Integer.parseInt(scanner.nextLine());

        Usuario usuarioEncontrado = buscarUsuarioPorId(idUsuario);
        if (usuarioEncontrado == null) {
            throw new Error("Usuário não encontrado.");
        }

        System.out.printf("Digite seu comentário: ");
        String descricaoComentario = scanner.nextLine();
        System.out.printf("Digite seu nota para a receita: ");
        Integer nota = Integer.parseInt(scanner.nextLine());
        String dataComentario = LocalDate.now().toString();

        Comentario comentario = new Comentario(receitaEncontrada, usuarioEncontrado, descricaoComentario, nota, dataComentario);
        comentarios.add(comentario);

        receitaEncontrada.adicionarComentario(comentario);
        usuarioEncontrado.adicionarComentario(comentario);
    }

    public static void criarUsuario(Scanner scanner) {

        System.out.printf("Digite o nome do usuario: ");
        String nomeUsuario = scanner.nextLine();
        System.out.printf("Digite o email do usuario: ");
        String email = scanner.nextLine();
        System.out.printf("Digite a data de nascimento do usuario: ");
        String dataNascimento = scanner.nextLine();
        System.out.printf("Digite o cep do usuario: ");
        Integer cep = Integer.parseInt(scanner.nextLine());
        System.out.printf("Digite o genero do usuario(M = Masculino / F = Feminino): ");
        Integer genero = scanner.nextLine().toLowerCase().equals("m") ? 1 : 0;
        System.out.printf("Digite a senha do usuario: ");
        String senha = scanner.nextLine();
        String inscrito = LocalDate.now().toString();

        Usuario usuario = new Usuario(nomeUsuario, email, dataNascimento, cep, genero, senha, inscrito);
        usuarios.add(usuario);
    }

    public static void criarReceita(Scanner scanner) {

        if (categorias.isEmpty()) {
            throw new Error("É necessário criar ao menos uma categoria antes.");
        }

        if (usuarios.isEmpty()) {
            throw new Error("É necessário criar ao menos um usuário antes.");
        }

        System.out.printf("Digite o título da receita: ");
        String titulo = scanner.nextLine();
        System.out.printf("Digite o desrição da receita: ");
        String descricao = scanner.nextLine();
        System.out.printf("Insira uma imagem para a receita: ");
        String imagem = scanner.nextLine();

        System.out.printf("Escolha a categoria da receita pelo ID:\n");
        for (Categoria c : categorias) {
            if (c.ativo == 1) {
                System.out.printf("[%d] - %s\n", c.id, c.categoria);
            }
        }
        Integer idCategoria = Integer.parseInt(scanner.nextLine());
        Categoria categoriaEncontrada = buscarCategoriaPorId(idCategoria);

        if (categoriaEncontrada == null) {
            throw new Error("Categoria não encontrada.");
        }

        
        System.out.printf("Escolha o usuário autor da receita pelo seu ID:\n");
        for (Usuario u : usuarios) {
            System.out.printf("[%d] - %s\n", u.idUsuario, u.nome);
            
        }
        Integer idUsuario = Integer.parseInt(scanner.nextLine());
        Usuario usuarioEncontrado = buscarUsuarioPorId(idUsuario);

        if (usuarioEncontrado == null) {
            throw new Error("Usuário não encontrado.");
        }
        
        Receita receita = new Receita(titulo, descricao, imagem, usuarioEncontrado);
        
        receitas.add(receita);

        receita.adicionarCategoria(categoriaEncontrada);
        usuarioEncontrado.adicionarReceita(receita);
    }

    public static Usuario buscarUsuarioPorId(int id) {

        for (Usuario u : usuarios) {
            if (u.idUsuario == id) {
                return u;
            }
        }
        return null;
    }

    public static Categoria buscarCategoriaPorId(int id) {

        for (Categoria c : categorias) {
            if (c.id == id) {
                return c;
            }
        }
        return null;
    }

    public static Receita buscarReceitaPorId(int id) {

        for (Receita r : receitas) {
            if (r.idReceita == id) {
                return r;
            }
        }
        return null;
    }
}
