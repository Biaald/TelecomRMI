import java.rmi.Remote;
import java.rmi.RemoteException;

// Esta é a ÚNICA interface remota do seu sistema agora
public interface ComunicadorRMI extends Remote {
    // O professor sugeriu esta assinatura.
    // RemoteObjectRef pode ser uma String com o nome do serviço alvo.
    byte[] doOperation(String objectReference, int methodId, byte[] arguments) throws RemoteException;
}