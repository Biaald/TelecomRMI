import java.rmi.Naming;
import java.util.Scanner;

public class ClienteRMI {
    // Variável global para controlar o ID sequencial das requisições (Exigência do protocolo)
    private static int contadorRequisicao = 1;

    public static void main(String[] args) {
        Scanner leitor = new Scanner(System.in);
        
        try {
            // 1. O Cliente se conecta ao Gateway (Despachante)
            GatewayRMI gateway = (GatewayRMI) Naming.lookup("rmi://localhost/GatewayTelecom");
            
            int opcao = 0;
            while (opcao != 4) {
                System.out.println("\n--- TELECOM ATENDIMENTO ---");
                System.out.println("1. Registrar Nova Reclamação");
                System.out.println("2. Consultar Status de Protocolo");
                System.out.println("3. Consultar Linhas Corporativas (Teste de Agregação)"); 
                System.out.println("4. Sair");
                System.out.print("Escolha uma opção: ");
                
                opcao = Integer.parseInt(leitor.nextLine());

                if (opcao == 1) {
                    System.out.print("Número da Linha: ");
                    String linha = leitor.nextLine();
                    System.out.print("Descrição do Problema: ");
                    String motivo = leitor.nextLine();
                    
                    // A) Transforma os dados locais num JSON simples
                    String argumentosJson = "{\"linha\":\"" + linha + "\", \"motivo\":\"" + motivo + "\"}";
                    
                    // B) Empacota a mensagem inteira com o cabeçalho (0 = Request)
                    String pacoteJson = empacotar(0, contadorRequisicao++, "ServicoReclamacao", "registrarReclamacao", argumentosJson);
                    
                    // C) Converte pra bytes, viaja pela rede RMI, e recebe os bytes de volta
                    byte[] respostaBytes = gateway.doOperation(pacoteJson.getBytes());
                    
                    // D) Desempacota os bytes da resposta e exibe
                    String resposta = desempacotar(respostaBytes);
                    System.out.println("\n[SERVIDOR]: " + resposta);

                } else if (opcao == 2) {
                    System.out.print("Digite o número do protocolo: ");
                    String protocolo = leitor.nextLine();
                    
                    String argumentosJson = "{\"protocolo\":\"" + protocolo + "\"}";
                    
                    String pacoteJson = empacotar(0, contadorRequisicao++, "ServicoReclamacao", "consultarStatusReclamacao", argumentosJson);
                    
                    byte[] respostaBytes = gateway.doOperation(pacoteJson.getBytes());
                    
                    String resposta = desempacotar(respostaBytes);
                    System.out.println("\n[STATUS ATUAL]: " + resposta);
                    
                } else if (opcao == 3) {
                    System.out.print("Digite o CNPJ da Empresa (Dica: digite 11.222.333/0001-44): ");
                    String cnpj = leitor.nextLine();
                    
                    String argumentosJson = "{\"cnpj\":\"" + cnpj + "\"}";
                    String pacoteJson = empacotar(0, contadorRequisicao++, "ServicoReclamacao", "consultarLinhasEmpresa", argumentosJson);
                    
                    byte[] respostaBytes = gateway.doOperation(pacoteJson.getBytes());
                    
                    String resposta = desempacotar(respostaBytes);
                    System.out.println("\n[DADOS DA EMPRESA]: " + resposta);
                    
                } else if (opcao == 4) {
                    System.out.println("Saindo do sistema..."); 
                } else {
                    System.out.println("Opção inválida. Tente novamente.");
                }   
            }

        } catch (Exception e) {
            System.err.println("Erro na comunicação: " + e.getMessage());
        } finally {
            leitor.close();
        }
    }

    // MÉTODOS DE EMPACOTAMENTO (Tradução para Bytes)
    private static String empacotar(int messageType, int requestId, String objectRef, String methodId, String arguments) {
        
        String argsEscapados = arguments.replace("\"", "\\\""); 
        return "{" +
                "\"messageType\":" + messageType + "," +
                "\"requestId\":" + requestId + "," +
                "\"objectReference\":\"" + objectRef + "\"," +
                "\"methodId\":\"" + methodId + "\"," +
                "\"arguments\":\"" + argsEscapados + "\"" +
                "}";
    }

    private static String desempacotar(byte[] bytesResposta) {
        // Transforma os bytes de volta em String
        String jsonRecebido = new String(bytesResposta);
        
        // Extrai apenas o valor de "arguments" da resposta JSON
        try {
            String[] partes = jsonRecebido.split("\"arguments\":\"");
            if (partes.length > 1) {
                return partes[1].split("\"")[0];
            }
        } catch (Exception e) {
            return "Erro ao desempacotar a resposta.";
        }
        return jsonRecebido;
    }
}