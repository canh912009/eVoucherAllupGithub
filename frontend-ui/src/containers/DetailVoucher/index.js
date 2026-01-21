import { Button } from "@mui/material";
import { styled } from "@mui/material/styles";
import axios from "axios";
import _ from "lodash";
import moment from "moment";
import React, { useCallback, useEffect, useState } from "react";
import Countdown, { zeroPad } from "react-countdown";
import { useTranslation } from "react-i18next";
import { AiOutlineCheck } from "react-icons/ai";
import { MdWrongLocation } from "react-icons/md";
import { SlArrowDown, SlArrowUp } from 'react-icons/sl';
import { useNavigate } from "react-router-dom";
import "slick-carousel/slick/slick-theme.css";
import "slick-carousel/slick/slick.css";
import Cookies from "universal-cookie";
import GlobalBackdrop from "../../components/GlobalBackdrop";
import PopupError from "../../components/PopupCustom/error";
import DashedLine from "../../components/ui/DashedLine";
import IMAGE_ENGLISH from "../../images/english.png";
import IMAGE_SEND from "../../images/image_send.png";
import LOGO from "../../images/Logo.png";
import IMAGE_VIETNAM from "../../images/vietnam.png";
import { getCookieMobileEncode, setCookieMobileEncode } from "../../utils/cookie";
import { setSessionItem } from "../../utils/sessionStorage";
import { getImage, getImageSrc, getStatus, isExpiredVoucher, isVersionV2, renderMessage } from "../../utils/utils";
import VoucherStatus, { TransferStatus } from "../../utils/VoucherStatus";
import VoucherType from "../../utils/VoucherType";
import SystemType from "./../../utils/SystemType";
import PanelActiveProduct from "./PanelActiveProduct";
import PanelOtpRequired from "./PanelOtpRequired";
import PopupConfirm from "./PopupConfirm";
import PopupMessage from "./PopupMessage";
import PopupProductGuide from "./PopupProductGuide";
import PopupUpdateInfo from "./PopupUpdateInfo";
import ProductBulk from "./ProductBulk";
import ProductChoice from "./ProductChoice";
import ProductInternalLC from "./ProductInternalLC";
import { Container } from "./styles";
import ProductVNPTEpay from "./VNPTEpay/ProductVNPTEpay";
import { TOPUP_OPTIONS } from "./VNPTEpay/VNPTEpayUtils";
import VoucherVNPTEpayTopup from "./VNPTEpay/VoucherVNPTEpayTopup";
import VoucherVNPTEpayTopupSuccess from "./VNPTEpay/VoucherVNPTEpayTopupSuccess";
import VoucherScanCode from "./VoucherScanCode";
import PIN_DISPLAY_TYPE from "./PinDisplayType";

const BlankButton = styled(Button)`
  width: 146px;
  @media (min-width: 400px) {
    width: 172px;
  }
  @media (min-width: 500px) {
    width: 200px;
    margin-left: 16px;
  }
`;
const BootstrapButton = styled(Button)({
  boxShadow: "none",
  textTransform: "none",
  border: "1px solid",
  lineHeight: 1.5,
  backgroundColor: "#4c3d3d",
  borderColor: "#4c3d3d",
  fontFamily: [
    "-apple-system",
    "BlinkMacSystemFont",
    '"Segoe UI"',
    "Roboto",
    '"Helvetica Neue"',
    "Arial",
    "sans-serif",
    '"Apple Color Emoji"',
    '"Segoe UI Emoji"',
    '"Segoe UI Symbol"',
  ].join(","),
  "&:hover": {
    backgroundColor: "#4c3d3d",
    borderColor: "#4c3d3d",
    boxShadow: "none",
  },
  "&:active": {
    boxShadow: "none",
    backgroundColor: "#4c3d3d",
    borderColor: "#4c3d3d",
  },
  "&:focus": {
    boxShadow: "0 0 0 0.2rem rgba(0,123,255,.5)",
  },
});

function allowShowChoiceProducts(extPinDisplayType, dataVoucher) {
  // console.log("Current data voucher", dataVoucher);
  return extPinDisplayType === SystemType.CHOICE
    && !isExpiredVoucher(dataVoucher)
    && (dataVoucher?.voucherStatus === VoucherStatus.NORMAL || dataVoucher?.voucherStatus === VoucherStatus.PART_USED)
    && dataVoucher?.goods?.choices && dataVoucher?.goods?.choices.map.length > 0;
}

function allowShowBulkProducts(extPinDisplayType, dataVoucher) {
  // console.log("dataVoucher", dataVoucher);
  return extPinDisplayType === SystemType.BULK
    && !isExpiredVoucher(dataVoucher)
    && (dataVoucher?.voucherStatus === VoucherStatus.NORMAL || dataVoucher?.voucherStatus === VoucherStatus.PART_USED)
  // && dataVoucher?.goods?.bulkCategories && dataVoucher?.goods?.bulkCategories?.length > 0;
}

function allowShowVNPTEpay(extPinDisplayType, dataVoucher) {
  // console.log(`allowShowVNPTEpay dataVoucher: ${JSON.stringify(dataVoucher)}`);
  return extPinDisplayType === SystemType.VNPT_EPAY
    && !isExpiredVoucher(dataVoucher)
    && (dataVoucher?.voucherStatus === VoucherStatus.NORMAL || dataVoucher?.voucherStatus === VoucherStatus.PART_USED);
}

function allowShowInternalLC(extPinDisplayType, dataVoucher) {
  // console.log(`allowShowInternalLC dataVoucher: ${JSON.stringify(dataVoucher)}`);
  return extPinDisplayType === SystemType.INTERNAL
    // && !isExpiredVoucher(dataVoucher)
    && dataVoucher?.voucherType === VoucherType.LC
  // && (dataVoucher?.voucherStatus === VoucherStatus.NORMAL || dataVoucher?.voucherStatus === VoucherStatus.PART_USED);
}

