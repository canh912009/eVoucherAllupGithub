import {
    Box,
    Button,
    FormControl,
    Link,
    Typography
} from "@mui/material";
import { yellow } from '@mui/material/colors';
import axios from "axios";
import moment from "moment/moment";
import { useEffect, useState } from "react";
import { Trans, useTranslation } from "react-i18next";
import OtpInput from 'react-otp-input';
import GlobalBackdrop from "../../components/GlobalBackdrop";
import LOGO from "../../images/topup_confirm.png";
import { getOtpExpireDtNew, setCookieMobileEncode } from "../../utils/cookie";
import useCountdown from "../../utils/useCountdown";
import { formatTimeSeconds, isValidVietnamesePhoneNumber } from "../../utils/utils";
import PanelOtpError from "./PanelOtpError";
import { InputOTP2, PanelOtpRequiredStyle } from "./styles";

const TIME_OTP_EXPIRE_MINUTE = process.env.REACT_APP_TIME_OTP_EXPIRE;

const PanelOtpRequired = ({ dataPre, onReload }) => {
    // console.log('PanelOtpRequired dataPre', dataPre);

    const TEST = false;
    const phoneNumber = dataPre?.phoneNumber;
    const { t } = useTranslation();
    const [loading, setLoading] = useState(false);
    const [errorData, setErrorData] = useState(null);
    const [otp, setOtp] = useState('');
    const [isActive, setIsActive] = useState(false);
    const [expireDt, setExpireDt] = useState(dataPre?.otpExpireDt);

    // Countdown
    const TIME_COUNTDOWN = TEST ? 6 : TIME_OTP_EXPIRE_MINUTE * 60;

    // const createErrorData = (code, message) => ({
    //     errorCode: code,
    //     errorMessage: message,
    //     onClose: () => setErrorData(null)
    // });

    const handleCountdownComplete = () => {
        console.log('Countdown completed!');
    };

    const { start: startCountdown, stop: stopCountdown, time: timeCountdown, isRunning: isCountdownRunning, reset: resetCountdown } = useCountdown(TIME_COUNTDOWN, handleCountdownComplete);
    // console.log(`timeCountdown: ${timeCountdown}, isCountdownRunning: ${isCountdownRunning}`);

    const getOtpDescription = () => {
        if (expireDt) {
            return <Trans
                i18nKey="voucher.voucher_confirm_otp_notification"
                values={{ expireDt: moment(expireDt).format('DD/MM/YYYY') }}
                components={{ strong: <strong /> }}
            />
        } else {
            return <Trans
                i18nKey="voucher.voucher_confirm_otp_notification_expire"
                components={{ strong: <strong /> }}
            />
        }
    }

    const getOtpDescriptionResend = () => {
        if (expireDt) {
            if (isCountdownRunning) {
                return (
                    <span style={{ opacity: '0.8' }}>
                        {t('voucher.voucher_confirm_otp_notification_donot_receive')} {' '}
                        <Trans
                            i18nKey="voucher.voucher_confirm_otp_notification_try_again"
                            values={{ timeOTP: formatTimeSeconds(timeCountdown) }}
                            components={{ strong: <strong />, redText: <span style={{ color: 'red' }} /> }}
                        />
                    </span>
                );
            } else {
                return (
                    <span style={{ opacity: '0.8' }}>
                        <Trans
                            i18nKey="voucher.voucher_confirm_otp_notification_donot_receive"
                            components={{ redText: <span style={{ opacity: '0.8' }} /> }}
                        />
                        <Link href="#" onClick={handleResendOtp} color="red" underline="hover" style={{ marginLeft: '8px', fontWeight: 'bold', textDecoration: 'underline' }}>
                            {t('voucher.resendOTP')}
                        </Link>
                    </span>
                );
            }

        } else {
            return (
                <Link href="#" onClick={handleResendOtp} color="red" underline="hover" style={{ marginLeft: '8px', fontWeight: 'bold', textDecoration: 'underline' }}>
                    {t('voucher.resendOTP')}
                </Link>
            )
        }
    }

    const handleResendOtp = async () => {
        if (TEST) {
            resetCountdown();
            startCountdown();

            const newOtpExpireDt = getOtpExpireDtNew();
            setExpireDt(newOtpExpireDt);
            setCookieMobileEncode(phoneNumber, { otpExpireDt: newOtpExpireDt });
        } else {
            setLoading(true);
            axios.post(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/v2/vouchers/createOtp`, {
                mobileNumber: phoneNumber
            }).then(async (res) => {
                console.log(`handleResendOtp response: ${JSON.stringify(res)}`);
                setLoading(false);

                // Reset countdown
                resetCountdown();
                startCountdown();

                // Update cookie new expire date (still old otp)
                const newOtpExpireDt = getOtpExpireDtNew();
                setExpireDt(newOtpExpireDt);
                setCookieMobileEncode(phoneNumber, { otpExpireDt: newOtpExpireDt });
            }).catch((e) => {
                console.log(`handleResendOtp error: ${JSON.stringify(e)}`);
                setLoading(false);
            })
        }
    }

    const handleConfirmOtp = () => {
        console.log(`handleConfirmOtp: ${otp}`);
        onReload(otp);
    }

    useEffect(() => {
        if (otp?.length === 6) {
            setIsActive(true);
        } else {
            setIsActive(false);
        }
    }, [otp]);

    useEffect(() => {
        // console.log(`isCountdownRunning: ${isCountdownRunning}, isOtpValid: ${isOtpValid}, isOtpSent: ${isOtpSent}, errorData: ${JSON.stringify(errorData)}`);
        if (errorData) {
            resetCountdown();
            return;
        }
        if (!isCountdownRunning && expireDt) {
            startCountdown();
            return;
        }
    }, [errorData, isCountdownRunning, expireDt]);

    useEffect(() => {
        const isValidPhone = isValidVietnamesePhoneNumber(phoneNumber?.trim());
        if (!isValidPhone) {
            setExpireDt(null);
            stopCountdown();
            resetCountdown();
            return;
        }
    }, [phoneNumber]);

    return (
        <>
            {errorData && <PanelOtpError errorData={errorData} />}
            {!errorData && (
                <PanelOtpRequiredStyle>
                    <GlobalBackdrop isLoading={loading} />
                    <div className="container">
                        <Box display="flex" position={'relative'} sx={{ backgroundColor: 'white', borderRadius: '20px' }} boxSizing={'border-box'} flexDirection="column" alignItems="flex-start" width="100%" paddingX={{ xs: 4, md: 6 }} paddingY={2}>
                            {/* Logo */}
                            <Box display="flex" justifyContent={"center"} width="100%" mt={"10px"} mb={"20px"}>
                                <Box
                                    component="img"
                                    src={LOGO}
                                    alt="Logo"
                                    sx={{
                                        display: 'flex',
                                        justifyContent: 'center',
                                        width: '140px',
                                        height: 'auto',
                                        borderRadius: '8px',
                                    }}
                                />
                            </Box>

                            {/* Header */}
                            <Box display="flex" flexDirection="row" justifyContent={"center"} width="100%" mb={1}>
                                <Typography variant="h6" fontWeight={600} fontSize={'1rem'} component="label">
                                    {t('voucher.otp_required')}
                                </Typography>
                            </Box>

                            {/* Note 1*/}
                            <Box display="flex" flexDirection="row" justifyContent={"center"} width="100%">
                                <Typography variant="body2" textAlign={"center"} fontWeight={400} fontSize={12} component="label" style={{ opacity: 0.8 }}>
                                    {getOtpDescription()}
                                </Typography>
                            </Box>

                            {/* Note 2*/}
                            <Box display="flex" flexDirection="row" justifyContent={"center"} width="100%">
                                <Typography variant="body2" textAlign={"center"} fontWeight={400} fontSize={12} component="label">
                                    {getOtpDescriptionResend()}
                                </Typography>
                            </Box>

                            {/* Row input OTP */}
                            <Box display="flex" width="100%" mt={4} mb={2} flexDirection="row" justifyContent="center">
                                <FormControl variant="outlined" fullWidth margin="normal" sx={{ marginTop: '10px' }}>
                                    <Box display="flex" justifyContent="center" width="100%">
                                        <OtpInput
                                            value={otp}
                                            onChange={setOtp}
                                            numInputs={6}
                                            inputType="number"
                                            renderSeparator={false}
                                            renderInput={(props, index) => <InputOTP2 {...props} index={index} />}
                                        />
                                    </Box>
                                </FormControl>
                            </Box>

                            {/* Row Button Active */}
                            <Box display="flex" flexDirection="column" width="100%">
                                <Button
                                    variant="contained"
                                    sx={{
                                        backgroundColor: yellow[600],
                                        color: 'black',
                                        textTransform: 'none',
                                        borderRadius: '8px',
                                        '&:hover': {
                                            backgroundColor: yellow[700]
                                        },
                                    }}
                                    disabled={!isActive}
                                    onClick={() => handleConfirmOtp()}
                                >
                                    {t("voucher.continue")}
                                </Button>
                            </Box>
                        </Box>
                    </div>
                </PanelOtpRequiredStyle>
            )}
        </>

    )
}

export default PanelOtpRequired;