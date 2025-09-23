import java.util.ArrayList;
import java.util.Scanner;

public class Menu2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        ArrayList<Categoria> categorias = new ArrayList<>();

        try {

            int escolha = 0;

            while (escolha != 9) {

                System.out.printf("+-----------------------------------+\n");
                System.out.printf("|          Quase tudo gostoso       |\n");
                System.out.printf("+-----------------------------------+\n");
                System.out.printf("|  [1] Adicionar categoria          |\n");
                System.out.printf("|  [2] Listar categorias            |\n");
                System.out.printf("+-----------------------------------+\n");
                System.out.printf("|  [3] Adicionar comentário         |\n");
                System.out.printf("|  [4] Listar comentários           |\n");
                System.out.printf("+-----------------------------------+\n");
                System.out.printf("|  [5] Adicionar usuário            |\n");
                System.out.printf("|  [6] Listar usuário               |\n");
                System.out.printf("+-----------------------------------+\n");
                System.out.printf("|  [7] Adicionar receita            |\n");
                System.out.printf("|  [8] Listar receitas              |\n");
                System.out.printf("+-----------------------------------+\n");
                System.out.printf("|  [9] Encerrar programa            |\n");
                System.out.printf("+-----------------------------------+\n");

                escolha = scanner.nextInt();

                switch (escolha) {
                    case 1:

                        System.out.printf("Digite a descrição da cateogira: ");
                        String nomeCategoria = scanner.next();
                        System.out.printf("Digite o status da cateogira: ");
                        Integer ativo = scanner.nextInt();

                        Categoria categoria = new Categoria(nomeCategoria, ativo);
                        categorias.add(categoria);

                        break;

                    case 2:

                        for (Categoria c : categorias) {
                            System.out.printf("Descrição da categoria: %s\n", c.categoria);
                            System.out.printf("Status da categoria: %s\n", (c.ativo == 1) ? "Ativo" : "Inativo");
                        }

                        break;
                    case 5:

                        System.out.printf("Digite o nome do usuario: ");
                        String nomeUsuario = scanner.next();
                        System.out.printf("Digite o email do usuario: ");
                        Integer email = scanner.nextInt();
                        System.out.printf("Digite a data de nascimento do usuario: ");
                        Integer dataNascimento = scanner.nextInt();
                        System.out.printf("Digite o cep do usuario: ");
                        Integer cep = scanner.nextInt();
                        System.out.printf("Digite o genero do usuario: ");
                        Integer genero = scanner.nextInt();
                        System.out.printf("Digite a senha do usuario: ");
                        Integer senha = scanner.nextInt();
                        System.out.printf("Digite o salt do usuario: ");
                        Integer salt = scanner.nextInt();
                        System.out.printf("Digite a data de iscrição do usuario: ");
                        Integer inscrito = scanner.nextInt();
                        System.out.printf("Digite o uuid do usuario: ");
                        Integer uuid = scanner.nextInt();

                        Usuario usuario = new Usuario(nomeUsuario, email, dataNascimento, cep, genero, senha, salt, inscrito, uuid);
                        usuarios.add(usuario);

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
}
