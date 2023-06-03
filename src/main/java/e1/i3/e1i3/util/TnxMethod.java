package e1.i3.e1i3.util;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.Transaction;

import java.io.IOException;

public class TnxMethod {
    static Web3j web3 ;


    //트랜잭션 methodID를 통해 type 분류
    public static String getTnxMethod(String tnxHash, String protocol) throws IOException {

        if(protocol.equalsIgnoreCase("ethereum")) {
            web3 = Web3j.build(new HttpService("https://ethereum-sepolia.luniverse.io/1685726982179085997"));
        }
        else if(protocol.equalsIgnoreCase("polygon")) {
            web3 = Web3j.build(new HttpService("https://polygon-mumbai.luniverse.io/1684984631913669590"));
        }

        Transaction transaction = web3.ethGetTransactionByHash(tnxHash).send().getTransaction().get();
        String inputData = transaction.getInput();

        if(inputData.length()<10) { return "etc";}

        String methodId = inputData.substring(0, 10);



        switch (methodId) {
            case "0xa9059cbb" : methodId = "Transfer"; break;
            case "0x095ea7b3" : methodId = "Approve"; break;
            case "0x9848a6b3" : methodId = "Swap"; break;
            case "0x18cbafe5" : methodId = "Swap"; break;
            case "0x3ccfd60b" : methodId = "Withdraw"; break;
            case "0x67f239dd" : methodId = "Entry"; break;
            case "0x33bb7f91" : methodId = "Defi"; break;
            case "0xb60d4288" : methodId = "Defi"; break;
            case "0x1593a8c7" : methodId = "Defi"; break;
            case "0x160344e2" : methodId = "Defi"; break;
            case "0x1b9265b8" : methodId = "Defi"; break;
            case "0x0d730acc" : methodId = "NFT"; break;
            default: methodId = "etc";
        }

        return methodId;
    }


}
