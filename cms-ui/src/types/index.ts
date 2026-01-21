import type { NextPage } from 'next';
import { ImageLoader } from 'next/image';

export type NextPageWithLayout<P = {}> = NextPage<P> & {
    authorization?: boolean;
    getLayout?: (page: React.ReactElement) => React.ReactNode;
};

export interface User {
    id: string;
    name: string;
    // shops: Shop[];
    // managed_shop: Shop;
    is_active: boolean;
    adminName: string;
    email: string;
    created_at: string;
    updated_at: string;
    profile?: Profile;
    // address: Address[];
    // orders?: OrderPaginator;
    email_verified: boolean;
    mobileNumber: boolean;
    roleCode: string;
}

export interface Profile {
    id: string;
    avatar?: Attachment;
    bio?: string;
    contact?: string;
    // socials?: Social[];
    customer?: User;
}

export interface Attachment {
    path: string;
}

export interface AttachmentSettings {
    thumbnail: string;
    original: string;
    id?: string;
}

export interface LoginInput {
    phoneNumber: string;
    password: string;
}

export interface AuthResponse {
    token: string;
    permissions: string[];
}

export interface BaseResponse<T> {
    data: T;
    message: string;
    timestamp: string;
    errorCode: string;
    totalCount: number;
}

export interface CommonFields {
    regId: string,
    regDt: string,
    updtId: string,
    updtDt: string,
    validYn: string,
    rejectReason?: string;
}

export interface DataAuthRes {
    token: string;
    roleCode: string;
    expiredDate: number;
}

export interface Settings {
    id: string;
    language: string;
    options: SettingsOptions;
}

export interface SettingsOptions {
    siteTitle?: string;
    siteSubtitle?: string;
    currency?: string;
    logo?: AttachmentSettings;
}

export interface GetParams {
    slug: string;
    language: string;
}

export interface PathOptions {
    page?: number;
    pageSize?: number;
}

export interface QueryOptions {
    language?: string;
    limit?: number;
    pageSize?: number;
    page?: number;
    orderBy?: string;
    sortedBy?: SortOrder;
}

export interface UploadResponse {
    path?: string;
}

export enum SortOrder {
    Asc = 'ASC',
    Desc = 'DESC',
    None = '',
}

export enum CommonYesNoEnum {
    YES = 'Y',
    NO = 'N',
    NULL = ''
}

export enum CommonStatusCode {
    PROCESSING = 'PROCESSING',
    WAIT_APPRV = 'WAIT_APPRV',
    APPROVED = 'APPROVED',
    CANCEL_APPRV = 'CANCEL_APPRV',
    REJECTED = 'REJECTED',
    END = 'END',
    CANCEL = 'CANCEL',
    PUBLISHING = 'PUBLISHING'
}

export enum CommonApproveStatusAction {
    REQ = 'REQ',
    CANCEL_REQ = 'CANCEL_REQ',
    APPRV = 'APPRV',
    CANCEL_APPRV = 'CANCEL_APPRV',
    REJCT = 'REJCT',
}

export enum CustomerTypeCode {
    B2B = 'B2B',
    CHANNEL = 'CHANNEL',
}

export enum PublishDetailStatusCode {
    STRT_PUB = 'STRT_PUB',
    FAIL_PUB = 'FAIL_PUB',
    END_PUB = 'END_PUB',
    STRT_GEN_MSG = 'STRT_GEN_MSG',
    FAIL_GEN_MSG = 'FAIL_GEN_MSG',
    END_GEN_MSG = 'END_GEN_MSG',
    STRT_SND_MSG = 'STRT_SND_MSG',
    FAIL_SND_MSG = 'FAIL_SND_MSG',
    RESULT_FAIL = 'RESULT_FAIL',
    RESULT_PENDING = 'RESULT_PENDING',
}

export enum VoucherStatusCode {
    DISABLED = 'DISABLED',
    EXPIRE = 'EXPIRE',
    NORMAL = 'NORMAL',
    PART_USED = 'PART_USED',
    USED = 'USED',
}

export interface PaginatorInfo<T> {
    current_page: number;
    data: T[];
    first_page_url: string;
    from: number;
    last_page: number;
    last_page_url: string;
    links: any[];
    next_page_url: string | null;
    path: string;
    per_page: number;
    prev_page_url: string | null;
    to: number;
    total: number;
}

