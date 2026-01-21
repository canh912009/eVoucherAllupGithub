import axios from "axios";
import React, { useEffect, useState } from "react";
import { useTranslation } from "react-i18next";
import "slick-carousel/slick/slick-theme.css";
import "slick-carousel/slick/slick.css";
import Cookies from "universal-cookie";
import GlobalBackdrop from "../../components/GlobalBackdrop";
import IMAGE_ENGLISH from "../../images/english.png";
import LOGO from "../../images/Logo.png";
import ERROR_IMAGE from "../../images/red-error.png";
import IMAGE_VIETNAM from "../../images/vietnam.png";
import { getImage } from "../../utils/utils";
import ActiveError from "./ActiveError";
import ActiveInfo from "./ActiveInfo";
import ActiveSuccess from "./ActiveSuccess";
import { Container } from "./styles";

export const RESULT_STATUS = {
    FAIL: "FAIL",
    SUCCESS: "SUCCESS",
    INPUT: "INPUT",
};

const DetailVoucher = () => {
    document.title = "Voucher";
    const cookies = new Cookies();

    const { t, i18n } = useTranslation();
    if (!cookies.get("locales")) {
        cookies.set("locales", "vi");
    }
    const asPath = window.location.search;
    const urlParams = new URLSearchParams(asPath);
    const activationKey = urlParams.get("_Ss");
    const [imagePath, setImagePath] = useState(null);
    const [goodName, setGoodName] = useState(null);
    const [loading, setLoading] = useState(false);
    const [valueName, setValueName] = useState("");
    const [phone, setPhone] = useState("");

    const [result, setResult] = useState(
        //         {
        //     status: "",
        //     detail: {
        //       title: t("errorTitle.incorrect_qr_voucher"),
        //       message: t("error.incorrect_qr_voucher"),
        //     },
        //   }
        null
    );

    const onResetData = () => {
        const fetchData = async () => {
            setLoading(true);
            if (!activationKey) {
                setResult({
                    status: RESULT_STATUS.FAIL,
                    detail: {
                        title: "errorTitle.incorrect_qr_voucher",
                        message: "error.incorrect_qr_voucher",
                    },
                    backInError: false,
                });
            }
            try {
                const res = await axios.get(
                    `${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/publish/publishDetails?activationKey=${activationKey}`
                );
                const { data } = res.data;

                console.log(data);

                if (data) {
                    setLoading(false);
                    setGoodName(data.name);
                    setImagePath(data.imagePath);
                    setResult({
                        status: RESULT_STATUS.INPUT,
                        detail: {
                            title: "",
                            message: "",
                        },
                    });
                } else {
                    setResult({
                        status: RESULT_STATUS.FAIL,
                        detail: {
                            title: "errorTitle.incorrect_qr_voucher",
                            message: "error.incorrect_qr_voucher",
                        },
                        backInError: false,
                    });
                    setLoading(false);
                }
            } catch (error) {
                setResult({
                    status: RESULT_STATUS.FAIL,
                    detail: {
                        title: "errorTitle.incorrect_qr_voucher",
                        message: "error.incorrect_qr_voucher",
                    },
                    backInError: false,
                    showErrorImage: true,
                });
                setLoading(false);
            }
        };

        fetchData();
    };

    useEffect(() => {
        onResetData();
    }, [activationKey]);

    const onErrorBack = () => {
        setValueName("");
        setPhone("");
        setResult({
            status: RESULT_STATUS.INPUT,
            detail: {
                title: "",
                message: "",
            },
        });
    };

    return (
        <Container>
            <GlobalBackdrop isLoading={loading} />

            {/*{dataVoucher && !dataVoucher.active*/}
            {/*    && <ActiveVoucher data={dataVoucher}*/}
            {/*                      setIsReload={setIsReload}*/}
            {/*                        checkExpriceDate={() => {checkExpriceDate()}}*/}
            {/*    />*/}
            {/*}*/}

            <div className="row">
                <div className="container voucher_info">
                    <div className="logo" style={{ margin: "20px 0 0 0" }}>
                        <img src={LOGO} alt="" />
                        <div className={"row_language"}>
                            <div
                                className={"item_language"}
                                style={{ margin: "0 8px" }}
                                onClick={() => {
                                    i18n.changeLanguage("vi");
                                    cookies.set("locales", "vi");
                                }}
                            >
                                <img
                                    style={{
                                        height: "20px",
                                        width: "30px",
                                        borderRadius: "4px",
                                    }}
                                    src={IMAGE_VIETNAM}
                                    alt=""
                                />
                                {i18n.language !== "vi" && (
                                    <div className={"background_mask_language"}></div>
                                )}
                            </div>
                            <div
                                className={"item_language"}
                                onClick={() => {
                                    i18n.changeLanguage("en");
                                    cookies.set("locales", "en");
                                }}
                            >
                                <img
                                    style={{
                                        height: "20px",
                                        width: "30px",
                                        borderRadius: "4px",
                                    }}
                                    src={IMAGE_ENGLISH}
                                    alt=""
                                />
                                {i18n.language === "vi" && (
                                    <div className={"background_mask_language"}></div>
                                )}
                            </div>
                        </div>
                    </div>
                    <div className="dash_line" />
                    <div className="line_solid">
                        <div className="left_line" />
                        <div className="right_line" />
                    </div>
                    {/*if result.showErrorImage : show error image else show good image*/}
                    {result != null && result.showErrorImage ? (
                        <div style={{ display: "flex", justifyContent: "center" }}>
                            <div style={{ width: '80%', textAlign: 'center', padding: '30px 0', marginBottom: '20px', backgroundColor: '#D9D9D9', borderRadius: '15px' }}>
                                <img src={ERROR_IMAGE} alt="" style={{ width: '90px', height: '90px' }} />
                            </div>
                        </div>
                    ) : (
                        <>
                            <div style={{ display: "flex", justifyContent: "center" }}>
                                <img
                                    style={{ width: "90%" }}
                                    src={
                                        imagePath?.startsWith("http")
                                            ? imagePath
                                            : process.env.REACT_APP_IMAGE_URL + imagePath
                                    }
                                    onError={({ currentTarget }) => {
                                        currentTarget.onerror = null; // prevents looping
                                        currentTarget.src = getImage(null);
                                    }}
                                    alt=""
                                />
                            </div>
                            <div className="product_name">{goodName || "NULL"}</div>
                        </>
                    )}

                </div>

                <div
                    className="container receiver_info"
                    style={{ margin: "40px auto 0 auto" }}
                >
                    <div className="line_solid">
                        <div className="left_line" />
                        <div className="right_line" />
                    </div>
                    <div style={{ padding: "0 15px" }}>
                        {console.log(result)}
                        {result && result.status === RESULT_STATUS.FAIL && (
                            <ActiveError
                                title={t(result.detail.title)}
                                message={t(result.detail.message)}
                                backWhenError={result.backInError}
                                onErrorBack={onErrorBack}
                                backTitle={t("button.back_to_info")}
                            />
                        )}

                        {result && result.status === RESULT_STATUS.SUCCESS && (
                            <ActiveSuccess
                                title={t("title.voucher_active_success")}
                                message={t("message.voucher_active_success")}
                                name={valueName}
                                phone={phone}
                            />
                        )}

                        {(result && (
                            !result.status ||
                            result.status === RESULT_STATUS.INPUT)) && (
                                <ActiveInfo
                                    t={t}
                                    setLoading={setLoading}
                                    activationKey={activationKey}
                                    setResult={setResult}
                                    valueName={valueName}
                                    setValueName={setValueName}
                                    phone={phone}
                                    setPhone={setPhone}
                                // setErrorData={setErrorData}
                                />
                            )}
                    </div>
                </div>
            </div>
        </Container>
    );
};
export default DetailVoucher;
