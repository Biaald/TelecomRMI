import java.util.ArrayList;
import java.util.List;

// Entidade 1
// Agregação 1: uma linha telefônica possui ("tem-um") serviços contratados.
public class Linha {
    private String numeroTelefone;
    private String titular;
    private List<Servico> servicosContratados;

    // Construtor vazio (OBRIGATÓRIO para a biblioteca JSON recriar o objeto)
    public Linha() {
        this.servicosContratados = new ArrayList<>();
    }

    public Linha(String numeroTelefone, String titular) {
        this.numeroTelefone = numeroTelefone;
        this.titular = titular;
        this.servicosContratados = new ArrayList<>();
    }

    public void adicionarServico(Servico servico) {
        this.servicosContratados.add(servico);
        servico.setAtivo(true);
    }

    // Getters e Setters Completos (O JSON precisa dos Setters)
    public List<Servico> getServicosContratados() { return servicosContratados; }
    public void setServicosContratados(List<Servico> servicosContratados) { this.servicosContratados = servicosContratados; }
    
    public String getNumeroTelefone() { return numeroTelefone; }
    public void setNumeroTelefone(String numeroTelefone) { this.numeroTelefone = numeroTelefone; }
    
    public String getTitular() { return titular; }
    public void setTitular(String titular) { this.titular = titular; }
}