import { t } from "i18next";
import SUCCESS_IMAGE from "../../images/popupSuccess.png";

import React from "react";
import styled from "styled-components";

const Container = styled.div`
  .row_popup {
    position: relative !important;
    height: max-content !important;
    padding: 12px 0 34px 0 !important;
    display: flex;
    flex-direction: column;
    align-content: center;
    vertical-align: middle;
    align-items: center;

    img {
      //   position: absolute !important;
      //   left: 50% !important;
      //   top: -70px !important;
      //   transform: translateX(-50%) !important;
      width: 90px !important;
    }

    h1 {
      //   padding-top: 40px;
      width: 80%;
      font-family: "Inter";
      font-style: normal;
      font-weight: 700;
      font-size: 22px;
      text-align: center;
      color: #000000;
    }

    h2 {
      font-family: "Inter";
      font-style: normal;
      font-weight: 400;
      font-size: 12px;
      text-align: center;
      color: #696969;
      text-align: justify;
    }
    .text-right {
      text-align: right;
    }
  }
`;

const ActiveSuccess = ({ title, message, name, phone }) => {
  return (
    <>
      <Container>
        <div className="row_popup">
          <img src={SUCCESS_IMAGE} alt="" />
          <div className="dash_line" style={{ width: "100%" }} />
          <h1 style={{ textTransform: "uppercase" }}>{title}</h1>
          <table style={{ width: "95%" }}>
            <tr>
              <td>{t("voucher.name")}</td>
              <td className="text-right">{name}</td>
            </tr>
          </table>
          <table style={{ width: "95%" }}>
            <tr>
              <td>{t("voucher.phoneNumber")}</td>
              <td className="text-right">{phone}</td>
            </tr>
          </table>
          <h2>{message}</h2>
          <div className="dash_line" style={{ width: "100%" }} />
        </div>
      </Container>
    </>
  );
};

export default ActiveSuccess;
