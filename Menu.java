import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {

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

                        Categoria categoria = criarCategoria(scanner);

                        break;

                    case 2:

                        ArrayList<Categoria> categorias = Categoria.listarCategorias();

                        for (Categoria c : categorias) {
                            System.out.printf("Descrição da categoria: %s\n", c.categoria);
                            System.out.printf("Status da categoria: %s\n", (c.ativo == 1) ? "Ativo" : "Inativo");
                        }

                        break;

                    case 5:

                        Usuario usuario = criarUsuario(scanner);

                        break;

                    case 7:

                        // Receita receita = criarReceita(scanner);

                        break;

                    case 8:

                        ArrayList<Receita> receitas = Receita.listarReceitas();

                        for (Receita r : receitas) {
                            System.out.printf("Título da receita: %s\n", r.titulo);
                            System.out.printf("Descrição: %s\n", r.descricao);
                            System.out.printf("Imagem: %s\n", r.imagem);
                            System.out.printf("Autor da receita: %s\n", r.usuario.nome);
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

    public static Categoria criarCategoria(Scanner scanner) {

        System.out.printf("Digite a descrição da cateogira: ");
        String nomeCategoria = scanner.next();
        System.out.printf("Digite o status da cateogira: ");
        Integer ativo = scanner.nextInt();

        return new Categoria(nomeCategoria, ativo);

    }

    public static Usuario criarUsuario(Scanner scanner) {

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

        return new Usuario(nomeUsuario, email, dataNascimento, cep, genero, senha, salt, inscrito, uuid);

    }

    public static Receita criarReceita(Scanner scanner) {

        ArrayList<Usuario> usuarios = Usuario.listarUsuarios();
        int i = 0;

        System.out.printf("Digite o título da receita:");
        String titulo = scanner.next();
        System.out.printf("Digite o desrição da receita:");
        String descricao = scanner.next();
        System.out.printf("Insira uma imagem para a receita:");
        String imagem = scanner.next();
        System.out.printf("Escolha um id do usuário para ser autor da receita:\n");
        for (Usuario u : usuarios) {
            System.out.printf("[%d] - %s\n", u.idUsuario, u.nome);
        }
        int id = scanner.nextInt();

        for (Usuario u : usuarios) {
            if (id == u.idUsuario) {

            }
        }
        
        
        return new Receita(titulo, descricao, imagem,);
    }
}
