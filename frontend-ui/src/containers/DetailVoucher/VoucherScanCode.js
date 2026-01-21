import React from "react";
import AQUA_LOGO from "../../images/AQUA_LOGO.png";
import URBOX_LOGO from "../../images/ur_box_logo.png";
import GIFTPOP_LOGO from "../../images/gitfpop_logo.png";
import VIET_UNION_CORP_LOGO from "../../images/VIET_UNION_CORP_0305458683_logo.jfif";
import WATANE_LOGO from "../../images/WATANE_LOGO.jfif";
import BG_QRCODE from "../../images/bg_qr_code.png";
import Barcode from "react-barcode";
import Slider from "react-slick";
import QRCode from "react-qr-code";
import Countdown from "react-countdown";
import {styled} from "@mui/material/styles";
import {Button} from "@mui/material";

import PIN_DISPLAY_TYPE, {getDisplayType} from "./PinDisplayType";
import SystemType, { hasLogoTypes, SUPPLIERS } from "../../utils/SystemType";
import { getHasScanCodeStatus, usableStatus } from "../../utils/VoucherStatus";
import { AQUA_HOTLINE, getImageSrc } from "../../utils/utils";

const BootstrapButton = styled(Button)({
    boxShadow: "none",
    textTransform: "none",
    border: "1px solid",
    lineHeight: 1.5,
    backgroundColor: "#4c3d3d",
    borderColor: "#4c3d3d",
    fontFamily: [
        "-apple-system",
        "BlinkMacSystemFont",
        '"Segoe UI"',
        "Roboto",
        '"Helvetica Neue"',
        "Arial",
        "sans-serif",
        '"Apple Color Emoji"',
        '"Segoe UI Emoji"',
        '"Segoe UI Symbol"',
    ].join(","),
    "&:hover": {
        backgroundColor: "#4c3d3d",
        borderColor: "#4c3d3d",
        boxShadow: "none",
    },
    "&:active": {
        boxShadow: "none",
        backgroundColor: "#4c3d3d",
        borderColor: "#4c3d3d",
    },
    "&:focus": {
        boxShadow: "0 0 0 0.2rem rgba(0,123,255,.5)",
    },
});

const getScanCodeValueByType = (systemType, otp, pinNo, displayType, serialNo) => { 
    switch (systemType) { 
        case SystemType.INTERNAL:
            if (PIN_DISPLAY_TYPE.BARCODE === displayType) {
                return serialNo;
            }
            return otp;
        case SystemType.EXTERNAL:
        case SystemType.GIFTPOP:
        case SystemType.UR_BOX:
        case SystemType.WATANE:    
            return pinNo;
        default:
            return null;
    }
}

const validStatus = getHasScanCodeStatus();

const includePassword = (t, pinPassword, systemType) => {
    return pinPassword && (
        <div
            style={{
                display: "flex",
                justifyContent: "center",
                marginTop: "0px",
                alignItems: "center",
                alignContent: "center",
            }}
        >
            <div
                style={{
                    marginRight: "4px",
                    color: "black",
                }}
            >

                {hasLogoTypes.includes(systemType) ?
                    t("voucher.pinSerial")
                    : t("voucher.pinCode")}
            </div>
            <div
                style={{
                    padding: "3px 3px",
                    color: "black",
                }}
            >
                {pinPassword}
            </div>
        </div>
    );
}

const includeReloadButton = (isIncludeReloadButton, t) => { 
    return (isIncludeReloadButton && <div
            style={{
                width: "180px",
                height: "180px",
                textAlign: "center",
                lineHeight: "180px",
            }}
        >
            <BootstrapButton
                sx={{ backgroundColor: "orange" }}
                variant="contained"
                onClick={() => {
                    window.location.reload();
                }}
                className="button_action"
            >
                {t("voucher.createNewQR")}
            </BootstrapButton>
        </div>
                            
    )
}

