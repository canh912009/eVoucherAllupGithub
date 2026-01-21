import { Backdrop, Button } from '@mui/material';
import isNumeric from "antd/es/_util/isNumeric";
import axios from 'axios';
import moment from "moment/moment";
import React, { useEffect, useState } from 'react';
import { useTranslation } from 'react-i18next';
import { AiOutlineCheck, AiOutlineClose } from "react-icons/ai";
import { NumericFormat } from "react-number-format";
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

const StoreVoucher = () => {
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
    const [isScan, setIsScan] = useState(false)

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
                    const { otp, voucher, expireTime } = data;

                    if (voucher) {
                        if (voucher.voucherType !== "PP") {
                            setPaymentAmount('0')
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
                    title: `ERROR_${error?.response?.data?.code}`,
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
                const voucherInfo = res.data.data;
                console.log("Voucher info", voucherInfo)

                if (voucherInfo) {
                    setLoading(false);
                    localStorage.clear()
                    setDataVoucher(voucherInfo);
                    if (voucherInfo.voucherType !== "PP") {
                        setPaymentAmount('0')
                    }
                } else {
                    setLoading(false);
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
                    title: `ERROR_${error?.response?.data?.code}`,
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
                const res = await axios.get(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/store/findById/${encodeURIComponent(storeID)}`);

                if (res.data) {
                    setDataStore(res.data);
                    setLoading(false);
                } else {
                    setIsErrorSearchStore(true);
                    setLoading(false);
                }
            } catch (error) {
                setLoading(false);
                setErrorData({
                    title: error?.response?.data?.code,
                    message: renderMessage(error?.response?.data?.code, t),
                    onAction: () => {
                        setStoreID(null);
                        setErrorData(null)
                        setDataStore(null)
                    }
                })
            }
        }, 1500);
    };

    const onUseVoucher = () => {
        if (dataVoucher?.voucherType === "PP") {
            if (isNumeric(paymentAmount)) {
                if (Number(paymentAmount) <= Number(dataVoucher.balance)) {
                    setLoading(true);
                    setTimeout(async () => {
                        try {
                            const res = await axios.post(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/webpos/vouchers/exchange`, {
                                otp: `${otp}`,
                                storeId: storeID,
                                listPrice: dataVoucher?.goods?.listPrice,
                                paymentAmount: paymentAmount,
                                staffMobileNum: dataStore?.data?.tel,
                                goodsId: dataVoucher?.goods?.id,
                                goodsName: dataVoucher?.goods?.name,
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
                } else {
                    setErrorData({
                        title: "Error input amount",
                        message: "Amount cannot be greater than balance !",
                        onAction: () => {
                            setErrorData(null)
                            setDataStore(null)
                        }
                    })
                }
            } else {
                setErrorData({
                    title: "Error input amount", message: "Amount must be number !", onAction: () => {
                        setErrorData(null)
                        setDataStore(null)
                    }
                })
            }
        } else {
            setLoading(true);
            setTimeout(async () => {
                try {
                    const res = await axios.post(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/webpos/vouchers/exchange`, {
                        otp: `${otp}`,
                        storeId: storeID,
                        listPrice: dataVoucher?.goods?.listPrice,
                        paymentAmount: paymentAmount,
                        staffMobileNum: dataStore?.data?.tel,
                        goodsId: dataVoucher?.goods?.id,
                        goodsName: dataVoucher?.goods?.name,
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
        }
    };

    const checkExpriceDate = () => {
        const time1 = moment(dataVoucher?.expireDate);
        const time2 = moment();

        if (time1.isBefore(time2)) {
            console.log(false)
            return false
        } else if (time1.isAfter(time2)) {
            console.log(true)
            return true
        } else {
            console.log(null)
            return null
        }
    }
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
                            </div>
                            <div className="title">
                                <h1>{dataVoucher?.subject || 'NULL'}</h1>
                                <h2>{dataVoucher?.goods?.name || 'NULL'}</h2>
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
                                        <h1>{t('voucher.typeVoucher')}</h1>
                                        <h2>{getTypeVoucherString(dataVoucher?.voucherType) || 'DEFAULT TYPE'}</h2>
                                    </div>
                                    {getPrice(dataVoucher, t)}
                                </div>
                            </div>
                            <div className="line_dashed">
                                <div className="left_line" />
                                <div className="right_line" />
                            </div>
                            <div className="show_instruction">
                                <div className="time_voucher">
                                    {t('voucher.expiryDate')}: {moment(dataVoucher?.expireDate || null).format("DD/MM/YYYY")}
                                </div>
                                {dataVoucher?.voucherType === "PP" && (dataVoucher?.voucherStatus === "NORMAL" || dataVoucher?.voucherStatus === "PART_USED") &&
                                    <div className={"input_amount"}>
                                        <NumericFormat
                                            pattern="\d*"
                                            style={{
                                                width: "100%",
                                                backgroundColor: "white",
                                                marginTop: "8px",
                                                height: "40px",
                                                borderRadius: "6px",
                                                padding: "0px 8px 0px 40px",
                                                fontSize: "16px"
                                            }}
                                            allowLeadingZeros
                                            thousandSeparator=","
                                            placeholder={t('voucher.placeholderInputAmount')}
                                            value={paymentAmount}
                                            onValueChange={(values, sourceInfo) => {
                                                setPaymentAmount(values.value)
                                                if (Number(values.value) <= Number(dataVoucher.balance)) {
                                                    setErrorText(null)
                                                } else {
                                                    setErrorText("Amount cannot be greater than balance")
                                                }
                                            }}
                                        />
                                        <div className={"affter_input"}>VNĐ</div>
                                    </div>}

                                <div style={{ height: "30px", lineHeight: "30px", color: "red", fontFamily: "Lato" }}>
                                    {errorText && errorText}
                                </div>
                            </div>
                        </div>

                        {checkExpriceDate() === false && dataVoucher?.expireDate &&
                            <div style={{ textAlign: "center", marginTop: "20px", color: "red" }}>
                                {t('voucher.expireDate')} {dataVoucher?.expireDate}
                            </div>
                        }

                        {checkExpriceDate() && (dataVoucher?.voucherStatus === "NORMAL" || dataVoucher?.voucherStatus === "PART_USED") &&
                            <div>
                                <div style={{
                                    textAlign: "center",
                                    fontSize: "20px",
                                    margin: "12px 0",
                                    fontFamily: "Lato",
                                    fontWeight: "bold",
                                }}>{t('voucher.scanQR')}
                                </div>
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
                                                        if (result) {
                                                            if (isScan === false) {
                                                                setStoreID(result?.text);
                                                                onSearchStore(result?.text)
                                                                setIsScan(true)
                                                                setColorBorderQr("green")
                                                            }
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
                            </div>}
                    </div>}
            </div>


            {dataStore && <Backdrop
                sx={{ color: '#fff', zIndex: 10 }}
                open={true}
            >
                <div className="pop_up_modal">
                    <div className="row_popup">
                        <img src={IMAGE_POPUP} alt="" />
                        <h1>{t('voucher.titleUseVoucher')}</h1>
                        <h2>{t('voucher.subUseVoucher')}</h2>
                        <div style={{ width: "90%", margin: "auto", borderTop: "0.5px dashed #e0e0e0" }}></div>
                        <div style={{ display: "flex", justifyContent: "center" }}>
                            <Button
                                onClick={onUseVoucher}
                                className={"button_action"} sx={{ backgroundColor: '#008000' }}
                                variant='contained'><AiOutlineCheck style={{ marginRight: "8px" }} />{t('voucher.confirm')}
                            </Button>
                            <Button
                                onClick={() => {
                                    setStoreID(null)
                                    setDataStore(null)
                                    setIsScan(false)
                                }}
                                className={"button_action"} sx={{ backgroundColor: '#D1293D' }}
                                variant='contained'><AiOutlineClose style={{ marginRight: "8px" }} />{t('voucher.cancel')}
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
                <PopupError title={t('voucher.errorFindStore')} message={t('voucher.subErrorFindStore')}
                    onAction={() => {
                        setIsErrorSearchStore(false)
                    }} />}
            {errorData &&
                <PopupError title={errorData.title} message={errorData.message} onAction={errorData.onAction} />}
        </Container>
    );
};

export default StoreVoucher;
