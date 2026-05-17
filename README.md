#  Sistema de Telecomunicações Distribuído (Java RMI com Protocolo de Requisição-Resposta)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![RMI](https://img.shields.io/badge/RMI-Remote__Method__Invocation-blue?style=for-the-badge)
![JSON](https://img.shields.io/badge/Format-JSON_Bytes-orange?style=for-the-badge)

Este repositório contém a implementação do **Trabalho 2** da disciplina de **Sistemas Distribuídos** do curso de Engenharia de Computação da **Universidade Federal do Ceará (UFC) - Campus Quixadá**, sob orientação do **Professor Rafael Braga**.

O objetivo deste projeto é reimplementar o sistema de atendimento de telecomunicações do Trabalho 1 utilizando o paradigma de **Invocação Remota de Método (RMI)** como meio de transporte, mas organizando a comunicação de forma explícita através de um **Protocolo de Requisição-Resposta** baseado na Seção 5.2 do livro-texto da disciplina.

**Regra de Ouro do Projeto:** É terminantemente proibido o uso de Sockets nativos (`java.net.Socket` ou `DatagramSocket`). Toda a comunicação trafega sobre a infraestrutura do RMI, porém de forma opaca (trafegando arrays de bytes manuais).

---

##  Arquitetura e Estrutura de Objetos

Para atender integralmente aos requisitos de modelagem orientada a objetos e persistência local exigidos no enunciado, o sistema foi estruturado da seguinte forma:

### 1. Entidades de Domínio (Mapeamento)
* **Classes Tipo Entidade (5):** `Linha`, `Servico`, `SigaMe`, `Secretaria` e `ClienteEmpresa`.
* **Composição do tipo Extensão ("É-Um" / Herança) [Mínimo 2]:** 
  * `SigaMe` estende (`extends`) `Servico`.
  * `Secretaria` estende (`extends`) `Servico`.
* **Composição do tipo Agregação ("Tem-Um") [Mínimo 2]:** 
  * `Linha` possui uma lista agregada de serviços contratados (`List<Servico>`).
  * `ClienteEmpresa` possui uma lista agregada de linhas corporativas (`List<Linha>`).

### 2. Semântica de Passagem de Parâmetros
* **Passagem por Referência:** Utilizada no objeto remoto central (`GatewayImpl`), que estende `UnicastRemoteObject` e implementa a interface `GatewayRMI`.
* **Passagem por Valor:** Utilizada na execução dos objetos locais no servidor (`ReclamacaoServiceImpl`). Os argumentos e resultados são encapsulados em formato de **Representação Externa de Dados (EDR)** usando **JSON estruturado em arrays de bytes (`byte[]`)**, garantindo que o estado viaje por cópia completa.

---

##  O Protocolo de Requisição-Resposta (Seção 5.2)

Em vez de expor métodos de negócio transparentes via RMI (o que violaria o propósito de entender a camada de middleware), a interface remota expõe um único método despachante genérico (**Gateway/Dispatcher Pattern**):

```java
public interface GatewayRMI extends java.rmi.Remote {
    byte[] doOperation(byte[] mensagemEmpacotada) throws java.rmi.RemoteException;
}
```
## Fluxo de Execução Simplificado

1. **ClienteRMI**: O usuário interage com o menu CLI. Ao escolher "Registrar Reclamação", os dados de entrada são convertidos em uma string JSON local.  


2. **Empacotamento**: O cliente monta o objeto de protocolo contendo ``messageType = 0`` , gera um ``requestId`` sequencial, define o ``methodId`` e injeta os argumentos. Tudo isso é convertido em bytes (``.getBytes()``).  


3. **Transporte RMI**: O cliente invoca ``gateway.doOperation(bytes)``. O RMI encapsula esses bytes e envia ao Servidor.  


4. **Despacho (ServidorRMI)**: O ``GatewayImpl`` intercepta os bytes recebidos via ``getRequest()`` , faz o desempacotamento, lê o cabeçalho, descobre o método solicitado e repassa os parâmetros por valor para a instância local de ``ReclamacaoServiceImpl``.  


5. **Retorno (Reply)**: O resultado gerado pela lógica local ("Reclamação registrada com sucesso...") é envelopado em um novo pacote com ``messageType = 1`` , convertido em bytes e devolvido como retorno do método RMI através do ``sendReply()``. O cliente desempacota e exibe na tela.

## Como Compilar e Executar

Como o servidor inicia o registro de nomes dinamicamente via código (``LocateRegistry.createRegistry``), não é necessário rodar o utilitário ´´rmiregistry´´ do sistema operacional por fora.

**1. Clonar o Repositório**
```
git clone [https://github.com/Biaald/TelecomRMI.git](https://github.com/Biaald/TelecomRMI.git)
cd TelecomRMI
```

**2. Compilar os Arquivos Java**

Abra o terminal na pasta do projeto e execute:
```
javac *.java
```

**3. Iniciar o ServidorRMI (Gateway)**

Execute o servidor central para abrir a porta de escuta RMI:
```
java ServidorRMI
```

Mensagem esperada no console: ``--- Gateway RMI Pronto ---``

**4. Iniciar o ClienteRMI**

Abra uma nova janela ou aba do terminal e execute a interface do cliente:

```
java ClienteRMI
```