const renderBarCode = (dataVoucher, systemType, codeValue, t, pinPassword, displayType) => { 
    // console.log("renderBarCode", dataVoucher, systemType, codeValue, t, pinPassword, displayType);

    return (<div
        style={{
            width: "320px",
            margin: "0 auto",
        }}
    >
        <div style={{position: "relative"}}>

            {dataVoucher?.goods?.brand?.logoUrl && (
                <div style={{ 
                    position: "absolute", 
                    width: "100%", 
                    height: "98px", 
                    top: "68px", 
                    display: 'flex', 
                    justifyContent: 'center', 
                    alignItems: 'center'
                }}>
                    <img
                        style={{
                            width: "100px",
                            height: "auto",
                            position: 'absolute',
                            top: '48%',
                            left: '50%',
                            transform: 'translate(-50%, -50%)'
                        }}
                        src={getImageSrc(dataVoucher?.goods?.brand?.logoUrl)}
                        alt=""
                    />
                </div>
            )}
            <img style={{width: "320px"}} src={BG_QRCODE} alt=""/>
            <div
                style={{
                    position: "absolute",
                    zIndex: "2",
                    top: "22%",
                    width: "320px",
                    display: "flex",
                    justifyContent: "start",
                    flexDirection: "column",
                    alignItems: "center",
                }}
            >
                <div
                    style={{
                        height: "fit-content",
                        display: "flex",
                        flexDirection: "column",
                        justifyContent: "center",
                        marginTop: "60px",
                        maxWidth: "300px",
                        overflow: "hidden",
                        alignItems: "center",
                    }}
                >
                    <div style={{ width: "80%", display: "flex", justifyContent: "center" }}>
                        {displayType === PIN_DISPLAY_TYPE.BARCODE_39 ? (
                            <Barcode value={codeValue} format="code39" />
                        ) : (
                            <Barcode value={codeValue} />
                        )}
                    </div>
                </div>
                {/* show external pin password */}
                {includePassword(t, pinPassword, systemType)}
            </div>

            <div
                style={{
                    position: "absolute",
                    zIndex: "2",
                    top: "86%",
                    width: "100%",
                    display: "flex",
                    justifyContent: "start",
                    flexDirection: "column",
                    alignItems: "center"
                }}
            >
                <span style={{fontSize: "13px", color: "red"}}>{t("voucher.hotline")}: {AQUA_HOTLINE}</span>
            </div>
        </div>
    </div>);
}

const getImageLogoByType = (dataVoucher, displayType) => {
    const logoStyles = {width: "93px", position: "absolute", top: "85px", left: "34%"};

    if (SystemType.INTERNAL === dataVoucher?.system) {
        if (PIN_DISPLAY_TYPE.BARCODE === displayType) {
            return (
                <img
                    style={{width: "140px", position: "absolute", top: "115px", left: "28%"}}
                    src={AQUA_LOGO}
                    alt=""
                />
            )
        }
    }
    if (SystemType.GIFTPOP === dataVoucher?.system) {
        return (
            <img
                style={logoStyles}
                src={GIFTPOP_LOGO}
                alt=""
            />
        )
    }
    if (SystemType.UR_BOX === dataVoucher?.system) {
        return (
            <img
                style={logoStyles}
                src={URBOX_LOGO}
                alt=""
            />
        )
    }
    if (SystemType.WATANE === dataVoucher?.system) {
        return (
            <img
                style={logoStyles}
                src={WATANE_LOGO}
                alt=""
            />
        )
    }
    if (SUPPLIERS.VIET_UNION_CORP_ID === dataVoucher?.goods?.brand?.supplier?.id) {
        return (
            <img
                style={logoStyles}
                src={VIET_UNION_CORP_LOGO}
                alt=""
            />
        )
    }
    return null;
};


const renderQrCode = (dataVoucher, systemType, codeValue, scanResult, pinPassword, isShowRerenderButton, t) => {
    console.log("render qr code", dataVoucher, systemType, codeValue, scanResult);

    const qrSize = dataVoucher?.goods?.brand?.logoUrl ? 160 : 200;

    return <div style={{ width: "320px", margin: "0 auto" }}>
        <div style={{ position: "relative" }}>
            {dataVoucher?.goods?.brand?.logoUrl && (
                <div style={{ 
                    position: "absolute", 
                    width: "100%", 
                    height: "98px", 
                    top: "68px", 
                    display: 'flex', 
                    justifyContent: 'center', 
                    alignItems: 'center'
                }}>
                    <img  
                        style={{
                            height: 'auto',
                            width: '100px',
                            position: 'absolute',
                            top: '40%',
                            left: '50%',
                            transform: 'translate(-50%, -50%)'
                        }}
                        src={getImageSrc(dataVoucher?.goods?.brand?.logoUrl)}
                        alt=""
                    />
                </div>
                
            )}
            <img
                style={{ width: "320px" }}
                src={BG_QRCODE}
                alt=""
            />
            <div
                style={{
                    position: "absolute",
                    zIndex: "2",
                    top: "19%",
                    width: "320px",
                    display: "flex",
                    justifyContent: "start",
                    flexDirection: "column",
                    alignItems: "center"
                }}
            >

                <div
                    style={{
                        height: "fit-content",
                        display: "flex",
                        flexDirection: "column",
                        justifyContent: "center",
                        marginTop: "60px",
                        maxWidth: "300px",
                        overflow: "hidden",
                        alignItems: "center"
                    }}
                >
                    {(scanResult && !isShowRerenderButton) && (
                        <div style={{ width: "80%", display: "flex", justifyContent: "center", flexDirection: "column" }}>
                            <QRCode
                                style={{
                                    height: "auto",
                                    width: "80%",
                                    maxWidth: "250px",
                                    margin: "0 auto"
                                }}
                                value={scanResult}
                            />
                            <div
                                style={{
                                    padding: "3px 3px",
                                    color: "black",
                                    textAlign: "center"
                                }}
                            >
                                {codeValue}
                            </div>
                        </div>
                    )}
                    {includeReloadButton(isShowRerenderButton, t)}
                </div>
                {includePassword(t, pinPassword, systemType)}
            </div>

            <div
                style={{
                    position: "absolute",
                    zIndex: "2",
                    top: "86%",
                    width: "320px",
                    display: "flex",
                    justifyContent: "start",
                    flexDirection: "column",
                    alignItems: "center"
                }}
            >
                <span style={{fontSize: "13px", color: "red"}}>{t("voucher.hotline")}: {AQUA_HOTLINE}</span>
            </div>
        </div>
    </div>;
}