export interface PaginatorInfoEV<T> {
    current_page: number;
    data: T[];
    first_page_url: string;
    from: number;
    last_page: number;
    last_page_url: string;
    links: any[];
    next_page_url: string | null;
    path: string;
    per_page: number;
    prev_page_url: string | null;
    to: number;
    totalCount: number;
}

export interface Settings {
    id: string;
    language: string;
    options: SettingsOptions;
}

// -> TODO: Simplify this
export interface MappedPaginatorInfo {
    currentPage: number;
    firstPageUrl: string;
    from: number;
    lastPage: number;
    lastPageUrl: string;
    links: any[];
    nextPageUrl: string | null;
    path: string;
    perPage: number;
    prevPageUrl: string | null;
    to: number;
    total: number;
    hasMorePages: boolean;
}

export interface MappedPaginatorInfoEV {
    currentPage: number;
    firstPageUrl: string;
    from: number;
    lastPage: number;
    lastPageUrl: string;
    links: any[];
    nextPageUrl: string | null;
    path: string;
    perPage: number;
    prevPageUrl: string | null;
    to: number;
    totalCount: number;
    hasMorePages: boolean;
}

export interface ForgetPasswordInput {
    email: string;
}

export interface VerifyForgetPasswordTokenInput {
    token: string;
    email: string;
}

export interface ResetPasswordInput {
    token: string;
    email: string;
    password: string;
}

export enum Permission {
    SuperAdmin = 'super_admin',
    StoreOwner = 'store_owner',
    Staff = 'staff',
    Customer = 'customer',
}

export interface RegisterInput {
    email: string;
    password: string;
    name: string;
    shop_id?: number;
    permission: Permission;
}

/**
 * Supplier
 */

export interface Supplier extends CommonFields {
    id: string;
    taxcode: string;
    supplierName: string;
    bankName: string;
    accountNumber: string;
    accountName: string;
    settlementMethodCode: string;
    supplyDiscountRate: number;
    supplyCommissionRate: number;
    vatIncludeYn: string;
    managerName: string;
    managerEmail: string;
    managerMobileNumber: string;
    primaryContactName: string;
    primaryContactEmail: string;
    primaryContactMobile: string;
    validYn: string;
    approveStatusCode: string;
    approveId: string;
}

export interface SupplierQueryOptions {
    supplierId: string;
    supplierName: string;
    approveStatusCode: string;
    taxcode: string;
}

export interface SupplierPaginator extends PaginatorInfoEV<Supplier> { }

/**
 * MenuGroups
 */
export interface MenuGroupQueryOptions {
    menuGroupId: string;
    menuGroupName: string;
    keyWord: string;
}
export interface MenuGroupsPaginator extends PaginatorInfoEV<MenuGroup> { }
export interface MenuGroup extends CommonFields {
    id: string;
    menuGroupName: string;
    sortOrder: number;
    menus: Menu[];
}

/**
 * Roles
 */
export interface RoleQueryOptions {
    roleCode: string;
    roleName: string;
    keyWord: string;
}
export interface RolesPaginator extends PaginatorInfoEV<Role> { }
export interface Role extends CommonFields {
    roleCode: string;
    roleName: string;
    sortOrder: number;
    menus: Array<any>;
    menuGroups: MenuGroup[];
}
/**
 * Dashboard Time
 */
export interface DashboardTimeQueryOptions {
    startDate: string;
    endDate: string;
    customerId: string;
    supplierId: string;
    brandId: string;
    storeId: string;
}
export interface SupplierChartItem {
    supplierName: string;
    itemCount: number;
}
export interface CustomerChartItem {
    customerName: string;
    campaignCount: number;
    voucherAmount: number;
}
export interface GoodChartItem {
    exchangeDate: string;
    goodName: string;
    goodCode: number;
    usedCount: number;
}
export interface CustomerTableItem {
    salesAmount: number;
    customerName: string;
    campaignCount: number;
    voucherCount: number;
}
export interface CustomerCampaignItem {
    othersCount: number;
    othersAmount: number;
    usedAmount: number;
    unusedAmount: number;
    unusedCount: number;
    campaignName: string;
    usedCount: number;
}
export interface CustomerTablePaginator extends PaginatorInfoEV<CustomerTableItem> { }

