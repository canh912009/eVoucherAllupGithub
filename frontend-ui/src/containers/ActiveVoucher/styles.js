/**
 * ...
 */

import styled from "styled-components";

const Container = styled.div`
  display: flex;
  justify-content: center;
  background-color: #fee715;
  margin: auto;
  height: max-content;
  min-height: 100vh;
  overflow: hidden;
  padding-bottom: 50px;
  @media (max-width: 375px) {
    width: 100%;
  }

  @media (min-width: 500px) {
    //width: 100vw;
  }

  .row {
    margin-top: 24px;
    max-width: 90%;
    @media (min-width: 500px) {
      width: 440px;
      padding: 0 40px;
    }
  }

  .dash_line {
    margin: 15px 0 20px 0;
    height: 1px;
    background: repeating-linear-gradient(
      90deg,
      #ccc 0px,
      #ccc 10px,
      rgba(0, 0, 0, 0) 0px,
      rgba(0, 0, 0, 0) 23px
    );
  }

  .pop_up_modal {
    position: relative;
    height: max-content;

    .row_popup {
      max-height: 60vh;
      height: max-content;
      overflow: auto;
      color: black;
      font-family: "Inter";
      position: relative;
      width: 320px;
      background: #ffffff;
      border-radius: 8px;
      padding: 20px 16px;

      .label {
        margin-top: 12px;
      }

      img {
        width: 48px;
      }

      h1 {
        font-family: "Inter";
        font-style: normal;
        font-weight: 700;
        font-size: 20px;
        color: #000000;
      }

      h2 {
        font-family: "Inter";
        font-style: normal;
        font-weight: 400;
        font-size: 16px;
        color: #696969;
      }

      .button_action {
        text-transform: inherit;
        font-weight: 400;
        text-align: center;
        margin: 0 12px;
        margin-top: 12px;
        margin-bottom: 12px;
        display: flex;
        font-size: 14px;
        justify-content: center;
        align-items: center;
        width: max-content;
        padding: 4px 8px;
        color: #ffffff;
        box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.1),
          0px 0px 0px 1px rgba(70, 79, 96, 0.16);
        border-radius: 6px;
        @media (min-width: 500px) {
          font-size: 16px;
          padding: 8px 12px;
        }
      }
    }
  }

  .message {
    background: #f6f1e9;
    border-radius: 8px;
    padding: 12px 20px;
    position: relative;
    margin-top: 80px;
    margin-bottom: 30px;

    .image_send {
      padding: 20px;
      border: 2px solid #ffffff;
      border-radius: 50%;
      background-color: #eed971;
      position: absolute;
      left: 50%;
      transform: translateX(-50%);
      top: -80px;

      img {
        width: 75px;
      }
    }

    h1 {
      font-family: "Inter";
      font-style: normal;
      font-weight: 700;
      font-size: 24px;
      text-align: center;
      margin-top: 44px;
      color: #000000;
    }

    h2 {
      font-family: "Inter";
      font-style: normal;
      font-weight: 400;
      font-size: 12px;
      text-align: justify;
      color: #696969;
      padding-bottom: 20px;
    }

    .from_share {
      position: absolute;
      bottom: -20px;
      left: 0;
      background: #ffd93d;
      width: 100%;
      padding: 12px 0;
      border-bottom-right-radius: 8px;
      border-bottom-left-radius: 8px;

      .text {
        font-family: "Inter";
        font-style: normal;
        text-align: center;
        font-weight: 400;
        font-size: 14px;
        color: #000000;
        margin-left: 20px;
      }
    }
  }

  .voucher_info {
    margin-top: 10px;
  }

  .receiver_info {
    margin-top: 40px;
  }

  .container {
    background-color: white;
    border-radius: 20px;
    padding: 12px 20px;
    position: relative;
    margin: auto;

    .mask {
      position: absolute;
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      border-radius: 20px;
      background: rgba(128, 128, 128, 0.4);
    }

    .tiker {
      position: absolute;
      left: -20px;
      top: -32px;
      width: 100px;
      transform: rotate(-15deg);
    }

    .logo {
      display: flex;
      justify-content: center;
      position: relative;

      img {
        height: 48px;
      }

      .row_language {
        position: absolute;
        right: -8px;
        display: flex;
        justify-content: center;
        align-items: center;
        align-content: center;

        .item_language {
          height: 20px;
          width: 30px;
          position: relative;
          border-radius: 4px;
        }

        .background_mask_language {
          position: absolute;
          height: 20px;
          width: 30px;
          background-color: rgba(59, 59, 59, 0.38);
          top: 0;
          left: 0;
          border-radius: 4px;
        }
      }
    }

    .line_solid {
      order: 4;
      align-self: stretch;
      width: 100%;
      position: absolute;
      // margin: 20px 0;
      rotation: 0;
      top: 44%;

      .left_line {
        position: absolute;
        left: -32px;
        // top: -10px;
        height: 24px;
        width: 24px;
        border-radius: 50%;
        background-color: #fee715;
      }

      .right_line {
        position: absolute;
        right: 9px;
        // top: -10px;
        height: 24px;
        width: 24px;
        border-radius: 50%;
        background-color: #fee715;
      }
    }

    .line_dashed {
      order: 4;
      align-self: stretch;
      width: 100%;
      position: relative;
      margin: 20px 0;
      rotation: 0;
      height: 48px;
      border-top: 0.5px dashed rgba(224, 224, 224, 1);
      border-bottom: 0.5px dashed rgba(224, 224, 224, 1);

      .time_voucher {
        line-height: 48px;
        margin-bottom: 12px;
        text-align: center;
        font-family: "Inter";
        font-style: normal;
        font-weight: 400;
        font-size: 14px;
        height: 16px;
        color: #757575;
        @media (min-width: 500px) {
          font-size: 16px;
        }
      }

      .left_line {
        position: absolute;
        left: -32px;
        top: 12px;
        height: 24px;
        width: 24px;
        border-radius: 50%;
        background-color: #fee715;
      }

      .right_line {
        position: absolute;
        right: -32px;
        top: 12px;
        height: 24px;
        width: 24px;
        border-radius: 50%;
        background-color: #fee715;
      }
    }

    .title_voucher {
      height: 120px;
      display: flex;
      justify-content: space-between;
      align-content: center;
      align-items: center;
      @media (min-width: 666px) {
        justify-content: space-around;
      }

      .image_product {
        width: 50%;

        img {
          height: 120px;
          width: 100%;
          @media (min-width: 666px) {
            max-width: 240px;
          }
        }
      }

      .title {
        width: 45%;
        height: 120px;
        display: flex;
        flex-direction: column;
        align-items: center;
        align-content: center;
        justify-content: space-between;

        img {
          height: 100px;
          width: 100%;
          object-fit: contain;
          @media (min-width: 666px) {
            width: 120px;
          }
        }

        div {
          width: 100%;
          font-family: "Lato";
          font-style: normal;
          font-weight: 500;
          font-size: 16px;
          text-overflow: ellipsis;
          overflow: hidden;
          text-align: center;
          letter-spacing: 0.02em;
          color: #000000;
        }
      }
    }

    .product_name {
      margin: 20px 0;
      font-family: "Lato";
      font-style: normal;
      font-weight: bold;
      font-size: 24px;
      text-align: center;
      line-height: 31px;
      letter-spacing: 0.02em;
      color: #000000;
    }

    .title {
      margin: 20px 0 0 0;
      font-family: "Lato";
      font-style: normal;
      font-weight: bold;
      font-size: 22px;
      text-align: center;
      line-height: 25px;
      letter-spacing: 0.02em;
      color: #000000;
    }

    .sub_title {
      font-family: "Lato";
      font-style: normal;
      font-weight: normal;
      font-size: 12px;
      text-align: center;
      line-height: 24px;
      letter-spacing: 0.02em;
      color: #000000;
    }

    .detail_customer {
      margin-top: 20px;
      @media (min-width: 666px) {
        display: flex;
        justify-content: space-between;
      }

      .row_detail_customer {
        @media (min-width: 500px) {
          margin: 0 50px;
        }
        display: flex;
        justify-content: space-between;

        .item_left {
          float: left;
          text-align: left;
          width: max-content;
        }

        .item_right {
          width: max-content;
          @media (min-width: 500px) {
            margin-left: 50px;
          }
          float: right;
          text-align: right;
        }
      }
    }

    .show_instruction {
      margin-top: 20px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;

      .text_show_instruction {
        font-family: "Lato";
        font-style: normal;
        font-weight: 400;
        font-size: 16px;
        text-align: left;
        color: #000000;
      }

      .button_show_instruction {
        text-transform: inherit;
        font-weight: 400;
        margin: 12px 8px 8px;
        text-align: center;
        display: flex;
        font-size: 14px;
        justify-content: center;
        align-items: center;
        width: 160px;
        padding: 10px 0;
        color: #ffffff;
        //background: #4c3d3d;
        box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.1),
          0px 0px 0px 1px rgba(70, 79, 96, 0.16);
        border-radius: 6px;
        @media (min-width: 500px) {
          font-size: 16px;
          padding: 8px 12px;
          width: 200px;
        }
        @media (max-width: 400px) {
          width: 132px;
          font-size: 12px;
        }
      }

      .button_show_instruction_disabled {
        position: relative;
        text-transform: inherit;
        font-weight: 400;
        margin: 12px 8px 8px;
        text-align: center;
        display: flex;
        font-size: 14px;
        justify-content: center;
        align-items: center;
        width: 160px;
        padding: 10px 0;
        background: #ccc;
        border: none;
        color: #000;
        box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.1),
          0px 0px 0px 1px rgba(70, 79, 96, 0.16);
        border-radius: 6px;

        .disabled {
          position: absolute;
          font-size: 12px;
          bottom: -12px;
          font-style: italic;
          text-align: center;
          width: 100%;
        }

        @media (min-width: 500px) {
          font-size: 16px;
          padding: 8px 12px;
          width: 200px;
        }
        @media (max-width: 400px) {
          width: 132px;
          font-size: 12px;
        }
      }
    }
  }

  .show_map {
    margin-top: 20px;
    width: 100%;
    height: 250px;
    background-color: white;
    border-radius: 8px;
    @media (min-width: 500px) {
      height: 300px;
    }
  }

  .list_map {
    margin-top: 20px;
    position: relative;
    width: 100%;
    height: 250px;
    background-color: white;
    overflow-y: auto;
    border-radius: 8px;

    .row_list_map {
      padding-left: 8px;
      padding-bottom: 8px;

      .item_map {
        color: #3a3a3a;
        cursor: pointer;
        display: flex;
        margin-top: 16px;
        margin-left: 8px;
        font-family: "Lato";
        letter-spacing: 0.5px;
        font-style: normal;
        font-weight: 400;
        font-size: 14px;
        line-height: 17px;

        .focus_item_map {
          overflow: hidden;
          width: 34px;
        }
      }
    }
  }

  .show_qr {
    display: flex;
    justify-content: center;
    padding-top: 64px;
    position: relative;

    img {
      position: absolute;
      width: 120px;
      top: 10px;
    }

    .no_show_qr {
      position: absolute;
      top: 120px;
      font-weight: 1000;
      left: 50%;
      font-size: 200px;
      transform: translateX(-50%);
    }

    .row_show_qr {
      z-index: 2;
      width: max-content;
      height: max-content;
      background-color: #ffffff;
      border-radius: 50px;
      padding: 20px;

      .border_qr {
        border-radius: 36px;
        padding: 14px;
        border: 12px solid #000;
        position: relative;

        .border_qr_white_y {
          width: 20px;
          height: 60%;
          top: 50%;
          transform: translateY(-50%);
          background-color: white;
          position: absolute;
        }

        .border_qr_white_x {
          width: 60%;
          height: 20px;
          left: 50%;
          transform: translateX(-50%);
          background-color: white;
          position: absolute;
        }
      }
    }
  }

  .list_action {
    display: flex;
    justify-content: center;
    padding-bottom: 32px;

    .button_action {
      text-transform: inherit;
      font-weight: 400;
      text-align: center;
      margin: 0 6px;
      margin-top: 28px;
      margin-bottom: 12px;
      display: flex;
      font-size: 14px;
      justify-content: center;
      align-items: center;
      width: max-content;
      padding: 8px 8px;
      color: #ffffff;
      box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.1),
        0px 0px 0px 1px rgba(70, 79, 96, 0.16);
      border-radius: 6px;
      @media (min-width: 500px) {
        font-size: 16px;
        padding: 8px 12px;
      }
    }
  }

  .slick-initialized .slick-slide {
    display: flex;
    justify-content: center;
    align-content: center;
    align-items: center;
    min-height: 300px;
    width: 100%;
    margin-top: 20px;
  }

  .slick-arrow {
    display: none !important;
  }
`;

