import React, { useEffect, useState } from "react";
import axios from "axios";
import Button from "@mui/material/Button";
import { Container } from "./styles";
import LOGO from "../../images/Logo.png";
import { BiArrowBack } from "react-icons/bi";
import { useNavigate } from "react-router-dom";
import PopupError from "../../components/PopupCustom/error";
import { useTranslation } from "react-i18next";
import { renderMessage } from "../../utils/utils";

export const regexNumberPhone = /^((09|03|07|08|05)+([0-9]{8})\b)/;

const ShareVoucher = () => {
  const { t, i18n } = useTranslation();
  const history = useNavigate();
  const asPath = window.location.pathname;
  const lastIndex = asPath.lastIndexOf("/");
  const otp = asPath.substring(lastIndex + 1, asPath.length); // remove preview
  const [dataInput, setDataInput] = useState({
    toName: null,
    to: null,
    otp: otp,
    message: "",
  });
  const [errorData, setErrorData] = useState(null);

  useEffect(() => {
    if (otp) {
      if (otp === localStorage.getItem("otp")) {
        const asPath = localStorage.getItem("shortLink");
        const lastIndex = asPath.lastIndexOf("/");
        const voucherID = asPath.substring(lastIndex + 1, asPath.length); // remove preview
        history(`/voucher?_Ss=${voucherID}`);
      }
    }
  }, []);

  const onTransfer = () => {
    axios
      .post(
        `${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/vouchers/transfer`,
        { ...dataInput }
      )
      .then((res) => {
        localStorage.setItem("otp", otp);
        localStorage.setItem("shortLink", res.data.data.shortLink);
        history("/response-transfer");
      })
      .catch((error) => {
        setErrorData({
          title: error?.response?.data?.code,
          message: renderMessage(error?.response?.data?.code, t),
          onAction: () => {
            setErrorData(null);
          },
        });
      });
  };

  return (
    <Container>
      {errorData && (
        <PopupError
          title={errorData.title}
          message={errorData.message}
          onAction={errorData.onAction}
        />
      )}
      <div className="row">
        <div className="header">
          <img src={LOGO} alt="" />
          {window.history.length > 1 && (
            <BiArrowBack
              color={"#000"}
              onClick={() => {
                history(-1);
              }}
              size={28}
              className={"icon_back"}
            />
          )}
        </div>
        <div
          style={{
            paddingTop: "30px",
            margin: "0 auto",
            maxWidth: "500px",
            paddingLeft: "20px",
            paddingRight: "20px",
          }}
        >
          <div
            style={{
              fontFamily: "Lato",
              color: "#1C1C1B",
              marginBottom: "8px",
              display: "flex",
            }}
          >
            {t("voucher.name")} <span style={{ color: "red" }}>*</span>:
          </div>
          <input
            placeholder={t("voucher.placeholderName")}
            style={{
              width: "95%",
              backgroundColor: "white",
              marginTop: "8px",
              height: "40px",
              borderRadius: "6px",
              padding: "0px 8px",
              border: "none",
              fontFamily: "Lato",
              fontSize: "16px",
            }}
            onChange={(e) => {
              let object = { ...dataInput };
              setDataInput({ ...object, toName: e.target.value });
            }}
          />

          <div
            style={{
              fontFamily: "Lato",
              color: "#1C1C1B",
              marginBottom: "8px",
              marginTop: "32px",
            }}
          >
            {t("voucher.phoneNumber")} <span style={{ color: "red" }}>*</span>:
          </div>
          <input
            placeholder={t("voucher.placeholderPhone")}
            pattern="\d*"
            type={"number"}
            style={{
              width: "95%",
              backgroundColor: "white",
              marginTop: "8px",
              height: "40px",
              borderRadius: "6px",
              padding: "0px 8px",
              fontFamily: "Lato",
              border: "none",
              fontSize: "16px",
            }}
            aria-describedby="outlined-weight-helper-text"
            onChange={(e) => {
              let object = { ...dataInput };
              setDataInput({ ...object, to: e.target.value });
            }}
          />

          <div
            style={{
              fontFamily: "Lato",
              color: "#1C1C1B",
              marginBottom: "8px",
              marginTop: "32px",
            }}
          >
            {t("voucher.transferMessage")}
          </div>
          <textarea
            placeholder={t("voucher.placeholderTransferMessage")}
            style={{
              width: "95%",
              backgroundColor: "white",
              marginTop: "8px",
              fontFamily: "Lato",
              borderRadius: "6px",
              border: "none",
              padding: "4px 8px",
              fontSize: "16px",
            }}
            rows="6"
            cols="50"
            onChange={(e) => {
              let object = { ...dataInput };
              setDataInput({ ...object, message: e.target.value });
            }}
          />

          <div
            style={{
              marginTop: "24px",
              fontStyle: "italic",
              color: "red",
              height: "40px",
            }}
          >
            {regexNumberPhone.test(dataInput.to) === false &&
              dataInput.to !== "" &&
              dataInput.to !== null && <div>{t("voucher.phoneNotFormat")}</div>}
            {dataInput.toName === "" && <div>{t("voucher.nameRequired")}</div>}
            {dataInput.to === "" && <div>{t("voucher.phoneRequired")}</div>}
            {dataInput.toName && dataInput.toName.length > 30 && (
              <div>{t("voucher.usernameLength")}</div>
            )}
            {dataInput.message && dataInput.message.length > 30 && (
              <div>{t("voucher.messageLength")}</div>
            )}
          </div>

          <div
            style={{
              display: "flex",
              justifyContent: "center",
            }}
          >
            <Button
              onClick={onTransfer}
              disabled={
                regexNumberPhone.test(dataInput.to) === false ||
                dataInput.toName === null ||
                dataInput.toName === "" ||
                dataInput.toName.length > 30 ||
                dataInput.message.length > 30
              }
              sx={{ backgroundColor: "#4c3d3d" }}
              variant="contained"
              className="button_action"
            >
              {t("voucher.buttonTransferMessage")}
            </Button>
          </div>
        </div>
      </div>
    </Container>
  );
};

export default ShareVoucher;