const renderScanCode = (
    dataVoucher,
    displayType,
    systemType,
    codeValue,
    scanResult,
    t,
    pinPassword,
    otp,
    isPosLink,
    status,
    isShowRerenderButton,
    settings
) => { 
    console.log("Render display", displayType,
    systemType,
    codeValue,
    scanResult,
    t,
    pinPassword,
    otp,
    isPosLink,
    status,
    settings)
    switch (displayType) {
        case PIN_DISPLAY_TYPE.BARCODE:
        case PIN_DISPLAY_TYPE.BARCODE_39:
        case PIN_DISPLAY_TYPE.TEXT:
            return renderBarCode(dataVoucher, systemType, codeValue, t, pinPassword, displayType);
        case PIN_DISPLAY_TYPE.QRCODE:
            return renderQrCode(dataVoucher, systemType, codeValue, scanResult, pinPassword, isShowRerenderButton, t);
        case PIN_DISPLAY_TYPE.QRBAR:
            return <Slider {...settings}>
                <div>
                    {renderQrCode(dataVoucher, systemType, codeValue, scanResult, pinPassword, isShowRerenderButton, t)}
                </div>

                <div>
                    {renderBarCode(dataVoucher, systemType, codeValue, t, pinPassword, displayType)}
                </div>
            </Slider>;
    }
}

const getScanResult = (systemType, status, otp, pinNo) => { 
    if (systemType === SystemType.INTERNAL) { 
        return (usableStatus.includes(status)
            ? `${process.env.REACT_APP_SELF_URL}/store-pos/${otp}`
            : `${process.env.REACT_APP_VOUCHER_API_VIEWER_STORE}/store-pos-cancel/${otp}`);
    } else {
        return pinNo;
    }
}

const getQrSize = (systemType, pinPassword) => { 
    if (hasLogoTypes.includes(systemType)) {
        return pinPassword ? "150" : "160";
    } else { 
        return "200";
    }
}



const VoucherScanCode = ({
    t,
    // dataVoucher?.voucherStatus
    status,
    // EXTERNAL | INTERNAL | URBOX | GIFTPOP
    // extPinDisplayType
    systemType,
    // default of INTERNAL is both QR & BARCODE, default for EXTERNAL is BARCODE
    displayType,
    // dataVoucher.externalPinPassword
    pinPassword,
    // extPinNo
    pinNo,
    serialNo,
    settings,
    // dataVoucher?.goods?.brand?.isPosLink
    otp,
    // dataVoucher?.goods?.brand?.isPosLink
    isPosLink,
    dataVoucher,
    renderTimeCancelUse
}) => {
    
    const pinDisplayType = getDisplayType(systemType, displayType, isPosLink);
    const isShowRerenderButton = (systemType === SystemType.INTERNAL && !otp);
    const scanResult = getScanResult(systemType, status, otp, pinNo);
    const codeValue = getScanCodeValueByType(systemType, otp, pinNo, displayType, serialNo);

    return (validStatus.includes(status)) && (
        <>
            {
                renderScanCode(
                    dataVoucher,
                    pinDisplayType,
                    systemType,
                    codeValue,
                    scanResult,
                    t,
                    pinPassword,
                    otp,
                    isPosLink,
                    status,
                    isShowRerenderButton,
                    settings
                
            )}

            {status === "USED" && (
                <div
                    style={{
                        textAlign: "center",
                        padding: "20px 0 ",
                        fontFamily: "Lato",
                        fontWeight: " 400",
                        letterSpacing: "0.308px",
                        width: "90%",
                        margin: "auto",
                    }}
                >
                    {t("voucher.textTimeOutCancel")}(
                    <span style={{ color: "red", fontWeight: "bold" }}>
                      {
                          <Countdown
                              onComplete={() => {}}
                              date={renderTimeCancelUse()}
                          />
                      }
                    </span>
                    )
                </div>
            )}
        </>
    );
}

export default VoucherScanCode;