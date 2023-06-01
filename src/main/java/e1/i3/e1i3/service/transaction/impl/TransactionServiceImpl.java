package e1.i3.e1i3.service.transaction.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import e1.i3.e1i3.domain.transaction.Transaction;
import e1.i3.e1i3.dto.tnxs.DailyTnxs;
import e1.i3.e1i3.repository.transaction.TransactionRepository;
import e1.i3.e1i3.service.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static e1.i3.e1i3.util.EthereumTimestampConverter.convertToDateTime;
import static e1.i3.e1i3.util.gasToEtherConverter.convertGasToEther;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    @Value("${LuniverseAuthToken}") // 변수 파일에 등록된 java.file.test 값 가져오기
    String token;

    @Autowired
    private TransactionRepository transactionRepository;
    //[지금 한것]
    //중복제거 해서 트랜잭션 정보 db 저장

    //[남은것]
    //1.db 저장되어 있는거 dto에 담아서 반환하는 함수 만들기
    // 근데 이제 daily를 반환해야 하니까 daily를 판단할 수있게 하는 함수를 만들어서
    // 해당 값 사이에 있는 정보들만 반환 할 수 있게 코드를 짜자.
    //2.회원가입이후 정보만 저장할 수 있게끔 로직 추가.
    @Override
    public void saveRecentTransactionList(String userAddress) throws IOException {



        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://web3.luniverse.io/v1/ethereum/mainnet/accounts/"+userAddress+"/transactions")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer " + token)
                    .build();

            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(responseBody);

                String code = rootNode.get("code").asText();
                JsonNode dataNode = rootNode.get("data");
                JsonNode itemsNode = dataNode.get("items");

                List<Transaction> transactions = new ArrayList<>();

                for (JsonNode itemNode : itemsNode) {
                    JsonNode receiptNode = itemNode.get("receipt");
                    JsonNode timestamp = itemNode.get("timestamp");
                    if (receiptNode != null) {
                        String gasUsed = receiptNode.get("gasUsed").asText();
                        String transactionHash = receiptNode.get("transactionHash").asText();

                        Optional<Transaction> existingTransaction = transactionRepository.findByTransactionHash(transactionHash);

                        if (existingTransaction.isEmpty()) {
                            Transaction transaction = new Transaction();
                            transaction.setGasUsed(convertGasToEther(gasUsed));
                            transaction.setTransactionHash(transactionHash);
                            transaction.setTimeStamp(convertToDateTime(timestamp.asLong()));
                            transaction.setUserAddress(userAddress);

                            transactions.add(transaction);
                        }
                    }
                }

                transactionRepository.saveAll(transactions);

            } else {
                // 응답이 실패한 경우의 처리
                System.out.println("Request failed with code: " + response.code());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public List<DailyTnxs> getDailyTransactionList(String userAddress, LocalDate date) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.atTime(23, 59, 59);

        List<Transaction> transactions = transactionRepository.findByUserAddressAndTimeStampBetween(userAddress, startDateTime, endDateTime);
        List<DailyTnxs> dailyTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            DailyTnxs dailyTnxs = new DailyTnxs();
            dailyTnxs.setTransactionHash(transaction.getTransactionHash());
            dailyTnxs.setMethod(transaction.getMethod());
            dailyTnxs.setActivity(transaction.getActivity());
            dailyTnxs.setTimeStamp(transaction.getTimeStamp());
            dailyTnxs.setGasUsed(transaction.getGasUsed());

            dailyTransactions.add(dailyTnxs);
        }

        return dailyTransactions;
    }
}
