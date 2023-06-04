package e1.i3.e1i3.util;

import okhttp3.*;

public class createWebhook {
    static String protocolurl;

    public static void createWebhook(String userAddress,String protocol, String Token) {

        if(protocol.equals("ethereum")) {
            protocolurl = "ethereum/sepolia";
        }
        else if(protocol.equals("polygon")) {
            protocolurl = "polygon/mumbai";
        }

        try {
            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create("{\"notification\":{\"webhookUrl\":\"http://52.78.33.217:8080/api/webhook\"},\"eventType\":\"SUCCESSFUL_TRANSACTION\",\"condition\":{\"addresses\":[\""+userAddress+"\"]}}",mediaType);
            Request request = new Request.Builder()
                    .url("https://web3.luniverse.io/v1/"+protocolurl+"/webhooks")
                    .post(body)
                    .addHeader("accept", "application/json")
                    .addHeader("content-type", "application/json")
                    .addHeader("Authorization", "Bearer " + Token)
                    .build();

            Response response = client.newCall(request).execute();
            
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
