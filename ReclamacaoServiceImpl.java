import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

// Serviço 1: Agora é uma classe LOCAL normal, sem dependências de RMI!
public class ReclamacaoServiceImpl {
    
    // Banco de dados simulado em memória (Protocolo -> Dados da Reclamação)
    private Map<String, String> chamadosAbertos;

    // Construtor normal, sem lançar RemoteException
    public ReclamacaoServiceImpl() {
        this.chamadosAbertos = new HashMap<>();
    }

    // Método de negócio puro, chamado localmente pelo Gateway
    public String registrarReclamacao(String numeroLinha, String motivo) {
        System.out.println("[INFO] Lógica Local executando: Recebendo reclamação para a linha " + numeroLinha);
        System.out.println("[DETALHE] Motivo: " + motivo);

        // Gera um número de protocolo único (8 caracteres em maiúsculo)
        String protocolo = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        
        // Salva no "banco de dados" em memória
        chamadosAbertos.put(protocolo, "Linha: " + numeroLinha + " - Motivo: " + motivo + " | Status: EM ANÁLISE");

        System.out.println("[SUCESSO] Protocolo " + protocolo + " gerado e salvo no mapa.\n");
        return "Reclamação registrada com sucesso! Seu protocolo é: " + protocolo;
    }

    // Método de negócio puro
    public String consultarStatusReclamacao(String protocolo) {
        System.out.println("[INFO] Lógica Local executando: Consultando status do protocolo " + protocolo);
        return chamadosAbertos.getOrDefault(protocolo, "Protocolo não encontrado no sistema.");
    }
}