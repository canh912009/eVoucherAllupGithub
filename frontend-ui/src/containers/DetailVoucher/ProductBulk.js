import { Backdrop, Button, Pagination } from "@mui/material";
import { styled } from "@mui/material/styles";
import { message } from "antd";
import axios from "axios";
import React, { useEffect, useRef, useState } from "react";
import { useTranslation } from "react-i18next";
import { AiOutlineHistory, AiOutlineMinusSquare, AiOutlinePlusSquare, AiOutlineSearch } from "react-icons/ai";
import { FaExpandAlt } from "react-icons/fa";
import { MdArrowBack, MdClear } from "react-icons/md";
import { SlArrowLeft } from 'react-icons/sl';
import OtpInput from 'react-otp-input';
import GlobalBackdrop from "../../components/GlobalBackdrop";
import PopupError from "../../components/PopupCustom/error";
import { default as IMAGE_DEFAULT_CHOICE, default as LOGO_MC_DONALD } from '../../images/imageVoucher.png';
import IMAGE_LOGO from '../../images/logo_white.png';
import IMAGE_OTP from '../../images/sendOTP.png';
import VoucherStatus from "../../utils/VoucherStatus";
import { getTypeVoucherString } from "../../utils/VoucherType";
import useDebounce from '../../utils/useDebounce';
import { ConvertNumber, getStatus, isUnlimitedProduct, renderMessage, isVersionV1, isVersionV2 } from "../../utils/utils";
import { InputOTP, ProductBulkStyle } from "./styles";

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
        axios.get(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/vouchers/bulk/children/${data.id}`)
            .then((response) => {
                setLoading(false)
                setListVoucher(response.data.data)
            }).catch((error) => {
                setLoading(false)
            }
            )
    }, [])

    const getTextDes = (item) => {
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
                                    src={getImageSrc(data?.goods?.imagePath)}
                                    alt=""
                                />
                            </div>
                        </div>

                        <div className="content">
                            <div className="frame-1000003390">
                                <div className="the-coffee"
                                    style={{ maxWidth: '100%', overflow: 'visible', wordWrap: 'break-word', whiteSpace: 'normal', fontSize: '16px' }}>{data?.goods?.name}</div>
                            </div>
                            <div className="item_product_value">
                                <span className="item_value_vnd">{ConvertNumber(data?.goods?.sellPrice)}</span>
                                <span className="item_value_vnd2"> VND</span>
                            </div>
                            <div
                                className="lowery-s-gourmet-whole-bean-and-wired-willey-s-white-coffee"
                                style={{ height: 'auto', maxWidth: '100%', font: '400 14px "Roboto-Regular", sans-serif' }}>
                                <p style={{ whiteSpace: "pre-line", overflow: "visible", wordWrap: 'break-word', textOverflow: "ellipsis" }} dangerouslySetInnerHTML={{ __html: data?.goods?.description }}></p>
                            </div>
                        </div>
                    </div>
                </div>
            </PopupContainer>
        </Backdrop>
    );
};


const ProductBulk = ({ dataPre, dataVoucher, otp, onReload }) => {
    // console.log(`ProductBulk dataPre: ${JSON.stringify(dataPre)}, otp: ${otp}`);
    // console.log(`ProductBulk dataVoucher: ${JSON.stringify(dataVoucher)}`);

    const { t, i18n } = useTranslation()

    const [selectChoice, setSelectChoice] = useState(null)
    const [showHistory, setShowHistory] = useState(false)
    const [errorOTP, setErrorOTP] = useState(null)
    const [loading, setLoading] = useState(false)
    const [errorData, setErrorData] = useState(null)

    const [popupData, setPopupData] = useState(null); // State for popup data

    const togglePopup = (data) => {
        setPopupData(data);
    };

    const onClickMinus = (id) => {
        setProducts(prevProducts => {
            if (prevProducts?.pageData?.length > 0) {
                const updatedPageData = prevProducts.pageData.map(item => {
                    const isUnlimited = isUnlimitedProduct(item.goods);
                    const numberSelect = item.numberSelect ?? 0;
                    const remainingCount = item.goods?.remainingCount ?? 0;

                    if (item?.bulkGoodsId === id && numberSelect >= 1 && (isUnlimited || remainingCount > 0)) {
                        return {
                            ...item,
                            numberSelect: numberSelect - 1,
                            goods: {
                                ...item.goods,
                                remainingCount: remainingCount + 1,
                            }
                        };
                    } else {
                        return {
                            ...item,
                            numberSelect: 0,
                            goods: {
                                ...item.goods,
                                remainingCount: remainingCount + numberSelect,
                            }
                        };
                    }
                });
                return { ...prevProducts, pageData: updatedPageData };
            }
            return prevProducts;
        });
    };

    const onClickPlus = (id) => {
        setProducts(prevProducts => {
            if (prevProducts?.pageData?.length > 0) {
                const updatedPageData = prevProducts.pageData.map(item => {
                    const isUnlimited = isUnlimitedProduct(item.goods);
                    const numberSelect = item.numberSelect ?? 0;
                    const remainingCount = item.goods?.remainingCount ?? 0;

                    if (item?.bulkGoodsId === id) {
                        if (numberSelect >= 0 && (isUnlimited || remainingCount > 0) && (numberSelect + 1) * item.goods?.sellPrice <= dataVoucher?.balance) {
                            return {
                                ...item,
                                numberSelect: numberSelect + 1,
                                goods: {
                                    ...item.goods,
                                    remainingCount: remainingCount - 1,
                                }
                            };
                        }
                    } else {
                        return {
                            ...item,
                            numberSelect: 0,
                            goods: {
                                ...item.goods,
                                remainingCount: remainingCount + numberSelect,
                            }
                        };
                    }
                    return item;
                });
                return { ...prevProducts, pageData: updatedPageData };
            }
            return prevProducts;
        });
    };


    const onGetListVoucherChild = () => {
        setShowHistory(true)
    }

    const onConfirmBulkV1 = (otp) => {
        setLoading(true)
        axios.post(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/bulk/choose`, {
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

    const onConfirmBulkV2 = (payload) => {
        setLoading(true)
        axios.post(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/v2/bulk/choose`, payload).then((response) => {
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
        // console.log('checkPlusIcon', item)

        item = { ...item, numberSelect: item?.numberSelect ?? 0 };

        if (isUnlimitedProduct(item?.goods)) {
            if (item?.numberSelect >= 0 && (item?.numberSelect + 1) * item.goods?.sellPrice <= dataVoucher?.balance) {
                return false;
            } else {
                return true;
            }
        } else {
            if (item.goods?.remainingCount === 0) {
                return true;
            } else if (item?.numberSelect >= 0 && item?.goods?.remainingCount >= 1 && (item?.numberSelect + 1) * item?.goods?.sellPrice <= dataVoucher?.balance) {
                return false;
            } else return true;
        }
    }

    const handlePurchase = (item) => {
        if (isVersionV2(dataPre)) {
            const payload = {
                "parentVoucherId": dataVoucher?.id,
                "otp": otp,
                "products": [
                    {
                        "goodsId": item.goods?.id,
                        "quantity": item.numberSelect
                    }
                ]
            };

            onConfirmBulkV2(payload);
        } else {
            setSelectChoice({
                "parentVoucherId": dataVoucher?.id,
                "token": null,
                "products": [
                    {
                        "goodsId": item.goods?.id,
                        "quantity": item.numberSelect
                    }
                ]
            })
        }
    }

    const PAGE_SIZE_CATEGORY = 8;
    const PAGE_SIZE_BRAND = 12;
    const PAGE_SIZE_PRODUCT = 10;

    const DEBOUNCE_DELAY = 500; // 500ms delay

    const [categorySearch, setCategorySearch] = useState('');
    const [categories, setCategories] = useState();
    const [categorySelected, setCategorySelected] = useState();
    const [currentPageCategory, setCurrentPageCategory] = useState(1);
    const debouncedCategorySearch = useDebounce(categorySearch, DEBOUNCE_DELAY);

    const [brandSearch, setBrandSearch] = useState('');
    const [brands, setBrands] = useState();
    const [brandSelected, setBrandSelected] = useState();
    const [currentPageBrand, setCurrentPageBrand] = useState(1);
    const debouncedBrandSearch = useDebounce(brandSearch, DEBOUNCE_DELAY);

    const [productSearch, setProductSearch] = useState('');
    const [products, setProducts] = useState();
    const [currentPageProduct, setCurrentPageProduct] = useState(1);
    const debouncedProductSearch = useDebounce(productSearch, DEBOUNCE_DELAY);

    const handleChangePageCategory = (event, value) => {
        setCurrentPageCategory(value);
    };

    const handleChangePageBrand = (event, value) => {
        setCurrentPageBrand(value);
    };

    const handleChangePageProduct = (event, value) => {
        setCurrentPageProduct(value);
    };

    // From list products to choose category and brand
    const handleClickBack = () => {
        // console.log('categorySelected', categorySelected);

        // setCurrentPageCategory(1);

        // setBrandSearch('');
        // setCurrentPageBrand(1);
        setBrandSelected(null);

        setProductSearch('');
        setCurrentPageProduct(1);
    };

    // Handle click on category item
    const handleClickCategory = (category) => {
        console.log('handleClickCategory', category, 'categorySelected', categorySelected);
        if (categorySelected?.bulkCtgrId === category?.bulkCtgrId) return;

        // Reset
        setBrandSelected(null);
        setBrandSearch('');
        setCurrentPageBrand(1);

        setProductSearch('');
        setCurrentPageProduct(1);
        setCategorySelected(category);
    };

    // Handle click on brand item
    const handleClickBrand = (brand) => {
        console.log('handleClickBrand', brand, 'brandSelected', brandSelected);

        if (brand?.bulkBrandId === brandSelected?.bulkBrandId) return;

        setProductSearch('');
        setCurrentPageProduct(1);
        setBrandSelected(brand);
    };

    const handleSearchCategory = (event) => {
        setCurrentPageCategory(1);

        setBrandSearch('');
        setCurrentPageBrand(1);

        setProductSearch('');
        setCurrentPageProduct(1);

        const value = event.target.value;
        setCategorySearch(value);
    };

    const handleSearchBrand = (event) => {
        setCurrentPageBrand(1);

        setProductSearch('');
        setCurrentPageProduct(1);

        const value = event.target.value;
        setBrandSearch(value);
    };

    const handleSearchProduct = (event) => {
        setCurrentPageProduct(1);

        const value = event.target.value;
        setProductSearch(value);
    };

    async function fetchCategories(pageNum, pageSize, goodsId, name) {
        try {
            const response = await axios.get(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/bulk/categories`, {
                params: { pageNum, pageSize, goodsId, name }
            });
            return response.data;
        } catch (error) {
            console.error('Error fetching categories:', error);
            throw error;
        }
    }

    async function fetchBrands(pageNum, pageSize, bulkCategoryId, name) {
        try {
            const response = await axios.get(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/bulk/brands`, {
                params: { pageNum, pageSize, bulkCategoryId, name }
            });
            return response.data;
        } catch (error) {
            console.error('Error fetching brands:', error);
            throw error;
        }
    }

    async function fetchGoods(pageNum, pageSize, bulkBrandId, name) {
        try {
            const response = await axios.get(`${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/bulk/goods`, {
                params: { pageNum, pageSize, bulkBrandId, name }
            });
            return response.data;
        } catch (error) {
            console.error('Error fetching goods:', error);
            throw error;
        }
    }

    const firstLoadRef = useRef(true);

    useEffect(() => {
        console.log(`currentPageCategory: ${currentPageCategory}, categorySearch: ${debouncedCategorySearch}`);

        if (!dataVoucher?.id || !dataVoucher?.goods?.id) {
            setCategories(null);
            return;
        }

        fetchCategories(currentPageCategory, PAGE_SIZE_CATEGORY, dataVoucher?.goods?.id, debouncedCategorySearch)
            .then(res => {
                console.log('Categories res:', res);
                setCategories(res?.data);
                if (firstLoadRef.current && res?.data?.pageData?.length > 0) {
                    setCategorySelected(res?.data?.pageData[0]);
                    firstLoadRef.current = false;
                }
            })
            .catch(error => console.error('Error:', error));
    }, [currentPageCategory, dataVoucher?.id, dataVoucher?.good?.id, debouncedCategorySearch]);


    useEffect(() => {
        console.log(`categorySelected: ${JSON.stringify(categorySelected)}, currentPageBrand: ${currentPageBrand}, brandSearch: ${debouncedBrandSearch}`);

        if (!categorySelected?.bulkCtgrId) {
            setBrands(null);
            return;
        }

        fetchBrands(currentPageBrand, PAGE_SIZE_BRAND, categorySelected?.bulkCtgrId, debouncedBrandSearch)
            .then(res => {
                console.log('Brands res:', res);
                setBrands(res?.data);
            })
            .catch(error => console.error('Error:', error));

    }, [categorySelected, currentPageBrand, debouncedBrandSearch]);

    useEffect(() => {
        console.log(`brandSelected: ${JSON.stringify(brandSelected)}, currentPageProduct: ${currentPageProduct}, productSearch: ${debouncedProductSearch}`);

        if (!brandSelected?.bulkBrandId) {
            setProducts(null);
            return;
        }

        fetchGoods(currentPageProduct, PAGE_SIZE_PRODUCT, brandSelected?.bulkBrandId, debouncedProductSearch)
            .then(res => {
                console.log('Goods res:', res);
                // setProducts(res?.data);

                // Initialize numberSelect to 0 for each item.goods
                const updatedData = res?.data?.pageData.map(item => ({
                    ...item,
                    numberSelect: 0
                }));

                setProducts({ ...res.data, pageData: updatedData });
            })
            .catch(error => console.error('Error:', error));

    }, [brandSelected, currentPageProduct, debouncedProductSearch]);

    const onReloadView = () => {
        // console.log('onReloadView', dataVoucher);

        if (products?.pageData && products?.pageData?.length > 0) {
            products.pageData = products.pageData.map(item => {
                return {
                    ...item,
                    numberSelect: 0
                };
            });
        }
    };

    useEffect(() => {
        if (dataVoucher) {
            onReloadView()
        }
    }, [dataVoucher])

    return (
        <ProductBulkStyle>
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
                onClickContinue={onConfirmBulkV1}
                onClose={() => {
                    setErrorOTP(null)
                    setSelectChoice(null)
                }} />}

            {popupData && (
                <PopupMessage data={popupData} onClose={() => setPopupData(null)} />
            )}

            <div className="product_choice">
                <div className="header">
                    <div className="graphic">
                        <div className="rectangle-1783"></div>
                        <div className="rectangle-1784"></div>
                    </div>
                    <div className="balance">
                        <div className="current-balance">{t('voucher.current_balance')}</div>
                        <div className="frame-1000003391">
                            <div className="_200-000">{ConvertNumber(dataVoucher?.balance ?? 0)}</div>
                            <div className="vnd">VND</div>
                        </div>
                        <div className="frame-1000003392">
                            <div className="voucher-original-price-500-000-00">
                                <span>
                                    <span className="voucher-original-price-500-000-00-span">
                                        {t('voucher.voucherOriginalPrice')} {ConvertNumber(dataVoucher?.voucherPrice ?? 0)}
                                    </span>
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

                {/* Brand Selected */}
                {brandSelected?.bulkBrandId && (
                    <div className="brand">
                        <div className="brand-left">
                            <SlArrowLeft className="arrow-icon"
                                onClick={handleClickBack}
                            />
                        </div>
                        <div className="brand-right">
                            <div className="brand-details">
                                <div className="brand-title">{brandSelected?.brand?.name}</div>
                                <div className="brand-description">{categorySelected?.category?.name}</div>
                            </div>
                            <img src={getImageSrc(brandSelected?.brand?.imgUrl)} alt="" className="brand-logo" />
                        </div>
                    </div>
                )}

                {/* Panel Categories */}
                {!brandSelected && (
                    <div className="product_choice" style={{ marginTop: "0px", padding: "4px", border: "none" }}>
                        <div className="header2">
                            <div className="title">{t('voucher.categories')}</div>
                            <div className="search">
                                <div className="search-container">
                                    <AiOutlineSearch className="search-icon" />
                                    <input type="text" placeholder={t('voucher.searchCategory')} className="search-input" name="categorySearch" value={categorySearch} onChange={handleSearchCategory} />
                                </div>
                            </div>
                        </div>
                        <div className="list-items">
                            {categories?.pageData?.map((category) => (
                                <div key={category.bulkCtgrId} className={`button item ${categorySelected?.bulkCtgrId === category?.bulkCtgrId ? 'selected' : ''}`}
                                    onClick={() => handleClickCategory(category)}>
                                    <div className="logo-wrapper">
                                        <img src={getImageSrc(category?.category?.imagePath)} alt="" className="logo" />
                                    </div>
                                    <div className="name">{category?.category?.name}</div>
                                </div>
                            ))}
                        </div>
                        <div className="pagination-container">
                            <Pagination
                                shape="rounded"
                                count={categories?.totalCount} // bên con API này nó làm theo kiểu Page num bắt đầu từ 0 @Tai
                                page={currentPageCategory}
                                onChange={handleChangePageCategory}
                                // color="primary"
                                sx={{
                                    '& .MuiPaginationItem-root': {
                                        backgroundColor: 'transparent',
                                        color: 'gray',
                                    },
                                    '& .MuiPaginationItem-root.MuiPaginationItem-page.Mui-selected': {
                                        backgroundColor: 'white',
                                        color: '#F4D160',
                                        fontWeight: 'bold',
                                        fontSize: '0.9rem',
                                    },
                                }}
                            />
                        </div>
                    </div>
                )}
            </div>

            {/* Panel Brands */}
            {!brandSelected && (
                <div className="product_choice" style={{ marginTop: "20px", padding: "4px" }}>
                    <div className="header2">
                        <div className="title">{t('voucher.brands')}</div>
                        <div className="search">
                            <div className="search-container">
                                <AiOutlineSearch className="search-icon" />
                                <input type="text" placeholder={t('voucher.searchBrand')} className="search-input" name="brandSearch" value={brandSearch} onChange={handleSearchBrand} />
                            </div>
                        </div>
                    </div>
                    <div className="list-items">
                        {brands?.pageData?.map((brand) => (
                            <div key={brand?.bulkBrandId}
                                className={`button item ${brandSelected?.bulkBrandId === brand?.bulkBrandId ? 'selected' : ''}`}
                                onClick={() => handleClickBrand(brand)}>
                                <div className="logo-wrapper">
                                    <img src={getImageSrc(brand?.brand?.imgUrl)} alt="" className="logo" />
                                </div>
                                <div className="name">{brand?.brand?.name}</div>
                            </div>
                        ))}
                    </div>
                    <div className="pagination-container">
                        <Pagination
                            shape="rounded"
                            count={brands?.totalCount}
                            page={currentPageBrand}
                            onChange={handleChangePageBrand}
                            // color="primary"
                            sx={{
                                '& .MuiPaginationItem-root': {
                                    backgroundColor: 'transparent',
                                    color: 'gray',
                                },
                                '& .MuiPaginationItem-root.MuiPaginationItem-page.Mui-selected': {
                                    backgroundColor: 'white',
                                    color: '#F4D160',
                                    fontWeight: 'bold',
                                    fontSize: '0.9rem',
                                },
                            }}
                        />
                    </div>
                </div>
            )}

            {/* Panel Products */}
            {brandSelected?.bulkBrandId && (
                <div className="product_choice" style={{ marginTop: "20px" }}>
                    <div className="header2">
                        <div className="title">{t('voucher.productsChoices')}</div>
                        <div className="search">
                            <div className="search-container">
                                <AiOutlineSearch className="search-icon" />
                                <input type="text" placeholder={t('voucher.searchProducts')} className="search-input" value={productSearch} onChange={handleSearchProduct} />
                            </div>
                        </div>
                    </div>

                    <div className="product_choice2">
                        <div className="container-product_choice">
                            {products?.pageData?.map((product, index) => {
                                return (
                                    <div className="card" key={product?.bulkGoodsId}>
                                        <div className="box" onClick={() => togglePopup(product)}>
                                            <button
                                                onClick={() => togglePopup(product)}
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
                                                    src={product?.goods?.imagePath?.startsWith('http') ? product?.goods?.imagePath : process.env.REACT_APP_IMAGE_URL + product?.goods?.imagePath || IMAGE_DEFAULT_CHOICE}
                                                    onError={({ currentTarget }) => {
                                                        currentTarget.onerror = null; // prevents looping
                                                        currentTarget.src = IMAGE_DEFAULT_CHOICE;
                                                    }}
                                                    alt=""
                                                />
                                            </div>
                                        </div>
                                        <div className="content" onClick={() => togglePopup(product)}>
                                            <div className="frame-1000003390">
                                                <div className="the-coffee">{product?.goods?.name}</div>
                                            </div>
                                            <div className="item_product_value">
                                                <span className="item_value_vnd">{ConvertNumber(product?.goods?.sellPrice)}</span>
                                                <span className="item_value_vnd2"> VND</span>
                                            </div>
                                            <div
                                                className="lowery-s-gourmet-whole-bean-and-wired-willey-s-white-coffee">
                                                <p style={{ whiteSpace: "pre-line", overflow: "hidden", textOverflow: "ellipsis" }} dangerouslySetInnerHTML={{ __html: product?.goods?.description }}></p>
                                            </div>
                                        </div>
                                        <div className="price-quantity">
                                            <div className="stocks_amount">
                                                {(!isUnlimitedProduct(product?.goods)) ?
                                                    <span
                                                        style={{ color: product?.goods?.remainingCount === 0 ? 'red' : '#039855' }}>{t('voucher.leftInStocks')} {product?.goods?.remainingCount}</span> :
                                                    <span>{t('voucher.unlimited')}</span>}
                                            </div>
                                            <div className="amount">
                                                <AiOutlineMinusSquare
                                                    style={{ color: product?.numberSelect === 0 ? '#ccc' : 'black' }}
                                                    onClick={() => {
                                                        onClickMinus(product?.bulkGoodsId)
                                                    }} className="plus-square" />
                                                <div className="_0">{product?.numberSelect}</div>
                                                <AiOutlinePlusSquare
                                                    style={{ color: checkPlusIcon(product) ? '#ccc' : 'black' }}
                                                    onClick={() => {
                                                        onClickPlus(product?.bulkGoodsId)
                                                    }} className="minus-square" />
                                            </div>
                                        </div>
                                        <BootstrapButton
                                            onClick={() => handlePurchase(product)}
                                            sx={{ backgroundColor: '#4c3d3d' }}
                                            variant='contained'
                                            disabled={!product?.numberSelect || product?.numberSelect === 0}>{t('voucher.select')}</BootstrapButton>
                                    </div>
                                )
                            })}
                        </div>
                        <div className="pagination-container">
                            <Pagination
                                shape="rounded"
                                count={products?.totalCount}
                                page={currentPageProduct}
                                onChange={handleChangePageProduct}
                                color="primary"
                            />
                        </div>
                    </div>
                </div>
            )}
        </ProductBulkStyle>
    );
};

export default ProductBulk;


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

function getImageSrc(imagePath) {
    if (!imagePath) {
        return LOGO_MC_DONALD;
    }
    return (imagePath.startsWith('http://') || imagePath.startsWith('https://'))
        ? imagePath
        : process.env.REACT_APP_IMAGE_URL + imagePath;
}
