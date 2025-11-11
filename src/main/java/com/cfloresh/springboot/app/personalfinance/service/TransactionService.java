package com.cfloresh.springboot.app.personalfinance.service;

import com.cfloresh.springboot.app.personalfinance.dto.TransactionDTO;
import com.cfloresh.springboot.app.personalfinance.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {
    private final List<Transaction> transactions = new ArrayList<>();

    public TransactionService() {
        transactions.add(new Transaction("images/avatars/emma-richardson.jpg", "Emma Richardson",
                "General", "2024-08-19T14:23:11Z", 75.5, false));

        transactions.add(new Transaction("images/avatars/savory-bites-bistro.jpg", "Savory Bites Bistro",
                "Dining Out", "2024-08-19T20:23:11Z", -55.5, false));

        transactions.add(new Transaction("images/avatars/daniel-carter.jpg", "Daniel Carter",
                "General", "2024-08-18T09:45:32Z", -42.3, false));

        transactions.add(new Transaction("images/avatars/sun-park.jpg", "Sun Park",
                "General", "2024-08-17T16:12:05Z", 120.0, false));

        transactions.add(new Transaction("images/avatars/urban-services-hub.jpg", "Urban Services Hub",
                "General", "2024-08-17T21:08:09Z", -65.0, false));

        transactions.add(new Transaction("images/avatars/liam-hughes.jpg", "Liam Hughes",
                "Groceries", "2024-08-15T18:20:33Z", 65.75, false));

        transactions.add(new Transaction("images/avatars/lily-ramirez.jpg", "Lily Ramirez",
                "General", "2024-08-14T13:05:27Z", 50.0, false));

        transactions.add(new Transaction("images/avatars/ethan-clark.jpg", "Ethan Clark",
                "Dining Out", "2024-08-13T20:15:59Z", -32.5, false));

        transactions.add(new Transaction("images/avatars/james-thompson.jpg", "James Thompson",
                "Entertainment", "2024-08-11T15:45:38Z", -5.0, false));
    }

    public List<TransactionDTO> getAllTransactions() {
        return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getAvatar(),
                transaction.getName(),
                transaction.getCategory(),
                transaction.getDate(),
                transaction.getAmount(),
                transaction.isRecurring()
        );
    }
}
