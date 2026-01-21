import axios from 'axios';
import _ from "lodash";
import React, { useEffect, useState } from 'react';
import { useTranslation } from "react-i18next";
import { BiArrowBack } from "react-icons/bi";
import { useLocation, useNavigate } from "react-router-dom";
import Cookies from "universal-cookie";
import GlobalBackdrop from "../../components/GlobalBackdrop";
import PopupError from "../../components/PopupCustom/error";
import LOGO from '../../images/Logo.png';
import LOGO_MC_DONALD from '../../images/imageVoucher.png';
import VOUCHER_EMPTY from '../../images/voucher_empty.png';
import VoucherStatus from "../../utils/VoucherStatus";
import { getSessionItem } from '../../utils/sessionStorage';
import { ConvertNumber, decryptData, encryptData, getStatus, isExpiredVoucher, isVersionV2, renderMessage } from "../../utils/utils";
import { getCookieMobileEncode, setCookieMobileEncode } from "../../utils/cookie";
import VoucherType, { getTypeVoucherString } from "../../utils/VoucherType";
import SystemType from "../../utils/SystemType";
import PanelOtpRequired from '../DetailVoucher/PanelOtpRequired';
import PopupConfirmOTP from "./PopupConfirmOTP";
import PopupEnterNumberPhone from "./PopupEnterNumberPhone";
import PopupErrorCheckOTP from "./PopupErrorCheckOTP";
import PopupErrorNumberPhone from "./PopupErrorNumberPhone";
import { Container } from './styles';

