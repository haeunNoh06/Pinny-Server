package com.example.demo.service;

import com.example.demo.domain.Income;
import com.example.demo.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    public Income getIncomeById(Long incomeId) {
        return incomeRepository.findById(incomeId).orElse(null);
    }

    public Income saveIncome(Income income) {
        return incomeRepository.save(income);
    }

    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    public List<Income> getIncomesByCategory(String category) {
        return incomeRepository.findAll().stream()
                .filter(income -> income.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public void deleteIncome(Long incomeId) {
        incomeRepository.deleteById(incomeId);
    }

    public Income updateIncome(Long incomeId, Income updatedIncome) {
        Optional<Income> existingIncomeOpt = incomeRepository.findById(incomeId);
        if (existingIncomeOpt.isPresent()) {
            Income existingIncome = existingIncomeOpt.get();
            existingIncome.setMoney(updatedIncome.getMoney());
            existingIncome.setCategory(updatedIncome.getCategory());
            existingIncome.setContent(updatedIncome.getContent());
            return incomeRepository.save(existingIncome);
        } else {
            return null;
        }
    }
}
