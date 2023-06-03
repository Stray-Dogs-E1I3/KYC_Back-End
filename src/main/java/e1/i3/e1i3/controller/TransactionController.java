package e1.i3.e1i3.controller;

import e1.i3.e1i3.dto.tnxs.DailyTnxs;
import e1.i3.e1i3.dto.tnxs.Diagram;
import e1.i3.e1i3.service.transaction.TransactionService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
@CrossOrigin(origins = {"http://localhost:3000"})
public class TransactionController {

    @Value("${SecretKey}")
    String secretKey;
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/main/{date}")
    public ResponseEntity<Object> getMain(HttpServletRequest request,@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) throws IOException {
        // 헤더에서 토큰 값 추출
        String userAddress = (Jwts.parser().setSigningKey(secretKey).parseClaimsJws((request.getHeader("Authorization")).substring(7)).getBody()).getSubject();

        //사용자의 트랜잭션을 luniverse api 호출을 통해 db에 저장합니다.
        transactionService.saveRecentTransactionList(userAddress,"ethereum","sepolia");
        transactionService.saveRecentTransactionList(userAddress,"polygon","mumbai");


        Map<String,Object> response=new HashMap<>();
        //사용자의 달력에 노출될 정보를 반환합니다.
        response.put("DailyGasfeeInCalendar",transactionService.getDailyGasfeeInCalendarView(userAddress,date));
        //사용자의 월별통계에 들어갈 정보를 반환합니다.
        response.put("Diagram",transactionService.getDiagram(userAddress,date));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //달력view에서 특정 일을 클릭하면 반환되는 dailyTnxsList입니다.
    @GetMapping("/daily/{date}")
    public ResponseEntity<Object> getDailyTnxsList(HttpServletRequest request,@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate date){
        //요청 헤더에서 jwt를 가져와 유저의 주소를 추출합니다.
        String userAddress = (Jwts.parser().setSigningKey(secretKey).parseClaimsJws((request.getHeader("Authorization")).substring(7)).getBody()).getSubject();

        Map<String,Object> response=new HashMap<>();
        response.put("DailyTnxsList",transactionService.getDailyTransactionList(userAddress,date));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
