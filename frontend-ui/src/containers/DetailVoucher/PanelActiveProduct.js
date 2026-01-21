import {
    Box,
    Button,
    FormControl,
    TextField,
    Typography
} from "@mui/material";
import { grey, red, yellow } from '@mui/material/colors';
import axios from "axios";
import moment from "moment/moment";
import { useEffect, useState } from "react";
import { Trans, useTranslation } from "react-i18next";
import OtpInput from 'react-otp-input';
import GlobalBackdrop from "../../components/GlobalBackdrop";
import CircleComponents from "../../components/ui/CircleComponents";
import DashedLine from "../../components/ui/DashedLine";
import { checkSerialNumber } from "../../utils/CommonUtils";
import { getCookieMobileEncode, getOtpExpireDtNew, setCookieMobileEncode } from "../../utils/cookie";
import useCountdown from "../../utils/useCountdown";
import { formatTimeSeconds, isValidVietnamesePhoneNumber, renderMessage } from "../../utils/utils";
import PanelActiveError from "./PanelActiveError";
import { InputOTP2, PanelActiveProductStyle } from "./styles";

const TIME_OTP_EXPIRE_MINUTE = process.env.REACT_APP_TIME_OTP_EXPIRE;

const PanelActiveProduct = ({ voucherId, onReload }) => {

    const dots = [
        { top: '250px' },
        { position: 'right', top: '250px' },
    ];

    const TEST = false;
    const { t } = useTranslation();
    const [loading, setLoading] = useState(false);

    const [isActive, setIsActive] = useState(false);

    const [serialNumber, setSerialNumber] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [expireDt, setExpireDt] = useState("");
    const [serialNumberWarning, setSerialNumberWarning] = useState('');

    // OTP
    const [otp, setOtp] = useState('');
    const [isGetOTP, setIsGetOTP] = useState(true);

    // Countdown
    const TIME_COUNTDOWN = TEST ? 10 : TIME_OTP_EXPIRE_MINUTE * 60;

    const [errorData, setErrorData] = useState(null);

    const createErrorData = (code, message) => ({
        errorCode: code,
        // errorMessage: message,
        errorMessage: renderMessage(code, t),
        onClose: () => setErrorData(null)
    });

    const handleCountdownComplete = () => {
        console.log('Countdown completed!');
    };

    const { start: startCountdown, stop: stopCountdown, time: timeCountdown, isRunning: isCountdownRunning, reset: resetCountdown } = useCountdown(TIME_COUNTDOWN, handleCountdownComplete);
    // console.log(`timeCountdown: ${timeCountdown}, isCountdownRunning: ${isCountdownRunning}`);
    const handleSerialNumberChange = (event) => {
        setSerialNumber(event.target.value);
        setSerialNumberWarning(''); // Clear warning when user starts typing
    };

    const handlePhoneNumberChange = (event) => {
        setPhoneNumber(event.target.value);
    };

    const createOTP = async () => {
        console.log(`createOTP: ${phoneNumber}`);

        // Step 0: Validate input
        if (!serialNumber?.trim()) {
            console.error('Serial number is required.');
            setSerialNumberWarning(`${t('voucher.serialNumber_required')}`);
            return;
        }

        // Step 2: Check if serialNumber exists
        try {
            setSerialNumberWarning(''); // Clear warning before proceeding

            const checkSerialNumberBody = {
                voucherId: voucherId,
                serialNumber: serialNumber.trim(),
            };

            const checkSerialResponse = await axios.post(
                `${process.env.REACT_APP_VOUCHER_API_VIEWER}/v2/vouchers/checkSerialNumber`,
                checkSerialNumberBody
            );

            const { data } = checkSerialResponse?.data || {};

            if (!data) {
                console.error('Serial number check failed or serial number does not exist.');
                setSerialNumberWarning(`${t('voucher.serialNumber_invalid')}`);
                return;
            }
        } catch (error) {
            console.error('Error checking serial number:', error);
            setSerialNumberWarning(`${t('voucher.serialNumber_invalid')}`);
            return;
        }

        // Step 3: Create OTP
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
                console.log(`createOTP response: ${JSON.stringify(res)}`);
                setLoading(false);

                // Reset countdown
                resetCountdown();
                startCountdown();

                // Update cookie new expire date (still old otp)
                const newOtpExpireDt = getOtpExpireDtNew();
                setExpireDt(newOtpExpireDt);
                setCookieMobileEncode(phoneNumber, { otpExpireDt: newOtpExpireDt });
            }).catch((e) => {
                console.log(`createOTP error: ${JSON.stringify(e)}`);
                setLoading(false);
            })
        }
    }

    const activateVoucher = () => {
        console.log(`activateVoucher: ${serialNumber}, ${phoneNumber}, ${otp}`);
        stopCountdown();

        if (TEST) {
            onReload(otp);

            // For test
            // setErrorData(createErrorData(1234, 'Voucher not found by serial number'));
        } else {
            setLoading(true);
            axios.post(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/v2/vouchers/activate`, {
                voucherId: voucherId,
                serialNumber: serialNumber?.trim(),
                phoneNumber: phoneNumber?.trim(),
                otp: otp
            }).then((res) => {
                console.log(`activateVoucher response: ${JSON.stringify(res)}`);
                setLoading(false);

                // Check if status code is 200
                if (res.status === 200) {
                    onReload(otp);
                } else {
                    setErrorData(createErrorData(res?.data?.code, res?.data?.message));
                }
            }).catch((e) => {
                console.log(`activateVoucher error: ${JSON.stringify(e)}`);
                setLoading(false);

                // Access error response data
                if (e.response) {
                    // Server responded with a status other than 2xx
                    console.log(`Error response: ${JSON.stringify(e.response.data)}`);
                    setErrorData(createErrorData(e.response.data?.code, e.response.data?.message));
                } else {
                    // No response received (e.g., network error)
                    setErrorData(createErrorData(e?.code || 'NETWORK_ERROR', e?.message || 'An unknown error occurred'));
                }
            })
        }
    }

    const getOtpDescription = () => {
        if (expireDt && isCountdownRunning) {
            return <Trans
                i18nKey="voucher.voucher_active_otp_notification_resend"
                values={{ expireDt: moment(expireDt).format('DD/MM/YYYY'), timeOTP: formatTimeSeconds(timeCountdown) }}
                components={{ strong: <strong />, redText: <span style={{ color: 'red' }} /> }}
            />
        } else {
            return <>{t('voucher.voucher_active_otp_notification')}</>;
        }
    }

    useEffect(() => {
        // console.log(`isCountdownRunning: ${isCountdownRunning}, isSentOTP: ${isSentOTP}, errorData: ${JSON.stringify(errorData)}`);
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
        // If countdown is running, disable get otp
        if (isCountdownRunning) {
            setIsGetOTP(false);
            return;
        }

        // If countdown completed, is valid phone number will enable get otp
        setIsGetOTP(isValidVietnamesePhoneNumber(phoneNumber.trim()));
    }, [phoneNumber, expireDt, isCountdownRunning]);

    useEffect(() => {
        if (checkSerialNumber(serialNumber?.trim()) && isValidVietnamesePhoneNumber(phoneNumber?.trim()) && otp?.length === 6) {
            setIsActive(true);
        } else {
            setIsActive(false);
        }
    }, [serialNumber, phoneNumber, otp]);

    useEffect(() => {
        const isValidPhone = isValidVietnamesePhoneNumber(phoneNumber?.trim());
        if (!isValidPhone) {
            setExpireDt(null);
            stopCountdown();
            resetCountdown();
            return;
        }

        const checkCookieValue = async () => {
            const cookieValue = await getCookieMobileEncode(phoneNumber);
            if (cookieValue?.otpExpireDt) {
                setExpireDt(cookieValue.otpExpireDt);
            }
        };

        checkCookieValue();

    }, [phoneNumber]);

    return (
        <>
            {errorData && <PanelActiveError errorData={errorData} />}
            {!errorData &&
                <PanelActiveProductStyle>
                    <GlobalBackdrop isLoading={loading} />
                    <div className="container">
                        <CircleComponents dots={dots} />

                        <Box display="flex" position={'relative'} sx={{ backgroundColor: 'white', borderRadius: '20px' }} boxSizing={'border-box'} flexDirection="column" alignItems="flex-start" width="100%" paddingX={{ xs: 4, md: 6 }} paddingY={2}>
                            {/* Row title */}
                            <Box width="100%" textAlign="center" mb={2}>
                                <Typography variant="h4" component="h1" gutterBottom sx={{
                                    fontWeight: 500,
                                    fontSize: '1.55rem'
                                }}>
                                    {t('voucher.voucher_active_title_1')}
                                </Typography>
                                <Typography variant="body2" sx={{
                                    color: 'text.secondary',
                                    fontSize: '0.75rem',
                                }}>
                                    {t('voucher.voucher_active_title_2')}
                                </Typography>
                            </Box>

                            {/* Row Dashed Line */}
                            <Box display="flex" flexDirection="column" width="100%" mb={2}>
                                <DashedLine height="3px" dashLength="20" gapLength="10" color="#D7D5D5" />
                            </Box>

                            {/* Row Serial Number */}
                            <Box display="flex" flexDirection="column" width="100%" mb={1}>
                                <Box display="flex" alignItems="center">
                                    <Typography variant="h8" fontWeight={400} component="label" htmlFor="serial-number">
                                        {t('voucher.serialNumber')}
                                    </Typography>
                                    <Typography variant="h8" fontWeight={400} component="span" color={red[500]} paddingLeft={0.5}>
                                        *
                                    </Typography>
                                </Box>

                                <FormControl variant="outlined" fullWidth margin="normal" sx={{ marginTop: '10px' }}>
                                    <TextField
                                        id="serial-number"
                                        variant="outlined"
                                        fullWidth
                                        value={serialNumber}
                                        onChange={handleSerialNumberChange}
                                        InputProps={{
                                            style: { padding: '4px 8px', borderRadius: '8px' },
                                        }}
                                    />
                                </FormControl>

                                {/* Warning Text */}
                                {serialNumberWarning && (
                                    <Typography variant="body2" color={red[500]} mt={0.5}>
                                        {serialNumberWarning}
                                    </Typography>
                                )}
                            </Box>

                            {/* Row Phone Number */}
                            <Box display="flex" flexDirection="column" width="100%" mb={2}>
                                <Box display="flex" alignItems="center">
                                    <Typography variant="h8" fontWeight={400} component="label" htmlFor="phone-number">
                                        {t('voucher.phoneNumber')}
                                    </Typography>
                                    <Typography variant="h8" fontWeight={400} component="span" color={red[500]} paddingLeft={0.5}>
                                        *
                                    </Typography>
                                </Box>

                                <FormControl variant="outlined" fullWidth margin="normal" sx={{ marginTop: '10px' }}>
                                    <TextField
                                        id="phone-number"
                                        variant="outlined"
                                        fullWidth
                                        type="tel"
                                        value={phoneNumber}
                                        onChange={handlePhoneNumberChange}
                                        InputProps={{
                                            style: { padding: '4px 8px', borderRadius: '8px' },
                                        }}
                                    />
                                </FormControl>
                            </Box>

                            {/* Row Get OTP */}
                            <Box display="flex" width="100%" mb={2} alignItems="center">
                                <Box width="75%" pr={1}>
                                    <Typography
                                        variant="body2"
                                        sx={{
                                            color: grey[600],
                                            fontSize: '0.775rem',
                                            opacity: 0.8,
                                        }}
                                    >
                                        {getOtpDescription()}
                                    </Typography>
                                </Box>
                                <Box width="25%" display="flex" justifyContent="flex-end">
                                    <Button
                                        variant="contained"
                                        sx={{
                                            backgroundColor: yellow[600],
                                            color: '#6B7280',
                                            border: '1px solid gray',
                                            borderRadius: '10px',
                                            fontSize: '0.8rem',
                                            fontWeight: 600,
                                            padding: '8px 6px',
                                            '&:hover': {
                                                backgroundColor: yellow[700],
                                            }
                                        }}
                                        disabled={!isGetOTP}
                                        onClick={() => createOTP()}
                                    >
                                        {t('voucher.buttonGetOtp')}
                                    </Button>
                                </Box>
                            </Box>

                            {/* Row input OTP */}
                            <Box display="flex" width="100%" mb={2} flexDirection="column">
                                <Box display="flex" alignItems="center">
                                    <Typography variant="h8" fontWeight={400} component="label" htmlFor="otp">
                                        OTP
                                    </Typography>
                                    <Typography variant="h8" fontWeight={400} component="span" color={red[500]} paddingLeft={0.5}>
                                        *
                                    </Typography>
                                </Box>
                                <FormControl variant="outlined" fullWidth margin="normal" sx={{ marginTop: '10px', textAlign: 'center', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                                    <Box display="flex" justifyContent="space-between" width="100%">
                                        <OtpInput
                                            value={otp}
                                            onChange={setOtp}
                                            numInputs={6}
                                            inputType="number"
                                            renderSeparator={null}
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
                                    onClick={() => activateVoucher()}
                                >
                                    {t("voucher.buttonActive")}
                                </Button>
                            </Box>
                        </Box>
                    </div>
                </PanelActiveProductStyle>}
        </>
    )
}

export default PanelActiveProduct;