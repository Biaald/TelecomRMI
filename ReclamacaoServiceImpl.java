import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public class ReclamacaoServiceImpl {
    
    private Map<String, String> chamadosAbertos;
    private Map<String, ClienteEmpresa> empresasCadastradas; 

    public ReclamacaoServiceImpl() {
        this.chamadosAbertos = new HashMap<>();
        this.empresasCadastradas = new HashMap<>();
        
        // Populando uma empresa de teste para provar a Agregação (ClienteEmpresa "tem-várias" Linhas)
        ClienteEmpresa empresaTeste = new ClienteEmpresa("11.222.333/0001-44", "Tech Corp S.A.");
        empresaTeste.adicionarLinha(new Linha("85-7777", "Diretoria"));
        empresaTeste.adicionarLinha(new Linha("85-8888", "Recepção"));
        empresasCadastradas.put(empresaTeste.getCnpj(), empresaTeste);
    }

    // Método de negócio puro, chamado localmente pelo Gateway
    public String registrarReclamacao(String numeroLinha, String motivo) {
        System.out.println("[INFO] Lógica Local executando: Recebendo reclamação para a linha " + numeroLinha);
        System.out.println("[DETALHE] Motivo: " + motivo);

        // Gera um número de protocolo único (8 caracteres em maiúsculo)
        String protocolo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        chamadosAbertos.put(protocolo, "Linha: " + numeroLinha + " - Motivo: " + motivo + " | Status: EM ANÁLISE");

        System.out.println("[SUCESSO] Protocolo " + protocolo + " gerado e salvo no mapa.\n");
        return "Reclamação registrada com sucesso! Seu protocolo é: " + protocolo;
    }

    public String consultarStatusReclamacao(String protocolo) {
        System.out.println("[INFO] Lógica Local executando: Consultando status do protocolo " + protocolo);
        return chamadosAbertos.getOrDefault(protocolo, "Protocolo não encontrado no sistema.");
    }

    public String consultarLinhasEmpresa(String cnpj) {
        System.out.println("[INFO] Lógica Local executando: Buscando empresa CNPJ " + cnpj);
        
        ClienteEmpresa emp = empresasCadastradas.get(cnpj);
        if (emp != null) {
            String resultado = "Empresa: " + emp.getNomeFantasia() + " | Linhas Contratadas: ";
            // Varre a lista de linhas agregadas
            for (Linha l : emp.getLinhasCorporativas()) {
                resultado += l.getNumeroTelefone() + " (" + l.getTitular() + "); ";
            }
            return resultado;
        }
        return "Empresa não encontrada no sistema.";
    }
}
