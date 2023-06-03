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

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${polygonAuthToken}")
    String polygonAuthToken;

    @Value("${ethereumAuthToken}")
    String ethereumAuthToken;

    @Autowired
    private TransactionService transactionService;

    private final UserRepository userRepository;
    @Override
    public void login(String userAddress) {



        userRepository.findByAddress(userAddress)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setAddress(userAddress);
                    // Perform any other necessary initialization for the new user
                    userRepository.save(newUser);

                    //사용자의 트랜잭션을 luniverse api 호출을 통해 db에 저장합니다.
                    try {
                        transactionService.saveRecentTransactionList(userAddress,"ethereum","sepolia");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        transactionService.saveRecentTransactionList(userAddress,"polygon","mumbai");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    createWebhook(userAddress,"ethereum",ethereumAuthToken);
                    createWebhook(userAddress,"polygon", polygonAuthToken);

                    return newUser; // 수정: User 객체를 반환하도록 수정
                });

    }
}
