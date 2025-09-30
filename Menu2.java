import java.util.ArrayList;
import java.util.List;
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
                     criarUsuario(scanner);
                        break;
                    
                    case 6:
                    ArrayList<Usuario> usuario = Usuario.listarUsuarios();

                        for (Usuario c : usuario) {
                            System.out.printf("Nome: %s\n", c.nome);
                            System.out.printf("Email: %s\n", c.email);
                            System.out.printf("Data de nascimento: %s\n", c.data_nascimento);
                            System.out.printf("Genero: %s\n", (c.genero == 1) ? "masculino" : "feminino");
                            System.out.printf("Senha: %s\n", c.senha);
                            System.out.printf("Salt: %s\n", c.salt);
                            System.out.printf("Data de inscrição: %s\n", c.inscrito);
                            System.out.printf("UUID: %s\n", c.uuid);
                        }
                        break;
                        
                    case 9:
                        System.out.println("Encerrando...");
                        System.exit(0);
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
     public static void criarUsuario(Scanner scanner) {

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

                        Usuario usuario = new Usuario(nomeUsuario, email, dataNascimento, cep, genero, senha, salt, inscrito, uuid);


    }
}
