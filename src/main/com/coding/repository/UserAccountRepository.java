package main.java.com.coding.repository;

import com.coding.config.ATMConfig;
import com.coding.model.UserAccount;

import java.util.List;
import java.util.Optional;

public class UserAccountRepository {

    List<UserAccount> userAccountList = ATMConfig.getUserAccountList();

    public UserAccount getById(Long userAccountId) {
        Optional<UserAccount> userAccountOptional = userAccountList.stream().filter(userAccount -> userAccount.getId().equals(userAccountId)).findFirst();
        if (userAccountOptional.isPresent()) {
            return userAccountOptional.get();
        }
        return null;
    }

    public UserAccount save(UserAccount userAccount) {
        for (int i = 0; i < userAccountList.size(); i++) {
            if (userAccountList.get(i).getId().equals(userAccount.getId())) {
                userAccountList.set(i, userAccount);
            }
        }
        return userAccount;
    }
}
