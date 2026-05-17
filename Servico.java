// 1. Superclasse de Serviços 
// Dica: Implementar Serializable é uma boa prática ao trafegar dados em Sistemas Distribuídos
public abstract class Servico implements java.io.Serializable {
    private String nome;
    private double valorMensal;
    private boolean ativo;

    // Construtor Vazio OBRIGATÓRIO para a biblioteca JSON
    public Servico() {
    }

    public Servico(String nome, double valorMensal) {
        this.nome = nome;
        this.valorMensal = valorMensal;
        this.ativo = false; // Começa inativo por padrão
    }

    // Getters e Setters Completos
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; } // Novo

    public double getValorMensal() { return valorMensal; }
    public void setValorMensal(double valorMensal) { this.valorMensal = valorMensal; } // Novo

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}

// 2. Subclasses de Serviços
class SigaMe extends Servico {
    private String numeroDestino; // Número para onde a chamada será desviada

    // Construtor Vazio para o JSON
    public SigaMe() {
        super();
        this.setNome("Siga-me");
    }

    public SigaMe(double valorMensal) {
        super("Siga-me", valorMensal);
    }
    
    public String getNumeroDestino() { return numeroDestino; }
    public void setNumeroDestino(String numeroDestino) { this.numeroDestino = numeroDestino; }
}

class Secretaria extends Servico {
    private int limiteMensagens; // Limite de mensagens na caixa postal

    // Construtor Vazio para o JSON
    public Secretaria() {
        super();
        this.setNome("Secretária Eletrônica");
    }

    public Secretaria(double valorMensal, int limiteMensagens) {
        super("Secretária Eletrônica", valorMensal);
        this.limiteMensagens = limiteMensagens;
    }
    
    public int getLimiteMensagens() { return limiteMensagens; }
    public void setLimiteMensagens(int limiteMensagens) { this.limiteMensagens = limiteMensagens; } // Novo
}