package com.example.bankapplication.Controller;

import com.example.bankapplication.Entity.AccountTransaction;
import com.example.bankapplication.Service.ServiceImpl.BankStatementService;
import com.itextpdf.text.DocumentException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/bankStatement")
public class AccountTransactionController {
    private final BankStatementService bankStatementService;
    @GetMapping
    public List<AccountTransaction> getAllTransaction(@RequestParam String accountNumber, @RequestParam String startDate, @RequestParam String endDate) throws DocumentException, FileNotFoundException {
        return bankStatementService.generateStatement(accountNumber, startDate, endDate);
    }
}
