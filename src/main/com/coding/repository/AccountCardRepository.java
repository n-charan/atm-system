package main.java.com.coding.repository;

import com.coding.config.ATMConfig;
import com.coding.model.AccountCard;

import java.util.List;
import java.util.Optional;

public class AccountCardRepository {

    private List<AccountCard> accountCards = ATMConfig.getAccountCardList();

    public AccountCard getAccountCardByCardNumber(String cardNumber) {
        AccountCard selectedAccountCard = null;
        Optional<AccountCard> accountCardOptional = accountCards.stream()
                .filter(accountCard -> accountCard.getCardNumber().equals(cardNumber))
                .findAny();
        if (accountCardOptional.isPresent()) {
            selectedAccountCard = accountCardOptional.get();
        }
        return selectedAccountCard;
    }

    public void save(AccountCard accountCard) {
        for (int i = 0; i < accountCards.size(); i++) {
            if (accountCards.get(i).getId().equals(accountCard.getId())) {
                accountCards.set(i, accountCard);
            }
        }
    }
}
