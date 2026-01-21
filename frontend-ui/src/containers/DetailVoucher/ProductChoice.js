import React, { useEffect, useState } from "react";
import { InputOTP, ProductChoiceStyle } from "./styles";
import IMAGE_DEFAULT_CHOICE from '../../images/imageVoucher.png'
import { AiOutlineHistory, AiOutlineMinusSquare, AiOutlinePlusSquare } from "react-icons/ai";
import { MdClear, MdArrowBack } from "react-icons/md";
import { Backdrop, Button } from "@mui/material";
import IMAGE_OTP from '../../images/sendOTP.png'
import OtpInput from 'react-otp-input';
import { ConvertNumber, getImage, getStatus, renderMessage, isUnlimitedProduct, isVersionV1, isVersionV2 } from "../../utils/utils";
import { styled } from "@mui/material/styles";
import axios from "axios";
import IMAGE_LOGO from '../../images/logo_white.png';
import LOGO_MC_DONALD from "../../images/imageVoucher.png";
import IMAGE_FAKE_QR from "../../images/IMAGE_FAKE_QR.png";
import { useTranslation } from "react-i18next";
import GlobalBackdrop from "../../components/GlobalBackdrop";
import { message } from "antd";
import PopupError from "../../components/PopupCustom/error";
import VoucherStatus from "../../utils/VoucherStatus";
import { getTypeVoucherString } from "../../utils/VoucherType";
import { FaExpandAlt } from "react-icons/fa";

const PopupConfirmOTP = ({ onClose, onClickContinue, error }) => {
    const { t, i18n } = useTranslation()
    const [otp, setOtp] = useState('');
    return (
        <Backdrop
            sx={{ color: '#fff', zIndex: 10 }}
            open={true}
        >
            <div style={{
                backgroundColor: "white",
                color: "black",
                width: "316px",
                height: "380px",
                borderRadius: "12px",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                position: "relative",
                fontFamily: "Roboto"
            }}>
                <div style={{ display: "flex", justifyContent: "center" }}>
                    <MdClear onClick={onClose} size={24}
                        style={{ position: "absolute", right: "16px", top: "16px", color: "gray" }} />
                    <img src={IMAGE_OTP} style={{ width: "100px", height: "100px", marginTop: "44px" }} alt="" />
                </div>
                <div style={{ textAlign: "center", fontFamily: "Roboto", fontSize: "14px", marginTop: "42px" }}>
                    {t('voucher.titleOTP')}
                </div>
                <div style={{ textAlign: "center", fontFamily: "Roboto", fontSize: "14px", marginTop: "12px" }}>
                    {t('voucher.subOTP')}
                </div>
                <div style={{ display: "flex", justifyContent: "center", marginTop: "12px", width: "240px" }}>
                    <OtpInput
                        value={otp}
                        onChange={setOtp}
                        inputType={"number"}
                        inputStyle={{ width: "24px" }}
                        numInputs={6}
                        renderSeparator={false}
                        renderInput={(props) => <InputOTP {...props} />}
                    />
                </div>

                {error ? <div style={{ textAlign: "center", color: "red", height: "16px", padding: "12px 0" }}>
                    {error}
                </div> : <div style={{ height: "16px", padding: "12px 0" }}></div>}

                <div style={{ display: "flex", justifyContent: "center", width: "100%" }}>
                    <button
                        onClick={() => {
                            onClickContinue(otp)
                        }}
                        disabled={otp.length !== 6} style={{
                            width: "calc(100% - 32px)",
                            height: "40px",
                            borderRadius: "16px",
                            border: "unset",
                            backgroundColor: otp.length === 6 ? "#4372EA" : "gray",
                            fontSize: "15px",
                            color: "white",
                            cursor: otp.length === 6 ? "pointer" : "no-drop"
                        }}>
                        {t('voucher.continue')}
                    </button>
                </div>
            </div>
        </Backdrop>
    );
};


