const VoucherStatus = {
    NORMAL: "NORMAL",
    PART_USED: "PART_USED",
    USED: "USED",
    EXPIRE: "EXPIRE",
    DISABLED: "DISABLED"
}

export const TransferStatus = {
    RECPTED: "RECPTED",
    TRANSFER: "TRANSFER",
    RECPT_WAIT: "RECPT_WAIT"
}

const getHasScanCodeStatus = () => { 
    return [
        VoucherStatus.NORMAL,
        VoucherStatus.PART_USED,
        VoucherStatus.USED
    ];
}

const usableStatus = [
    VoucherStatus.NORMAL,
    VoucherStatus.PART_USED
];

export default VoucherStatus;
export { getHasScanCodeStatus, usableStatus };