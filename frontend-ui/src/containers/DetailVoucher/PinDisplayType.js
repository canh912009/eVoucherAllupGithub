import SystemType from "../../utils/SystemType";

const PIN_DISPLAY_TYPE = {
    QRCODE : 'QRCODE',
    BARCODE : 'BARCODE',
    BARCODE_39 : 'BARCODE_39',
    TEXT : 'TEXT',
    QRBAR : 'QRBAR'
}

const getDefaultPinDisplay = (system) => {
    switch (system) {
        case SystemType.INTERNAL:
            return PIN_DISPLAY_TYPE.QRBAR;
        default:
            return PIN_DISPLAY_TYPE.BARCODE;
    }
}

const getDisplayType = (systemType, type, isPosLink) => {
    console.log("display type :", systemType, type, type ? type : getDefaultPinDisplay(systemType))
    console.log("type == urbox", systemType === SystemType.UR_BOX)
    if (systemType === SystemType.UR_BOX) {
        console.log('urbox')
        return type ? type : getDefaultPinDisplay(systemType);
    } else if (systemType === SystemType.INTERNAL) { 
        if (PIN_DISPLAY_TYPE.BARCODE === type) {
            return PIN_DISPLAY_TYPE.BARCODE;
        }
        return isPosLink ? PIN_DISPLAY_TYPE.QRBAR : PIN_DISPLAY_TYPE.QRCODE
    } else if (systemType === SystemType.EXTERNAL) {
        if (PIN_DISPLAY_TYPE.BARCODE_39 === type) {
            return PIN_DISPLAY_TYPE.BARCODE_39;
        }
        return PIN_DISPLAY_TYPE.BARCODE;
    } else {
        return getDefaultPinDisplay(systemType);
    }
}

export default PIN_DISPLAY_TYPE;
export {getDisplayType};