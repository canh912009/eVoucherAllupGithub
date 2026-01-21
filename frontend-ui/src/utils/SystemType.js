
const SystemType = {
    UR_BOX: 'UR_BOX',
    GIFTPOP: 'GIFTPOP',
    INTERNAL: "INTERNAL",
    EXTERNAL: "EXTERNAL",
    CHOICE: 'CHOICE',
    BULK: 'BULK',
    VNPT_EPAY: 'VNPT_EPAY',
    WATANE: 'WATANE'
}

const hasLogoTypes = [
    SystemType.UR_BOX, SystemType.GIFTPOP, SystemType.VNPT_EPAY
]


const SUPPLIERS = {
    VIET_UNION_CORP_ID: '0305458683'
}

export default SystemType;
export { hasLogoTypes, SUPPLIERS };