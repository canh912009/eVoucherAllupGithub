import { Backdrop, Button } from '@mui/material';
import MenuItem from '@mui/material/MenuItem';
import Select from '@mui/material/Select';
import axios from 'axios';
import moment from "moment/moment";
import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { AiOutlineCheck, AiOutlineClose } from "react-icons/ai";
import { QrReader } from 'react-qr-reader';
import { useNavigate } from "react-router-dom";
import GlobalBackdrop from "../../components/GlobalBackdrop";
import PopupError from "../../components/PopupCustom/error";
import IMAGE_POPUP from '../../images/image_popup.png';
import LOGO from '../../images/Logo.png';
import IMAGE_NO from "../../images/no.png";
import { getImage, getPrice, getStatus, renderMessage } from "../../utils/utils";
import { getTypeVoucherString } from "../../utils/VoucherType";
import { Container } from './styles';

const StoreVoucherCancel = () => {
    document.title = 'Store use voucher';
    const { t } = useTranslation()
    const history = useNavigate();
    const asPath = window.location.pathname;
    const lastIndex = asPath.lastIndexOf('/');
    const otp = asPath.substring(lastIndex + 1, asPath.length);
    const [dataVoucher, setDataVoucher] = useState(null);
    const [storeID, setStoreID] = useState(null);
    const [colorBorderQr, setColorBorderQr] = useState("black")
    const [loading, setLoading] = useState(false)
    const [isErrorUsingVoucher, setIsErrorUsingVoucher] = useState(null)
    const [isErrorSearchStore, setIsErrorSearchStore] = useState(false)
    const [paymentAmount, setPaymentAmount] = useState(null)
    const [dataStore, setDataStore] = useState(null)
    const [errorText, setErrorText] = useState(null)
    const [errorData, setErrorData] = useState(null)
    const [expireTime, setExpireTime] = useState(false)
    const [paymentHistory, setPaymentHistory] = useState(null)
    const [listPaymentHistoryResponse, setlistPaymentHistoryResponsePaymentHistory] = useState([])

    const handleError = (error) => {
        setColorBorderQr("red")
        console.error(error);
    };

    useEffect(() => {
        setLoading(true)
        const getByID = async () => {
            try {
                const res = await axios.get(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/vouchers/${localStorage.getItem("voucherID")}?posType=1`);
                const { data } = res.data;

                if (data) {
                    setLoading(false);
                    const { otp, voucher, expireTime, listPaymentHistoryResponse } = data;
                    setExpireTime(expireTime || null);

                    if (voucher) {
                        setlistPaymentHistoryResponsePaymentHistory(listPaymentHistoryResponse)
                        if (voucher.voucherType === "PP") {
                            setPaymentAmount('0')
                        } else {
                            let paymentID = listPaymentHistoryResponse[listPaymentHistoryResponse.length - 1]?.paymentHistory?.id
                            setPaymentHistory(paymentID)
                        }
                        setDataVoucher(voucher);
                    } else {
                        setDataVoucher(null);
                        setErrorData({
                            title: t('voucher.errorVoucherNull'),
                            message: t('voucher.errorVoucherNull'),
                            onAction: () => {
                                setErrorData(null)
                                setDataStore(null)
                            }
                        })
                    }
                } else {
                    setDataVoucher(null);
                    setErrorData({
                        title: t('voucher.errorVoucher'),
                        message: t('voucher.errorVoucher'),
                        onAction: () => {
                            setErrorData(null)
                            setDataStore(null)
                        }
                    })
                    setLoading(false);
                }
            } catch (error) {
                setLoading(false);
                setErrorData({
                    title: error?.response?.data?.code,
                    message: renderMessage(error?.response?.data?.code, t),
                    onAction: () => {
                        setErrorData(null)
                        setDataStore(null)
                    }
                })
            }
        };
        const getByOtp = async () => {
            try {
                const res = await axios.get(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/webpos/vouchers/otp/${otp}?posType=1`);
                const data = res.data.data.voucher;
                setExpireTime(res.data.data.expireTime)

                if (data) {
                    const listPayment = res?.data?.data?.listPaymentHistoryResponse
                    setLoading(false);
                    localStorage.clear()
                    setExpireTime(data.expireTime)
                    setDataVoucher(data);
                    setlistPaymentHistoryResponsePaymentHistory(listPayment)
                    if (res?.data?.data?.voucher?.voucherType === "PP") {
                        setPaymentAmount('0')
                    } else {
                        let paymentID = res?.data?.data?.listPaymentHistoryResponse[listPayment.length - 1]?.paymentHistory?.id
                        setPaymentHistory(paymentID)
                    }
                } else {
                    setDataVoucher(null);
                    setErrorData({
                        title: t('voucher.errorVoucherNull'),
                        message: t('voucher.errorVoucherNull'),
                        onAction: () => {
                            setErrorData(null)
                            setDataStore(null)
                        }
                    })
                }
            } catch (error) {
                setLoading(false);
                setErrorData({
                    title: error?.response?.data?.code,
                    message: renderMessage(error?.response?.data?.code, t),
                    onAction: () => {
                        setErrorData(null)
                        setDataStore(null)
                    }
                })
            }
        };

        if (localStorage.getItem("voucherID") && localStorage.getItem("otp") === otp) {
            getByID()
        } else {
            getByOtp();
        }
    }, [otp]);

    const constraints = {
        facingMode: { exact: "environment" },
        width: 240,
        height: 240,
    };


    const onSearchStore = (storeID) => {
        setLoading(true);

        setTimeout(async () => {
            try {
                const res = await axios.get(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/store/findById/${storeID}`);

                if (res.data) {
                    setDataStore(res.data);
                    setLoading(false);
                } else {
                    setIsErrorSearchStore(true);
                    setLoading(false);
                }
            } catch (error) {
                setIsErrorSearchStore(true);
                setStoreID(null);
                setLoading(false);
            }
        }, 1500);
    };

    const onCancelVoucher = () => {
        setTimeout(async () => {
            try {
                const res = await axios.post(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/webpos/vouchers/cancelPayment`, {
                    paymentHistoryId: `${paymentHistory}`,
                    storeId: storeID,
                    posType: "1",
                    posCd: "",
                    approvementNo: "",
                    posVerType: ""
                });

                setLoading(false);
                setStoreID(null);
                setDataStore(null);
                localStorage.setItem("voucherID", dataVoucher.id)
                localStorage.setItem("otp", otp)
                history('/response-cancel-use-voucher');
            } catch (error) {
                setLoading(false);
                setStoreID(null);
                setDataStore(null);
                setIsErrorUsingVoucher(error.response.data);
            }
        }, 1500);
    };
    const onFakeCancel = () => {
        setTimeout(async () => {
            try {
                const res = await axios.post(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/webpos/vouchers/cancelPayment`, {
                    paymentHistoryId: `${paymentHistory}`,
                    storeId: "1234567003-003-003",
                    posType: "1",
                    posCd: "",
                    approvementNo: "",
                    posVerType: ""
                });

                setLoading(false);
                setStoreID(null);
                setDataStore(null);
                localStorage.setItem("voucherID", dataVoucher.id)
                localStorage.setItem("otp", otp)
                history('/response-use-voucher');
            } catch (error) {
                setLoading(false);
                setStoreID(null);
                setDataStore(null);
                setIsErrorUsingVoucher(error.response.data);
            }
        }, 1500);
    };

    return (
        <Container>
            <div className="row">
                <GlobalBackdrop isLoading={loading} />
                {dataVoucher &&
                    <div>
                        <div className="detail_voucher"
                            style={{ backgroundColor: (dataVoucher?.voucherStatus !== "NORMAL" && dataVoucher?.voucherStatus !== "PART_USED") && "#D9D9D9" }}>
                            {(dataVoucher?.voucherStatus !== "NORMAL" && dataVoucher?.voucherStatus !== "PART_USED") && <>
                                <div className="mask"></div>
                                <img src={getStatus(dataVoucher)} alt="" className="tiker" /></>}
                            <div className="logo">
                                <img src={LOGO} alt="" />
                            </div>
                            <div className="line_solid" />
                            <div className="title_voucher">
                                <img
                                    src={process.env.REACT_APP_IMAGE_URL + dataVoucher?.goods?.imagePath || getImage(dataVoucher)}
                                    onError={({ currentTarget }) => {
                                        currentTarget.onerror = null; // prevents looping
                                        currentTarget.src = getImage(dataVoucher);
                                    }}
                                    alt=""
                                />
                                <div className="title">
                                    <h1>{dataVoucher?.subject || 'NULL'}</h1>
                                    <h2>{dataVoucher?.goods?.name || 'NULL'}</h2>
                                </div>
                            </div>
                            <div className="line_dashed" />
                            <div className="detail_customer">
                                <div className="row_detail_customer">
                                    <div className="item_left">
                                        <h1>{t('voucher.name')}</h1>
                                        <h2>
                                            {dataVoucher?.userName || 'NULL'}
                                        </h2>
                                    </div>
                                    <div className="item_right">
                                        <h1>{t('voucher.phoneNumber')}</h1>
                                        <h2>
                                            {dataVoucher?.userMobileNumber || 'NULL'}
                                        </h2>
                                    </div>
                                </div>
                                <div className="row_detail_customer">
                                    <div className="item_left">
                                        <h1>Type </h1>
                                        <h2>{getTypeVoucherString(dataVoucher) || 'DEFAULT TYPE'}</h2>
                                    </div>
                                    {getPrice(dataVoucher, t)}
                                </div>
                            </div>
                            <div className="line_dashed">
                                <div className="left_line" />
                                <div className="right_line" />
                            </div>
                            <div className="show_instruction" style={{ marginTop: "12px" }}>
                                <div className="time_voucher">
                                    {t('voucher.expiryDate')}: {moment(dataVoucher?.expireDate || null).format("DD/MM/YYYY")}
                                </div>
                                {listPaymentHistoryResponse?.length > 0 && (dataVoucher?.voucherType !== "PP") && <div>
                                    <div className="time_voucher">
                                        <span
                                            style={{ fontWeight: "bold" }}>Used or scanned time:</span> <span
                                                style={{ color: "red" }}>{listPaymentHistoryResponse[listPaymentHistoryResponse.length - 1]?.paymentHistory?.transactionDate}</span>
                                    </div>
                                    <div className="time_voucher">
                                        <span
                                            style={{ fontWeight: "bold" }}>Store scanned location: </span><span
                                                style={{ color: "red" }}>{listPaymentHistoryResponse[listPaymentHistoryResponse.length - 1]?.store?.storeName}</span>
                                    </div>
                                </div>}

                                {listPaymentHistoryResponse?.length > 0 && (dataVoucher?.voucherType === "PP") &&
                                    <Select
                                        labelId="demo-simple-select-label"
                                        id="demo-simple-select"
                                        sx={{ width: "100%", marginTop: "20px" }}
                                        size="small"
                                        value={paymentHistory}
                                        onChange={(e) => {
                                            setPaymentHistory(e.target.value)
                                        }}
                                    >
                                        {listPaymentHistoryResponse?.length > 0 && listPaymentHistoryResponse.map((item) => {
                                            if (item?.paymentHistory?.exchangeType === "USE") {
                                                return <MenuItem value={item?.paymentHistory?.id}>
                                                    {item?.store?.storeName} - {item?.paymentHistory?.transactionDate}
                                                </MenuItem>
                                            }
                                        })}
                                    </Select>}
                                <div style={{ height: "30px", lineHeight: "30px", color: "red", fontFamily: "Lato" }}>
                                    {errorText && errorText}
                                </div>
                            </div>
                        </div>
                        {(dataVoucher?.voucherStatus === "NORMAL" || dataVoucher?.voucherStatus === "PART_USED" || (dataVoucher?.voucherStatus === "USED")) &&
                            <div>
                                <div className="show_qr">
                                    <img src={IMAGE_NO} alt="" />
                                    <div className="row_show_qr">
                                        <div className="border_qr" style={{ borderColor: colorBorderQr }}>
                                            <div
                                                className="border_qr_white_y"
                                                style={{ left: '-14px' }}
                                            />
                                            <div
                                                className="border_qr_white_y"
                                                style={{ right: '-14px' }}
                                            />
                                            <div className="border_qr_white_x"
                                                style={{ top: '-14px' }} />
                                            <div
                                                className="border_qr_white_x"
                                                style={{ bottom: '-14px' }}
                                            />
                                            <div style={{ width: "240px", height: "240px" }}>
                                                <QrReader
                                                    // facingMode={"user"}
                                                    constraints={constraints}
                                                    delay={1000}
                                                    onResult={(result, error) => {
                                                        if (!!result) {
                                                            setStoreID(result?.text);
                                                            onSearchStore(result?.text)
                                                            setColorBorderQr("green")
                                                        } else {
                                                            setColorBorderQr("black")
                                                        }
                                                    }}
                                                    onError={handleError}
                                                />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div style={{
                                    textAlign: "center",
                                    fontSize: "14px",
                                    margin: "12px 0",
                                    fontFamily: "Lato",
                                    fontWeight: "bold",
                                }}>{t('voucher.scanQRAgain')}
                                </div>
                            </div>}
                    </div>}
                {/*<button onClick={onFakeCancel}>Cancel Use Voucher</button>*/}
            </div>


            {dataStore && <Backdrop
                sx={{ color: '#fff', zIndex: 10 }}
                open={true}
            >
                <div className="pop_up_modal">
                    <div className="row_popup">
                        <img src={IMAGE_POPUP} alt="" />
                        <h1>{t('voucher.titleCancelUseVoucher')}</h1>
                        <h2>{t('voucher.subCancelUseVoucher')}</h2>
                        <div style={{ width: "90%", margin: "auto", borderTop: "0.5px dashed #e0e0e0" }}></div>
                        <div style={{ display: "flex", justifyContent: "center" }}>
                            <Button
                                onClick={onCancelVoucher}
                                className={"button_action"} sx={{ backgroundColor: '#008000' }}
                                variant='contained'><AiOutlineCheck
                                    style={{ marginRight: "8px" }} /> {t('voucher.confirm')}</Button>
                            <Button
                                onClick={() => {
                                    setStoreID(null)
                                    setDataStore(null)
                                }}
                                className={"button_action"} sx={{ backgroundColor: '#D1293D' }}
                                variant='contained'><AiOutlineClose style={{ marginRight: "8px" }} /> {t('voucher.cancel')}
                            </Button>
                        </div>
                        <div className={"footer"}></div>
                    </div>
                </div>
            </Backdrop>}


            {isErrorUsingVoucher !== null &&
                <PopupError title={isErrorUsingVoucher?.code} message={renderMessage(isErrorUsingVoucher?.code, t)}
                    onAction={() => {
                        setIsErrorUsingVoucher(null)
                    }} />}
            {isErrorSearchStore &&
                <PopupError title={"Error Find Store"} message={"Store does not exist"} onAction={() => {
                    setIsErrorSearchStore(false)
                }} />}
            {errorData &&
                <PopupError title={errorData.title} message={errorData.message} onAction={errorData.onAction} />}
        </Container>
    );
};

export default StoreVoucherCancel;
