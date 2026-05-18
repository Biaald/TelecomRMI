import java.util.ArrayList;
import java.util.List;

// Entidade 2
// Agregação 2: um cliente corporativo possui ("tem-um") várias linhas telefônicas.
public class ClienteEmpresa {
    private String cnpj;
    private String nomeFantasia;
    private List<Linha> linhasCorporativas;

    public ClienteEmpresa() {
        this.linhasCorporativas = new ArrayList<>();
    }

    public ClienteEmpresa(String cnpj, String nomeFantasia) {
        this.cnpj = cnpj;
        this.nomeFantasia = nomeFantasia;
        this.linhasCorporativas = new ArrayList<>();
    }

    public void adicionarLinha(Linha linha) {
        this.linhasCorporativas.add(linha);
    }

    // Getters e Setters
    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }

    public List<Linha> getLinhasCorporativas() { return linhasCorporativas; }
    public void setLinhasCorporativas(List<Linha> linhasCorporativas) { this.linhasCorporativas = linhasCorporativas; }
}