export interface ItemTableItem {
      goodName: string;
      publishCount: number;
      usedCount: number;
      usedAmount: number;
      itemCode: number;
}
export interface ItemTablePaginator extends PaginatorInfoEV<ItemTableItem> {}

export interface BrandTableItem {
    brandName: string;
    publishCount: number;
    usedCount: number;
    usedAmount: number;
}
export interface BrandTablePaginator extends PaginatorInfoEV<BrandTableItem> {}

export interface CampaignTableItem {
    campaignId: number;
    campaignName: string;
    deliveryCount: number;
    totalVoucherCount: number;
    totalSendFailCount: number;
    totalSendSuccessCount: number;
}
export interface CampaignTablePaginator extends PaginatorInfoEV<CampaignTableItem> {}

export interface DeliveryTableItem {
    deliveryId: number;
    deliveryName: string;
    voucherCount: number;
    totalSendFailCount: number;
    totalSendSuccessCount: number;
}
export interface DeliveryTablePaginator extends PaginatorInfoEV<DeliveryTableItem> {}


export interface StoreTableItem {
    storeName: string;
    storeId: string;
    usedCount: number;
    usedAmount: number;
}
export interface StoreTablePaginator extends PaginatorInfoEV<StoreTableItem> {}

/**
 * Raw Settlement
 */
export interface RawSettlementQueryOptions {
    transactionStartDate: string;
    transactionEndDate: string;
    settlementTarget: string;  //company type
    companyName: any; // select after select company type
    voucherTypeCode: string;
    settlementMethodCode: string;
    campaignId: string;
    campaignName: any;
    publishId: string;
    publishName: any;
    sort?: string;
    direction?: string;
}
export interface RawSettlementPaginator extends PaginatorInfoEV<RawSettlement> { }
export interface RawSettlement {
    logId: number;
    ev: string;
    settlementLogType: string;
    publishId: number;
    publishDetailId: number;
    pin: string;
    transactionDate: string;
    logCreateDate: string;
    voucherTypeCode: string;
    goodsId: number;
    customerId: string;
    customerName: string;
    supplierId: string;
    supplierName: string;
    companyName: string;
    brandId: string;
    storeId: any;
    userMobileNumber: string;
    userEmail: string;
    staffMobileNumber: any;
    settlementCompleteYn: any;
    settlementCompleteDate: any;
    settlementTarget: string;
    settlementMethodCode: string;
    listPrice: number;
    salesPrice: number;
    discountRate: number;
    discountAmount: any;
    discountAppliedAmount: any;
    settlementAmount: any;
    vatIncludeYn: string;
    vatAmount: any;
    commissionRate: any;
    commissionAmount: any;
    sendCost: number;
    settlementExceptReasonCode: any;
    settlementExceptReason: any;
    remainBalance: any;
    parentVoucherEv: string;
    originalEv: string;
    serialNo: string;
    activationDate: string;

    // excel download
    system: string;
}

/**
 * Menus
 */
export interface MenuQueryOptions {
    menuId: string;
    menuName: string;
    menuUrl: string;
    keyWord: string;
}
export interface MenusPaginator extends PaginatorInfoEV<Menu> { }
export interface Menu extends CommonFields {
    id: string;
    menuGroupId: number;
    menuName: string;
    sortOrder: number;
    menuUrl: string;
}

/**
 * Campaign
 */
export interface Campaign extends CommonFields {
    id: string;
    campaignName: string;
    customerId: string;
    customerType: string;
    customerContractId: string;
    startDate: string;
    endDate: string;
    messageSubject: string;
    messageContent: string;
    messageCallingNumber: string;
    campaignGoods: string;
    approveStatusCode: string;
    statusCode: string;
    goods?: any;
    listGoods: Good[];
    publishes: Delivery[];
    customer: Customer;
    customerContract: CustomerContract;
    messageTemplate: MessageTemplate;
    contentLink: string;
    contentImagePath: string;
    contentImageName: string;
    senderName: string;
    showPopupYn: string;
}

export interface CampaignQueryOptions {
    campaignId: string;
    campaignName: string;
    customerName: string;
    statusCode: string;
}