const ProductChoiceStyle = styled.div`
  margin-top: 40px;

  .product_choice,
  .product_choice * {
    box-sizing: border-box;
  }

  .product_choice {
    background: #fcfcfc;
    border-radius: 6px;
    border-style: solid;
    border-color: #c7c7c7;
    border-width: 1px;
    display: flex;
    flex-direction: column;
    gap: 20px;
    align-items: center;
    justify-content: flex-start;
    flex-shrink: 0;
    position: relative;
  }

  .header {
    background: #101828;
    border-radius: 4px;
    padding: 24px 16px 24px 16px;
    display: flex;
    flex-direction: row;
    align-items: flex-start;
    justify-content: space-between;
    align-self: stretch;
    flex-shrink: 0;
    height: 114px;
    position: relative;
    overflow: hidden;
  }

  .graphic {
    flex-shrink: 0;
    width: 152.88px;
    height: 147.88px;
    position: absolute;
    right: -28px;
    top: -48px;
  }

  .rectangle-1783 {
    background: rgba(73, 103, 143, 0.2);
    border-radius: 9.6px;
    width: 136.32px;
    height: 136.32px;
    position: absolute;
    left: -4.73px;
    top: 97.75px;
    box-shadow: inset 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
    transform-origin: 0 0;
    transform: rotate(-62deg) scale(1, 1);
  }

  .rectangle-1784 {
    background: rgba(73, 103, 143, 0.8);
    border-radius: 9.6px;
    width: 136.32px;
    height: 136.32px;
    position: absolute;
    left: 12px;
    top: 98.37px;
    box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.25);
    transform-origin: 0 0;
    transform: rotate(-62deg) scale(1, 1);
  }

  .balance {
    display: flex;
    flex-direction: column;
    gap: 4px;
    align-items: flex-start;
    justify-content: center;
    flex-shrink: 0;
    position: relative;
  }

  .current-balance {
    color: rgba(248, 249, 252, 0.7);
    text-align: left;
    font: 400 12px/16px "Roboto-Regular", sans-serif;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }

  .frame-1000003391 {
    display: flex;
    flex-direction: row;
    gap: 4px;
    align-items: flex-end;
    justify-content: flex-start;
    flex-shrink: 0;
    position: relative;
  }

  ._200-000 {
    color: #fcfcfd;
    text-align: left;
    font: 500 24px "Roboto-Medium", sans-serif;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }

  .vnd {
    color: #fcfcfd;
    text-align: left;
    font: 500 12px/32px "Roboto-Medium", sans-serif;
    position: relative;
    width: 25px;
    height: 20px;
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }

  .frame-1000003392 {
    display: flex;
    flex-direction: row;
    gap: 2px;
    align-items: center;
    align-content: center;
    justify-content: flex-start;
    flex-shrink: 0;
    position: relative;
  }

  .voucher-original-price-500-000-00 {
    text-align: left;
    font: 400 11px/16px "Roboto-Regular", sans-serif;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }

  .voucher-original-price-500-000-00-span {
    color: #d0d5dd;
    font: 400 14px/16px "Roboto-Regular", sans-serif;
  }

  .voucher-original-price-500-000-00-span2 {
    color: rgba(208, 213, 221, 0.2);
    font: 400 14px/16px "Roboto-Regular", sans-serif;
  }

  .voucher-original-price-500-000-00-span3 {
    color: #d0d5dd;
    font: 400 14px/16px "Roboto-Regular", sans-serif;
  }

  .vnd2 {
    color: #d0d5dd;
    text-align: left;
    font: 400 8px "Roboto-Regular", sans-serif;
    position: relative;
    width: 17px;
    height: 13px;
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }

  .button {
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    gap: 8px;
    align-items: flex-start;
    justify-content: center;
    flex-shrink: 0;
    position: relative;
    overflow: hidden;
  }

  .state-layer {
    display: flex;
    flex-direction: row;
    gap: 8px;
    align-items: center;
    justify-content: center;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .material-symbols-history {
    color: rgba(255, 243, 201, 0.8);
    flex-shrink: 0;
    width: 18px;
    height: 18px;
    position: relative;
    overflow: visible;
  }

  .button2 {
    color: rgba(255, 243, 201, 0.8);
    text-align: center;
    font: 500 14px/20px "Roboto-Medium", sans-serif;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
  }

  .header2 {
    padding: 0px 0px 8px 0px;
    display: flex;
    flex-direction: row;
    gap: 8px;
    align-items: center;
    justify-content: flex-start;
    flex-shrink: 0;
    width: 342px;
    position: relative;
  }

  .choose-product {
    color: #1d2939;
    text-align: left;
    font: 500 16px/24px "Roboto-Medium", sans-serif;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }

  .product_choice2 {
    padding: 16px 0px 16px 0px;
    display: flex;
    flex-direction: row;
    gap: 8px;
    align-items: flex-start;
    justify-content: flex-start;
    flex-shrink: 0;
    position: relative;
  }

  .container-product_choice {
    display: flex;
    gap: 40px 8px;
    align-items: flex-start;
    justify-content: flex-start;
    flex-wrap: wrap;
    flex-shrink: 0;
    width: 356px;
    position: relative;
  }

  .card {
    background: rgb(245 242 242);
    border-radius: 6px;
    padding: 8px 0px 8px 0px;
    display: flex;
    flex-direction: column;
    gap: 12px;
    align-items: center;
    justify-content: flex-start;
    flex-shrink: 0;
    position: relative;
    padding-bottom: 0 !important;
  }

  .box {
    flex-shrink: 0;
    width: 174px;
    height: 102px;
    position: relative;
  }

  .image {
    width: 174px;
    height: 102px;
    position: absolute;
    left: 0px;
    top: 0px;
  }

  .product-image {
    opacity: 0.23000000417232513;
    width: 174px;
    height: 102px;
    position: absolute;
    left: calc(50% - 87px);
    top: calc(50% - 51px);
  }

  .product {
    width: 142px;
    height: 139px;
    position: absolute;
    left: calc(50% - 71px);
    top: calc(50% - 52px);
  }

  .image_item_product {
    width: 142px;
    height: auto;
    position: absolute;
    left: calc(50% - 71px);
    top: 0px;
  }

  .content {
    padding: 0px 4px 0px 4px;
    display: flex;
    flex-direction: column;
    gap: 8px;
    align-items: flex-start;
    justify-content: flex-start;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .frame-1000003390 {
    display: flex;
    flex-direction: row;
    gap: 18px;
    justify-content: center;
    align-self: stretch;
    align-content: center;
    align-items: center;
    flex-shrink: 0;
    position: relative;
  }

  .the-coffee {
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
    max-width: 150px;
    color: #101828;
    text-align: center;
    font: 600 14px/16px "Roboto-Medium", sans-serif;
    position: relative;
  }

  .item_product_value {
    width: 100%;
    color: #101828;
    text-align: center;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .item_value_vnd {
    color: #101828;
    font: 600 14px/16px "Roboto-CondensedSemiBold", sans-serif;
  }

  .item_value_vnd2 {
    padding-left: 4px;
    color: #101828;
    font: 600 14px/16px "Roboto-CondensedSemiBold", sans-serif;
  }

  .lowery-s-gourmet-whole-bean-and-wired-willey-s-white-coffee {
    text-overflow: ellipsis;
    white-space: nowrap;
    overflow: hidden;
    max-width: 150px;
    color: #101828;
    margin: auto;
    text-align: center;
    font: 400 10px/16px "Roboto-Regular", sans-serif;
    height: 36px;
  }

  .price-quantity {
    padding: 4px 8px 4px 8px;
    display: flex;
    flex-direction: row;
    gap: 4px;
    align-items: center;
    justify-content: flex-start;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .stocks_amount {
    color: #039855;
    text-align: center;
    font: 400 10px "Roboto-Regular", sans-serif;
    position: relative;
    width: 79px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .amount {
    display: flex;
    flex-direction: row;
    gap: 8px;
    align-items: center;
    align-content: center;
    justify-content: flex-end;
    flex-shrink: 0;
    width: 74px;
    position: relative;
  }

  .minus-square {
    flex-shrink: 0;
    width: 20px;
    height: 20px;
    position: relative;
    overflow: visible;
    color: gray;
    cursor: pointer;
  }

  ._0 {
    width: 16px;
    text-align: center;
    color: #101828;
    font: 500 16px "Roboto-Medium", sans-serif;
    position: relative;
  }

  .plus-square {
    flex-shrink: 0;
    width: 20px;
    height: 20px;
    position: relative;
    overflow: visible;
    color: gray;
    cursor: pointer;
  }

  .regular-button {
    background: var(--neutral-200, #f1f3f9);
    border-radius: 2.5px;
    border-style: solid;
    border-color: rgba(225, 230, 239, 0.3);
    border-width: 1px;
    padding: 4px 12px 4px 12px;
    display: flex;
    flex-direction: row;
    gap: 4px;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    width: 160px;
    position: relative;
    overflow: hidden;
  }

  .button_select_choice {
    color: var(--text-icon-disabled-black, rgba(29, 36, 51, 0.65));
    text-align: center;
    font: 500 12px/16px "Roboto-Medium", sans-serif;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
  }

  ._70-000-vnd {
    color: #101828;
    text-align: left;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }

  ._70-000-vnd-span {
    color: #101828;
    font: 400 14px/16px "Roboto-CondensedSemiBold", sans-serif;
  }

  ._70-000-vnd-span2 {
    color: #101828;
    font: 400 10px/16px "Roboto-CondensedSemiBold", sans-serif;
  }

  ._4-left-in-stocks {
    color: #fdb022;
    text-align: center;
    font: 400 10px "Roboto-Regular", sans-serif;
    position: relative;
    width: 79px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .minus-square2 {
    flex-shrink: 0;
    width: 14px;
    height: 14px;
    position: relative;
    overflow: visible;
  }

  ._1 {
    color: #101828;
    text-align: left;
    font: 500 10px "Roboto-Medium", sans-serif;
    position: relative;
  }

  .plus-square2 {
    flex-shrink: 0;
    width: 14px;
    height: 14px;
    position: relative;
    overflow: visible;
  }

  .regular-button2 {
    background: #f4d160;
    border-radius: 2.5px;
    border-style: solid;
    border-color: rgba(225, 230, 239, 0.3);
    border-width: 1px;
    padding: 4px 12px 4px 12px;
    display: flex;
    flex-direction: row;
    gap: 4px;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    width: 160px;
    position: relative;
    overflow: hidden;
  }

  .button_select_choice2 {
    color: #fcfcfc;
    text-align: center;
    font: 500 12px/16px "Roboto-Medium", sans-serif;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  ._90-000-vnd {
    color: #101828;
    text-align: left;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }

  ._90-000-vnd-span {
    color: #101828;
    font: 400 14px/16px "Roboto-CondensedSemiBold", sans-serif;
  }

  ._90-000-vnd-span2 {
    color: #101828;
    font: 400 10px/16px "Roboto-CondensedSemiBold", sans-serif;
  }

  ._1-left-in-stocks {
    color: #b42318;
    text-align: center;
    font: 400 10px "Roboto-Regular", sans-serif;
    position: relative;
    width: 79px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .minus-square3 {
    flex-shrink: 0;
    width: 14px;
    height: 14px;
    position: relative;
    overflow: visible;
  }

  .plus-square3 {
    flex-shrink: 0;
    width: 14px;
    height: 14px;
    position: relative;
    overflow: visible;
  }

  .item_value_vnd3 {
    color: #101828;
    font: 400 14px/16px "Roboto-CondensedSemiBold", sans-serif;
  }

  .item_value_vnd4 {
    color: #101828;
    font: 400 10px/16px "Roboto-CondensedSemiBold", sans-serif;
  }

  ._0-stocks {
    color: #e4e7ec;
    text-align: center;
    font: 400 10px "Roboto-Regular", sans-serif;
    position: relative;
    width: 79px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .minus-square4 {
    flex-shrink: 0;
    width: 14px;
    height: 14px;
    position: relative;
    overflow: visible;
  }

  ._02 {
    color: rgba(16, 24, 40, 0.5);
    text-align: left;
    font: 500 10px "Roboto-Medium", sans-serif;
    position: relative;
  }

  .plus-square4 {
    flex-shrink: 0;
    width: 14px;
    height: 14px;
    position: relative;
    overflow: visible;
  }

  .button_select_choice3 {
    color: rgba(29, 36, 51, 0.25);
    text-align: center;
    font: 500 12px/16px "Roboto-Medium", sans-serif;
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .button_back_to_voucher {
    height: 44px;
    width: 100vw;
    background-color: #4c3d3d;
    display: flex;
    align-items: center;
    align-content: center;
    color: white;
    font-weight: bold;
    font-size: 16px;
    position: fixed;
    left: 0;
    top: 0;
    z-index: 2;
  }

  .icon_list_product_choice {
    width: 100%;
    display: flex;
    justify-content: center;
    margin-top: 56px;

    img {
      width: 120px;
    }
  }

  .row_list_voucher {
    margin-top: 30px;
    display: grid;
    gap: 40px;
    grid-template-columns: 1fr;
    @media (min-width: 800px) {
      grid-template-columns: 1fr 1fr;
    }
    @media (min-width: 1200px) {
      grid-template-columns: 1fr 1fr 1fr;
    }

    .item_voucher {
      width: 340px;
      height: 147px;
      display: flex;
      margin: auto;
      background-color: white;
      position: relative;
      box-shadow: 0px 4px 6px rgba(0, 0, 0, 0.25);
      border-radius: 10px;
      justify-content: space-between;

      .mask {
        border-radius: 10px;
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: rgba(128, 128, 128, 0.8);
        font-family: "Roboto";
        font-style: normal;
        font-weight: 700;
        font-size: 20px;
        color: #ffffff;
        line-height: 200px;
        text-align: center;
      }

      .tiker {
        position: absolute;
        right: -20px;
        top: -40px;
        width: 100px;
      }

      .line_dashed {
        z-index: 0;
        position: relative;
        border-left: 2px dashed #f4f2f5;

        .line_dashed_absolute {
          z-index: 2;
          position: absolute;
          left: 50%;
          transform: translateX(-50%);
          height: 28px;
          width: 28px;
          border-radius: 50%;
          background-color: #fee715;
          box-shadow: none;
        }
      }

      .logo {
        width: 186px;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        align-content: center;

        .name {
          margin-bottom: 12px;
          font-family: "Poppins";
          font-style: normal;
          font-weight: 500;
          font-size: 18px;
          line-height: 27px;
          color: #3e548d;
          text-align: center;
        }

        img {
          height: 65px;
        }
      }

      .detail {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        align-content: center;
        padding: 0 12px;

        img {
          width: 28px;
        }

        .button_use_voucher {
          cursor: pointer;
          margin-top: 6px;
          margin-bottom: 6px;
          display: flex;
          font-size: 12px;
          justify-content: center;
          align-items: center;
          font-family: "Poppins";
          width: max-content;
          padding: 8px 5px;
          color: #ffffff;
          background: #4c3d3d;
          box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.1),
            0px 0px 0px 1px rgba(70, 79, 96, 0.16);
          border-radius: 10px;
        }

        .price {
          font-family: "Roboto";
          font-style: normal;
          font-weight: 500;
          font-size: 13px;
          color: #b9b5c4;

          span {
            color: red;
          }
        }
      }
    }
  }
`;

const InputOTP = styled.input`
  border-radius: unset;
  border-bottom: 2px solid #000000;
  border-width: 0 0 2px 0;
  margin: 0 6px;
  height: 32px;
  width: 24px !important;
  font-size: 24px !important;
  padding: 0 !important;

  ::-webkit-outer-spin-button,
  ::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }

  :focus-visible {
    outline: unset;
    border-bottom: 2px solid #0d46cc;
  }
`;

export { Container, ProductChoiceStyle, InputOTP };
