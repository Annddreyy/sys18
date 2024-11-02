import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс счета пользователя в банке
 * @author Андрей
 * @version 1.0
 */
public class Account {
    private final Lock LOCK_OBJECT = new ReentrantLock();
    private final Condition balanceIncreased = LOCK_OBJECT.newCondition();

    /** Сумма дененг на балансе */
    public int accountBalance;

    /**
     * Конструктор класса
     * @param balance Начальный баланс аккаунта
     */
    Account(int balance) {
        this.accountBalance = balance;
    }

    /**
     * Получение информации о количестве доступных на счете средств
     * @return Баланс на аккаунте пользователя
     */
    public int getBalance() {
        LOCK_OBJECT.lock();
        try {
            return accountBalance;
        } finally {
            LOCK_OBJECT.unlock();
        }
    }

    /**
     * Добавление суммы на счет
     * @param amount Колиество денег, которые нужно начислить
     */
    public void deposit(int amount) {
        LOCK_OBJECT.lock();
        try {
            accountBalance += amount;
            balanceIncreased.signalAll();
        } finally {
            LOCK_OBJECT.unlock();
        }
    }

    /**
     * Снятие средств со счета
     * @param amount Сумма, которую необходимо снять
     */
    public void withdraw(int amount) {
        LOCK_OBJECT.lock();
        try {
            if (accountBalance < amount) {
                System.out.println("Недостатоно средств на счете для снятия.");
            }
            accountBalance -= amount;
        } finally {
            LOCK_OBJECT.unlock();
        }
    }
}