export interface CampaignPaginator extends PaginatorInfoEV<Campaign> { }

/**
 * Supplier Contract
 */

export interface SupplierContract extends CommonFields {
    id: string;
    contractName: string;
    startDate: string;
    endDate: string;
    supplierId: string;
    supplier: Supplier;
    supplyDiscountRate: number;
    supplyDiscountAmount: number;
    supplyCommissionRate: number;
    supplyVatIncludeYn: string;
    supplySettlementMethodCode: string;
    approveStatusCode: string;
    statusCode: string;
    contractFilePath: string;
    contractFileName: string;
}

export interface SupplierContractQueryOptions {
    contractId: string;
    contractName: string;
    supplierId: string;
    supplierName: string;
    approveStatusCode: string;
}

export interface SupplierContractPaginator extends PaginatorInfoEV<SupplierContract> { }

/**
 * Customer Contract
 */

export interface CustomerContract extends CommonFields {
    id: string;
    contractName: string;
    startDate: string;
    endDate: string;
    customerId: string;
    customer: Customer;
    sellDiscountAmount: number;
    sellDiscountCost: number;
    sellDiscountRate: number;
    sellCommissionRate: number;
    sellVatIncludeYn: string;
    sellSettlementMethodCode: string;
    approveStatusCode: string;
    contractFilePath: string;
    contractFileName: string;
}

export interface CustomerContractQueryOptions {
    contractId: string;
    contractName: string;
    customerId: string;
    customerName: string;
    approveStatusCode: string;
}

export interface CustomerContractPaginator extends PaginatorInfoEV<CustomerContract> { }

/**
 * Brands
*/

export interface Brands extends CommonFields {
    id: string,
    brandName: string,
    supplier: Supplier,
    supplierId: string,
    supplierName: string,
    defaultBrandYn: string

    brandImagePath: string,
    brandImageName: string,

    brandLogoPath: string,
    brandLogoName: string,

    description: string,

    listGoods: Good[],
    stores: Store[],
    isPosLink: string,
    displayType: string,
    systemTypeCode: Code,
    system: string,

    brandCode?: string, // just for GIFTPOP SUPPLIER
    categoryCode?: string[], //   for BULK filter
    bulkGoods?: Good[]; //   for BULK
    brandId : string, //   for BULK
    displayIndex? : number; //BULK
    brand? : Brands; //BULK

    appId: string,
    serialNumberPrefix: string,
    serialNumberTotalLength: number,
    authenticationKey: string,
    encryptionKey: string
    ipWhiteList: string
}

export interface BrandsQueryOptions {
    brandId: string,
    brandName: string,
    supplierId: string,
    supplierName: string,
    validYn: string //Active
    categoryCode?: string //BULK product
    systems?: string; // for BULK product : systemsEXTERNAL,INTERNAL,...
}

export interface BrandsPaginator extends PaginatorInfoEV<Brands> { }

export interface BrandsLogo extends ImageLoader {
}

export interface BrandsLogoParam {
    brandImagePath: string
}

/**
 * Good
 */
export interface Good extends CommonFields {
    id: number;
    goodsName: string;
    supplierGoodsId: string;
    goodsDescription: string;
    supplier: Supplier;
    brand: Brands;
    supplierContractId: string;
    goodsTypeCode: Code;
    goodsType: string;

    periodType: string;
    periodTerm: string;
    periodExpireDate: string;

    validYn: string;
    goodsStatusCode: string;

    goodsImgPath: string,
    goodsImgName: string,

    // temparity
    listPrice: string|Code|number ;   //code for case VnptTopup
    sellPrice: string;
    settlementMethodTypeCode: Code;
    settlementMethodCode: string;
    supplyDiscountAmount: string;
    supplyCommissionRate: string;
    vatIncludeYn: string;
    startDate: string;
    endDate: string;

    exceptStores: Store[];
    listGoodsChoice: Good[];
    categories: Category[];
    bulkCategories: Category[];  //BULK
    brandsBULK?: Brands[];
    goodsBULK?: Good[];

    system: string;
    displayType: string;
    displayTypeCode: Code;

    isValid: boolean;
    brandIds?: string[], //   for BULK filter
    goodsId : string, //   for BULK
    displayIndex? : number , //   for BULK
    goods? : Good , //   for BULK

