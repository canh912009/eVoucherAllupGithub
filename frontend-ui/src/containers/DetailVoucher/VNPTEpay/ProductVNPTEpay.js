import {
    Box,
    Button,
    Checkbox,
    FormControl,
    FormControlLabel,
    FormGroup,
    MenuItem,
    Select,
    TextField,
    Typography
} from "@mui/material";
import { grey, red, yellow } from '@mui/material/colors';
import axios from "axios";
import { useEffect, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import GlobalBackdrop from "../../../components/GlobalBackdrop";
import CircleComponents from "../../../components/ui/CircleComponents";
import { REGEX_VIETNAMESE_PHONE_NUMBER, isVersionV2 } from "../../../utils/utils";
import { ProductVNPTEpayStyle } from "../styles";
import PopupConfirmVNPTEpay from "./PopupConfirmVNPTEpay";
import PopupErrorVNPTEpay from "./PopupErrorVNPTEpay";
import PopupWarningVNPTEpay from "./PopupWarningVNPTEpay";
import { ALLOWED_ACTIONS, TOPUP_OPTIONS, TYPE_CARD_CODE, VNPTEPAY_ERROR_CODE, getDataByErrorCode, getProvider, getTelcoName, getTypeCardCodeByProduct } from "./VNPTEpayUtils";

const ProductVNPTEpay = ({ dataPre, dataVoucher, otp, onReload }) => {
    // console.log(`ProductVNPTEpay dataPre: ${JSON.stringify(dataPre)}, otp: ${otp}`);

    const dots = [
        { top: '165px' },
        { position: 'right', top: '165px' },
    ];

    const testErrorPopup = false;

    const { t, i18n } = useTranslation();
    const [loading, setLoading] = useState(false);

    const [isInputValid, setIsInputValid] = useState(false);

    const vnptProducts = dataVoucher?.goods?.vnpt?.vnptProducts || [];
    const [vnptProduct, setVnptProduct] = useState({});
    const [phoneProvider, setPhoneProvider] = useState(null);

    // For check card code mobile data => original teleco name (not contain 'data')
    const [telcoNameSelect, setTelcoNameSelect] = useState(null);

    const [typeAction, setTypeAction] = useState('');
    const [allowedActions, setAllowedActions] = useState('');

    const [phone, setPhone] = useState(dataVoucher?.userMobileNumber || '');
    const phoneInputRef = useRef(null); // Ref for phone input

    const [errorData, setErrorData] = useState(null);

    const [isOpenWarning, setIsOpenWarning] = useState(false);
    const [isOpenConfirm, setIsOpenConfirm] = useState(false);


    const handleSelectChangeProvider = (event) => {
        const newProviderCode = event.target.value;
        const newVnptProduct = vnptProducts.find((p) => p.providerCode === newProviderCode);
        setVnptProduct(newVnptProduct);
        // setAllowedActions(newVnptProduct?.vnptProvider?.allowedActions || '');
        // setTypeAction('');
    };

    const handleCheckboxChange = (event) => {
        const { value } = event.target;
        const typeActionSelected = value === typeAction ? '' : value;

        setTypeAction(typeActionSelected);
        setPhone(dataVoucher?.userMobileNumber || '');
    };

    const handleInputChangePhone = (event) => {
        setPhone(event.target.value);
    };

    const isShowWarningPopup = () => {
        // If the provider of the phone number is not the same as the provider selected => show popup warning (If telco name is data will change to not data)
        return phoneProvider !== null && phoneProvider.providerCode !== null && phoneProvider.providerNm != null && phoneProvider.providerCode !== vnptProduct?.providerCode
            && telcoNameSelect != null && telcoNameSelect !== phoneProvider.providerNm;
    }

    const handleClickRedeem = () => {
        console.log(`handleClickRedeem typeAction: ${typeAction}, phone: ${phone}, phoneProvider: ${JSON.stringify(phoneProvider)}, vnptProduct: ${JSON.stringify(vnptProduct)}}`);

        if (typeAction === TOPUP_OPTIONS.CARDCODE) {
            const typeCard = getTypeCardCodeByProduct(vnptProduct);
            console.log(`typeCard: ${typeCard}`);

            if (isShowWarningPopup() && (typeCard === TYPE_CARD_CODE.CARD_MOBILE || typeCard === TYPE_CARD_CODE.CARD_MOBILE_DATA)) {
                setIsOpenWarning(true);
            } else {
                setIsOpenConfirm(true);
            }
        } else if (typeAction === TOPUP_OPTIONS.TOPUP) {
            const phoneTrim = phone ? phone.trim() : phone;
            const isValidPhone = REGEX_VIETNAMESE_PHONE_NUMBER.test(phoneTrim);
            console.log(`handleClickRedeem phone: ${phoneTrim}, isValidPhone: ${isValidPhone}`);

            if (isValidPhone) {
                if (isShowWarningPopup()) {
                    setIsOpenWarning(true);
                } else {
                    setIsOpenConfirm(true);
                }
            } else {
                // Show popup error: Incorrect phone
                setErrorData(getDataByErrorCode(VNPTEPAY_ERROR_CODE.INCORRECT_PHONE, '', t, phone, vnptProduct?.vnptProvider?.providerNm || vnptProduct?.providerCode));
            }
        }
    }

    const handleTestErrorPopup = (errorCode) => {
        if (testErrorPopup) {
            setErrorData(getDataByErrorCode(errorCode, 'Error message here', t, phone, vnptProduct?.vnptProvider?.providerNm || vnptProduct?.providerCode));
        }
    }

    const onConfirmWarning = () => {
        setIsOpenWarning(false);
        setIsOpenConfirm(true);
    }

    const onConfirmTopup = () => {
        console.log(`onConfirmTopup vnptProduct: ${JSON.stringify(vnptProduct)}, typeAction: ${typeAction}, phone: ${phone.trim()}`);
        const isV2 = isVersionV2(dataPre);

        const url = isV2 ? `${process.env.REACT_APP_VOUCHER_API_VIEWER}/v2/vnpt/purchase` : `${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/vnpt/purchase`;
        const payload = {
            ev: dataVoucher.id,
            providerCode: vnptProduct?.providerCode,
            action: typeAction,
            ...(typeAction === TOPUP_OPTIONS.TOPUP && { receiverPhoneNo: phone.trim() }),
            ...(isV2 && { otp })
        }
        console.log(`onConfirmTopup payload: ${JSON.stringify(payload)}`);

        setLoading(true);
        axios.post(url, {
            ...payload
        }).then((response) => {
            onCloseTopup();
            onReload();
            setTimeout(() => {
                setLoading(false);
            }, 1000)
        }).catch((error) => {
            onCloseTopup();
            setLoading(false);

            const errorCode = error?.response?.data?.code;
            const errorMessage = error?.response?.data?.message;

            setErrorData(getDataByErrorCode(errorCode, errorMessage, t, phone, vnptProduct?.vnptProvider?.providerNm || vnptProduct?.providerCode));
        })
    }

    const onCloseWarning = () => {
        setIsOpenWarning(false);
        setIsOpenConfirm(false);
        setErrorData(null);
    }
    const onCloseTopup = () => {
        setIsOpenConfirm(false);
        setErrorData(null);
    }
    const onCloseError = () => {
        if (errorData?.errorCode !== VNPTEPAY_ERROR_CODE.INCORRECT_PHONE) {
            onReload();
        }
        setErrorData(null);
    };

    useEffect(() => {
        if (vnptProduct) {
            setAllowedActions(vnptProduct?.vnptProvider?.allowedActions || '');
            setTypeAction('');

            const telcoName = getTelcoName(vnptProduct.providerCode);
            console.log(`providerCode: ${vnptProduct?.providerCode} => telcoName: ${telcoName}`);

            setTelcoNameSelect(telcoName || vnptProduct?.vnptProvider?.providerNm || vnptProduct?.providerCode);
        }
    }, [vnptProduct]);

    useEffect(() => {
        if (phone && phone.trim()) {
            const phoneProvider = getProvider(phone.trim());
            console.log(`phoneProvider: ${JSON.stringify(phoneProvider)}`);
            setPhoneProvider(phoneProvider);
        }
    }, [phone]);

    useEffect(() => {
        if (!vnptProduct?.providerCode || !typeAction) {
            setIsInputValid(false);
            return;
        }
        if (typeAction === TOPUP_OPTIONS.TOPUP && !phone.trim()) {
            setIsInputValid(false);
            return;
        }
        setIsInputValid(true);
    }, [phone, typeAction, vnptProduct]);

    useEffect(() => {
        if (typeAction === TOPUP_OPTIONS.TOPUP && phoneInputRef.current) {
            phoneInputRef.current.focus();
        }
    }, [typeAction]);

    useEffect(() => {
        if (!dataVoucher?.userMobileNumber || !vnptProducts) return;

        const findProvider = getProvider(dataVoucher.userMobileNumber);
        console.log(`phone: ${dataVoucher.userMobileNumber}, findProvider: ${JSON.stringify(findProvider)}`);

        if (!findProvider?.providerCode) {
            setVnptProduct(vnptProducts[0]);
            return;
        }

        const initVnptProduct = vnptProducts.find(
            (p) => p.providerCode === findProvider.providerCode || p.providerCode === findProvider.providerDataCode
        );

        console.log(`initVnptProduct: ${JSON.stringify(initVnptProduct)}`);

        setVnptProduct(initVnptProduct ?? vnptProducts[0]);
    }, [dataVoucher, vnptProducts]);

    return (
        <ProductVNPTEpayStyle>

            <GlobalBackdrop isLoading={loading} />

            <div className="container">

                <CircleComponents dots={dots} />

                <Box display="flex" position={'relative'} sx={{ backgroundColor: 'white', borderRadius: '20px' }} boxSizing={'border-box'} flexDirection="column" alignItems="flex-start" width="100%" paddingX={{ xs: 4, md: 6 }} paddingY={4}>
                    {/* Row Card Provider */}
                    <Box display="flex" flexDirection="column" width="100%" mb={2}>
                        <Box display="flex" alignItems="center">
                            <Typography variant="h8" fontWeight={400} component="label" htmlFor="card-provider">
                                {t('topup.card_provider')}
                            </Typography>
                            <Typography variant="h8" fontWeight={400} component="span" color={red[500]} paddingLeft={0.5}>
                                *
                            </Typography>
                        </Box>

                        <FormControl variant="outlined" fullWidth margin="normal" sx={{ marginTop: '10px' }}>
                            <Select
                                labelId="card-provider-label"
                                id="card-provider"
                                value={vnptProduct?.providerCode || ''}
                                onChange={handleSelectChangeProvider}
                                size="small"
                                sx={{ borderRadius: '8px' }}
                            >
                                {dataVoucher?.goods?.vnpt?.vnptProducts?.map((item) => (
                                    <MenuItem key={item.providerCode} value={item.providerCode}>{item.vnptProvider?.providerNm || item.providerCode}</MenuItem>
                                ))}

                            </Select>
                        </FormControl>

                        <Box display="flex" flexDirection="column" width="100%">
                            <Typography variant="body2" color={grey[600]}>
                                {vnptProduct?.description}
                            </Typography>
                        </Box>
                    </Box>

                    {/* Row Action */}
                    <Box display="flex" flexDirection="column" width="100%" mb={2}>
                        <Box display="flex" alignItems="center">
                            <Typography variant="h8" fontWeight={400} component="label" htmlFor="card-provider">
                                {t('topup.action')}
                            </Typography>
                            <Typography variant="h8" fontWeight={400} component="span" color={red[500]} paddingLeft={0.5}>
                                *
                            </Typography>
                        </Box>
                        <FormControl variant="outlined" fullWidth margin="normal" sx={{ marginTop: '10px' }}>
                            <FormGroup row sx={{ gap: 2 }}>
                                <Box
                                    display="flex"
                                    alignItems="center"
                                    justifyContent="center"
                                    border={1}
                                    borderRadius="8px"
                                    borderColor="grey.400"
                                    paddingLeft={{ xs: 2, md: 3 }}
                                    paddingY={1}
                                    flexGrow={1}
                                    width={i18n.language === 'en' ? '120px' : '100px'}
                                >
                                    <FormControlLabel
                                        control={
                                            <Checkbox
                                                checked={typeAction === TOPUP_OPTIONS.CARDCODE}
                                                disabled={!(allowedActions === ALLOWED_ACTIONS.CARDCODE_AND_TOPUP || allowedActions === ALLOWED_ACTIONS.CARDCODE)}
                                                onChange={handleCheckboxChange}
                                                value={TOPUP_OPTIONS.CARDCODE}
                                                color="primary"
                                                sx={{
                                                    paddingX: '2px',
                                                    paddingY: '1px',
                                                    borderRadius: '8px',
                                                    '&.Mui-checked': {
                                                        color: yellow[700],
                                                    },
                                                }}
                                            />
                                        }
                                        label={
                                            <Typography variant="body2" ml={1} color={(allowedActions === ALLOWED_ACTIONS.CARDCODE_AND_TOPUP || allowedActions === ALLOWED_ACTIONS.CARDCODE) ? '' : grey[300]}>
                                                {t('topup.phone_card')}
                                            </Typography>
                                        }
                                        style={{ flex: 1 }}
                                    />
                                </Box>
                                <Box
                                    display="flex"
                                    alignItems="center"
                                    justifyContent="center"
                                    border={1}
                                    borderRadius="8px"
                                    borderColor="grey.400"
                                    paddingLeft={{ xs: 2, md: 3 }}
                                    paddingY={1}
                                    flexGrow={1}
                                    width={i18n.language === 'en' ? '120px' : '140px'}
                                >
                                    <FormControlLabel
                                        border={1}
                                        borderColor="red.500"
                                        control={
                                            <Checkbox
                                                checked={typeAction === TOPUP_OPTIONS.TOPUP}
                                                disabled={!(allowedActions === ALLOWED_ACTIONS.CARDCODE_AND_TOPUP || allowedActions === ALLOWED_ACTIONS.TOPUP)}
                                                onChange={handleCheckboxChange}
                                                value={TOPUP_OPTIONS.TOPUP}
                                                color="primary"
                                                padding={1}
                                                sx={{
                                                    paddingX: '2px',
                                                    paddingY: '1px',
                                                    borderRadius: '8px',
                                                    '&.Mui-checked': {
                                                        color: yellow[700],
                                                    },
                                                }}
                                            />
                                        }
                                        label={
                                            <Typography variant="body2" ml={1} color={(allowedActions === ALLOWED_ACTIONS.CARDCODE_AND_TOPUP || allowedActions === ALLOWED_ACTIONS.TOPUP) ? '' : grey[300]}>
                                                {t('topup.topup')}
                                            </Typography>
                                        }
                                        style={{ flex: 1 }}
                                    />
                                </Box>
                            </FormGroup>
                        </FormControl>
                    </Box>

                    {typeAction === TOPUP_OPTIONS.CARDCODE && (
                        <Box display="flex" flexDirection="column" width="100%" mb={2}>
                            <Typography variant="body2" color={grey[600]}>
                                {t('topup.topup_note_1')}
                            </Typography>
                        </Box>
                    )}
                    {typeAction === TOPUP_OPTIONS.TOPUP && (
                        <>
                            <Box display="flex" flexDirection="column" width="100%" mb={2}>
                                <Typography variant="body2" color={grey[600]}>
                                    {t('topup.topup_note_2')}
                                </Typography>
                            </Box>

                            {/* Row input phone */}
                            <Box display="flex" flexDirection="column" width="100%" mb={2}>
                                <Box display="flex" alignItems="center">
                                    <Typography variant="h8" fontWeight={400} component="label" htmlFor="card-provider">
                                        {t('topup.receiver_phone_number')}
                                    </Typography>
                                    <Typography variant="h8" fontWeight={400} component="span" color={red[500]} paddingLeft={0.5}>
                                        *
                                    </Typography>
                                </Box>
                                <FormControl variant="outlined" fullWidth margin="normal" sx={{ marginTop: '10px' }}>
                                    <TextField
                                        inputRef={phoneInputRef}
                                        type="tel"
                                        id="phone"
                                        variant="outlined"
                                        value={phone}
                                        onChange={handleInputChangePhone}
                                        fullWidth
                                        // sx={{
                                        //     "& .MuiOutlinedInput-root": {
                                        //         "&.Mui-focused fieldset": {
                                        //             borderColor: yellow[700],
                                        //         },
                                        //     },
                                        // }}
                                        InputProps={{
                                            style: { padding: '4px 8px', borderRadius: '8px' },
                                        }}
                                    />
                                </FormControl>
                            </Box>
                        </>
                    )}

                    {/* Row Button Redeem */}
                    <Box display="flex" flexDirection="column" width="100%" mb={2}>
                        <Button
                            variant="contained"
                            disabled={!isInputValid}
                            sx={{
                                backgroundColor: yellow[600],
                                color: 'black',
                                textTransform: 'none',
                                borderRadius: '8px',
                                '&:hover': {
                                    backgroundColor: yellow[700]
                                },
                            }}
                            onClick={handleClickRedeem}
                        >
                            {typeAction === TOPUP_OPTIONS.TOPUP ? t("button.topup") : t("button.redeem")}
                        </Button>
                    </Box>

                    {testErrorPopup && (
                        <Box display="flex" flexDirection="column" width="100%" mb={2}>
                            {Object.keys(VNPTEPAY_ERROR_CODE).map((key) => (
                                <Button
                                    key={key}
                                    variant="contained"
                                    sx={{
                                        backgroundColor: yellow[600],
                                        color: 'black',
                                        textTransform: 'none',
                                        borderRadius: '8px',
                                        '&:hover': {
                                            backgroundColor: yellow[700],
                                        },
                                        mb: 1, // Add some margin between buttons
                                    }}
                                    onClick={() => handleTestErrorPopup(VNPTEPAY_ERROR_CODE[key])}
                                >
                                    {t(`button.${key.toLowerCase()}`)}
                                </Button>
                            ))}
                        </Box>
                    )}

                </Box>
            </div>

            {isOpenWarning && (
                <PopupWarningVNPTEpay onSubmit={onConfirmWarning} onClose={onCloseWarning} phoneProvider={phoneProvider} telcoNameSelect={telcoNameSelect} typeAction={typeAction} phone={phone} />
            )}

            {isOpenConfirm && (
                <PopupConfirmVNPTEpay onSubmit={onConfirmTopup} onClose={onCloseTopup} vnptProduct={vnptProduct} typeAction={typeAction} phone={phone} value={vnptProduct?.faceValue || ''} />
            )}

            {errorData && (
                <PopupErrorVNPTEpay data={errorData} onClose={onCloseError} />
            )}

        </ProductVNPTEpayStyle>
    )
}

export default ProductVNPTEpay;