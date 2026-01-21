import { Backdrop } from "@mui/material";
import axios from "axios";
import React, { useEffect, useState } from "react";
import { Trans, useTranslation } from "react-i18next";
import { AiOutlineHistory } from "react-icons/ai";
import { MdArrowBack } from "react-icons/md";
import { SlArrowDown, SlArrowUp } from 'react-icons/sl';
import GlobalBackdrop from "../../components/GlobalBackdrop";
import PopupError from "../../components/PopupCustom/error";
import DashedLine from "../../components/ui/DashedLine";
import IMAGE_DEFAULT_CHOICE from '../../images/imageVoucher.png';
import IMAGE_LOGO from '../../images/logo_white.png';
import IMAGE_TIKER_USED from '../../images/Sticker_EN/used.png';
import IMAGE_TIKER_USED_VN from '../../images/Sticker_VN/used.png';
import { isExpiredVoucher } from "../../utils/utils";
import VoucherStatus, { TransferStatus } from "../../utils/VoucherStatus";
import { ProductInternalLCStyle } from "./styles";
import VoucherScanCode from "./VoucherScanCode";

const padZero = (num) => {
    return num.toString().padStart(2, '0');
}

export const PopupListVoucherChild = ({ onClose, data }) => {
    const { t, i18n } = useTranslation()
    const [listVoucher, setListVoucher] = useState([]);
    const [loading, setLoading] = useState(false);

    const [isAscending, setIsAscending] = useState(true);

    const toggleHistory = () => {
        setIsAscending(!isAscending);
    };

    useEffect(() => {
        setLoading(true)
        axios.get(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/vouchers/lc/history/${data.id}`)
            .then((response) => {
                setLoading(false)
                setListVoucher(response.data.data)
            }).catch((error) => {
                setLoading(false)
            }
            )
    }, [data.id]);

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
                <div className={"icon_list_voucher_child"}>
                    <img src={IMAGE_LOGO} alt="" />
                </div>

                <DashedLine height="3px" dashLength="20" gapLength="10" color="#D7D5D5" />

                <div style={{
                    width: "100%",
                    display: "flex",
                    justifyContent: "left",
                    fontSize: "20px",
                    fontWeight: "600",
                    marginTop: "10px",
                    marginLeft: "8%",
                }}>
                    <span>
                        {t('voucher.voucher_history')}
                    </span>
                    <div onClick={toggleHistory} style={{ cursor: "pointer", marginLeft: "10px" }}>
                        {isAscending ? <SlArrowUp /> : <SlArrowDown />}
                    </div>
                </div>

                {!loading && isAscending && (
                    <div>
                        {listVoucher?.length > 0 ? (
                            <div className="row_list_voucher">
                                {listVoucher.map((item, index) => (
                                    <div key={index} className="item_voucher">
                                        <div className="logo">
                                            <img
                                                src={i18n.language === "vi" ? IMAGE_TIKER_USED_VN : IMAGE_TIKER_USED}
                                                onError={({ currentTarget }) => {
                                                    currentTarget.onerror = null;
                                                    currentTarget.src = IMAGE_DEFAULT_CHOICE;
                                                }}
                                                alt=""
                                            />
                                        </div>
                                        <div className="line_dashed">
                                            <div className="line_dashed_absolute" style={{ top: '-14px' }} />
                                            <div className="line_dashed_absolute" style={{ bottom: '-14px' }} />
                                        </div>
                                        <div className="detail">
                                            <div className="title">
                                                <span>{item.storeName}</span>
                                            </div>
                                            <div className="date">
                                                <span>{item.transactionDate}</span>
                                            </div>
                                            <div className="remaining-count">
                                                <span>
                                                    <Trans
                                                        i18nKey="voucher.voucher_history_times_left"
                                                        values={{ param: padZero(item.remainingCount) }}
                                                        components={{ strong: <strong /> }}
                                                    />
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div style={{ textAlign: "center", marginTop: "24px", fontSize: "24px", fontStyle: "italic" }}>
                                {t('voucher.noVoucherChild')}
                            </div>
                        )}
                    </div>
                )}
            </div>
        </Backdrop>
    );
};


const ProductInternalLC = ({ dataVoucher, onReload, otp }) => {
    // console.log('ProductInternalLC', dataVoucher);

    const { t, i18n } = useTranslation();
    const [showHistory, setShowHistory] = useState(false);
    const [loading, setLoading] = useState(false);
    const [errorData, setErrorData] = useState(null);

    const settings = {
        speed: 500,
        slidesToShow: 1,
        slidesToScroll: 1,
    };

    const onReloadView = () => {

    }

    useEffect(() => {
        if (dataVoucher) {
            onReloadView()
        }
    }, [dataVoucher])

    const onGetListVoucherChild = () => {
        setShowHistory(true)
    }

    const allowShowScanCode = () => {
        return (dataVoucher?.voucherStatus === VoucherStatus.NORMAL || dataVoucher?.voucherStatus === VoucherStatus.PART_USED)
            && !isExpiredVoucher(dataVoucher)
    }

    const getVoucherDescription = () => {
        if (isExpiredVoucher(dataVoucher)) {
            return <Trans
                i18nKey="voucher.voucher_lc_expired"
                values={{ date: dataVoucher?.expireDate }}
                components={{ strong: <strong /> }}
            />
        }
        switch (dataVoucher?.voucherStatus) {
            case VoucherStatus.EXPIRE:
                return <Trans
                    i18nKey="voucher.voucher_lc_expired"
                    values={{ date: dataVoucher?.expireDate }}
                    components={{ strong: <strong /> }}
                />
            case VoucherStatus.USED:
                return <Trans
                    i18nKey="voucher.voucher_lc_used"
                    values={{ param: padZero(dataVoucher?.usageCount) + '/' + padZero(dataVoucher?.usageCount) }}
                    components={{ strong: <strong /> }}
                />
            case VoucherStatus.DISABLED:
                if (dataVoucher?.transferStatus === TransferStatus.TRANSFER) {
                    return <Trans
                        i18nKey="voucher.voucher_is_transferred"
                        components={{ strong: <strong /> }}
                    />
                } else {
                    return <Trans
                        i18nKey="voucher.voucher_is_disabled"
                        components={{ strong: <strong /> }}
                    />
                }
            default:
                return null;
        }
    }

    return (
        <ProductInternalLCStyle>

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
                }}
            />}

            <div className="container">
                <div className="header">
                    <div className="graphic">
                        <div className="rectangle-1783"></div>
                        <div className="rectangle-1784"></div>
                    </div>
                    <div className="balance">
                        <div className="current-balance">{t('voucher.remaining_count')}</div>
                        <div className="frame-1000003391">
                            <div className="_200-000">{padZero(dataVoucher?.usageRemainingCount)}</div>
                            <div className="count_times">{t('voucher.count_times')}</div>
                        </div>
                        <div className="frame-1000003392">
                            <div className="voucher-original-price-500-000-00">
                                <span>
                                    <span className="voucher-original-price-500-000-00-span">
                                        {t('voucher.total_count_times', { quantity: padZero(dataVoucher?.usageCount) })}
                                    </span>
                                </span>
                            </div>
                        </div>
                    </div>
                    <div className="button" onClick={onGetListVoucherChild}>
                        <div className="state-layer">
                            <AiOutlineHistory className="material-symbols-history" />
                            <div className="button2">{t('voucher.history')}</div>
                        </div>
                    </div>
                </div>

                <div className="body">
                    {allowShowScanCode() && (
                        <div className="qr-code">
                            <VoucherScanCode
                                t={t}
                                status={dataVoucher?.voucherStatus}
                                systemType={dataVoucher?.system}
                                displayType={dataVoucher?.extPinType}
                                pinPassword={dataVoucher?.externalPinPassword}
                                pinNo={dataVoucher?.extPinNo}
                                serialNo={null}
                                settings={settings}
                                otp={otp}
                                isPosLink={dataVoucher?.goods?.brand?.posLink}
                                dataVoucher={dataVoucher}
                            // renderTimeCancelUse={renderTimeCancelUse}
                            />
                        </div>
                    )
                    }

                    <div style={{ textAlign: "center" }}>
                        <span style={{ color: "red" }}> {getVoucherDescription()} </span>
                    </div>
                </div>
            </div>
        </ProductInternalLCStyle>
    );
};

export default ProductInternalLC;