    vnptGoods: VnptGood[], //vnptEpay
    xpayGoods: VnptGood[],
    usageCount? : any , //   for LC
}

export interface GoodQueryOptions {
    goodsId: string;
    goodsName: string;
    supplierName: string;
    brandName: string;
    validYn: CommonYesNoEnum;
    isExpired: CommonYesNoEnum;
    keyWord: string;
    system?: string;
    systems?: string; // systems=CHOICE,EXTERNAL,INTERNAL

    //bulk
    categoryCode?: string;
    brandId?: string;
}

export interface GoodPaginator extends PaginatorInfoEV<Good> { }

/**
 * Category
 */

export interface CategoryQueryOptions extends QueryOptions {
    categoryCode: string;
    categoryName: string;
    validYn: string;
}

export interface CategoryPaginator extends PaginatorInfoEV<Category> { }

export interface Category extends CommonFields {
    categoryCode: string;
    categoryName: string;
    validYn: string;
    imagePath: string;
    imageName: string;
    bulkBrands?: Brands[]; //BULK
    displayIndex? : number; //BULK
    category? : Category //BULK
}

/**
 * Customer
 */

export interface Customer extends CommonFields {
    id: string;
    customerName: string;
    customerTypeCode: string;
    taxcode: string;
    // customerId: string;
    bankName: string;
    accountNumber: string;
    accountName: string;
    representativeMail: string;
    representativeMobile: string;
    sellDiscountRate: number;
    sellCommissionRate: number;
    vatIncludeYn: string;
    settlementMethodCode: string;
    sendCost: number;
    managerName: string;
    managerEmail: string;
    managerMobileNo: string;
    primaryContactName: string;
    primaryContactEmail: string;
    primaryContactMobileNo: string;
    approveStatusCode: string;
    approverId: string;
    admin: any; //object
}

export interface CustomerQueryOptions {
    customerId: string;
    customerName: string;
    customerTypeCode: string;
    taxcode: string;
    approveStatusCode: string;
}

export interface CustomerPaginator extends PaginatorInfoEV<Customer> { }

/**
 * Code Groups
 */

export interface CodeGroup extends CommonFields {
    codeGroupId: string;
    codeGroupName: string;
    codes: Code[];
}

/**
 * Codes
 */

export interface Code extends CommonFields {
    id: string;
    codeId: string;
    codeName: string;
    codeGroupId: string;
    sortOrder: number;
}

export interface CodeQueryOptions {
    codeId: string;
    codeName: string;
    keyWord: string;
}

export interface CodePaginator extends PaginatorInfoEV<Code> { }


/**
 * Store
 */

export interface Store extends CommonFields {
    id: string;
    storeName: string;
    storeType: string;
    brand: Brands;
    brandId: string;
    brandName: string;
    supplier: Supplier;
    supplierId: string;
    supplierName: string;
    region: string;
    mapCode: string;
    mapInteractionType: string;
    fullAddress: string;
    telephoneNumber: string;
    storeImagePath: string,
    storeImageName: string,
}

export interface StoreQueryOptions {
    storeId: string;
    storeName: string;
    region: string;
    supplierName: string;
    brandName: string;
    validYn: string;
}

export interface StorePaginator extends PaginatorInfoEV<Store> { }

/**
 * Province
 */
export interface Province {
    id: number;
    name: string;
    code: string;
}

/**
 * District
 */
export interface District {
    id: number;
    name: string;
    code: string;
    parent_id: number;
}

/**
 * Ward
 */
export interface Ward {
    id: number;
    name: string;
    code: string;
    parent_id: number;
}

/**
 * Delivery
 */
export enum DeliveryReceiveType {
    ZALO = 'ZALO',
    SMS = 'SMS',
    DOWNLOAD = 'DOWNLOAD',
    PAPER = 'PAPER',
    EMAIL = 'EMAIL',
}

export enum DeliveryUploadType {
    FILE = 'FILE',
    TEXT = 'TEXT',
}

export interface DeliveryUserInput {
    id: string;
    phone: string;
    name: string;
    gender?: string;
    birthday?: string;
    address?: string;
    email?: string;
}

