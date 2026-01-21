package asia.castis.evoucher.push.constant;

public class Constant {
    public static final String BRAND_NAME = "aQua VN";
    public static final class MSG_TYPE {
        public static final String ZALO = "ZALO";
        public static final String SMS = "SMS";
    }

    public static final class MSG_SENDING_STATUS {
        // response fail
        // sending sms fail
       public static final int FAIL = 0;
       // sending zalo fail
       public static final int ZALO_FAIL = -1;
        public static final int LOCAL_FAIL = 3;
        public static final int DLR_FAIL = 4;
        public static final int SUCCESS = 1;
        // delivering status
        public static final int PENDING_NO_RESULT = 2;
        public static final int PENDING_NO_RESULT2 = -11;
    }
    public static final class MSG_SENDING_INQUIRY_STATUS {
        public static final int PENDING_ALLOWED = 1;
        public static final int PENDING_DELIVERY = 2;
        public static final int DELIVERY = 3;
        public static final int REJECTED = 4;
        public static final int DELIVERED = 5;
        public static final int DELETED = 6;
    }
    public static final class PUBLIC_STATUS_CODE {
        //request approve
        public static final String WAIT_APPRV = "WAIT_APPRV";
        //cancel publish before approve
        public static final String CANCEL = "CANCEL";
        //after approved
        public static final String APPROVED = "APPROVED";
        //cancel approve after approved
        public static final String CANCEL_APPRV = "CANCEL_APPRV";
        //Reject approve
        public static final String REJECTED = "REJECTED";
        //start publish
        //end publish
        public static final String PUBLISHING = "PUBLISHING";

        public static final String GENERATING = "GENERATING";

        //end generate message
        //start send mesage to end user
        public static final String SENDING = "SENDING";
        //end send message to end user
        //start receive result
        public static final String WAIT_FOR_SEND_RESULT = "WAIT_FOR_SEND_RESULT";

        //after all result received
        public static final String FINISHED = "FINISHED";
        // fail on processing
        public static final String FAIL_PUBLISHING = "FAIL_PUBLISHING";
        public static final String FAIL_GENERATING = "FAIL_GENERATING";
        public static final String FAIL_SENDING = "FAIL_SENDING";

    }
    public static final class PUBLIC_DETAIL_STATUS_CODE {
        /*
        request approve	No status because not yet created
        cancel publish before approve	No status because not yet created
        after approved	No status because not yet created
        cancel approve after approved	No status because not yet created
        Reject approve	No status because not yet created
        */
        //start publish
        public static final String STRT_PUB = "STRT_PUB";
        //fail publish
        public static final String FAIL_PUB = "FAIL_PUB";
        //end publish
        public static final String END_PUB = "END_PUB";
        //start generate message
        public static final String STRT_GEN_MSG = "STRT_GEN_MSG";

        //end generate message
        public static final String END_GEN_MSG = "END_GEN_MSG";
        //fail generate message
        public static final String FAIL_GEN_MSG = "FAIL_GEN_MSG";
        //start send message to end user
        // got it
        public static final String STRT_SND_MSG = "STRT_SND_MSG";
        //fail send message to end user
        public static final String FAIL_SND_MSG = "FAIL_SND_MSG";
        //end send message to end user
        public static final String END_SND_MSG = "END_SND_MSG";
        //start receive result
        //result can not received
        public static final String RESULT_PENDING = "RESULT_PENDING";
        //result success
        public static final String RESULT_SUCCESS = "RESULT_SUCCESS";
        //result fail
        public static final String RESULT_FAIL = "RESULT_FAIL";
    }
    public static final class SMS_ERROR_CODE {
        public static final int ACCESS_TOKEN_EXPIRED = 1013;
        public static final int ACCESS_TOKEN_EMPTY = 1014;
    }
}
