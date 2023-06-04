package e1.i3.e1i3.service.user.impl;

import e1.i3.e1i3.domain.user.User;
import e1.i3.e1i3.repository.user.UserRepository;
import e1.i3.e1i3.service.transaction.TransactionService;
import e1.i3.e1i3.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static e1.i3.e1i3.util.createWebhook.createWebhook;
import static e1.i3.e1i3.util.createAuth.createToken;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

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

    @Autowired
    private TransactionService transactionService;

    private final UserRepository userRepository;

    private static final String ETHEREUM = "ethereum";
    private static final String POLYGON = "polygon";
    @Override
    public void login(String userAddress)  {

        // 사용자 지갑주소를 db와 비교하여 신규 사용자의 경우 db에 저장합니다
        userRepository.findByAddress(userAddress)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setAddress(userAddress);
                    // Perform any other necessary initialization for the new user
                    userRepository.save(newUser);


                    try {
                        //사용자의 트랜잭션을 luniverse api 호출을 통해 db에 저장합니다.
                        transactionService.saveRecentTransactionList(userAddress,ETHEREUM,"sepolia");
                        transactionService.saveRecentTransactionList(userAddress,POLYGON,"mumbai");

                        //사용자의 지갑 주소를 통해 luniverse createWebhook api를 호출하여 webhook을 생성합니다.
                        createWebhook(userAddress,ETHEREUM,createToken(etherNode,etherKey,etherSecret));
                        createWebhook(userAddress,POLYGON,createToken(polygonNode,polygonKey,polygonSecret));
                    } catch (IOException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    return newUser; // 수정: User 객체를 반환하도록 수정
                });

    }
}