export interface DeliveryUser {
    id?: string;
    userMobileNum: string;
    userNm: string;
    gender?: string;
    birthday?: string;
    address?: string;
    email: string;
    smsStatus?: string;
    externalPinNo?: string;
}

export interface DeliveryVoucher {
    ev: string;
    serialNo: string;
    price: number;
    extPin: string;
    expirationDate: string;
    shortLink: string;
    activationUrl: string;
    activationDate: string;
    otp: string;
}

export interface Delivery extends CommonFields {
    id: string;
    publishName: string;
    campaignId: string;
    campaignName: string;
    smsType: string;
    numberOfVouchers?: number;   // smsType DOWNLOAD
    bookingYn: string;
    bookingDate: string;
    receiverNoDuplicateAllowYn: string;
    contentLink: string;
    contentImagePath: string;
    contentImageName: string;
    uploadType: string;
    uploadFile: string;
    uploadText: string;
    uploadFileName: string;
    uploadFilePath: string;
    endUsers: DeliveryUser[];
    messageSubject: string;
    messageContent: string;
    sellPrice: number;
    sellListPrice: number;
    sellSettlementMethodCode: string;
    sellDiscountAmount: number;
    sellCommissionRate: number;
    sellVatIncludeYn: string;
    approveStatusCode: string;
    statusCode: string;
    goods: Good;
    goodsId: string;
    campaign: Campaign;
    senderName: string;
    downloadVouchers: DeliveryVoucher[];
    showPopupYn: string;
}

export interface DeliveryQueryOptions {
    publishId: string;
    publishName: string;
    campaignName: string;
    goodsName: string;
    statusCode: string;
    smsType: string;
}

export interface DeliveryPaginator extends PaginatorInfoEV<Delivery> { }

/**
 * Admins
 */
export interface Admin {
    id: string;
    adminName: string;
    email: string;
    mobileNumber: string;
    telephone: string;
    lastLoginDate: Date;
    passwordInitYn: string;
    passwordUpdateDate: Date;
    loginFailCount: number;
    roleCode: string;
    adminCorporationId: string;
    adminCorporationName: string;
    validYn: string;
    role: RoleCode;

    supplierName: string;
    brandName: string;
    storeName: string;
    customerName: string;
    corporation: Corporation;
}

export interface AdminQueryOptions {
    adminId: string;
    adminName: string;
    mobilePhone: string;
    email: string;
    corporationName: string;
    roleCode: string;
    roleCodes: string; //
}

export interface AdminPaginator extends PaginatorInfoEV<Admin> { }

export interface RoleCode {
    code: string;
    name: string;
}

export interface Corporation {
    adminCorporationId: string | undefined;
    adminCorporationName: string | undefined;
}

/**
 * Message template
 */

export interface MessageTemplate {
    id: number;
    name: string;
    messageString: string;
    system: string;
}

export interface MessageTemplatePaginator extends PaginatorInfoEV<MessageTemplate> { }


/**
 * External PIN Upload
 */
export interface ExternalPinUpload {
    regId: string,
    regDt: string,
    updtId: string,
    updtDt: string,
    id: string,
    uploadName: string,
    goodsId: string,
    uploadFilePath: string,
    uploadFileName: string,
    rowCount: string,
    memo: string,
    status: string,
    pins: ExternalPinUploadData[],
    displayType: any,
}

export interface ExternalPinUploadData {
    id: string,
    externalPinNo: string,
    goodsId: string,
    externalPinUpload: string,
    publishDetailId: string,
    status: string,
    expireTime: string,
    password: string,
}


export interface ExternalPinUploadQueryOptions {
    goodsId: string,
}

export interface ExternalPinUploadPaginator extends PaginatorInfoEV<ExternalPinUpload> { }

export interface CsHistoryExchange { //for normal voucher
    id: number;
    exchangeDate: string;
    storeId: string;
    storeName: string;
    storeStaff: string;
    pinStatus: string;
    exchangeType: string;
    exchangeAmount: number;
}

export interface CsHistoryPurchase { //for choice voucher
    id: number;
    exchangeDate: string;
    storeId: string;
    storeName: string;
    storeStaff: string;
    pinStatus: string;
    exchangeType: string;
    exchangeAmount: number;
}

