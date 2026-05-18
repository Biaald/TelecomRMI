import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

// O Servidor Principal
public class ServidorRMI {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            
            // Instancia o Gateway, não o serviço de reclamação diretamente
            GatewayImpl gateway = new GatewayImpl();
            Naming.rebind("rmi://localhost/GatewayTelecom", gateway);
            
            System.out.println("--- Gateway RMI Pronto ---");
        } catch (Exception e) {
            System.err.println("Erro no Servidor: " + e.getMessage());
        }
    }
}

// A implementação do Gateway que faz o "Despacho" 
class GatewayImpl extends UnicastRemoteObject implements GatewayRMI {
    
    private ReclamacaoServiceImpl logicaReclamacao;
    public GatewayImpl() throws RemoteException {
        super();
        this.logicaReclamacao = new ReclamacaoServiceImpl();
    }

    @Override
    public byte[] doOperation(byte[] mensagemEmpacotada) throws RemoteException {
        // 1. DESEMPACOTAMENTO
        String jsonRecebido = new String(mensagemEmpacotada);
        System.out.println("\n[Gateway] Recebido pacote: " + jsonRecebido);
        
        String resultado = "";

        // 2. ROTEAMENTO (Select Object & Execute Method)
        try {
            if (jsonRecebido.contains("\"methodId\":\"registrarReclamacao\"")) {
                String linha = extrairValor(jsonRecebido, "linha");
                String motivo = extrairValor(jsonRecebido, "motivo");
                resultado = logicaReclamacao.registrarReclamacao(linha, motivo);

            } else if (jsonRecebido.contains("\"methodId\":\"consultarStatusReclamacao\"")) {
                String protocolo = extrairValor(jsonRecebido, "protocolo");
                resultado = logicaReclamacao.consultarStatusReclamacao(protocolo);
                
            } else if (jsonRecebido.contains("\"methodId\":\"consultarLinhasEmpresa\"")) {
                String cnpj = extrairValor(jsonRecebido, "cnpj");
                resultado = logicaReclamacao.consultarLinhasEmpresa(cnpj);
                
            } else {
                resultado = "Erro: Método não encontrado no Servidor.";
            }
        } catch (Exception e) {
            resultado = "Erro ao processar a requisição: " + e.getMessage();
        }

        // 3. EMPACOTAMENTO DA RESPOSTA (Reply)
        String respostaJson = "{\"messageType\":1, \"arguments\":\"" + resultado + "\"}";
        System.out.println("[Gateway] Enviando resposta: " + respostaJson);
        
        return respostaJson.getBytes();
    }

    // Método auxiliar para pegar os valores de dentro do nosso JSON em formato texto
    private String extrairValor(String json, String chave) {
        try {
            String jsonLimpo = json.replace("\\\"", "\"");            
            String[] partes = jsonLimpo.split("\"" + chave + "\":\"");
            if (partes.length > 1) {
                return partes[1].split("\"")[0];
            }
        } catch (Exception e) {
            return "Erro ao extrair valor do JSON.";
        }
        return "Desconhecido";
    }
}