export const LIMIT = 10;
export const PAGE_SIZE = 10;
export const PAGE_SIZE_STOCK = 50;
export const PAGE_SIZE_MIN = 5;
export const PAGE_SIZE_MAX = 10000;
export const TOKEN_KEY = 'token';
export const PERMISSIONS_KEY = 'permissions';
export const AUTH_CRED = 'AUTH_CRED';
export const USER_INFO = 'USER_INFO';
export const EMAIL_VERIFIED = 'emailVerified';
export const RESPONSIVE_WIDTH = 659 as number;

// https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
export const ACCEPTED_FILE_TYPES =
  'image/*,application/pdf,application/zip,application/vnd.rar,application/epub+zip,.psd';

export const PERMISSIONS_EV = {
  ROLE_ADMIN: 'ROLE_ADMIN',
  ROLE_OPERATOR: 'ROLE_OPERATOR',

  ROLE_CUSTOMER: 'ROLE_CUSTOMER',

  ROLE_SUPPLIER: 'ROLE_SUPPLIER',
  ROLE_BRAND: 'ROLE_BRAND',
  ROLE_STORE: 'ROLE_STORE',
}

export const ROLES = [
  { code: PERMISSIONS_EV.ROLE_ADMIN, roleName: PERMISSIONS_EV.ROLE_ADMIN },
  { code: PERMISSIONS_EV.ROLE_OPERATOR , roleName: PERMISSIONS_EV.ROLE_OPERATOR  },
  { code: PERMISSIONS_EV.ROLE_SUPPLIER , roleName: PERMISSIONS_EV.ROLE_SUPPLIER  },
  { code: PERMISSIONS_EV.ROLE_BRAND , roleName: PERMISSIONS_EV.ROLE_BRAND  },
  { code: PERMISSIONS_EV.ROLE_STORE , roleName: PERMISSIONS_EV.ROLE_STORE  },
  { code: PERMISSIONS_EV.ROLE_CUSTOMER , roleName: PERMISSIONS_EV.ROLE_CUSTOMER  }
]

export const CODE_GROUP = {
  /* Approve status code for customer, supplier */
  APPROVE_STATUS_CD_1: 'APPROVE_STATUS_CD_1',

  /* Approve status code for customer contract, supplier contract */
  APPROVE_STATUS_CD_2: 'APPROVE_STATUS_CD_2',

  /* Campaign status */
  CAMPAIGN_STATUS: 'CAMPAIGN_STATUS',

  /* Company type code */
  COMPANY_TYPE: 'COMPANY_TYPE',

  /* Delivery status */
  DELIVERY_STATUS: 'DELIVERY_STATUS',

  /* Goods type */
  GOODS_TYPE: 'GOODS_TYPE',

  /* system brand */
  SYSTEM_TYPE: 'SYSTEM',

  /* Period type */
  PERIOD_TYPE: 'PERIOD_TYPE',

  /* Settlement method code */
  SETTLEMENT_METHOD_CD: 'SETTLEMENT_METHOD_CD',

  VNPT_FACE_VALUES: 'VNPT_FACE_VALUES',

  XPAY_FACE_VALUES: 'XPAY_FACE_VALUES',

  /* Sms type */
  SMS_TYPE: 'SMS_TYPE',

  /* Upload data type publish */
  UPLOAD_DATA_TYPE: 'UPLOAD_DATA_TYPE',

  /* Valid Yes/No */
  VALID_YN: 'VALID_YN',

  /* External pin display type */
  EXT_PIN_DISPLAY_TYPE: 'EXT_PIN_DISPLAY_TYPE',

  /* Voucher status (pin status of cs management) */
  VOUCHER_STATUS: 'VOUCHER_STATUS',

  CUSTOMER_TYPE: 'CUSTOMER_TYPE',
  GIFTPOP: 'GIFTPOP',
  UR_BOX: 'UR_BOX',
  WATANE: 'WATANE',
  VNPT_EPAY: 'VNPT_EPAY',
  XPAY: 'XPAY',
}

export const SYSTEM_BRAND_TYPE = {
  ALL: "ALL",
  NORMAL: "NORMAL",

  // ........
  INTERNAL: "INTERNAL",
  EXTERNAL: "EXTERNAL",
  GIFTPOP: "GIFTPOP",
  UR_BOX: "UR_BOX",
  WATANE: "WATANE",
  VNPT_EPAY: "VNPT_EPAY",
  XPAY: 'XPAY',

  CHOICE: "CHOICE",
  BULK: "BULK",
}

export const POPUP_DELETE_TYPE = {
  CATEGORY: "CATEGORY",
  BRAND: "BRAND",
  GOOD: "GOOD",
}

export const SMS = {
  SENDER_NAME_MAX_LENGTH: 18,
  CUSTOMER_WITH_GOOD_TYPE_CHOICE_MAX_LENGTH: 150,
  USERNAME_MAX_LENGTH: 28,
}

export const PASS_REGEX_STRING = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#\$%\^&\*])(?=.{8,})/

export const VIETNAMSE_SPECIAL_CHARACTERS = 'ÁÀẢÃẠáàảãạÂẤẦẨẪẬâấầẩẫậĂẮẰẲẴẶăắằẳẵặĐđÉÈẺẼẸéèẻẽẹÊẾỀỂỄỆêếềểễệÓÒỎÕỌóòỏõọÔỐỒỔỖỘôốồổỗộƠỚỜỞỠỢơớờởỡợÍÌỈĨỊíìỉĩịÚÙỦŨỤúùủũụƯỨỪỬỮỰưứừửữựÝỲỶỸỴýỳỷỹỵ';
export const REGEX_VIETNAMSE_CHARACTERS_NUMBERS_SPACE = /^[a-z0-9A-ZÁÀẢÃẠáàảãạÂẤẦẨẪẬâấầẩẫậĂẮẰẲẴẶăắằẳẵặĐđÉÈẺẼẸéèẻẽẹÊẾỀỂỄỆêếềểễệÓÒỎÕỌóòỏõọÔỐỒỔỖỘôốồổỗộƠỚỜỞỠỢơớờởỡợÍÌỈĨỊíìỉĩịÚÙỦŨỤúùủũụƯỨỪỬỮỰưứừửữựÝỲỶỸỴýỳỷỹỵ\s]*$/
export const REGEX_LETTERS_NUMBERS_SPACES_SOME_CHARACTERS = /^[a-zA-Z0-9\,\-\_\(\)\s]*$/

export const CHANNEL_APP_ID_DEFAULT = "AQUA"
