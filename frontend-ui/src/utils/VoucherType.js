const VoucherType = {
    SI: 'SI',
    PP: 'PP',
    CH: "CH",
    BK: "BK",
    LC: 'LC',
    DC: 'DC',
}

export default VoucherType;

export const getTypeVoucherString = (voucherType) => {
    if (voucherType === VoucherType.SI) {
        return "Single Item";
    } else if (voucherType === VoucherType.PP) {
        return "Pre-Paid";
    } else if (voucherType === VoucherType.DC) {
        return "Discount";
    } else if (voucherType === VoucherType.LC) {
        return "Limited Count";
    } else if (voucherType === VoucherType.CH) {
        return "Choice";
    } else if (voucherType === VoucherType.BK) {
        return "Bulk";
    } else {
        return null
    }
}