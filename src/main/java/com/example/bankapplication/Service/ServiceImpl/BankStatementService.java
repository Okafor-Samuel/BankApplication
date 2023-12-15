package com.example.bankapplication.Service.ServiceImpl;

import com.example.bankapplication.Dto.EmailDto;
import com.example.bankapplication.Entity.AccountTransaction;
import com.example.bankapplication.Repository.AccountTransactionRepository;
import com.example.bankapplication.Repository.UserRepository;
import com.example.bankapplication.Service.EmailService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Data
public class BankStatementService {
    private final AccountTransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final EmailServiceImpl emailServiceImpl;
    private static final String FILE ="/Users/decagon/Documents/BankAppStatement.pdf";

    public List<AccountTransaction> generateStatement(String accountNumber, String startDate, String endDate) throws DocumentException, FileNotFoundException {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        List<AccountTransaction> transactionList = transactionRepository.findAll()
                .stream()
                .filter(accountTransaction -> accountTransaction.getAccountNumber().equals(accountNumber))
                .filter(accountTransaction -> accountTransaction.getCreatedAt().equals(start))
                .filter(accountTransaction -> accountTransaction.getCreatedAt().equals(end)).toList();

        var user = userRepository.findByAccountNumber(accountNumber);
        var targetUser = user.get();
        var customerName = targetUser.getFirstName()+" "+targetUser.getLastName()+" "+targetUser.getOtherName();
        var customerAddress = targetUser.getAddress();

        Rectangle statementSize= new Rectangle(PageSize.A4);
        Document document= new Document(statementSize);
        log.info("Setting size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        //First Column of the table
        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("SAMUEL OKAFOR BANK"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);
        PdfPCell bankAddress = new PdfPCell(new Phrase("Orchid, Lekki-Lagos, Nigeria."));
        bankAddress.setBorder(0);

        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress );

        //Second Column of the table
        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: "+ startDate)); //or Start
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("End Date: "+endDate)); // or End
        stopDate.setBorder(0);
        PdfPCell name = new PdfPCell(new Phrase("Customer Name: "+customerName));
        name.setBorder(0);
        PdfPCell space = new PdfPCell();
        space.setBorder(0);
        PdfPCell address = new PdfPCell(new Phrase("Customer Address: "+customerAddress));
        address.setBorder(0);

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(stopDate);
        statementInfo.addCell(name);
        statementInfo.addCell(space);
        statementInfo.addCell(address);

        //Transaction Table
        PdfPTable transactionTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);
        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);
        PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionAmount.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);
        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.BLUE);
        status.setBorder(0);

        transactionTable.addCell(date);
        transactionTable.addCell(transactionType);
        transactionTable.addCell(transactionAmount);
        transactionTable.addCell(status);

        //To populate all the transaction we got from the list of transaction into the transaction table
        transactionList.forEach(accountTransaction -> {
            transactionTable.addCell(new Phrase(accountTransaction.getCreatedAt().toString()));
            transactionTable.addCell(new Phrase(accountTransaction.getTransactionType()));
            transactionTable.addCell(new Phrase(accountTransaction.getTransactionAmount().toString()));
            transactionTable.addCell(new Phrase(accountTransaction.getStatus()));
        });

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionTable);

        document.close();

        EmailDto emailDto = EmailDto.builder()
                .recipient(user.get().getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Kindly find your requested account statement attached")
                .attachment(FILE)
                .build();
        emailServiceImpl.sendEmailWithAttachment(emailDto);

        return transactionList;

    }


}
