package e1.i3.e1i3.service.transaction.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import e1.i3.e1i3.domain.transaction.Transaction;
import e1.i3.e1i3.dto.tnxs.CalendarResDTO;
import e1.i3.e1i3.dto.tnxs.DailyTnxs;
import e1.i3.e1i3.dto.tnxs.Diagram;
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
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static e1.i3.e1i3.util.EthereumTimestampConverter.convertToDateTime;
import static e1.i3.e1i3.util.createAuth.createToken;
import static e1.i3.e1i3.util.gasToEtherConverter.convertGasToEther;
import static e1.i3.e1i3.util.TnxMethod.getTnxMethod;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    @Value("${ethereum-NODE-ID}")
    String etherNode;

    @Value("${ethereum-Key-ID}")
    String etherKey;

    @Value("${ethereum-Key-Secret}")
    String etherSecret;

    @Value("${polygon-NODE-ID}")
    String polygonNode;

    @Value("${polygon-Key-ID}")
    String polygonKey;

    @Value("${polygon-Key-Secret}")
    String polygonSecret;

    String Token;

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
    public void saveRecentTransactionList(String userAddress,String protocol,String network) throws IOException, InterruptedException {


        if(protocol.equals("ethereum")) {
            Token = createToken(etherNode,etherKey,etherSecret);
        }
        else if(protocol.equals("polygon")){
            Token = createToken(polygonNode,polygonKey,polygonSecret);
        }

        try {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("https://web3.luniverse.io/v1/"+protocol+"/"+network+"/accounts/"+userAddress+"/transactions?relation=from&status=success&rpp=50")
                    .get()
                    .addHeader("accept", "application/json")
                    .addHeader("Authorization", "Bearer " + Token)
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
                        String tnxMethod = getTnxMethod(transactionHash,protocol);
                        Optional<Transaction> existingTransaction = transactionRepository.findByTransactionHash(transactionHash);

                        if (existingTransaction.isEmpty()) {
                            Transaction transaction = new Transaction();
                            transaction.setGasUsed(convertGasToEther(gasUsed));
                            transaction.setTransactionHash(transactionHash);
                            transaction.setTimeStamp(convertToDateTime(timestamp.asLong()));
                            transaction.setUserAddress(userAddress);
                            transaction.setMethod(tnxMethod);
                            transaction.setProtocol(protocol);
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
    public void saveRecentTransaction(Object eventData) throws IOException {

        //webhook으로 받은 트랜잭션 정보를 db에 저장

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.valueToTree(eventData);

        JsonNode eventNode = rootNode.get("event");
        String protocol = rootNode.get("protocol").asText();
        JsonNode messageNode = eventNode.get("message");
        String userAddress = eventNode.get("targetAddress").asText();

        if (messageNode != null) {
            String transactionHash = messageNode.get("hash").asText();
            String gasUsed = messageNode.get("receipt_gas_used").asText();
            Long timestamp = messageNode.get("block_timestamp").asLong();
            String tnxMethod = getTnxMethod(transactionHash,protocol);


            Optional<Transaction> existingTransaction = transactionRepository.findByTransactionHash(transactionHash);

            if (existingTransaction.isEmpty()) {
                Transaction transaction = new Transaction();
                transaction.setGasUsed(convertGasToEther(gasUsed));
                transaction.setTransactionHash(transactionHash);
                transaction.setTimeStamp(convertToDateTime(timestamp));
                transaction.setUserAddress(userAddress);
                transaction.setMethod(tnxMethod);
                transaction.setProtocol(protocol.toLowerCase());
                transactionRepository.save(transaction);
            }
        }

    }

    @Override
    public List<DailyTnxs> getDailyTransactionList(String userAddress, LocalDate date) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.atTime(23, 59, 59);

        List<Transaction> transactions = transactionRepository.findByUserAddressAndTimeStampBetween(userAddress, startDateTime, endDateTime);
        List<DailyTnxs> dailyTransactions = new ArrayList<>();

        DecimalFormat decimalFormat = new DecimalFormat("#.#######"); // 소수점 이하 7자리까지 표시

        for (Transaction transaction : transactions) {
            DailyTnxs dailyTnxs = new DailyTnxs();
            dailyTnxs.setTransactionHash(transaction.getTransactionHash());
            dailyTnxs.setMethod(transaction.getMethod());
            dailyTnxs.setTimeStamp(transaction.getTimeStamp());
            dailyTnxs.setProtocol(transaction.getProtocol());
            dailyTnxs.setGasUsed(decimalFormat.format(transaction.getGasUsed()));
            dailyTransactions.add(dailyTnxs);
        }

        return dailyTransactions;
    }

    @Override
    public List<CalendarResDTO> getDailyGasfeeInCalendarView(String userAddress) {
        List<Transaction> transactions = transactionRepository.findByUserAddress(userAddress);
        Map<LocalDate, Double> dailyGasFees = new HashMap<>();

        for (Transaction transaction : transactions) {
            LocalDateTime timeStamp = transaction.getTimeStamp();
            LocalDate transactionDate = timeStamp.toLocalDate();

            double gasUsed = transaction.getGasUsed();
            dailyGasFees.merge(transactionDate, gasUsed, Double::sum);
        }

        List<CalendarResDTO> calendarResDTOs = new ArrayList<>();
        for (Map.Entry<LocalDate, Double> entry : dailyGasFees.entrySet()) {
            LocalDate entryDate = entry.getKey();
            double totalGasFee = entry.getValue();

            DecimalFormat decimalFormat = new DecimalFormat("#.#######"); // 소수점 이하 7자리까지 표시
            String formattedTotalGasFee = decimalFormat.format(totalGasFee);

            CalendarResDTO calendarResDTO = new CalendarResDTO();
            calendarResDTO.setDate(entryDate);
            calendarResDTO.setTitle(formattedTotalGasFee);
            calendarResDTOs.add(calendarResDTO);
        }

        return calendarResDTOs;
    }

    @Override
    public Diagram getDiagram(String userAddress, LocalDate date) {

        // 원형통계에 들어갈 데이터 가공

        Diagram diagram = new Diagram();

        diagram.setTransfer(transactionRepository.getCountTransactionsMethod(userAddress, "Transfer",date));
        diagram.setApprove(transactionRepository.getCountTransactionsMethod(userAddress, "Approve",date));
        diagram.setEntry(transactionRepository.getCountTransactionsMethod(userAddress,"Entry",date));
        diagram.setSwap(transactionRepository.getCountTransactionsMethod(userAddress, "Swap",date));
        diagram.setWithdraw(transactionRepository.getCountTransactionsMethod(userAddress, "Withdraw",date));
        diagram.setDefi(transactionRepository.getCountTransactionsMethod(userAddress,"Defi",date));
        diagram.setNft(transactionRepository.getCountTransactionsMethod(userAddress,"Nft",date));
        diagram.setEtc(transactionRepository.getCountTransactionsMethod(userAddress, "etc",date));

        return diagram;
    }
}
