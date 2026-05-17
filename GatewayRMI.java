import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GatewayRMI extends Remote {
    // Recebe o pacote de bytes, processa e devolve a resposta também em bytes
    byte[] doOperation(byte[] mensagemEmpacotada) throws RemoteException;
}