export interface CsHistoryTransfer {
    id: number;
    voucherUUID: string;
    toVoucherUUID: string;
    transferDate: string;
    targetNumber: string;
    targetName: string;
    accessLink: string;
    pinStatus: string;
    pin: string;
    transferStatusCode: string;
}

export interface Cs extends CommonFields {
    id: string;
    voucherUUID: string;
    voucherTypeCode: string;
    voucherType: string;
    parentVoucherEv: string;
    parentSystem: string;
    campaignId: number;
    campaignName: string;
    deliveryId: number;
    deliveryName: string;
    deliveryDate: string;
    productId: number;
    productName: string;
    targetNumber: string;
    targetEmail: string;
    targetName: string;
    pin: string;
    pinStatus: string;
    pinPassword: string;
    parentVoucherToken: string; //OTP
    serialNo: string;
    activationUrl: string;
    activationDate : string;
    expireDate: string;
    accessLink: string;
    startDate: string;
    endDate: string;
    exchangeDate: string;
    messageType: string;
    result: string;
    exChangeHistories: CsHistoryExchange[];
    transferHistories: CsHistoryTransfer[];
    childOfChoiceVoucherList: CsHistoryPurchase[];
    publishDetailStatusCode: string;
    system: string;
    requestCount: number;  //Extend Button
    underProcess: boolean;  //Extend Button

    // excel download
    remainingBalance?: string;
    remainingCount?: string;
    otp?: string;
    password?: string;
    transactionId?: string;
    requestId?: string;
    provider?: string;
    faceValue?: string;
    cardSerial?: string;
    cardPin?: string;
    topupNumber?: string;
}

export interface CsQueryOptions {
    startDate: string;
    endDate: string;
    deliveryDate: string;
    pinStatus: string;
    pins: string;
    deliveryId: string;
    deliveryName: string;
    campaignId: string;
    campaignName: string;
    targetNumbers: string;
    targetNames: string;
    sort: string;
    direction: SortOrder;
    voucherUUID: string;
    serialNo: string;
    productId: string;
    productName: string;
    voucherExpireBefore: string;
}

export interface CsExtendQueryOptions {
    customerId: string;
    customerName: string;
    publishId: string;
    publishName: string;
    targetName: string;
    targetNumber : string
    ev: string;
    requestStatus: string;
}

export interface CsStatusQueryOptions {
    ev?: string;
    reason?: string;
}

export interface CsApproveExtendQueryOptions {
    ev: string;
    memo : string;
    reqId : string;
    reqStatus : string;
    approveMemo : string;
}

export interface CsPaginator extends PaginatorInfoEV<Cs> { }
export interface StockPaginator extends PaginatorInfoEV<Stock> { }

/**
 * VNPT EPay
 */

export interface VnptGood extends CommonFields {
    id: number;
    providerCode: string;
    faceValue: string|number;
    validYn: string;
    description: string;
    providerCd: string;
}

export interface VNPTEPayGift extends CommonFields {
    id: number;
    giftTitle: string;
    brandName: string;
    price: number;
    quantity: number;
}

export interface VNPTEPayProvider extends CommonFields {
    providerCd: string;
    topupProviderCd: string;
    providerNm: string;
    providerType: string;
    validYn: string;
    allowedCardFaces: string;
    allowedActions: string;
    description: string;
}

export interface VNPTEPayProvidersQueryOptions {
    providerCode: string;
    providerName: string;
    validYn : string;
}

export interface VNPTEPayPIN {
    id: number;
    giftTitle: string;
    brandName: string;
    price: number;
    quantity: number;
    createDate: string;
}

export interface VNPTEPayPINPurchase{
    brandId: string;
    giftId: number;
    quantity: number;
}

export interface VNPTEPayBalance {
    balance: number;
}

export interface VNPTEPayBrandPaginator extends PaginatorInfoEV<VNPTEPayProvider> { }
export interface VNPTEPayGiftPaginator extends PaginatorInfoEV<VNPTEPayGift> { }
export interface VNPTEPayPINPaginator extends PaginatorInfoEV<VNPTEPayPIN> { }

/**
 * GiftPop
 */
export interface GiftPopBrandQueryOptions {
    brandCode: string;
    brandName: string;
}

export interface GiftPopBrand {
    brandCode: string;
    brandName: string;
    brandLogo: string;
}

