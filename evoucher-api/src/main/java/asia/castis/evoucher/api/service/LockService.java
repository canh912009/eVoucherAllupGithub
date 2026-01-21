package asia.castis.evoucher.api.service;


import org.redisson.api.RLock;

public interface LockService {
    public RLock getPurchaseChoiceLocker(String ev);
}