const VoucherList = () => {
    const location = useLocation();
    document.title = 'List Voucher';
    const { t, i18n } = useTranslation();
    const history = useNavigate();

    const CHECK_TYPES = {
        OTP: "OTP",
        OTP_REQUIRED: "OTP_REQUIRED",
        NUMBER_PHONE: 'numberPhone',
        LIST_VOUCHER: 'listVoucher',
        ERROR_CHECK_PHONE: 'errorCheckPhone',
        ERROR_CHECK_OTP: 'errorCheckOTP',
    }

    const [dataCache, setDataCache] = useState(null);

    const [numberPhone, setNumberPhone] = useState(null);
    const [listVoucher, setListVoucher] = useState(null);
    const [errorData, setErrorData] = useState(null)
    const [loading, setLoading] = useState(false)
    const [timeOTP, setTimeOTP] = useState(null)
    const [errorOTP, setErrorOTP] = useState(null)
    const [isCheck, setIsCheck] = useState(null)
    const [displayType, setDisplayType] = useState("Valid")
    const [expireDt, setExpireDt] = useState(null);

    const [isShowErrorPopup, setShowErrorPopup] = useState(false);

    useEffect(() => {
        const fetchData = async () => {
            const dataPre = getSessionItem('data_pre');
            console.log(`dataPre: ${JSON.stringify(dataPre)}`);

            if (dataPre?.phoneNumber) {
                try {
                    const cookieData = await getCookieMobileEncode(dataPre.phoneNumber);
                    console.log(`cookieData: ${JSON.stringify(cookieData)}`);

                    if (cookieData) {
                        const updatedDataCache = {
                            ...dataPre,
                            ...(dataPre?.otpExpireDt === null ? { otpExpireDt: cookieData?.otpExpireDt } : {}),
                            otp: cookieData?.otp,
                        };

                        setDataCache(updatedDataCache);
                    } else {
                        setDataCache(dataPre);
                    }
                } catch (error) {
                    console.error('Error fetching cookie data:', error);
                }
            } else if (dataPre) {
                setDataCache(dataPre);
            }
        };

        fetchData();
    }, []);

    useEffect(() => {
        if (isVersionV2(dataCache)) {
            if (dataCache?.otp) {
                setIsCheck(CHECK_TYPES.LIST_VOUCHER);
            } else {
                setIsCheck(CHECK_TYPES.OTP_REQUIRED);
            }
        } else {
            setIsCheck(CHECK_TYPES.NUMBER_PHONE);
            const tokenStorage = sessionStorage.getItem('valid');
            if (tokenStorage) {
                decryptData(tokenStorage).then(result => {
                    const arrayToken = result.split(",")
                    encryptData(arrayToken[0]).then(result => {

                        const checkNumber = sessionStorage.getItem(result);
                        encryptData(arrayToken[1]).then(result1 => {
                            if (result1 === checkNumber) {
                                // onGetListVoucher(arrayToken[0], displayType)
                                setNumberPhone(arrayToken[0])
                                setIsCheck(CHECK_TYPES.LIST_VOUCHER)
                            } else {
                                setIsCheck(CHECK_TYPES.NUMBER_PHONE)
                            }
                        })
                    })
                })
            } else {
                setIsCheck(CHECK_TYPES.NUMBER_PHONE)
            }
        }
    }, [dataCache]);

    const onCallBack = (otp) => {
        console.log(`onCallBack: ${otp}`);
        setShowErrorPopup(true);
        setDataCache({ ...dataCache, otp: otp });
    }

    const onGetListVoucherV1 = (numberPhone, displayType) => {
        if (!numberPhone) {
            console.warn("numberPhone is null");
            return;
        }
        encryptData(numberPhone).then(result => {
            setLoading(true)
            axios
                .get(
                    `${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/vouchers/getUserVoucherList?mobileNumber=${encodeURIComponent(result)}&pageSize=1000&pageNum=0&status=1&sortBy=issueDate&sortDirection=DESC&supplierName=1&brandName=1`,
                )
                .then(res => {
                    setLoading(false)
                    if (res.data && res.data.data && res.data.data.pageData) {
                        setListVoucher(res.data.data.pageData);
                        if (displayType === "All") {
                            setListVoucher(res.data.data.pageData);
                        } else { // Available vouchers only
                            setListVoucher(_.remove(res.data.data.pageData, function (n) {
                                return !isExpiredVoucher(n) && n.voucherStatus !== VoucherStatus.DISABLED && n.voucherStatus !== VoucherStatus.USED;
                            })
                            )
                        }
                    } else {
                        setErrorData({
                            title: 0,
                            message: renderMessage(0, t),
                            onAction: () => {
                                setErrorData(null)
                            }
                        })
                    }
                })
                .catch(error => {
                    setLoading(false)
                    setErrorData({
                        title: error?.response?.data?.code,
                        message: renderMessage(error?.response?.data?.code, t),
                        onAction: () => {
                            setErrorData(null)
                        }
                    })
                });
        });
    }

    const onGetListVoucherV2 = (dataCache, displayType) => {
        encryptData(dataCache?.phoneNumber).then(result => {
            setLoading(true);
            const payload = {
                mobileNumber: `${result}`,
                pageSize: 1000,
                pageNum: 0,
                status: 1,
                sortBy: 'issueDate',
                sortDirection: 'DESC',
                supplierName: '1',
                brandName: '1',
                otp: dataCache?.otp
            }
            axios
                .post(
                    `${process.env.REACT_APP_VOUCHER_API_VIEWER}/v2/vouchers/getUserVoucherList`,
                    payload
                )
                .then(res => {
                    setLoading(false)
                    if (res.data && res.data.data && res.data.data.pageData) {
                        setListVoucher(res.data.data.pageData);

                        // Update cookie with new expire date
                        setCookieMobileEncode(dataCache?.phoneNumber, { otp: dataCache?.otp, otpExpireDt: dataCache?.otpExpireDt });

                        if (displayType === "All") {
                            setListVoucher(res.data.data.pageData);
                        } else { // Available vouchers only
                            setListVoucher(_.remove(res.data.data.pageData, function (n) {
                                return !isExpiredVoucher(n) && n.voucherStatus !== VoucherStatus.DISABLED && n.voucherStatus !== VoucherStatus.USED;
                            })
                            )
                        }
                    } else {
                        setErrorData({
                            title: 0,
                            message: renderMessage(0, t),
                            onAction: () => {
                                setErrorData(null)
                            }
                        })
                    }
                })
                .catch(error => {
                    console.log(`onGetListVoucherV2 error: ${error?.response?.data?.code}`);
                    setLoading(false);
                    if (1401 === error?.response?.data?.code) {
                        if (isShowErrorPopup) {
                            setErrorData({
                                title: error?.response?.data?.code,
                                message: renderMessage(error?.response?.data?.code, t),
                                onAction: () => {
                                    setErrorData(null)
                                }
                            })
                        }

                        setIsCheck(CHECK_TYPES.OTP_REQUIRED);
                        return;
                    }
                    setLoading(false)
                    setErrorData({
                        title: error?.response?.data?.code,
                        message: renderMessage(error?.response?.data?.code, t),
                        onAction: () => {
                            setErrorData(null)
                        }
                    })
                });
        });
    }

    useEffect(() => {
        if (isCheck === CHECK_TYPES.LIST_VOUCHER) {
            if (isVersionV2(dataCache) && dataCache?.phoneNumber && dataCache?.otp) {
                onGetListVoucherV2(dataCache, displayType)
            } else {
                onGetListVoucherV1(numberPhone, displayType)
            }
        }
    }, [isCheck, displayType])

    const createNewOTP = (numberPhone) => {
        encryptData(numberPhone).then(result => {
            setLoading(true)
            axios
                .post(
                    `${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/vouchers/create-otp-voucher?mobileNumber=${encodeURIComponent(result)}`,
                )
                .then(res => {
                    setLoading(false)
                    onCheckOTP(numberPhone)
                })
                .catch(error => {
                    setLoading(false)
                    if (error?.response?.data?.code == 1027) {
                        onCheckOTP(numberPhone)
                    } else {
                        setErrorData({
                            title: error?.response?.data?.code,
                            message: renderMessage(error?.response?.data?.code, t),
                            onAction: () => {
                                setErrorData(null)
                            }
                        })
                    }
                });
        });
    }


    const onCheckOTP = (numberPhone) => {
        encryptData(numberPhone).then(result => {
            setLoading(true)
            axios
                .get(
                    `${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/vouchers/get-otp-voucher?mobileNumber=${encodeURIComponent(result)}`,
                )
                .then(res => {
                    setLoading(false)
                    if (res.data && res.data.code === 200) {
                        if (res.data.data) {
                            setTimeOTP(parseInt(res.data.data.regDt) + (process.env.REACT_APP_TIME_OTP_EXPIRE * 60 * 1000 + 1000))
                            encryptData(numberPhone).then(result => {
                                const numberPhoneEncrypt = result
                                encryptData(res.data.data.otp).then(result1 => {
                                    const otpEncrypt = result1
                                    sessionStorage.setItem(numberPhoneEncrypt, otpEncrypt)
                                })
                            })

                            // OTP valid until
                            if (res.data.data.expireDt) {
                                setExpireDt(res.data.data.expireDt);
                            }
                        } else {
                            createNewOTP(numberPhone)
                        }

                    } else {
                        // setIsCheck("errorCheckOTP")
                    }
                })
                .catch(error => {
                    setLoading(false)
                    createNewOTP(numberPhone)
                });
        });

    }


    const onVerifyOTP = (value) => {
        encryptData(numberPhone).then(result => {
            setLoading(true)
            axios
                .get(
                    `${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/vouchers/get-otp-voucher?mobileNumber=${encodeURIComponent(result)}`,
                )
                .then(res => {
                    setLoading(false)
                    if (res.data && res.data.code === 200) {
                        if (res.data.data) {
                            setTimeOTP(parseInt(res.data.data.regDt) + (process.env.REACT_APP_TIME_OTP_EXPIRE * 60 * 1000 + 1000))
                            encryptData(numberPhone).then(result => {
                                const numberPhoneEncrypt = result
                                encryptData(res.data.data.otp).then(result1 => {
                                    const otpEncrypt = result1
                                    sessionStorage.setItem(numberPhoneEncrypt, otpEncrypt)
                                })
                            })

                            if (value === res.data.data.otp) {
                                setIsCheck(CHECK_TYPES.LIST_VOUCHER)
                                encryptData(`${numberPhone},${res.data.data.otp}`).then(result => {
                                    sessionStorage.setItem("valid", result)
                                })
                            } else {
                                setIsCheck(CHECK_TYPES.ERROR_CHECK_OTP)
                            }


                        } else {
                        }

                    } else {
                        // setIsCheck("errorCheckOTP")
                    }
                })
                .catch(error => {
                    setLoading(false)
                });
        });

    }

    const onCheckNumberPhone = (value) => {
        encryptData(value).then(result => {
            setLoading(true)
            axios
                .get(
                    `${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/vouchers/check-voucher-exist?mobileNumber=${encodeURIComponent(result)}`,
                )
                .then(res => {
                    setLoading(false)
                    if (res.data && res.data.data) {
                        setNumberPhone(value)
                        onCheckOTP(value)
                        setIsCheck(CHECK_TYPES.OTP)

                    } else {
                        setIsCheck(CHECK_TYPES.ERROR_CHECK_PHONE)
                    }
                })
                .catch(error => {
                    setLoading(false)
                    setErrorData({
                        title: error?.response?.data?.code,
                        message: renderMessage(error?.response?.data?.code, t),
                        onAction: () => {
                            setErrorData(null)
                        }
                    })
                });
        });
    }

    const getTextDes = (dataVoucher) => {
        const cookies = new Cookies();
        if (cookies.get('locales') === "vi") {
            if (isExpiredVoucher(dataVoucher)) {
                if (dataVoucher?.voucherStatus === VoucherStatus.USED) {
                    return "Voucher đã được sử dụng"
                }
                return "Voucher đã hết hạn sử dụng"
            } else if (dataVoucher && dataVoucher.voucherStatus === VoucherStatus.NORMAL && dataVoucher.transferStatus === "RECPTED") {
                return null
            } else if (dataVoucher && dataVoucher.voucherStatus === VoucherStatus.USED) {
                return "Voucher đã được sử dụng"
            } else if (dataVoucher && dataVoucher.voucherStatus === VoucherStatus.DISABLED) {
                if (dataVoucher.transferStatus === "TRANSFER") {
                    return "Voucher đang được chuyển nhượng"
                } else {
                    return "Voucher bị vô hiệu hóa"
                }
            }
        } else {
            if (isExpiredVoucher(dataVoucher)) {
                if (dataVoucher?.voucherStatus === VoucherStatus.USED) {
                    return "Voucher has been used"
                }
                return "Voucher has expired"
            } else if (dataVoucher && dataVoucher.voucherStatus === VoucherStatus.NORMAL && dataVoucher.transferStatus === "RECPTED") {
                return null
            } else if (dataVoucher && dataVoucher.voucherStatus === VoucherStatus.USED) {
                return "Voucher has been used"
            } else if (dataVoucher && dataVoucher.voucherStatus === VoucherStatus.DISABLED) {
                if (dataVoucher.transferStatus === "TRANSFER") {
                    return "Vouchers has transfered"
                } else {
                    return "Voucher is disabled"
                }
            }
        }
    }


    return (
        <Container>

            <GlobalBackdrop isLoading={loading} />

            {errorData &&
                <PopupError title={errorData.title} message={errorData.message} onAction={errorData.onAction} />}

            {isCheck === CHECK_TYPES.OTP_REQUIRED && (
                <div
                    style={{
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'flex-start',
                        minHeight: '100vh',
                        paddingTop: '48px',
                        paddingLeft: '16px',
                        paddingRight: '16px',
                    }}
                >
                    <PanelOtpRequired dataPre={dataCache} onReload={onCallBack} />
                </div>
            )}

            {(isCheck === CHECK_TYPES.LIST_VOUCHER && listVoucher) && <div className="row">
                <div className="header">
                    <img src={LOGO} alt="" />
                    {(window.history.length > 1) && <BiArrowBack color={"#000"} onClick={() => {
                        history(-1)
                    }} size={28} className={"icon_back"} />}
                </div>

                <div style={{
                    display: "flex",
                    justifyContent: "center",
                    marginTop: "12px",
                    fontFamily: "Lato !important"
                }}>
                    <div onClick={(e) => {
                        setDisplayType("Valid")
                    }} style={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        alignContent: "center",
                        padding: "4px 8px",
                        backgroundColor: "white",
                        borderRadius: "12px",
                        marginRight: "8px",
                        cursor: "pointer",
                        minWidth: "150px"
                    }}>
                        <div style={{
                            borderRadius: "50%",
                            border: "1px solid #ccc",
                            marginRight: "4px",
                            padding: "4px",
                        }}>
                            <div style={{
                                width: "12px",
                                height: "12px",
                                borderRadius: "50%",
                                backgroundColor: displayType === "Valid" ? "blue" : "unset"
                            }}>
                            </div>
                        </div>
                        <div>{t('voucher.validVoucher')}</div>
                    </div>
                    <div onClick={(e) => {
                        setDisplayType("All")
                    }} style={{
                        display: "flex",
                        alignItems: "center",
                        justifyContent: "center",
                        alignContent: "center",
                        padding: "4px 8px 6px 8px",
                        backgroundColor: "white",
                        borderRadius: "12px",
                        marginLeft: "8px",
                        minWidth: "150px",
                        cursor: "pointer"
                    }}>
                        <div style={{
                            borderRadius: "50%",
                            border: "1px solid #ccc",
                            marginRight: "4px",
                            padding: "4px"
                        }}>
                            <div style={{
                                width: "12px",
                                height: "12px",
                                borderRadius: "50%",
                                backgroundColor: displayType === "All" ? "blue" : "unset"
                            }}>
                            </div>
                        </div>
                        <div>{t('voucher.allVoucher')}</div>
                    </div>
                </div>

                {listVoucher.length > 0 ? <div className="row_list_voucher">
                    {listVoucher &&
                        listVoucher.map((item, index) => {
                            if (item?.transferStatus !== "RECPT_WAIT") {
                                return (
                                    <div className="item_voucher">
                                        <div className="logo">
                                            <div className="name">
                                                {item?.goods?.name || 'NULL'}
                                            </div>
                                            <img
                                                src={item?.goods?.imagePath.startsWith('http') ? item?.goods?.imagePath : process.env.REACT_APP_IMAGE_URL + item?.goods?.imagePath || LOGO_MC_DONALD}
                                                onError={({ currentTarget }) => {
                                                    currentTarget.onerror = null; // prevents looping
                                                    currentTarget.src = LOGO_MC_DONALD;
                                                }}
                                                alt=""
                                            />
                                        </div>

                                        {((item?.voucherStatus !== "NORMAL" && item?.voucherStatus !== "PART_USED") || isExpiredVoucher(item)) &&
                                            <>
                                                <div className="mask"></div>
                                            </>}
                                        {(!isExpiredVoucher(item) && item?.transferStatus === "RECPT_WAIT") ? null :
                                            <img src={getStatus(item)} alt="" className="tiker" />}

                                        <div style={{
                                            position: "absolute",
                                            color: "#fff",
                                            fontSize: "16px",
                                            textAlign: "center",
                                            width: "100%",
                                            top: "82px",
                                            zIndex: "2",
                                            fontWeight: "bold"
                                        }}>
                                            {getTextDes(item)}
                                        </div>
                                        <div className="line_dashed">
                                            <div
                                                className="line_dashed_absolute"
                                                style={{ top: '-14px' }}
                                            />
                                            <div
                                                className="line_dashed_absolute"
                                                style={{ bottom: '-14px' }}
                                            />
                                        </div>
                                        <div className="detail">
                                            {/* <img src={IMAGE_FAKE_QR} alt=""/> */}
                                            <div style={{ zIndex: 2 }}
                                                onClick={() => {
                                                    window.location = item?.shortLink
                                                }}
                                                className="button_use_voucher"
                                            >
                                                {t('voucher.buttonViewVoucher')}
                                            </div>
                                            <div className="price">
                                                {t('voucher.price')}:{' '}
                                                <span style={{ color: 'red' }}>
                                                    {ConvertNumber(item?.voucherPrice)} VNĐ
                                                </span>
                                            </div>
                                            {(item?.voucherType === VoucherType.PP || item.system === SystemType.CHOICE || item.system === SystemType.BULK ) &&
                                                <div className="price"
                                                    style={{ paddingTop: "6px", width: "max-content" }}>
                                                    {t('voucher.balance')}:{' '}
                                                    <span style={{ color: 'red' }}>
                                                        {ConvertNumber(item?.balance)} VNĐ
                                                    </span>
                                                </div>
                                            }
                                            <div className="price" style={{ paddingTop: "6px" }}>
                                                {t('voucher.type')}:{' '}
                                                <span style={{ color: 'red' }}>
                                                    {getTypeVoucherString(item?.voucherType)}
                                                </span>
                                            </div>
                                            {item?.voucherStatus === VoucherStatus.USED ? (
                                                <div className="price" style={{ paddingTop: "6px" }}>
                                                    {t('voucher.label_usedDate')}:{' '}<span style={{ color: 'red' }}>{item?.lastExchangeDate?.split(' ')[0]}</span>
                                                </div>
                                            ) : (
                                                <div className="price" style={{ paddingTop: "6px" }}>
                                                    {t('voucher.label_expireDate')}:{' '}<span style={{ color: 'red' }}>{item?.expireDate?.split(' ')[0]}</span>
                                                </div>
                                            )}
                                        </div>
                                    </div>
                                )
                            }
                        })}
                </div> : <div style={{ width: "100%" }}>
                    <div style={{ marginTop: "40px", display: "flex", justifyContent: "center" }}>
                        <img style={{ width: "80%", maxWidth: "400px" }} src={VOUCHER_EMPTY} alt="" />
                    </div>
                    <div style={{
                        textAlign: "center",
                        fontStyle: "italic",
                        fontSize: "20px",
                        color: "red",
                        fontWeight: "bold"
                    }}>
                        {t('voucher.listVoucherEmpty')}
                    </div>
                </div>}
            </div>
            }
            {
                isCheck === CHECK_TYPES.NUMBER_PHONE &&
                <PopupEnterNumberPhone
                    error={errorOTP}
                    onClickContinue={onCheckNumberPhone}
                    onClose={() => {
                        history(-1)
                    }} />
            }

            {
                isCheck === CHECK_TYPES.ERROR_CHECK_PHONE &&
                <PopupErrorNumberPhone
                    error={errorOTP}
                    onClickContinue={(e) => {
                        setIsCheck(CHECK_TYPES.NUMBER_PHONE)
                    }}
                    onClose={() => {
                        setIsCheck(CHECK_TYPES.NUMBER_PHONE)
                    }} />
            }

            {
                isCheck === CHECK_TYPES.OTP &&
                <PopupConfirmOTP
                    createOTP={() => {
                        createNewOTP(numberPhone)
                    }}
                    time={timeOTP}
                    expireDt={expireDt}
                    error={errorOTP}
                    onClickContinue={onVerifyOTP}
                    onClose={() => {
                        setIsCheck(CHECK_TYPES.NUMBER_PHONE)
                    }} />
            }

            {
                isCheck === CHECK_TYPES.ERROR_CHECK_OTP &&
                <PopupErrorCheckOTP
                    error={errorOTP}
                    onClickContinue={(e) => {
                        setIsCheck(CHECK_TYPES.OTP)
                    }}
                    onClose={() => {
                        setIsCheck(CHECK_TYPES.OTP)
                    }} />
            }

        </Container>
    )
}

export default VoucherList;
