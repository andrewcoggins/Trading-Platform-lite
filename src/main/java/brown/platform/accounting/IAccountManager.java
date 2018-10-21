package brown.platform.accounting;

import java.util.List;
import java.util.Map;

import brown.platform.accounting.library.Account;
import brown.platform.accounting.library.InitialEndowment;

/**
 * account manager stores agent accounts.
 * @author andrew
 *
 */
public interface IAccountManager {

  /**
   * Get all accounts from the manager, in a List
   */
  List<Account> getAccounts();
  
  /**
   * @param ID - agent's ID whose account is to be stored
   * @param account - agent's account
   */
  void setAccount(Integer ID, Account account);
  
  /**
   * gets an account from an agent's private id, if it exists
   * @param ID - agent's private id
   * @return - account, if it exists; 
   *   otherwise null (as per Java maps)
   */
  Account getAccount(Integer ID);
  
  /**
   * does the manager contain an account associated with an agent's ID? 
   * @param ID - agent's private ID
   * @return - Boolean indicating if account manager has this account
   */
  Boolean containsAcct(Integer ID);

  /**
   * ets all accounts to empty state.
   */
  void reset();

  /**
   * resets accounts to their initial endowments, ostensibly as defined in the constructor.
   */
  void reendow(Map<Integer, InitialEndowment> intialEndowments);
  
}