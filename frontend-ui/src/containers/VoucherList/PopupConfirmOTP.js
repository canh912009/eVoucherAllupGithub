import React, {useState} from 'react';
import {Trans, useTranslation} from "react-i18next";
import {Backdrop} from "@mui/material";
import {MdClear} from "react-icons/md";
import IMAGE_OTP from "../../images/sendOTP.png";
import OtpInput from "react-otp-input";
import {InputOTP} from "./styles";
import Countdown, {zeroPad} from "react-countdown";
import moment from "moment/moment";

const PopupConfirmOTP = ({onClose, onClickContinue, error, time, createOTP, expireDt}) => {
    const {t, i18n} = useTranslation()
    const [otp, setOtp] = useState('');
    const [isResend, setIsResend] = useState(expireDt != null);

    const checkExpriceDate = () => {
        const time1 = moment(time);
        const time2 = moment();

        if (time1.isBefore(time2)) {
            return false
        } else if (time1.isAfter(time2)) {
            return true
        } else {
            return null
        }
    }
    const renderer = ({hours, minutes, seconds}) => (
        <span>
    {zeroPad(minutes)}:{zeroPad(seconds)}
  </span>
    );
    return (
        <Backdrop
            sx={{color: '#fff', zIndex: 10}}
            open={true}
        >
            <div style={{
                backgroundColor: "white",
                color: "black",
                width: "90vw",
                maxWidth: "500px",
                borderRadius: "12px",
                paddingBottom: "16px",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                position: "relative",
                fontFamily: "Roboto"
            }}>
                <div style={{display: "flex", justifyContent: "center"}}>
                    <MdClear onClick={onClose} size={24}
                             style={{position: "absolute", right: "16px", top: "16px", color: "gray"}}/>
                    <img src={IMAGE_OTP} style={{width: "100px", height: "100px", marginTop: "44px"}} alt=""/>
                </div>
                <div style={{
                    textAlign: "center",
                    fontFamily: "Roboto",
                    fontSize: "16px",
                    margin: "42px 24px 0",
                    fontWeight: "bold"
                }}>
                    {t('voucher.titleCheckOTP')}
                </div>
                <div style={{textAlign: "center", fontFamily: "Roboto", fontSize: "14px", margin: "12px 24px 0"}}>
                    {t('voucher.descCheckOTP')}
                </div>

                <div style={{textAlign: "center", fontFamily: "Roboto", fontSize: "12px", margin: "12px 24px 0"}}>
                    <div style={{textAlign: "center", marginTop: "20px"}}>
                        <Trans
                            i18nKey="voucher.subOTPdesc"
                            values={{ expireDt: expireDt ?? moment(time || undefined).add(30, 'days').format('YYYY-MM-DD') }}
                            components={{ strong: <strong /> }}
                        />
                    </div>
                </div>

                {(checkExpriceDate() && isResend === false) &&
                    <div style={{textAlign: "center", fontFamily: "Roboto", fontSize: "12px", margin: "12px 24px 0"}}>
                        <div style={{textAlign: "center"}}>
                            {t('voucher.textCancelOTPTime')}
                            <span
                                style={{color: "red"}}>
                            {" "}
                                <Countdown
                                    onComplete={() => {
                                        setIsResend(true)
                                    }}
                                    renderer={renderer}
                                    date={time}/>
                        </span>
                        </div>
                    </div>}

                {(checkExpriceDate() !== true || isResend) && <div style={{
                    textAlign: "center",
                    fontFamily: "Roboto",
                    fontSize: "16px",
                    margin: "24px 24px 0",
                    display: "flex",
                    fontStyle: "italic",
                    fontWeight: "bold"
                }}>
                    <div>
                        {t('voucher.titleResendOTP')}
                    </div>
                    {':'}
                    <div style={{marginLeft: "4px", color: "red", cursor: "pointer"}} onClick={() => {
                        setIsResend(false)
                        createOTP()
                    }}>
                        {t('voucher.resendOTP')}
                    </div>
                </div>}

                <div style={{
                    display: "flex",
                    justifyContent: "space-between",
                    marginTop: "24px",
                    width: "calc(100% - 32px)"
                }}>
                    <OtpInput
                        value={otp}
                        onChange={(e) => {
                            setOtp(e)
                        }}
                        inputType={"number"}
                        inputStyle={{width: "15%"}}
                        numInputs={6}
                        renderSeparator={false}
                        renderInput={(props) => <InputOTP {...props} />}
                    />
                </div>

                {error ? <div style={{textAlign: "center", color: "red", height: "16px", padding: "12px 0"}}>
                    {error}
                </div> : <div style={{height: "16px", padding: "12px 0"}}></div>}

                <div style={{display: "flex", justifyContent: "center", width: "100%"}}>
                    <button
                        onClick={() => {
                            onClickContinue(otp)
                        }}
                        disabled={otp.length !== 6}
                        style={{
                            width: "calc(100% - 32px)",
                            height: "40px",
                            borderRadius: "16px",
                            fontWeight: 'bold',
                            border: "unset",
                            color: otp.length === 6 ? "#383A42" : "white",
                            backgroundColor: otp.length === 6 ? "#FDDF47" : "gray",
                            fontSize: "15px",
                            cursor: otp.length === 6 ? "pointer" : "no-drop"
                        }}>
                        {t('voucher.continue')}
                    </button>
                </div>
            </div>
        </Backdrop>
    );
};

export default PopupConfirmOTP;