const DetailVoucher = () => {
  document.title = "Voucher";
  const cookies = new Cookies();

  const { t, i18n } = useTranslation();
  if (!cookies.get("locales")) {
    cookies.set("locales", "vi");
  }
  const history = useNavigate();
  const asPath = window.location.search;
  const urlParams = new URLSearchParams(asPath);
  const voucherID = urlParams.get("_Ss");

  const [dataPreCheck, setDataPreCheck] = useState(null);
  const [isFirstLoad, setIsFirstLoad] = useState(true);

  const [isShowMap, setIsShowMap] = useState(false);
  const [displayingStoreList, setDisplayingStoreList] = useState(false);
  const [otp, setOtp] = useState(null);
  const [otpV2, setOtpV2] = useState(null);
  const [dataVoucher, setDataVoucher] = useState(null);
  const [dataStore, setDataStore] = useState([]);
  const [storeFocusID, setStoreFocusID] = useState(null);
  const [loading, setLoading] = useState(false);
  const [expireTime, setExpireTime] = useState(null);
  const [errorData, setErrorData] = useState(null);
  const [valueName, setValueName] = useState(null);
  const [isUpdateInfo, setUpdateInfo] = useState(false);
  const [isConfirm, setIsConfirm] = useState(false);
  const [extPinNo, setExtPinNo] = useState(null);
  const [serialNo, setSerialNo] = useState(null);
  const [displayType, setDisplayType] = useState(null);
  const [extPinDisplayType, setExtPinDisplayType] = useState(null);
  const [messageType, setMessageType] = useState(null);
  const [isOpenPopupMessage, setIsOpenPopupMessage] = useState(false);
  const [isOpenPopupProductGuid, setIsOpenPopupProductGuid] = useState(false);
  const [isAscending, setIsAscending] = useState(true);

  const settings = {
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
  };

  const toggleSortOrder = () => {
    setIsAscending(!isAscending);
  };

  function fixExpireDate(dateString) {
    // If dateString is null or undefined, return null
    if (!dateString) {
      return null;
    }

    // If the input date is before the current date, return null
    const currentDate = moment();
    const inputDate = moment(dateString, 'YYYY-MM-DD');

    if (inputDate.isBefore(currentDate, 'day')) {
      return null;
    }

    // Otherwise, add 1 day to the input date and return the new date
    return inputDate.add(1, 'days').format('YYYY-MM-DD');
  }

  const onPreCheck = (otpV2) => {
    console.log(`onPreCheck otpV2: ${otpV2}`);

    if (otpV2) {
      setOtpV2(otpV2);
    }

    const fetchData = async () => {
      setLoading(true);
      try {
        const res = await axios.post(
          `${process.env.REACT_APP_VOUCHER_API_VIEWER}/preCheck`, {
          voucherId: voucherID
        }
        );
        const { data } = res.data;
        if (data) {
          setLoading(false);
          setDataPreCheck({ ...data, otpExpireDt: fixExpireDate(data.otpExpireDt) });
        } else {
          setLoading(false);
          setDataPreCheck(null);
          setErrorData({
            title: t("voucher.errorVoucherNull"),
            message: t("voucher.errorVoucherNull"),
            onAction: () => {
              setErrorData(null);
              setDataStore([]);
            },
          });
        }
      } catch (error) {
        setLoading(false);
        console.error('Error during preCheck:', error);
        setErrorData({
          title: t("voucher.errorVoucherNull"),
          message: t("voucher.errorVoucherNull"),
          onAction: () => {
            setErrorData(null);
            setDataStore([]);
          },
        });
      }
    }

    fetchData();
  }

  const onResetData = useCallback(() => {
    console.log(`onResetData: ${voucherID}`);

    const fetchDataV1 = async () => {
      setLoading(true);
      try {
        const res = await axios.get(
          `${process.env.REACT_APP_VOUCHER_API_VIEWER}/viewer/${voucherID}`
        );
        const { data } = res.data;
        console.log("Data V1", data)

        if (data) {
          setLoading(false);
          const { otp, voucher, expireTime, extPinNo, serialNo, displayType, system } = data;
          setExtPinNo(extPinNo);
          setSerialNo(serialNo);
          setDisplayType(displayType);
          setExtPinDisplayType(system);
          setOtp(otp || null);
          setExpireTime(expireTime || null);
          setMessageType(voucher?.smsType || null);

          if (voucher) {
            setValueName(voucher?.userName);
            setDataVoucher(voucher);
            if (
              voucher?.voucherStatus === VoucherStatus.NORMAL ||
              voucher?.voucherStatus === VoucherStatus.PART_USED
            ) {
              if (!localStorage.getItem(`${voucherID}`) && data.voucher?.showPopupYn === "Y") {
                setIsOpenPopupMessage(true);
                localStorage.setItem(`${voucherID}`, true);
              }
            }
            if (voucher.goods?.id) {
              try {
                const storeRes = await axios.get(
                  `${process.env.REACT_APP_VOUCHER_API_VIEWER}/store/search?goodsId=${voucher.goods.id}`
                );
                console.log("Store response: ", storeRes);
                let listStore = storeRes?.data?.data || []
                voucher?.goods?.exceptStoreIds?.map((item) => {
                  _.remove(listStore, function (n) {
                    return n.storeId === item;
                  });
                })
                console.log("Current store data", listStore);
                setDataStore(listStore);
              } catch (error) {
                console.log("error", error)
                setErrorData({
                  title: error?.response?.data?.code,
                  message: renderMessage(error?.response?.data?.code, t),
                  onAction: () => {
                    setErrorData(null)
                    setDataStore([])
                  }
                })
              }
            }
          } else {
            setDataVoucher(null);
            setErrorData({
              title: t("voucher.errorVoucherNull"),
              message: t("voucher.errorVoucherNull"),
              onAction: () => {
                setErrorData(null);
                setDataStore([]);
              },
            });
          }
        } else {
          setDataVoucher(null);
          setErrorData({
            title: t("voucher.errorVoucherNull"),
            message: t("voucher.errorVoucherNull"),
            onAction: () => {
              setErrorData(null);
              setDataStore([]);
            },
          });
          setLoading(false);
        }
      } catch (error) {
        console.log("erorr", error);
        setLoading(false);
        if (error.response && error.response.status === 404) {
          setErrorData({
            title: error?.response?.data?.code,
            message: t("voucher.errorVoucherNull"),
            onAction: () => {
              setErrorData(null);
              setDataStore([]);
            },
          });
        } else if (
          error.response &&
          error.response.status === 400 &&
          error.response.data.code === 4006
        ) {
          setErrorData({
            title: error?.response?.data?.code,
            message: error.response.data.message,
            onAction: () => {
              setErrorData(null);
              setDataStore([]);
            },
          });
        } else {
          setErrorData({
            title: error?.response?.data?.code,
            message: renderMessage(error?.response?.data?.code, t),
            onAction: () => {
              setErrorData(null);
              setDataStore([]);
            },
          });
        }
      }
    };

    const fetchDataV2 = async () => {
      setLoading(true);
      try {
        const res = await axios.post(
          `${process.env.REACT_APP_VOUCHER_API_VIEWER}/v2/vouchers`,
          {
            voucherId: voucherID,
            otp: otpV2,
          }
        );
        const { data } = res.data;
        console.log("Data V2", data)

        if (data) {
          setLoading(false);
          const { otp, voucher, expireTime, extPinNo, serialNo, displayType, system } = data;

          setExtPinNo(extPinNo);
          setSerialNo(serialNo);
          setDisplayType(displayType);
          setExtPinDisplayType(system);
          setOtp(otp || null);
          setExpireTime(expireTime || null);
          setMessageType(voucher?.smsType || null);

          if (voucher) {
            setValueName(voucher?.userName);
            setDataVoucher(voucher);
            if (
              voucher?.voucherStatus === VoucherStatus.NORMAL ||
              voucher?.voucherStatus === VoucherStatus.PART_USED
            ) {
              if (!localStorage.getItem(`${voucherID}`) && data.voucher?.showPopupYn === "Y") {
                setIsOpenPopupMessage(true);
                localStorage.setItem(`${voucherID}`, true);
              }
            }
            if (voucher.goods?.id) {
              try {
                const storeRes = await axios.get(
                  `${process.env.REACT_APP_VOUCHER_API_VIEWER}/store/search?goodsId=${voucher.goods.id}`
                );
                console.log("Store response: ", storeRes);
                let listStore = storeRes?.data?.data || []
                voucher?.goods?.exceptStoreIds?.map((item) => {
                  _.remove(listStore, function (n) {
                    return n.storeId === item;
                  });
                })
                console.log("Current store data", listStore);
                setDataStore(listStore);
              } catch (error) {
                console.log("error", error)
                setErrorData({
                  title: error?.response?.data?.code,
                  message: renderMessage(error?.response?.data?.code, t),
                  onAction: () => {
                    setErrorData(null)
                    setDataStore([])
                  }
                })
              }
            }
          } else {
            setOtpV2(null);
            setDataVoucher(null);
            setErrorData({
              title: t("voucher.errorVoucherNull"),
              message: t("voucher.errorVoucherNull"),
              onAction: () => {
                setErrorData(null);
                setDataStore([]);
              },
            });
          }
        } else {
          setOtpV2(null);
          setDataVoucher(null);
          setErrorData({
            title: t("voucher.errorVoucherNull"),
            message: t("voucher.errorVoucherNull"),
            onAction: () => {
              setErrorData(null);
              setDataStore([]);
            },
          });
          setLoading(false);
        }
      } catch (error) {
        console.log("erorr", error);
        setLoading(false);
        setOtpV2(null);
        if (error.response && error.response.status === 404) {
          setErrorData({
            title: error?.response?.data?.code,
            message: t("voucher.errorVoucherNull"),
            onAction: () => {
              setErrorData(null);
              setDataStore([]);
            },
          });
        } else if (
          error.response &&
          error.response.status === 400 &&
          error.response.data.code === 4006
        ) {
          setErrorData({
            title: error?.response?.data?.code,
            message: error.response.data.message,
            onAction: () => {
              setErrorData(null);
              setDataStore([]);
            },
          });
        } else {
          setErrorData({
            title: error?.response?.data?.code,
            message: renderMessage(error?.response?.data?.code, t),
            onAction: () => {
              setErrorData(null);
              setDataStore([]);
            },
          });
        }
      }
    };

    if (isVersionV2(dataPreCheck)) {
      // voucher is v2, activated and not otp required or exist otp
      if (dataPreCheck?.activated && (!dataPreCheck?.otpRequired || otpV2)) {
        fetchDataV2();
      }
    } else {
      fetchDataV1();
    }
  }, [voucherID, dataPreCheck, otpV2]);

  useEffect(() => {
    async function fetchData() {
      if (isVersionV2(dataPreCheck) && dataPreCheck?.activated && dataPreCheck?.phoneNumber && dataPreCheck?.otpExpireDt) {
        // OTP expired date is must be after current date
        if (moment(dataPreCheck?.otpExpireDt).isAfter(moment())) {
          const cookieData = await getCookieMobileEncode(dataPreCheck.phoneNumber);
          console.log(`cookieData: ${JSON.stringify(cookieData)}`);
          if (cookieData?.otp) {
            setOtpV2(cookieData?.otp);
          }
        }
      }
    }

    console.log(`isFirstLoad: ${isFirstLoad}`);

    // Only run on the first page load
    if (dataPreCheck && isFirstLoad) {
      setIsFirstLoad(false);
      fetchData();
    }
  }, [dataPreCheck, isFirstLoad]);

  useEffect(() => {
    async function fetchData() {
      if (dataPreCheck?.phoneNumber && otpV2 && dataVoucher?.id) {
        const cookieData = {
          otp: otpV2,
          otpExpireDt: dataPreCheck?.otpExpireDt,
        }
        setCookieMobileEncode(dataPreCheck.phoneNumber, cookieData);
      }
    }

    fetchData();
  }, [dataPreCheck, dataVoucher, otpV2]);

  const renderTimeCancelUse = () => {
    const initialDateTime = new Date(dataVoucher?.lastExchangeDate);
    const sevenDaysLater = new Date(initialDateTime);
    sevenDaysLater.setDate(sevenDaysLater.getDate() + 7);
    return sevenDaysLater;
  };

  useEffect(() => {
    if (isUpdateInfo === false && dataVoucher) {
      setValueName(dataVoucher.userName);
    }
  }, [isUpdateInfo]);

  useEffect(() => {
    if (voucherID) {
      onPreCheck();
    }
  }, [voucherID]);

  useEffect(() => {
    console.log("dataPreCheck: ", dataPreCheck);
    if (dataPreCheck) {
      onResetData();
    }
  }, [dataPreCheck, onResetData]);

  const onTransfer = (fromData) => {
    setIsConfirm(false);
    axios
      .post(
        `${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/vouchers/receipt`,
        fromData
      )
      .then((res) => {
        history("/response-confirm-transfer");
      })
      .catch((e) => {
        setErrorData({
          title: "Error",
          message: `${e.response.data.message}`,
          onAction: () => {
            setErrorData(null);
          },
        });
      });
  };

  const goToVoucherList = () => {
    setSessionItem('data_pre', { ...dataPreCheck, otp: otpV2 });
    history("/voucher-list");
  };

  function openGoogleMap(mapCode) {
    try {
      window.open(
        "https://maps.google.com?q=" + encodeURIComponent(mapCode)
      );
      // setDataMap(encodeURIComponent(item.mapCode))
    } catch (e) {
      console.log(e);
    }
  }

  function allowShowScanCode() {
    return !displayingStoreList
      && !(dataVoucher?.voucherStatus === VoucherStatus.USED)
      && !isExpiredVoucher(dataVoucher)
      && !isShowMap
      && dataVoucher.system !== SystemType.CHOICE
      && dataVoucher.system !== SystemType.BULK
      && dataVoucher.system !== SystemType.VNPT_EPAY;
  }

  function allowShowVNPTEpayTopup() {
    return !displayingStoreList
      && dataVoucher?.voucherStatus === VoucherStatus.USED
      && !isExpiredVoucher(dataVoucher)
      && !isShowMap
      && dataVoucher.system === SystemType.VNPT_EPAY;
  }


  function choiceVoucherHeader() {
    return <div style={{ display: "flex", justifyContent: "center" }}>
      <img
        style={{
          width: "90%",
          opacity: 0, // Initially hide the image
          transition: 'opacity 0.5s ease', // CSS transition for opacity
        }}
        src={
          dataVoucher?.goods?.imagePath?.startsWith("http")
            ? dataVoucher?.goods?.imagePath
            : process.env.REACT_APP_IMAGE_URL +
            dataVoucher?.goods?.imagePath ||
            getImage(dataVoucher)
        }
        onLoad={(event) => {
          event.currentTarget.style.opacity = 1; // Fade in the image on load
        }}
        onError={({ currentTarget }) => {
          currentTarget.onerror = null; // prevents looping
          currentTarget.src = getImage(dataVoucher);
        }}
        alt=""
      />
    </div>;
  }

  function normalVoucherHeaderWithBrand() {
    return <div className="title_voucher">
      <div className="image_product">
        <img
          src={
            dataVoucher?.goods?.imagePath?.startsWith("http")
              ? dataVoucher?.goods?.imagePath
              : process.env.REACT_APP_IMAGE_URL +
              dataVoucher?.goods?.imagePath ||
              getImage(dataVoucher)
          }
          onError={({ currentTarget }) => {
            currentTarget.onerror = null; // prevents looping
            currentTarget.src = getImage(dataVoucher);
          }}
          alt=""
        />
      </div>

      <div className="title">
        <img
          src={
            dataVoucher?.goods?.brand?.imgUrl?.startsWith("http")
              ? dataVoucher?.goods?.brand?.imgUrl
              : process.env.REACT_APP_IMAGE_URL +
              dataVoucher?.goods?.brand?.imgUrl ||
              getImage(dataVoucher)
          }
          onError={({ currentTarget }) => {
            currentTarget.onerror = null; // prevents looping
            currentTarget.src = getImage(dataVoucher);
          }}
          alt=""
        />
        <div>{dataVoucher?.goods?.brand?.name || "NULL"}</div>
      </div>
    </div>;
  }

  function allowTransfer(dataVoucher) {
    if (isExpiredVoucher(dataVoucher)) {
      console.log(`allowTransfer is false, because voucher is expired`);
      return false;
    }
    let allowed = false;
    allowed = dataVoucher?.voucherStatus === VoucherStatus.NORMAL || dataVoucher.voucherStatus === VoucherStatus.PART_USED;
    if (allowed && dataVoucher?.voucherType === "PP") {
      allowed = dataVoucher.voucherPrice / 2 - dataVoucher.balance < 0;
    }
    return allowed;
  }

  const isShowTicker = (dataVoucher) => {
    // Voucher not VNPT_EPAY
    if (dataVoucher?.system !== SystemType.VNPT_EPAY)
      return true;

    // Voucher VNPT_EPAY
    if (dataVoucher?.system === SystemType.VNPT_EPAY && (dataVoucher?.voucherStatus !== VoucherStatus.USED
      || dataVoucher?.voucherStatus === VoucherStatus.USED && dataVoucher?.transferStatus !== null))
      return true;

    return false;
  }

  const isShowMask = (dataVoucher) => {
    // Voucher not VNPT_EPAY
    if (dataVoucher?.system !== SystemType.VNPT_EPAY) {
      if ((dataVoucher?.voucherStatus !== VoucherStatus.NORMAL &&
        dataVoucher?.voucherStatus !== VoucherStatus.PART_USED) ||
        isExpiredVoucher(dataVoucher)) {
        return true;
      }
    }

    // Voucher VNPT_EPAY
    if (dataVoucher?.system === SystemType.VNPT_EPAY && (dataVoucher?.voucherStatus !== VoucherStatus.USED
      || dataVoucher?.voucherStatus === VoucherStatus.USED && dataVoucher?.transferStatus !== null)) {
      if (dataVoucher?.voucherStatus === VoucherStatus.EXPIRE || dataVoucher?.voucherStatus === VoucherStatus.DISABLED
        || isExpiredVoucher(dataVoucher)) {
        return true;
      }
    }

    return false;
  }

  function renderVoucherInfoAndButtons(dataPreCheck, dataVoucher) {
    if (!dataPreCheck || dataVoucher?.transferStatus === TransferStatus.RECPT_WAIT) {
      return <></>
    }

    // If first render (preCheck)
    if (dataPreCheck && !dataVoucher) {
      return <div className="row">
        <div
          className="detail_voucher"
        >
          <div className="logo">
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

          <div className="line_solid">
            <div className="left_line" />
            <div className="right_line" />
          </div>

          <div style={isAscending ? { paddingBottom: "20px" } : { paddingBottom: "10px" }}>
            <DashedLine height="2px" />
          </div>

          <div style={{ display: "flex", justifyContent: "center" }}>
            <img
              style={{
                width: "90%",
                opacity: 0,
                transition: 'opacity 0.5s ease',
              }}
              src={getImageSrc(dataPreCheck.voucherImageUrl) || getImage(null)}
              onLoad={(event) => {
                event.currentTarget.style.opacity = 1;
              }}
              onError={({ currentTarget }) => {
                currentTarget.onerror = null; // prevents looping
                currentTarget.src = getImage(dataVoucher);
              }}
              alt=""
            />
          </div>
          <div className="product_name">
            {dataPreCheck?.voucherName || "NULL"}
          </div>
        </div>

        {isVersionV2(dataPreCheck) && (
          <>
            {!dataPreCheck?.activated && (
              <PanelActiveProduct voucherId={voucherID} onReload={onPreCheck} />
            )}

            {dataPreCheck?.activated && dataPreCheck?.otpRequired && (
              <>
                {!otpV2 && (
                  <PanelOtpRequired dataPre={dataPreCheck} onReload={onPreCheck} />
                )}
              </>
            )}
          </>
        )}

      </div>
    }

    // If second render (voucher)
    return <div className="row">
      {dataVoucher && (
        <div
          className="detail_voucher"
          style={{
            backgroundColor:
              dataVoucher?.system !== SystemType.VNPT_EPAY &&
              dataVoucher?.voucherStatus !== VoucherStatus.NORMAL &&
              dataVoucher?.voucherStatus !== VoucherStatus.PART_USED &&
              "#D9D9D9",
          }}
        >

          {isShowTicker(dataVoucher) && (
            <>
              {isShowMask(dataVoucher) && <div className="mask"></div>}
              <img src={getStatus(dataVoucher)} alt="" className="tiker" />
            </>
          )}

          <div className="logo">
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

          {/* Info Product not Bulk */}
          {extPinDisplayType !== SystemType.BULK && (
            <>
              <div className="line_solid">
                <div className="left_line" />
                <div className="right_line" />
              </div>
              {choiceVoucherHeader()}
              <div className="product_name">
                {dataVoucher?.goods?.name || "NULL"}
              </div>
              <div className="line_dashed">
                <div className="time_voucher">
                  {t("voucher.expiryDate")}:{" "}
                  {moment(dataVoucher?.expireDate || null).format("DD/MM/YYYY")}
                </div>
                <div className="left_line" />
                <div className="right_line" />
              </div>
              <div className="show_instruction">
                <div style={{ display: "flex" }}>
                  <BootstrapButton
                    sx={{ backgroundColor: "#4c3d3d" }}
                    variant="contained"
                    onClick={() => {
                      setIsOpenPopupMessage(true);
                    }}
                    className="button_show_instruction"
                  >
                    {t("voucher.gift_message")}
                  </BootstrapButton>
                  <BootstrapButton
                    sx={{ backgroundColor: "#4c3d3d" }}
                    variant="contained"
                    onClick={() => {
                      setIsOpenPopupProductGuid(true);
                    }}
                    className="button_show_instruction"
                  >
                    {t("voucher.product_guide")}
                  </BootstrapButton>
                </div>
                <div style={{ display: "flex" }}>
                  <BootstrapButton
                    sx={{ backgroundColor: "#4c3d3d" }}
                    variant="contained"
                    onClick={() => {
                      history("/list-instruction");
                    }}
                    className="button_show_instruction"
                  >
                    {t("voucher.showInstruction")}
                  </BootstrapButton>
                  <BootstrapButton
                    sx={{ backgroundColor: "#4c3d3d" }}
                    variant="contained"
                    onClick={goToVoucherList}
                    className={"button_show_instruction"}
                  >
                    {t("voucher.showVoucherList")}
                  </BootstrapButton>
                </div>
                {extPinDisplayType !== SystemType.CHOICE && (
                  <div style={{ display: "flex" }}>
                    {extPinDisplayType !== SystemType.VNPT_EPAY ? (
                      <BootstrapButton
                        variant="contained"
                        onClick={() => {
                          setDisplayingStoreList(true);
                        }}
                        className="button_show_instruction"
                      >
                        <div style={{ position: "relative" }}>
                          {t("voucher.showListStore")}
                        </div>
                      </BootstrapButton>
                    ) : <BlankButton></BlankButton>}

                    {allowTransfer(dataVoucher) ? (
                      <BootstrapButton
                        sx={{ backgroundColor: "#4c3d3d" }}
                        variant="contained"
                        onClick={() => {
                          history(`/share-voucher/${otp}`);
                        }}
                        className="button_show_instruction"
                      >
                        {t("voucher.transferringOthers")}
                      </BootstrapButton>
                    ) : (
                      <BlankButton></BlankButton>
                    )}
                  </div>
                )}
              </div>
            </>
          )}

          {/* Info Product Bulk */}
          {extPinDisplayType === SystemType.BULK && (
            <>
              <div className="line_solid">
                <div className="left_line" style={isAscending ? { top: "140px" } : {}} />
                <div className="right_line" style={isAscending ? { top: "140px" } : {}} />
              </div>

              <div style={isAscending ? { paddingBottom: "20px" } : { paddingBottom: "10px" }}>
                <DashedLine height="2px" />
              </div>

              {isAscending && <div style={{ paddingBottom: "20px" }}>{choiceVoucherHeader()}</div>}

              <div className="product_name" style={{ marginBottom: "0px", marginTop: "0px" }}>
                <div className="name">{dataVoucher?.goods?.name || "NULL"}</div>
                <div className="icon" onClick={toggleSortOrder}>
                  {isAscending ? <SlArrowUp /> : <SlArrowDown />}
                </div>
              </div>

              {isAscending && (
                <div className="line_dashed" style={{ margin: "0px", borderTop: "none", borderBottom: "2px dashed rgb(224, 224, 224)" }}>
                  <div className="time_voucher" style={{ lineHeight: "36px" }}>
                    {t("voucher.expiryDate")}:{" "}
                    {moment(dataVoucher?.expireDate || null).format("DD/MM/YYYY")}
                  </div>
                </div>
              )}
              {isAscending && (
                <div className="show_instruction">
                  <div style={{ display: "flex" }}>
                    <BootstrapButton
                      sx={{ backgroundColor: "#4c3d3d" }}
                      variant="contained"
                      onClick={() => {
                        setIsOpenPopupMessage(true);
                      }}
                      className="button_show_instruction"
                    >
                      {t("voucher.gift_message")}
                    </BootstrapButton>
                    <BootstrapButton
                      sx={{ backgroundColor: "#4c3d3d" }}
                      variant="contained"
                      onClick={() => {
                        setIsOpenPopupProductGuid(true);
                      }}
                      className="button_show_instruction"
                    >
                      {t("voucher.product_guide")}
                    </BootstrapButton>
                  </div>
                  <div style={{ display: "flex" }}>
                    <BootstrapButton
                      sx={{ backgroundColor: "#4c3d3d" }}
                      variant="contained"
                      onClick={() => {
                        history("/list-instruction");
                      }}
                      className="button_show_instruction"
                    >
                      {t("voucher.showInstruction")}
                    </BootstrapButton>
                    <BootstrapButton
                      sx={{ backgroundColor: "#4c3d3d" }}
                      variant="contained"
                      onClick={goToVoucherList}
                      className={"button_show_instruction"}
                    >
                      {t("voucher.showVoucherList")}
                    </BootstrapButton>
                  </div>
                </div>
              )}
            </>
          )}

        </div>
      )}

      {allowShowChoiceProducts(extPinDisplayType, dataVoucher) && (
        <ProductChoice
          dataPre={dataPreCheck}
          dataVoucher={dataVoucher}
          otp={otpV2}
          onReload={onResetData}
        />
      )}

      {allowShowBulkProducts(extPinDisplayType, dataVoucher) && (
        <ProductBulk
          dataPre={dataPreCheck}
          dataVoucher={dataVoucher}
          otp={otpV2}
          onReload={onResetData}
        />
      )}

      {allowShowVNPTEpay(extPinDisplayType, dataVoucher) && (
        <ProductVNPTEpay
          dataPre={dataPreCheck}
          dataVoucher={dataVoucher}
          otp={otpV2}
          onReload={onResetData}
        />
      )}

      {allowShowInternalLC(extPinDisplayType, dataVoucher) && (
        <ProductInternalLC
          dataVoucher={dataVoucher}
          onReload={onResetData}
          otp={otp}
        />
      )}

      {!allowShowInternalLC(extPinDisplayType, dataVoucher) && isExpiredVoucher(dataVoucher) && dataVoucher?.expireDate && (
        <div
          style={{ textAlign: "center", marginTop: "20px", color: "red" }}
        >
          {t("voucher.expireDate")} {dataVoucher?.expireDate}
        </div>
      )}

      {/*count down*/}
      {!allowShowInternalLC(extPinDisplayType, dataVoucher) && 
        extPinDisplayType === SystemType.INTERNAL && displayType !== PIN_DISPLAY_TYPE.BARCODE &&
        !displayingStoreList &&
        !isShowMap &&
        !isExpiredVoucher(dataVoucher) &&
        (dataVoucher?.voucherStatus === VoucherStatus.NORMAL ||
          dataVoucher?.voucherStatus === VoucherStatus.PART_USED) && (
          <div style={{ textAlign: "center", marginTop: "20px" }}>
            {t("voucher.textCancelQrTime")}
            <span style={{ color: "red" }}>
              {" "}
              <Countdown
                onComplete={() => {
                  setOtp(null);
                }}
                renderer={({ minutes, seconds }) => (
                  <span>
                    {zeroPad(minutes)}:{zeroPad(seconds)}
                  </span>
                )}
                date={expireTime}
              />
            </span>
          </div>
        )}

      {!allowShowInternalLC(extPinDisplayType, dataVoucher) && allowShowScanCode() && (
        <VoucherScanCode
          t={t}
          status={dataVoucher?.voucherStatus}
          systemType={extPinDisplayType}
          // systemType={SystemType.UR_BOX}
          // displayType={PIN_DISPLAY_TYPE.QRBAR}
          displayType={displayType}

          // pinPassword={null}
          pinPassword={dataVoucher.externalPinPassword}
          pinNo={extPinNo}
          serialNo={serialNo}
          settings={settings}
          otp={otp}
          isPosLink={dataVoucher?.goods?.brand?.posLink}
          dataVoucher={dataVoucher}
          renderTimeCancelUse={renderTimeCancelUse}
        />
      )
      }

      {allowShowVNPTEpayTopup() && (
        <>
          {dataVoucher?.goods?.vnpt?.selectedAction === TOPUP_OPTIONS.CARDCODE && (
            <VoucherVNPTEpayTopup
              dataVoucher={dataVoucher}
              onReload={onResetData}
            />
          )}
          {dataVoucher?.goods?.vnpt?.selectedAction === TOPUP_OPTIONS.TOPUP && (
            <VoucherVNPTEpayTopupSuccess
              dataVoucher={dataVoucher}
              onReload={onResetData}
            />
          )}
        </>

      )}
      {displayingStoreList && (
        <>
          <div className="list_map">
            <div className="row_list_map">
              {dataStore.length > 0 ?
                dataStore.map((_store, index) => {
                  if (_store.validYN === "Y") {
                    return (
                      <div
                        style={{
                          color:
                            index === storeFocusID ? "orange" : "black",
                        }}
                        // onClick={() => {
                        //   if (_store.mapCode) {
                        //     setStoreFocusID(index);
                        //     openGoogleMap(_store.mapCode);
                        //   } else {
                        //     console.log("Map code not found for this store ", _store);
                        //   }
                        // }}
                        className="item_map"
                      >
                        <div className="focus_item_map">
                          {index === storeFocusID && <AiOutlineCheck />}
                        </div>
                        <div
                          style={{
                            marginRight: "12px",
                            width: "80%",
                          }}
                        >
                          {_store.fullAddress.trim()}
                          <hr />
                        </div>
                      </div>
                    );
                  } else {
                    {
                      console.log("Store validYN = N ", _store)
                    }
                  }
                })
                :
                <div style={{
                  display: "flex",
                  flexDirection: "column",
                  justifyContent: "center",
                  alignItems: "center",
                  marginTop: "20px"
                }}>
                  <MdWrongLocation style={{ fontSize: '64px', color: 'red' }} />
                  <p>
                    {t("voucher.noStore")}
                  </p>
                </div>
              }
            </div>
          </div>
          <div className="list_action">
            <BootstrapButton
              sx={{ backgroundColor: "#4c3d3d", marginTop: "20px" }}
              variant="contained"
              onClick={() => {
                setDisplayingStoreList(false);
              }}
              className="button_show_instruction"
            >
              {t("voucher.buttonBackQR")}
            </BootstrapButton>
          </div>
        </>
      )}
      {isShowMap && (
        <div className="list_action">
          <BootstrapButton
            sx={{ backgroundColor: "#4c3d3d" }}
            variant="contained"
            onClick={() => {
              setIsShowMap(false);
              setDisplayingStoreList(true);
            }}
            className="button_show_instruction"
          >
            {t("voucher.backToListStore")}
          </BootstrapButton>
        </div>
      )}
    </div>;
  }

  return (
    <Container>
      <GlobalBackdrop isLoading={loading} />

      {isOpenPopupProductGuid && (
        <PopupProductGuide
          data={dataVoucher}
          onClose={() => {
            setIsOpenPopupProductGuid(false);
          }}
        />
      )}

      {isOpenPopupMessage && (
        <PopupMessage
          data={dataVoucher}
          onClose={() => {
            localStorage.setItem(`${voucherID}`, true);
            setIsOpenPopupMessage(false);
          }}
        />
      )}

      {errorData && (
        <PopupError
          title={errorData.title}
          message={errorData.message}
          onAction={errorData.onAction}
        />
      )}

      {isUpdateInfo && (
        <PopupUpdateInfo
          t={t}
          otp={otp}
          valueName={valueName}
          setValueName={setValueName}
          onTransfer={(data) => {
            onTransfer(data);
          }}
          onClose={(data) => {
            setUpdateInfo(data);
          }}
        />
      )}

      {isConfirm && (
        <PopupConfirm
          onTransfer={(data) => {
            onTransfer(data);
          }}
          t={t}
          otp={otp}
          setIsConfirm={setIsConfirm}
          setUpdateInfo={setUpdateInfo}
          valueName={valueName}
        />
      )}

      {renderVoucherInfoAndButtons(dataPreCheck, dataVoucher)}

      {/*voucher is in RECPT_WAIT status ( cho xác nhận )*/}

      {dataVoucher && dataVoucher?.transferStatus === "RECPT_WAIT" && (
        <div className="row">
          {dataVoucher && (
            <div className="message">
              <div className="image_send">
                <img src={IMAGE_SEND} alt="" />
              </div>
              <h1>{t("voucher.messageDetail")}</h1>
              <h2 style={{ textAlign: "center" }}>
                {dataVoucher.transferMessage}
              </h2>
              <div className="from_share">
                <div className="text">
                  {t("voucher.from")}: {dataVoucher?.originalUserName}
                </div>
              </div>
            </div>
          )}
          {dataVoucher && (
            <div
              className="detail_voucher"
              style={{
                backgroundColor:
                  dataVoucher?.transferStatus !== "RECPT_WAIT" && "#D9D9D9",
              }}
            >
              {((dataVoucher?.voucherStatus !== VoucherStatus.NORMAL &&
                dataVoucher?.voucherStatus !== VoucherStatus.PART_USED) ||
                isExpiredVoucher(dataVoucher)) && (
                  <div className="mask"></div>
                )}
              {isExpiredVoucher(dataVoucher) &&
                dataVoucher?.transferStatus === "RECPT_WAIT" ? null : (
                <img src={getStatus(dataVoucher)} alt="" className="tiker" />
              )}

              <div className="logo">
                <img src={LOGO} alt="" />
                <div className={"row_language"}>
                  <div
                    className={"item_language"}
                    onClick={() => {
                      i18n.changeLanguage("vi");
                      cookies.set("locales", "vi");
                    }}
                    style={{ margin: "0 8px" }}
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
              <div className="line_solid">
                <div className="left_line" />
                <div className="right_line" />
              </div>

              {choiceVoucherHeader()}

              <div className="product_name">
                {dataVoucher?.goods?.name || "NULL"}
              </div>
              <div className="line_dashed">
                <div className="time_voucher">
                  {t("voucher.expiryDate")}:{" "}
                  {moment(dataVoucher?.expireDate || null).format(
                    "DD/MM/YYYY"
                  )}
                </div>
                <div className="left_line" />
                <div className="right_line" />
              </div>
              <div className="show_instruction">
                <div style={{ display: "flex" }}>
                  <BootstrapButton
                    sx={{ backgroundColor: "#4c3d3d" }}
                    variant="contained"
                    onClick={() => {
                      setIsOpenPopupMessage(true);
                    }}
                    className="button_show_instruction"
                  >
                    {t("voucher.gift_message")}
                  </BootstrapButton>
                  <BootstrapButton
                    sx={{ backgroundColor: "#4c3d3d" }}
                    variant="contained"
                    onClick={() => {
                      setIsOpenPopupProductGuid(true);
                    }}
                    className="button_show_instruction"
                  >
                    {t("voucher.product_guide")}
                  </BootstrapButton>
                </div>
                {extPinDisplayType === "CHOICE" ? (
                  <div>
                    <div style={{ display: "flex" }}>
                      <BootstrapButton
                        sx={{ backgroundColor: "#4c3d3d" }}
                        variant="contained"
                        onClick={goToVoucherList}
                        className="button_show_instruction"
                      >
                        {t("voucher.showVoucherList")}
                      </BootstrapButton>
                      <BootstrapButton
                        sx={{ backgroundColor: "#4c3d3d" }}
                        variant="contained"
                        onClick={() => {
                          history("/list-instruction");
                        }}
                        className="button_show_instruction"
                      >
                        {t("voucher.showInstruction")}
                      </BootstrapButton>
                    </div>
                  </div>
                ) : (
                  <div>
                    {dataVoucher?.voucherStatus !== "USED" && (
                      <div style={{ display: "flex" }}></div>
                    )}
                    <div style={{ display: "flex" }}>
                      <BootstrapButton
                        sx={{ backgroundColor: "#4c3d3d" }}
                        variant="contained"
                        onClick={() => {
                          history("/list-instruction");
                        }}
                        className="button_show_instruction"
                      >
                        {t("voucher.showInstruction")}
                      </BootstrapButton>
                      {dataVoucher?.voucherStatus === VoucherStatus.NORMAL ||
                        dataVoucher?.voucherStatus === VoucherStatus.PART_USED ? (
                        <>
                          {dataVoucher?.voucherType === "PP" ? (
                            dataVoucher.voucherPrice / 2 -
                            dataVoucher.balance <
                            0 && (
                              <BootstrapButton
                                sx={{ backgroundColor: "#4c3d3d" }}
                                variant="contained"
                                onClick={() => {
                                  history(`/share-voucher/${otp}`);
                                }}
                                className="button_show_instruction"
                              >
                                {t("voucher.transferringOthers")}
                              </BootstrapButton>
                            )
                          ) : (
                            <BootstrapButton
                              sx={{ backgroundColor: "#4c3d3d" }}
                              variant="contained"
                              onClick={() => {
                                history(`/share-voucher/${otp}`);
                              }}
                              className="button_show_instruction"
                            >
                              {t("voucher.transferringOthers")}
                            </BootstrapButton>
                          )}
                        </>
                      ) : (
                        <BootstrapButton
                          sx={{ backgroundColor: "#4c3d3d" }}
                          variant="contained"
                          onClick={goToVoucherList}
                          className="button_show_instruction"
                        >
                          {t("voucher.showVoucherList")}
                        </BootstrapButton>
                      )}
                    </div>
                  </div>
                )}
              </div>
            </div>
          )}
          {dataVoucher?.transferStatus === "RECPT_WAIT" &&
            !isExpiredVoucher(dataVoucher) && (
              <div className="list_action">
                <Button
                  sx={{ backgroundColor: "#990011", marginTop: "32px" }}
                  variant="contained"
                  onClick={() => {
                    setIsConfirm(true);
                  }}
                  className="button_show_instruction"
                >
                  {t("voucher.acceptTheVoucher")}
                </Button>
              </div>
            )}

          {isExpiredVoucher() && (
            <div
              style={{ textAlign: "center", marginTop: "20px", color: "red" }}
            >
              {t("voucher.expireDate")} {dataVoucher?.expireDate}
            </div>
          )}
        </div>
      )}
    </Container>
  );
};
export default DetailVoucher;