export const PopupListVoucherChild = ({ onClose, data }) => {
    const { t, i18n } = useTranslation()
    const [listVoucher, setListVoucher] = useState([]);
    const [loading, setLoading] = useState(true)

    useEffect(() => {
        setLoading(true)
        axios.get(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/vouchers/choice/children/${data.id}`)
            .then((response) => {
                setLoading(false)
                setListVoucher(response.data.data)
            }).catch((error) => {
                setLoading(false)
            }
            )
    }, [])

    const getTextDes = (item) => {
        // console.log(`getTextDes`, item);
        if (item?.voucherStatus === "USED" && item?.voucherType === "PP") {
            return <>
                <div>{t('voucher.CAN_NOT_USE')}</div>
                <div>{t('voucher.current_balance')} {item.balance}</div>
            </>
        } else if (item?.voucherStatus === "USED") {
            return <div>{t('voucher.CAN_NOT_USE')}</div>
        } else if (item?.voucherStatus === "DISABLED" && item?.voucherType === "PP") {
            return <>
                <div>{t('voucher.VOUCHER_DISABLE')}</div>
                <div>{t('voucher.current_balance')} {item.balance}</div>
            </>
        } else if (item?.voucherStatus === "DISABLED") {
            return <div>{t('voucher.VOUCHER_DISABLE')}</div>
        } else if (item?.voucherStatus === "DISABLED" && item?.voucherType === "PP") {
            return <>
                <div>{t('voucher.VOUCHER_DISABLE')}</div>
                <div>{t('voucher.current_balance')} {item.balance}</div>
            </>
        } else if (item?.voucherStatus === "EXPIRED" && item?.voucherType === "PP") {
            return <>
                <div>{t('voucher.voucher_expired')}</div>
                <div>{t('voucher.current_balance')} {item.balance}</div>
            </>
        } else if (item?.voucherStatus === "EXPIRED") {
            return <div>{t('voucher.voucher_expired')}</div>
        }
    }


    return (
        <Backdrop
            sx={{ color: '#fff', zIndex: 10 }}
            open={true}
        >
            <div style={{
                backgroundColor: "#fee715",
                color: "black",
                width: "100vw",
                height: "100vh",
                position: "relative",
                fontFamily: "Roboto",
                overflow: "auto",
            }}>
                <GlobalBackdrop isLoading={loading} />

                <div className={"button_back_to_voucher"} onClick={onClose}>
                    <MdArrowBack size={20} style={{ marginLeft: "20px", marginRight: "8px" }} />
                    <span> {t('voucher.backToVoucher')}</span>
                </div>
                <div className={"icon_list_product_choice"}>
                    <img src={IMAGE_LOGO} alt="" />
                </div>
                <div style={{
                    width: "100%",
                    display: "flex",
                    justifyContent: "center",
                    fontSize: "20px",
                    fontWeight: "600"
                }}>
                    {t('voucher.yourVoucherList')}
                </div>

                {!loading && <div>
                    {listVoucher.length > 0 ?
                        <div className="row_list_voucher">
                            {listVoucher &&
                                listVoucher.map((item, index) => {
                                    if (item?.voucherStatus === "NORMAL" || item?.voucherStatus === "PART_USED") {
                                        // if (index < 3) {
                                        return (
                                            <div className="item_voucher">
                                                <div className="logo">
                                                    <div className="name">
                                                        {item?.goods?.name || 'NULL'}
                                                    </div>
                                                    <img
                                                        src={item?.goods?.imagePath.startsWith('http') ? item?.goods?.imagePath : process.env.REACT_APP_IMAGE_URL + item?.goods?.imagePath || IMAGE_DEFAULT_CHOICE}
                                                        onError={({ currentTarget }) => {
                                                            currentTarget.onerror = null; // prevents looping
                                                            currentTarget.src = IMAGE_DEFAULT_CHOICE;
                                                        }}
                                                        alt=""
                                                    />
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
                                                    <div
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
                                                    {item?.voucherType === "PP" &&
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
                                                    <div className="price" style={{ paddingTop: "6px" }}>
                                                        {t('voucher.label_expireDate')}:{' '}
                                                        <span style={{ color: 'red' }}>
                                                            {item?.expireDate?.split(' ')[0]}
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                        )
                                    }
                                })}

                            {listVoucher && listVoucher.map((item) => {
                                if (item?.voucherStatus !== "NORMAL" && item?.voucherStatus !== "PART_USED") {
                                    return (
                                        <div className="item_voucher">
                                            <div className="mask" />
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
                                            <img src={getStatus(item)} alt="" className="tiker" />
                                            <div className="logo">
                                                <div className="name">
                                                    {item?.goods?.name || 'AquaV'}
                                                </div>
                                                <img
                                                    src={item?.goods?.imagePath.startsWith('http') ? item?.goods?.imagePath : process.env.REACT_APP_IMAGE_URL + item?.goods?.imagePath || IMAGE_DEFAULT_CHOICE}
                                                    onError={({ currentTarget }) => {
                                                        currentTarget.onerror = null; // prevents looping
                                                        currentTarget.src = IMAGE_DEFAULT_CHOICE;
                                                    }}
                                                    alt=""
                                                />
                                            </div>
                                            <div className="line_dashed" style={{ border: "none" }}>
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
                                                {/*<img src={IMAGE_FAKE_QR} alt=""/>*/}
                                                <div
                                                    style={{ zIndex: "2", marginTop: "38px" }}
                                                    onClick={() => {
                                                        window.location = item?.shortLink
                                                    }}
                                                    className="button_use_voucher"
                                                >
                                                    {t('voucher.buttonViewVoucher')}
                                                </div>
                                                {/*    <div style={{marginTop: "8px"}} className="price">*/}
                                                {/*        {t('voucher.price')}:{' '}*/}
                                                {/*        <span style={{color: 'red'}}>*/}
                                                {/*    {ConvertNumber(item?.voucherPrice)} VNĐ*/}
                                                {/*</span>*/}
                                                {/*    </div>*/}
                                                <div className="price" style={{ paddingTop: "24px" }}>
                                                    {t('voucher.type')}:{' '}
                                                    <span style={{ color: 'red' }}>
                                                        {getTypeVoucherString(item?.voucherType)}
                                                    </span>
                                                </div>
                                                {item?.voucherStatus === VoucherStatus.USED ? (
                                                    <div className="price">
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
                        </div> :
                        <div style={{ textAlign: "center", marginTop: "24px", fontSize: "24px", fontStyle: "italic" }}>
                            {t('voucher.noVoucherChild')}
                        </div>}
                </div>}
            </div>
        </Backdrop>
    );
};

// Styled component for the popup container using MUI's styled
const PopupContainer = styled('div')({
    background: 'white',
    borderRadius: '8px',
    position: 'relative',
});

const PopupMessage = ({ data, onClose }) => {
    console.log(`data`, data)
    const handleClickOutside = (event) => {
        if (event.target.className.includes('MuiBackdrop-root')) {
            onClose(); // Trigger onClose when clicking on the backdrop
        }
    };

    return (
        <Backdrop sx={{ color: '#fff', zIndex: 20 }} open={true} onClick={handleClickOutside}>
            <PopupContainer onClick={(e) => e.stopPropagation()}> {/* Prevent click inside the popup from closing */}
                <div className="pop_up_modal">
                    <div className="row_popup">
                        <MdClear onClick={onClose} style={{ position: "absolute", right: "10px", cursor: "pointer", zIndex: 21 }} size={32} />

                        <div className="box" style={{ position: 'relative', width: '96%', height: 'auto', margin: 'auto' }}>
                            <div className="product" style={{ position: 'relative', margin: 'auto', height: 'auto', width: '164px', left: 'auto' }}>
                                <img
                                    style={{ position: 'relative', width: '164px', height: 'auto', left: 'auto' }}
                                    className={"image_item_product"}
                                    src={data?.imagePath?.startsWith('http') ? data?.imagePath : process.env.REACT_APP_IMAGE_URL + data?.imagePath || IMAGE_DEFAULT_CHOICE}
                                    onError={({ currentTarget }) => {
                                        currentTarget.onerror = null; // prevents looping
                                        currentTarget.src = IMAGE_DEFAULT_CHOICE;
                                    }}
                                    alt=""
                                />
                            </div>
                        </div>

                        <div className="content" style={{ marginTop: '10px' }}>
                            <div className="frame-1000003390">
                                <div className="the-coffee"
                                    style={{ maxWidth: '100%', overflow: 'visible', wordWrap: 'break-word', whiteSpace: 'normal', fontSize: '16px' }}>{data?.name}</div>
                            </div>
                            <div className="item_product_value">
                                <span className="item_value_vnd">{ConvertNumber(data?.sellPrice)}</span>
                                <span className="item_value_vnd2"> VND</span>
                            </div>
                            <div
                                className="lowery-s-gourmet-whole-bean-and-wired-willey-s-white-coffee"
                                style={{ height: 'auto', maxWidth: '100%', font: '400 14px "Roboto-Regular", sans-serif' }}>
                                <p style={{ whiteSpace: "pre-line", overflow: "visible", wordWrap: 'break-word', textOverflow: "ellipsis" }} dangerouslySetInnerHTML={{ __html: data?.description }}></p>
                            </div>
                        </div>
                    </div>
                </div>
            </PopupContainer>
        </Backdrop>
    );
};

const ProductChoice = ({ dataPre, dataVoucher, otp, onReload }) => {
    // console.log(`ProductChoice dataPre: ${JSON.stringify(dataPre)}, otp: ${otp}`);
    // console.log(`ProductChoice dataVoucher: ${JSON.stringify(dataVoucher)}`);

    const { t, i18n } = useTranslation()
    const [listProductChoice, setListProductChoice] = useState([])
    const [selectChoice, setSelectChoice] = useState(null)
    const [showHistory, setShowHistory] = useState(false)
    const [errorOTP, setErrorOTP] = useState(null)
    const [loading, setLoading] = useState(false)
    const [errorData, setErrorData] = useState(null)

    const [popupData, setPopupData] = useState(null); // State for popup data

    const togglePopup = (data) => {
        setPopupData(data);
    };

    const onReloadView = () => {
        if (dataVoucher?.goods?.choices && dataVoucher?.goods?.choices.map.length > 0) {
            let array = []
            dataVoucher?.goods?.choices.map((item) => {
                array.push({ ...item, numberSelect: 0 })
            })
            setListProductChoice(array)
        }
    }

    useEffect(() => {
        if (dataVoucher) {
            onReloadView()
        }
    }, [dataVoucher])
    const onClickMinus = (id) => {
        let array = []
        listProductChoice.map((item) => {
            if (isUnlimitedProduct(item)) {
                if (item.id === id && item.numberSelect >= 1) {
                    array.push({ ...item, remainingCount: item.remainingCount + 1, numberSelect: item.numberSelect - 1 })
                } else {
                    array.push({ ...item, remainingCount: item.remainingCount + item.numberSelect, numberSelect: 0 })
                }
            } else {
                if (item.id === id && item.numberSelect >= 1 && item.remainingCount >= 0) {
                    array.push({ ...item, remainingCount: item.remainingCount + 1, numberSelect: item.numberSelect - 1 })
                } else {
                    array.push({ ...item, remainingCount: item.remainingCount + item.numberSelect, numberSelect: 0 })
                }
            }
        })
        setListProductChoice(array)
    }

    const onClickPlus = (id) => {
        let array = []
        listProductChoice.map((item) => {
            if (isUnlimitedProduct(item)) {
                if (item.id === id) {
                    if (item.numberSelect >= 0 && (item.numberSelect + 1) * item.sellPrice <= dataVoucher?.balance) {
                        array.push({
                            ...item,
                            remainingCount: item.remainingCount - 1,
                            numberSelect: item.numberSelect + 1
                        })
                    } else {
                        array.push(item)
                    }
                } else {
                    array.push({ ...item, remainingCount: item.remainingCount + item.numberSelect, numberSelect: 0 })
                }
            } else {
                if (item.id === id) {
                    if (item.numberSelect >= 0 && item.remainingCount >= 1 && (item.numberSelect + 1) * item.sellPrice <= dataVoucher?.balance) {
                        array.push({
                            ...item,
                            remainingCount: item.remainingCount - 1,
                            numberSelect: item.numberSelect + 1
                        })
                    } else {
                        array.push(item)
                    }
                } else {
                    array.push({ ...item, remainingCount: item.remainingCount + item.numberSelect, numberSelect: 0 })
                }
            }
        })
        setListProductChoice(array)
    }

    const onGetListVoucherChild = () => {
        setShowHistory(true)
    }

    const onConfirmChoiceV1 = (otp) => {
        setLoading(true)
        axios.post(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/vouchers/choice/choose`, {
            ...selectChoice, token: otp
        }).then((response) => {
            setErrorOTP(null)
            setSelectChoice(null)
            message.success(t('voucher.choiceSuccess'), [3])
            onReload()
            setTimeout(() => {
                setLoading(false)
                setShowHistory(true)
            }, 1000)
        }).catch((error) => {
            setLoading(false)
            if (error.response.data.code === 1025) {
                setErrorOTP(t('voucher.errorOTP'))
            } else {
                setSelectChoice(null)
                setErrorData({
                    title: error?.response?.data?.code,
                    message: renderMessage(error?.response?.data?.code, t),
                    onAction: () => {
                        onReload()
                        setTimeout(() => {
                            onReloadView()
                            setErrorData(null)
                        }, 1000)
                    }
                })
            }
        })
    }

    const onConfirmChoiceV2 = (payload) => {
        setLoading(true)
        axios.post(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/v2/choice/choose`, payload).then((response) => {
            setErrorOTP(null)
            setSelectChoice(null)
            message.success(t('voucher.choiceSuccess'), [3])
            onReload()
            setTimeout(() => {
                setLoading(false)
                setShowHistory(true)
            }, 1000)
        }).catch((error) => {
            setLoading(false)
            if (error.response.data.code === 1025) {
                setErrorOTP(t('voucher.errorOTP'))
            } else {
                setSelectChoice(null)
                setErrorData({
                    title: error?.response?.data?.code,
                    message: renderMessage(error?.response?.data?.code, t),
                    onAction: () => {
                        onReload()
                        setTimeout(() => {
                            onReloadView()
                            setErrorData(null)
                        }, 1000)
                    }
                })
            }
        })
    }

    const checkPlusIcon = (item) => {
        if (isUnlimitedProduct(item)) {
            if (item.numberSelect >= 0 && (item.numberSelect + 1) * item.sellPrice <= dataVoucher?.balance) {
                return false
            } else {
                return true
            }
        } else {
            if (item.remainingCount === 0) {
                return true
            } else if (item.numberSelect >= 0 && item.remainingCount >= 1 && (item.numberSelect + 1) * item.sellPrice <= dataVoucher?.balance) {
                return false
            } else return true
        }
    }

    const handlePurchase = (item) => {
        if (isVersionV2(dataPre)) {
            const payload = {
                "choiceVoucherId": dataVoucher?.id,
                "otp": otp,
                "choices": [
                    {
                        "goodsId": item.id,
                        "quantity": item.numberSelect
                    }
                ]
            };

            onConfirmChoiceV2(payload);
        } else {
            setSelectChoice({
                "choiceVoucherId": dataVoucher?.id,
                "token": null,
                "choices": [
                    {
                        "goodsId": item.id,
                        "quantity": item.numberSelect
                    }
                ]
            })
        }
    }

    const getColor = (quantity) => {
        if (quantity === 0) return 'red';
        if (quantity < 10) return '#FDB022';
        return '#039855';
    };

    const getTextKey = (quantity) => {
        if (quantity === 0) return 'voucher.leftInStocksZero';
        if (quantity < 10) return 'voucher.leftInStocksLess';
        return 'voucher.leftInStocksMore';
    };

    const QuantityIndicator = ({ balance, item }) => {
        // console.log('balance', balance, 'item', item)
        // If balance is not enough, show red color
        if (balance && item?.sellPrice && balance < item.sellPrice) {
            return (
                <span style={{ color: getColor(0) }}>
                    {t('voucher.remainingNotEnough')}
                </span>
            );
        }

        // If quantity is unlimited, show green color
        if (isUnlimitedProduct(item)) {
            return <span>{t('voucher.unlimited')}</span>;
        }

        // If quantity is not unlimited, show color by quantity
        const textKey = getTextKey(item?.remainingCount);
        return (
            <span style={{ color: getColor(item?.remainingCount) }}>
                {t(textKey, { quantity: item?.remainingCount })}
            </span>
        );
    };

    return (
        <ProductChoiceStyle>

            {errorData &&
                <PopupError title={errorData.title} message={errorData.message} onAction={errorData.onAction} />}

            <GlobalBackdrop isLoading={loading} />

            {showHistory && <PopupListVoucherChild
                data={dataVoucher}
                onClose={() => {
                    onReload()
                    setTimeout(() => {
                        setShowHistory(false)
                    }, 100)
                }} />}

            {isVersionV1(dataPre) && selectChoice && <PopupConfirmOTP
                error={errorOTP}
                onClickContinue={onConfirmChoiceV1}
                onClose={() => {
                    setErrorOTP(null)
                    setSelectChoice(null)
                }} />}

            {popupData && (
                <PopupMessage data={popupData} onClose={() => setPopupData(null)} />
            )}

            <div style={{ marginBottom: "20px", fontSize: "20px", fontWeight: "bold" }}>
                {t('voucher.productsChoices')}
            </div>
            <div className="product_choice">
                <div className="header">
                    <div className="graphic">
                        <div className="rectangle-1783"></div>
                        <div className="rectangle-1784"></div>
                    </div>
                    <div className="balance">
                        <div className="current-balance">{t('voucher.current_balance')}</div>
                        <div className="frame-1000003391">
                            <div className="_200-000">{ConvertNumber(dataVoucher?.balance)}</div>
                            <div className="vnd">VND</div>
                        </div>
                        <div className="frame-1000003392">
                            <div className="voucher-original-price-500-000-00">
                                <span>
                                    <span className="voucher-original-price-500-000-00-span">
                                        {t('voucher.voucherOriginalPrice')} {ConvertNumber(dataVoucher?.voucherPrice)}
                                    </span>
                                    {/*<span className="voucher-original-price-500-000-00-span2">*/}
                                    {/*    00*/}
                                    {/*</span>*/}
                                    <span className="voucher-original-price-500-000-00-span3">
                                        {" "}
                                    </span>
                                </span>
                            </div>
                            <div className="vnd2">VND</div>
                        </div>
                    </div>
                    <div className="button" onClick={onGetListVoucherChild}>
                        <div className="state-layer">
                            <AiOutlineHistory className="material-symbols-history" />
                            <div className="button2">{t('voucher.history')}</div>
                        </div>
                    </div>
                </div>

                {listProductChoice.length > 0 && <div className="header2">
                    <div className="choose-product">{t('voucher.chooseProduct')}</div>
                </div>}

                <div className="product_choice2">
                    <div className="container-product_choice">
                        {listProductChoice.length > 0 &&
                            listProductChoice.map((item, index) => {
                                return (
                                    <div className="card" key={index}>
                                        <div className="box" onClick={() => togglePopup(item)}>
                                            <button
                                                onClick={() => togglePopup(item)}
                                                style={{
                                                    position: 'absolute',
                                                    top: '-8px',
                                                    right: '-10px',
                                                    background: 'none',
                                                    border: 'none',
                                                    cursor: 'pointer',
                                                    zIndex: 1
                                                }}
                                                aria-label="Expand"
                                            >
                                                <FaExpandAlt size={16} style={{ opacity: 0.4 }} />
                                            </button>
                                            <div className="product">
                                                <img
                                                    className={"image_item_product"}
                                                    src={item?.imagePath?.startsWith('http') ? item?.imagePath : process.env.REACT_APP_IMAGE_URL + item?.imagePath || IMAGE_DEFAULT_CHOICE}
                                                    onError={({ currentTarget }) => {
                                                        currentTarget.onerror = null; // prevents looping
                                                        currentTarget.src = IMAGE_DEFAULT_CHOICE;
                                                    }}
                                                    alt=""
                                                />
                                            </div>
                                        </div>
                                        <div className="content" onClick={() => togglePopup(item)}>
                                            <div className="frame-1000003390">
                                                <div className="the-coffee">{item?.name}</div>
                                            </div>
                                            <div className="item_product_value">
                                                <span className="item_value_vnd">{ConvertNumber(item?.sellPrice)}</span>
                                                <span className="item_value_vnd2"> VND</span>
                                            </div>
                                            <div
                                                className="lowery-s-gourmet-whole-bean-and-wired-willey-s-white-coffee">
                                                <p style={{ whiteSpace: "pre-line", overflow: "hidden", textOverflow: "ellipsis" }} dangerouslySetInnerHTML={{ __html: item?.description }}></p>
                                            </div>
                                        </div>
                                        <div className="price-quantity">
                                            <div className="stocks_amount">
                                                <QuantityIndicator balance={dataVoucher?.balance} item={item} />
                                            </div>
                                            <div className="amount">
                                                <AiOutlineMinusSquare
                                                    style={{ color: item.numberSelect === 0 ? '#ccc' : 'black' }}
                                                    onClick={() => {
                                                        onClickMinus(item.id)
                                                    }} className="plus-square" />
                                                <div className="_0">{item.numberSelect}</div>
                                                <AiOutlinePlusSquare
                                                    style={{ color: checkPlusIcon(item) ? '#ccc' : 'black' }}
                                                    onClick={() => {
                                                        onClickPlus(item.id)
                                                    }} className="minus-square" />
                                            </div>
                                        </div>
                                        <BootstrapButton
                                            onClick={() => handlePurchase(item)}
                                            sx={{ backgroundColor: '#4c3d3d' }}
                                            variant='contained'
                                            disabled={item.numberSelect === 0}>{t('voucher.select')}</BootstrapButton>
                                    </div>
                                )
                            })
                        }

                    </div>
                </div>
            </div>
        </ProductChoiceStyle>
    );
};

export default ProductChoice;


const BootstrapButton = styled(Button)({
    boxShadow: 'none',
    textTransform: 'none',
    border: 'unset',
    height: "28px",
    fontSize: "12px",
    width: "100%",
    backgroundColor: '#4c3d3d',
    borderColor: '#4c3d3d',
    color: "white",
    fontFamily: [
        '-apple-system',
        'BlinkMacSystemFont',
        '"Segoe UI"',
        'Roboto',
        '"Helvetica Neue"',
        'Arial',
        'sans-serif',
        '"Apple Color Emoji"',
        '"Segoe UI Emoji"',
        '"Segoe UI Symbol"',
    ].join(','),
    '&:hover': {
        backgroundColor: '#4c3d3d',
        borderColor: '#4c3d3d',
        boxShadow: 'none',
    },
    '&:active': {
        boxShadow: 'none',
        backgroundColor: '#4c3d3d',
        borderColor: '#4c3d3d',
    },
    '&:focus': {
        boxShadow: '0 0 0 0.2rem rgba(0,123,255,.5)',
    },
});