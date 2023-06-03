package e1.i3.e1i3.service.user.impl;

import e1.i3.e1i3.domain.user.User;
import e1.i3.e1i3.repository.user.UserRepository;
import e1.i3.e1i3.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static e1.i3.e1i3.util.createWebhook.createWebhook;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value("${polygonAuthToken}")
    String polygonAuthToken;

    @Value("${ethereumAuthToken}")
    String ethereumAuthToken;

    private final UserRepository userRepository;
    @Override
    public void login(String userAddress) {



        User user = userRepository.findByAddress(userAddress)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setAddress(userAddress);
                    // Perform any other necessary initialization for the new user
                    userRepository.save(newUser);

                    createWebhook(userAddress,"ethereum",ethereumAuthToken);
                    createWebhook(userAddress,"polygon", polygonAuthToken);

                    return newUser; // 수정: User 객체를 반환하도록 수정
                });
    }
}
