package asia.castis.evoucher.push.callback_receive.sms.constant;

public class Constant {
    public static final class MSG_TYPE {
        public static final String ZALO = "zalo";
        public static final String SMS = "sms";
    }

    public static final class MSG_SENDING_STATUS {
       public static final int FAIL = 0;
        public static final int SUCCESS = 1;
        public static final int PENDING_NO_RESULT = 2;
        public static final int PENDING_NO_RESULT2 = -11;
    }
}
