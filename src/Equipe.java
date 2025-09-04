import java.util.ArrayList;
import java.util.List;


public class Equipe {
    private List<Jogador> membros;

    public Equipe() {
        this.membros = new ArrayList<>();
    }

    public List<Jogador> getMembros() {
        return membros;
    }

    public void adicionarMembro(Jogador jogador) {
        if (jogador != null) {
            this.membros.add(jogador);
        }
    }

    public double getPercentualMedioDeVitorias() {
        if (membros.isEmpty()) {
            return 0.0;
        }
        double somaPercentuais = 0.0;
        for (Jogador jogador : membros) {
            somaPercentuais += jogador.getPercentualDeVitorias();
        }
        return somaPercentuais / membros.size();
    }

    public void exibirMembros() {
        System.out.print("Membros da equipe (Percentual Médio: " + String.format("%.2f", getPercentualMedioDeVitorias()) + "): ");
        for (Jogador j : membros) {
            System.out.print(j.getNome() + " (" + String.format("%.2f", j.getPercentualDeVitorias()) + ") ");
        }
        System.out.println();
    }
}