export interface GiftPopGood extends CommonFields {
    goodsId: string;
    goodsName: string;
    brandLogo: string;
    enGoodsName: string;
    krGoodsName: string;
    brandCode: string;
    brandName: string;
    originImg: string;
    listPrice: number;
    salePrice: number;
    shopPrice: number;
    saleRate: number;
    commissionRate: number;
    goodsType: string;
    saleFeeType: string;
    useYN: string;
    modDate: string;
    commtGuide: string;
    expiryDate: string;
    expiryDateEn: string;
    expiryDateKr: string;
    cateCode: string;
    stock: number;
    email: string;
    hotline: string;
    commtProduct: string;
    commtProductEn: string;
    commtGuideEn: string;
    commtGuideKr: string;
}

export interface GiftPopBrandPaginator extends PaginatorInfoEV<GiftPopBrand> { }
export interface GiftPopGoodPaginator extends PaginatorInfoEV<GiftPopGood> { }

export interface Stock extends CommonFields {
  id: number;
  supplierId: string;
  supplierName: string;
  brandId: string;
  brandName: string;
  goodsId: number;
  goodsName: string;
  expireTime: string; // ISO string
  remainDays: number;
  totalQuantity: number;
  totalAmount: number;
  insertedAt: string; // ISO string
  diff1d: number;
  diff7d: number;
  diff30d: number;
}

export interface StockQueryOptions {
  insertedAtFrom: string;
}

/**
 * UrBox
 */
export const UrBoxGoodType = {
    "1": "Voucher tiền mặt",
    "2": "Giftset",
    "3": "Combo",
    "4": "Thẻ balance",
    "5": "Thẻ điện thoại",
    "7": "Thẻ điểm",
    "8": "Topup điểm",
    "9": "Vật lý",
    "10": "Item (Sản phẩm cụ thể)",
    "11": "Voucher khuyến mãi",
    "12": "Bảo hiểm",
    "14": "Lượt quay số",
    "15": "Premium Service",
    "16": "Deal",
    "19": "Link quà UrCard"
}
export interface UrBoxBrandQueryOptions {
    brandName: string;
    categoryTitle: string;
}

export interface UrBoxBrand {
    id: string; // same brandCode Giftpop
    title: string; // same brandName Giftpop
    banner: string;
    description: string;
    images: string; // same brandLogo Giftpop
    cat_id: string;
    cat_title: string;
    parent_cat_id: string;
    gift_count: number;
}

export interface UrBoxGood extends CommonFields {
    id: string;
    brand: string;
    brand_id: string;
    cat_id: string;
    cat_title: string;
    gift_id: string;
    title: string;
    type: string;
    price: number;
    point: number;
    view: number;
    quantity: number;
    stock: number;
    image: string;
    images: string;
    image_rectangle: string[];
    expire_duration: number;
    code_display: string;
    code_display_type: string;
    price_promo: number;
    start_promo: number;
    end_promo: number;
    is_promo: number;
    is_unfix: string;
    brandLogoLoyalty: string;
    brandImage: string;
    brand_name: string;
    brand_online: number;
    parent_cat_id: string;
    usage_check: string;
    content: string;
    note: string;
    code_quantity: number;
    office: UrBoxGoodOffice[];
}

export interface WataneGood extends CommonFields {
    code: string;
    name: string;
    type: string;
    value: string|number;
    price: string|number;
    packageType: string;
    description: string;
    validIn: number;
    directVoucher: number;
}
export const WataneGoodType = {
    "1": "Super evoucher",
    "2": "Single brand evoucher",
    "3": "Limited brands evoucher"
}
export const WatanePackageType = {
    "1": "Single voucher",
    "2": "Combo voucher",
}

export interface UrBoxGoodOffice {
    brand_id: string;
    city_id: string;
    district_id: string;
    ward_id: string;
    street_id: string;
    code: string;
    address: string;
    address_en: string;
    number: string;
    phone: string;
    latitude: string;
    longitude: string;
    geo: string;
    isApply: string;
    id: string;
    brand_img_src: string;
    brand_title: string;
    title_city: string;
}

export interface UrBoxBrandPaginator extends PaginatorInfoEV<UrBoxBrand> { }
export interface UrBoxGoodPaginator extends PaginatorInfoEV<UrBoxGood> { }
