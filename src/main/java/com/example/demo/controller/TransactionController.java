package com.example.demo.controller;

import com.example.demo.domain.Expenditure;
import com.example.demo.domain.Income;
import com.example.demo.dto.TransactionDTO;
import com.example.demo.service.ExpenditureService;
import com.example.demo.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private ExpenditureService expenditureService;

    @Autowired
    private IncomeService incomeService;

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getTransactionsByCategory(@PathVariable String category) {
        List<TransactionDTO> transactionDTOs = new ArrayList<>();

        // 카테고리에 따른 지출 데이터 필터링
        List<Expenditure> expenditures = expenditureService.getExpendituresByCategory(category);
        for (Expenditure expenditure : expenditures) {
            transactionDTOs.add(convertToDTO(expenditure, "지출"));
        }

        // 카테고리에 따른 수입 데이터 필터링
        List<Income> incomes = incomeService.getIncomesByCategory(category);
        for (Income income : incomes) {
            transactionDTOs.add(convertToDTO(income, "수입"));
        }

        if (transactionDTOs.isEmpty()) {
            String message = "카테고리 '" + category + "'에 대한 거래 내역이 없습니다.";
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(transactionDTOs, HttpStatus.OK);
    }



    private TransactionDTO convertToDTO(Expenditure expenditure, String type) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(expenditure.getExpenditureId());
        transactionDTO.setMoney(expenditure.getMoney());
        transactionDTO.setCategory(expenditure.getCategory());
        transactionDTO.setContent(expenditure.getContent());
        transactionDTO.setCreatedAt(expenditure.getCreatedAt());
        transactionDTO.setType(type);
        return transactionDTO;
    }

    private TransactionDTO convertToDTO(Income income, String type) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(income.getIncomeId());
        transactionDTO.setMoney(income.getMoney());
        transactionDTO.setCategory(income.getCategory());
        transactionDTO.setContent(income.getContent());
        transactionDTO.setCreatedAt(income.getCreatedAt());
        transactionDTO.setType(type);
        return transactionDTO;
    }
}