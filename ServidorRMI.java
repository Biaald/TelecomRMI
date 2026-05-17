import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;

// O Servidor atualizado
public class ServidorRMI {
    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(1099);
            
            // Instanciamos o Gateway, não o serviço de reclamação diretamente
            GatewayImpl gateway = new GatewayImpl();
            
            Naming.rebind("rmi://localhost/GatewayTelecom", gateway);
            
            System.out.println("--- Gateway RMI (Trabalho 2) Pronto ---");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// A implementação do Gateway que faz o "Despacho"
class GatewayImpl extends UnicastRemoteObject implements GatewayRMI {
    
    // Objeto LOCAL (Sem RMI nativo nele) para processar a lógica
    private ReclamacaoServiceImpl logicaReclamacao;

    public GatewayImpl() throws RemoteException {
        super();
        this.logicaReclamacao = new ReclamacaoServiceImpl();
    }

    @Override
    public byte[] doOperation(byte[] mensagemEmpacotada) throws RemoteException {
        // 1. DESEMPACOTAMENTO (Representação Externa -> Objeto)
        String jsonRecebido = new String(mensagemEmpacotada);
        System.out.println("[Gateway] Recebido pacote: " + jsonRecebido);
        
        // Aqui você usaria uma biblioteca como Gson para converter o JSON para MensagemProtocolo
        // Por agora, vamos simular a lógica de despacho:
        
        String resultado = "";
        if (jsonRecebido.contains("registrarReclamacao")) {
            // Chama a sua lógica localmente (Passagem por valor)
            resultado = logicaReclamacao.registrarReclamacao("Simulado", "Motivo Simulado");
        }

        // 2. EMPACOTAMENTO DA RESPOSTA (Reply)
        String respostaJson = "{\"messageType\":1, \"arguments\":\"" + resultado + "\"}";
        
        return respostaJson.getBytes();
    }
}