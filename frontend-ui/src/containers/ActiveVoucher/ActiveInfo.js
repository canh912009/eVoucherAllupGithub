import React, { useState } from "react";
import { Button, TextField } from "@mui/material";
import { AiOutlineCheck } from "react-icons/ai";
import axios from "axios";
import { RESULT_STATUS } from ".";
import { regexNumberPhone } from "../ShareVoucher";

const ACTIVE_ERROR_CODE = {
  VOUCHER_NOT_FOUND: {
    code: 4001,
    messageKey: "error.voucher_not_found_by_serial",
  },
  ALREADY_ACTIVE: {
    code: 4005,
    messageKey: "error.activate_already_active",
  },
  INVALID_REQUEST: {
    code: 4040,
    messageKey: "error.active_key_mismatch",
  },
  CAN_NOT_ACTIVE: {
    code: 1029,
    messageKey: "error.can_not_active",
  },
};

const ActiveInfo = ({
  t,
  setResult,
  setLoading,
  activationKey,
  valueName,
  setValueName,
  phone,
  setPhone,
}) => {
  const [errorLengthName, setErrorLengthName] = useState(null);
  const [phoneErrorMsg, setPhoneErrorMsg] = useState(null);
  const [phoneConfirm, setPhoneConfirm] = useState("");
  const [serialNumber, setSerialNumber] = useState("");
  const [serialNumberErrorMsg, setSerialNumberErrorMsg] = useState(null);
  const [confirmErrorMsg, setConfirmErrorMsg] = useState(null);
  const [disabledActive, setDisableActive] = useState(true);

  const fromData = {
    userName: valueName,
    phoneNumber: phone,
    serialNumber: serialNumber,
    activationKey: activationKey,
  };

  const validPhoneNumber = (phone) => {
    if (!phone) {
      setPhoneErrorMsg(t("error.missing_phone"));
    } else if (!regexNumberPhone.test(phone)) {
      setPhoneErrorMsg(t("voucher.phoneNotFormat"));
    } else {
      setPhoneErrorMsg(null);
      comparePhoneAndConfirm(phone, phoneConfirm);
    }
    activeButton(valueName, phone, phoneConfirm, serialNumber);
  };

  const validConfirmPhone = (confirm) => {
    if (!confirm) {
      setConfirmErrorMsg(t("error.missing_confirm"));
    } else {
      comparePhoneAndConfirm(phone, confirm);
    }
    activeButton(valueName, phone, confirm, serialNumber);
  };

  const comparePhoneAndConfirm = (phone, confirmValue) => {
    if (phone && confirmValue) {
      if (phone === confirmValue) {
        setConfirmErrorMsg(null);
      } else {
        setConfirmErrorMsg(t("error.phone_not_equal_confirm"));
      }
    }
    activeButton(valueName, phone, confirmValue, serialNumber);
  };

  const activeButton = (name, phone, phoneConfirm, serialNumber) => {
    if (
      serialNumber !== undefined &&
      serialNumber !== null &&
      serialNumber !== "" &&
      name != undefined &&
      name !== null &&
      name !== "" &&
      name.length <= 30 &&
      phone !== undefined &&
      phone !== null &&
      phone !== "" &&
      regexNumberPhone.test(phone) &&
      phoneConfirm !== undefined &&
      phoneConfirm !== null &&
      phoneConfirm !== "" &&
      phone === phoneConfirm
    ) {
      setDisableActive(false);
    } else {
      setDisableActive(true);
    }
  };

  // useEffect(() => {
  //     axios.get('https://provinces.open-api.vn/api/p/')
  //         .then((res) => {
  //             setListProvince(res.data)
  //         })
  //     axios.get('https://provinces.open-api.vn/api/d/')
  //         .then((res) => {
  //             setListDivision(res.data)
  //         })
  // }, [])

  const onActive = (formData) => {
    const activate = async (formData) => {
      setLoading(true);
      console.log("active: ", formData);

      axios
        .post(
          `${process.env.REACT_APP_VOUCHER_API_VIEWER}/v1/vouchers/activate`,
          formData
        )
        .then((res) => {
          console.log("call active api return: ", res);
          setResult({
            status: "SUCCESS",
            detail: {
              title: "title.voucher_active_success",
              message: "message.voucher_active_success",
            },
          });
          // window.location.reload();
        })
        .catch((error) => {
          if (
            error.response &&
            error.response.status === 400 &&
            error.response.data &&
            error.response.data.code
          ) {
            console.log("response: ", error?.response?.data?.code);
            let message;
            switch (error.response.data.code) {
              case ACTIVE_ERROR_CODE.ALREADY_ACTIVE.code:
                message = ACTIVE_ERROR_CODE.ALREADY_ACTIVE.messageKey;
                break;
              case ACTIVE_ERROR_CODE.CAN_NOT_ACTIVE.code:
                message = ACTIVE_ERROR_CODE.CAN_NOT_ACTIVE.messageKey;
                break;
              case ACTIVE_ERROR_CODE.INVALID_REQUEST.code:
                message = ACTIVE_ERROR_CODE.INVALID_REQUEST.messageKey;
                break;
              case ACTIVE_ERROR_CODE.VOUCHER_NOT_FOUND.code:
                message = ACTIVE_ERROR_CODE.VOUCHER_NOT_FOUND.messageKey;
                break;
              default:
                message = 'voucher.UNKNOWN_ERROR';
            }

            setResult({
              status: RESULT_STATUS.FAIL,
              detail: {
                title: "errorTitle.error",
                message: message,
              },
              backInError: true,
            });
          } else {
            setResult({
              status: RESULT_STATUS.FAIL,
              detail: {
                title: "errorTitle.error",
                message: `${error.response.data.message}`,
              },
              backInError: true,
            });
          }
        })
        .finally(() => {
          setLoading(false);
        });
    };

    activate(formData);
  };

  return (
    <>
      <div className="title">{t("title.update_info")}</div>
      <div className="sub_title">{t("sub_title.update_info")}</div>
      <div className="dash_line" />
      <div style={{ margin: "0 0 15px 0" }}></div>
      <div style={{}}>
        <div className={"label"}>
          {t("voucher.serialNumber")} <span style={{ color: "red" }}>*</span>
        </div>
        <TextField
          sx={{ margin: "4px 0", width: "100%" }}
          id="outlined-controlled"
          label=""
          value={serialNumber}
          placeholder={t("placeholder.enterSerialNumber")}
          onChange={(event) => {
            let name = event.target.value;
            setSerialNumber(name);
            if (name.length < 1) {
              setSerialNumberErrorMsg(t("error.missing_serial_number"));
            } else {
              setSerialNumberErrorMsg(null);
            }
            activeButton(name, phone, phoneConfirm, event.target.value);
          }}
          size="small"
        />
        {serialNumberErrorMsg && (
          <div
            style={{
              color: "red",
              fontStyle: "italic",
            }}
          >
            {serialNumberErrorMsg}
          </div>
        )}
        <div className={"label"}>
          {t("voucher.name")} <span style={{ color: "red" }}>*</span>
        </div>
        <TextField
          sx={{ margin: "4px 0", width: "100%" }}
          id="outlined-controlled"
          label=""
          value={valueName}
          placeholder={t("placeholder.enterName")}
          onChange={(event) => {
            if (!event.target.value || event.target.value.length == 0) {
              setValueName(event.target.value);
              setErrorLengthName(t("error.missing_name"));
            } else if (event.target.value.length <= 30) {
              setValueName(event.target.value);
              setErrorLengthName(null);
            } else {
              setErrorLengthName(t("error.name_over_30_cha"));
            }
            activeButton(event.target.value, phone, phoneConfirm, serialNumber);
          }}
          onBlur={(event) => {
            if (valueName && valueName.length > 0 && valueName.length <= 30) {
              setErrorLengthName(null);
            }
          }}
          size="small"
        />
        {errorLengthName && (
          <div
            style={{
              color: "red",
              fontStyle: "italic",
            }}
          >
            {errorLengthName}
          </div>
        )}

        <div className={"label"}>
          {t("voucher.phoneNumber")} <span style={{ color: "red" }}>*</span>
        </div>
        <TextField
          sx={{ margin: "4px 0", width: "100%" }}
          id="outlined-controlled"
          label=""
          value={phone}
          placeholder={t("placeholder.enterPhone")}
          onChange={(event) => {
            setPhone(event.target.value);
            validPhoneNumber(event.target.value);
          }}
          size="small"
        />
        {phoneErrorMsg && (
          <div
            style={{
              color: "red",
              fontStyle: "italic",
            }}
          >
            {phoneErrorMsg}
          </div>
        )}

        <div className={"label"}>
          {t("voucher.phoneConfirm")} <span style={{ color: "red" }}>*</span>
        </div>
        <TextField
          sx={{ margin: "4px 0", width: "100%" }}
          id="outlined-controlled"
          label=""
          value={phoneConfirm}
          placeholder={t("placeholder.enterPhoneConfirm")}
          onChange={(event) => {
            setPhoneConfirm(event.target.value);
            validConfirmPhone(event.target.value);
          }}
          size="small"
        />
        {confirmErrorMsg && (
          <div
            style={{
              color: "red",
              fontStyle: "italic",
            }}
          >
            {confirmErrorMsg}
          </div>
        )}
      </div>

      <Button
        onClick={() => {
          onActive(fromData);
        }}
        disabled={disabledActive}
        sx={{ backgroundColor: "#039855", width: "100%", margin: "20px 0" }}
        variant="contained"
      >
        <AiOutlineCheck style={{ marginRight: "8px" }} />
        {t("voucher.buttonActive")}
      </Button>
    </>
  );
};

export default ActiveInfo;
