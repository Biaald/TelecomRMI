import java.io.Serializable;

public class MensagemProtocolo implements Serializable {
    public int messageType;      // 0 = Request, 1 = Reply
    public int requestId;        // ID sequencial da requisição
    public String objectReference; // Nome do serviço (ex: "ServicoReclamacao")
    public String methodId;      // Nome do método (ex: "registrarReclamacao")
    public String arguments;     // Dados reais em formato JSON
}
