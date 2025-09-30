import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static ArrayList<Usuario> usuarios = new ArrayList<>();
    public static ArrayList<Receita> receitas = new ArrayList<>();
    public static ArrayList<Categoria> categorias = new ArrayList<>();

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

                escolha = scanner.nextInt();

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

                    case 5:

                        criarUsuario(scanner);

                        break;

                    case 6:

                        for (Usuario u : usuarios) {
                            System.out.printf("Nome: s%", u.nome);
                            System.out.printf("Data de nascimento: s%", u.data_nascimento);
                            System.out.printf("Gênero: s%", u.genero);
                            System.out.printf("Data de inscrição: s%", u.inscrito);
                        }

                        break;

                    case 7:

                        criarReceita(scanner);

                        break;

                    case 8:

                        for (Receita r : receitas) {
                            System.out.printf("Título: %s\n", r.titulo);
                            System.out.printf("Descrição: %s\n", r.descricao);
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

        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            scanner.close();
        }
    }

    public static void criarCategoria(Scanner scanner) {

        System.out.printf("Digite a descrição da cateogira: ");
        String nomeCategoria = scanner.next();
        System.out.printf("Digite o status da cateogira: ");
        Integer ativo = scanner.nextInt();

        Categoria categoria = new Categoria(nomeCategoria, ativo);
        categorias.add(categoria);
    }

    public static void criarUsuario(Scanner scanner) {

        System.out.printf("Digite o ID do usuario: ");
        Integer idUsuario = scanner.nextInt();
        System.out.printf("Digite o nome do usuario: ");
        String nomeUsuario = scanner.next();
        System.out.printf("Digite o email do usuario: ");
        String email = scanner.next();
        System.out.printf("Digite a data de nascimento do usuario: ");
        String dataNascimento = scanner.next();
        System.out.printf("Digite o cep do usuario: ");
        Integer cep = scanner.nextInt();
        System.out.printf("Digite o genero do usuario: ");
        Integer genero = scanner.nextInt();
        System.out.printf("Digite a senha do usuario: ");
        String senha = scanner.next();
        System.out.printf("Digite o salt do usuario: ");
        String salt = scanner.next();
        System.out.printf("Digite a data de iscrição do usuario: ");
        String inscrito = scanner.next();
        System.out.printf("Digite o uuid do usuario: ");
        String uuid = scanner.next();

        Usuario usuario = new Usuario(idUsuario, nomeUsuario, email, dataNascimento, cep, genero, senha, salt, inscrito, uuid);
        usuarios.add(usuario);
    }

    public static void criarReceita(Scanner scanner) {

        Integer idUsuario;

        System.out.printf("\nDigite o título da receita:");
        String titulo = scanner.next();
        System.out.printf("\nDigite o desrição da receita:");
        String descricao = scanner.next();
        System.out.printf("\nInsira uma imagem para a receita:");
        String imagem = scanner.next();

        System.out.printf("Escolha o usuário autor da receita pelo seu ID:\n");
        for (Usuario u : usuarios) {
            System.out.printf("[%d] - %s\n", u.idUsuario, u.nome);
        }
        idUsuario = scanner.nextInt();
        Usuario usuarioEncontrado = buscarUsuarioPorId(idUsuario);


        Receita receita = new Receita(titulo, descricao, imagem, usuarioEncontrado);
        receitas.add(receita);
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
}
