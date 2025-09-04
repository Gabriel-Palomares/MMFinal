import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Matchmaking sera responsável por criar as equipes de forma balanceada.
// Recebe um objeto ModoDeJogo para saber as regras do jogo.
public class Matchmaking {

    private boolean validarJogadores(List<Jogador> jogadoresDisponiveis, int jogadoresPorEquipe) {
        if (jogadoresDisponiveis == null || jogadoresDisponiveis.size() < (jogadoresPorEquipe * 2)) {
            System.out.println("Número insuficiente de jogadores para formar pelo menos duas equipes.");
            return false;
        }
        if (jogadoresDisponiveis.size() % jogadoresPorEquipe != 0) {
            System.out.println("Número de jogadores incompatível com o tamanho da equipe.");
            return false;
        }
        return true;
    }

    public List<Equipe> criarPartidaBalanceada(List<Jogador> jogadoresDisponiveis, ModoDeJogo modoDeJogo) {
        int jogadoresPorEquipe = modoDeJogo.getJogadoresPorEquipe();

        if (!validarJogadores(jogadoresDisponiveis, jogadoresPorEquipe)) {
            return null;
        }

        List<Jogador> jogadoresExperientes = new ArrayList<>();
        List<Jogador> jogadoresNovatos = new ArrayList<>();

        // 1. Separar jogadores experientes de novatos
        for (Jogador jogador : jogadoresDisponiveis) {
            if (jogador.getPartidasJogadas() > 0) {
                jogadoresExperientes.add(jogador);
            } else {
                jogadoresNovatos.add(jogador);
            }
        }

        // 2. Ordenar os jogadores experientes por percentual de vitórias (do maior para o menor)
        Collections.sort(jogadoresExperientes, Comparator.comparingDouble(Jogador::getPercentualDeVitorias).reversed());

        int numeroDeEquipes = jogadoresDisponiveis.size() / jogadoresPorEquipe;
        List<Equipe> equipes = new ArrayList<>();
        for (int i = 0; i < numeroDeEquipes; i++) {
            equipes.add(new Equipe());
        }

        // 3. Distribuir jogadores experientes de forma balanceada
        int equipeIndex = 0;
        for (Jogador jogador : jogadoresExperientes) {
            equipes.get(equipeIndex).adicionarMembro(jogador);
            equipeIndex = (equipeIndex + 1) % numeroDeEquipes;
        }

        // 4. Distribuir jogadores novatos de forma aleatória e equilibrada
        Collections.shuffle(jogadoresNovatos);
        equipeIndex = 0; // Reinicia o índice
        for (Jogador jogador : jogadoresNovatos) {
            equipes.get(equipeIndex).adicionarMembro(jogador);
            equipeIndex = (equipeIndex + 1) % numeroDeEquipes;
        }

        System.out.println("Partida balanceada criada para o modo '" + modoDeJogo.getNome() + "'!");
        System.out.println("Total de equipes formadas: " + equipes.size());
        for (Equipe equipe : equipes) {
            equipe.exibirMembros();
        }

        return equipes;
    }
}
