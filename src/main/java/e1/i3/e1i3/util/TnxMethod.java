package e1.i3.e1i3.util;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.Transaction;

import java.io.IOException;

public class TnxMethod {
    static Web3j web3 = Web3j.build(new HttpService("https://ethereum-mainnet.luniverse.io/1685606211622472931")); // 로컬 이더리움 노드의 RPC 엔드포인트를 사용


    public static String getTnxMethod(String tnxHash) throws IOException {

        Transaction transaction = web3.ethGetTransactionByHash(tnxHash).send().getTransaction().get();
        String inputData = transaction.getInput();

        String methodId = inputData.substring(0, 10);

        switch (methodId) {
            case "0xa9059cbb" : methodId = "Transfer"; break;
            case "0x095ea7b3" : methodId = "Approve"; break;
            case "0x3593564c" : methodId = "Execute"; break;
            case "0x18cbafe5" : methodId = "Swap"; break;
            case "0x2e1a7d4d" : methodId = "Withdraw"; break;
            case "0xd0e30db0" : methodId = "Deposit"; break;
            default: methodId = "etc";
        }

        return methodId;
    }


}
