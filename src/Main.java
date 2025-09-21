import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final List<Jogador> jogadoresDisponiveis = new ArrayList<>();
    private static final Matchmaking matchmaking = new Matchmaking();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        exibirMenuPrincipal();
    }

    private static void exibirMenuPrincipal() {
        int opcao = -1;
        while (opcao != 0) {
            System.out.print("""
                **************************************************
                Bem-vindo ao Sistema de Matchmaking

                1. Adicionar jogador
                2. Listar jogadores disponíveis
                3. Criar partida (Modo de Jogo fixo)
                4. Simular partida (adicionar vitorias/derrotas)
                0. Sair

                Escolha uma opção:
                **************************************************
            """);

            try {
                opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        adicionarJogador();
                        break;
                    case 2:
                        listarJogadores();
                        break;
                    case 3:
                        criarPartida();
                        break;
                    case 4:
                        simularPartida();
                        break;
                    case 0:
                        System.out.println("Saindo do programa. Até mais!");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
            }
        }
    }

    private static void adicionarJogador() {
        int quantidade = 0;
        try {
            System.out.print("Quantos jogadores você quer adicionar: ");
            quantidade = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.nextLine();
            return;
        }

        for (int i = 0; i < quantidade; i++) {
            System.out.println("Adicionando jogador " + (i + 1) + " de " + quantidade + ":");
            System.out.print("Digite o nome do jogador: ");
            String nome = scanner.nextLine();

            jogadoresDisponiveis.add(new Jogador(nome));
            System.out.println("Jogador " + nome + " adicionado com sucesso!");
        }
    }

    private static void listarJogadores() {
        if (jogadoresDisponiveis.isEmpty()) {
            System.out.println("Nenhum jogador cadastrado.");
        } else {
            System.out.println("\n--- Jogadores Disponíveis ---");
            System.out.printf("%-4s%-20s%-10s%-15s%n", "ID", "Nome", "Vitórias", "Partidas");
            for (int i = 0; i < jogadoresDisponiveis.size(); i++) {
                Jogador j = jogadoresDisponiveis.get(i);
                System.out.printf("%-4d%-20s%-10d%-15d%n", (i + 1), j.getNome(), j.getNumeroDeVitorias(), j.getPartidasJogadas());
            }
        }
    }

    private static void criarPartida() {
        System.out.println("\n--- Criar Partida ---");
        System.out.print("Digite o número de jogadores por equipe (ex: 2 para duplas, 5 para 5v5): ");
        try {
            int jogadoresPorEquipe = scanner.nextInt();
            scanner.nextLine();

            if (jogadoresPorEquipe <= 0) {
                System.out.println("Número de jogadores por equipe deve ser maior que zero.");
                return;
            }

            ModoDeJogo modo = new ModoDeJogo("Personalizado " + jogadoresPorEquipe + "v" + jogadoresPorEquipe, jogadoresPorEquipe);
            matchmaking.criarPartidaBalanceada(jogadoresDisponiveis, modo);
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.nextLine();
        }
    }

    private static void simularPartida() {
        System.out.println("\n--- Simular Partida ---");
        System.out.print("Digite o número de jogadores por equipe para a partida: ");
        try {
            int jogadoresPorEquipe = scanner.nextInt();
            scanner.nextLine();
            ModoDeJogo modo = new ModoDeJogo("Simulação " + jogadoresPorEquipe + "v" + jogadoresPorEquipe, jogadoresPorEquipe);

            // 1. Cria a partida e armazena as equipes
            List<Equipe> equipesFormadas = matchmaking.criarPartidaBalanceada(jogadoresDisponiveis, modo);

            if (equipesFormadas == null) {
                System.out.println("Não foi possível simular a partida.");
                return;
            }

            // 2. Exibe as equipes para o usuário escolher a vencedora
            System.out.println("\nEquipes Formadas:");
            for (int i = 0; i < equipesFormadas.size(); i++) {
                System.out.printf("Equipe %d: ", (i + 1));
                equipesFormadas.get(i).exibirMembros();
            }

            // 3. Pede para o usuário escolher a equipe vencedora
            System.out.print("Digite o número da equipe vencedora: ");
            int indiceEquipeVencedora = scanner.nextInt() - 1;
            scanner.nextLine();

            if (indiceEquipeVencedora >= 0 && indiceEquipeVencedora < equipesFormadas.size()) {
                Equipe equipeVencedora = equipesFormadas.get(indiceEquipeVencedora);
                System.out.println("\nVitória registrada para a Equipe " + (indiceEquipeVencedora + 1) + "!");

                // 4. Registra a vitória para todos os membros da equipe
                for (Jogador jogador : equipeVencedora.getMembros()) {
                    jogador.adicionarVitoria();
                }

                // 5. Registra a derrota para os membros das outras equipes
                for (int i = 0; i < equipesFormadas.size(); i++) {
                    if (i != indiceEquipeVencedora) {
                        Equipe equipePerdedora = equipesFormadas.get(i);
                        for (Jogador jogador : equipePerdedora.getMembros()) {
                            jogador.adicionarDerrota();
                        }
                    }
                }
                System.out.println("Derrotas registradas para as outras equipes.");

            } else {
                System.out.println("Número de equipe inválido.");
            }

        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            scanner.nextLine();
        }
    }
}