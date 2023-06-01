package e1.i3.e1i3.controller;

import e1.i3.e1i3.service.transaction.TransactionService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/transaction")
@CrossOrigin(origins = {"http://localhost:3000"})
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/main")
    public String someEndpoint(HttpServletRequest request) throws IOException {
        // 헤더에서 토큰 값 추출
        //String address = (Jwts.parser().setSigningKey("yourSecretKey").parseClaimsJws((request.getHeader("Authorization")).substring(7)).getBody()).getSubject();

        //사용자의 트랜잭션을 luniverse api 호출을 통해 db에 저장합니다.
        transactionService.saveRecentTransactionList("0xa41478514D57F828323E514dbf7D483646032f0A");

        //사용자의 달력에 노출될 정보를 반환합니다.

        //사용자의 월별통계에 들어갈 정보를 반환합니다.




        return "Token에 담겨있는 address 주소 :";
    }

}
