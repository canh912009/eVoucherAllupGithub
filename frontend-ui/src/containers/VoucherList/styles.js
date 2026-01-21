/**
 * ...
 */

import styled from 'styled-components';

const Container = styled.div`
  display: flex;
  justify-content: center;
  background-color: #fee715;
  margin: auto;
  height: max-content;
  min-height: 100vh;
  padding-bottom: 50px;
  overflow: hidden;

  .row {
    padding-bottom: 20px;

    .header {
      display: flex;
      width: 100vw;
      margin: 0 !important;
      padding: 0 !important;
      overflow: hidden;
      justify-content: center;
      align-items: center;
      height: 103px;
      background-color: white;
      position: relative;

      img {
        height: 48px;
      }

      .icon_back {
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        left: 30px;
        cursor: pointer;
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
          font-family: 'Roboto';
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
          width: 80px;
          transform: rotate(-15deg);
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
          width: 170px;
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          align-content: center;

          .name {
            margin-bottom: 5px;
            font-family: Poppins;
            font-style: normal;
            font-weight: 800;
            overflow: hidden;
            width: 90%;
            word-wrap: break-word;
            font-size: 13px;
            /* line-height: 27px; */
            color: rgb(62, 84, 141);
            text-align: center;
          }

          img {
            height: auto;
            max-width: 82%;
            max-height: 100px;
          }
        }

        .detail {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          align-content: center;
          padding: 0 6px;

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
            font-family: 'Poppins';
            width: max-content;
            padding: 8px 5px;
            color: #ffffff;
            background: #4c3d3d;
            box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.1),
            0px 0px 0px 1px rgba(70, 79, 96, 0.16);
            border-radius: 10px;
          }

          .price {
            font-family: 'Roboto';
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
  }
`;


const PopupNumberPhone = styled.div`
  .modal-template,
  .modal-template * {
    box-sizing: border-box;
  }

  .modal-template {
    background: var(--surface-primary, #ffffff);
    border-radius: 12px;
    display: flex;
    width: 90%;
    max-width: 500px;
    margin: auto;
    flex-direction: column;
    gap: 0px;
    align-items: center;
    justify-content: flex-start;
    flex: 1;
    position: relative;
    box-shadow: var(--effects-shadow-s-4-box-shadow,
    0px 2px 4px -2px rgba(16, 24, 40, 0.06),
    0px 4px 8px -2px rgba(16, 24, 40, 0.1));
    overflow: hidden;
  }

  .header {
    padding: 0px 16px 0px 16px;
    display: flex;
    flex-direction: row;
    gap: 0px;
    align-items: center;
    justify-content: center;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .content {
    padding: 8px 0px 8px 0px;
    display: flex;
    flex-direction: column;
    gap: 6px;
    align-items: flex-end;
    justify-content: flex-start;
    flex: 1;
    position: relative;
  }

  .circle-button {
    background: var(--surface-primary, #ffffff);
    border-radius: 1000px;
    padding: 8px;
    display: flex;
    flex-direction: row;
    gap: 0px;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    position: relative;
  }

  .icn-close {
    flex-shrink: 0;
    width: 20px;
    height: 20px;
    position: relative;
    overflow: visible;
  }

  .content-to-swap {
    padding: 0px 0px 16px 0px;
    display: flex;
    flex-direction: column;
    gap: 16px;
    align-items: center;
    justify-content: flex-start;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .frame-1000003393 {
    display: flex;
    flex-direction: column;
    gap: 16px;
    align-items: center;
    justify-content: center;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .content2 {
    padding: 16px 0px 16px 0px;
    display: flex;
    flex-direction: column;
    gap: 16px;
    align-items: center;
    justify-content: flex-start;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .image-container {
    display: flex;
    flex-direction: column;
    gap: 0px;
    align-items: center;
    justify-content: flex-start;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .image {
    padding: 23px 0px 23px 0px;
    display: flex;
    flex-direction: column;
    gap: 8px;
    align-items: center;
    justify-content: flex-start;
    flex-shrink: 0;
    width: 220px;
    height: 219px;
    position: relative;
    overflow: hidden;
  }

  .freepik-background-complete-inject-63 {
    flex-shrink: 0;
    width: 200px;
    height: 153.54px;
    position: relative;
    overflow: visible;
  }

  .freepik-device-2-inject-63 {
    flex-shrink: 0;
    width: 97.65px;
    height: 61.83px;
    position: absolute;
    right: 15.33%;
    left: 40.28%;
    width: 44.39%;
    bottom: 46.18%;
    top: 25.59%;
    height: 28.23%;
    overflow: visible;
  }

  .freepik-device-1-inject-63 {
    flex-shrink: 0;
    width: 46.48px;
    height: 95.6px;
    position: absolute;
    right: 63.79%;
    left: 15.08%;
    width: 21.13%;
    bottom: 41.27%;
    top: 15.08%;
    height: 43.65%;
    overflow: visible;
  }

  .freepik-character-inject-63 {
    flex-shrink: 0;
    width: 53.84px;
    height: 133.45px;
    position: absolute;
    right: 44.65%;
    left: 30.88%;
    width: 24.47%;
    bottom: 13.04%;
    top: 26.02%;
    height: 60.94%;
    overflow: visible;
  }

  .text-and-supporting-text {
    padding: 0px 16px 0px 16px;
    display: flex;
    flex-direction: column;
    gap: 8px;
    align-items: flex-start;
    justify-content: flex-start;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .text {
    color: var(--shark-950, #2b2d33);
    text-align: center;
    font-family: var(--lato-18-px-bold-font-family, "Lato-Bold", sans-serif);
    font-size: var(--lato-18-px-bold-font-size, 18px);
    font-weight: var(--lato-18-px-bold-font-weight, 700);
    position: relative;
    align-self: stretch;
  }

  .supporting-text {
    color: var(--shark-500, #707887);
    text-align: center;
    font-family: var(--lato-13-px-regular-font-family,
    "Lato-Regular",
    sans-serif);
    font-size: var(--lato-13-px-regular-font-size, 13px);
    font-weight: var(--lato-13-px-regular-font-weight, 400);
    position: relative;
    align-self: stretch;
  }

  .otp {
    display: flex;
    justify-content: center;
    width: 100%;
  }

  .input-field {
    display: flex;
    justify-content: center;
    width: 100%;
  }

  .input-field-base {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .input-with-label {
    width: 100%;
    margin: auto;
    display: flex;
    justify-content: center;
    position: relative;
  }

  .input {
    background: var(--white, #ffffff);
    border-radius: 8px;
    border-style: solid;
    border-color: var(--gray-300, #d0d5dd);
    border-width: 1px;
    display: flex;
    flex-direction: row;
    gap: 0px;
    align-items: flex-start;
    justify-content: flex-start;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
    box-shadow: var(--shadow-xs-box-shadow,
    0px 1px 2px 0px rgba(16, 24, 40, 0.05));
    overflow: hidden;
  }

  .text-input {
    border: 1px solid rgba(0, 0, 0, 1);
    padding: 10px 14px 10px 8px;
    width: 90%;
    text-align: center;
    border-radius: 6px;
    font-size: 20px;
    font-weight: 700;
    letter-spacing: 2px;
  }

  input::-webkit-outer-spin-button,
  input::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }

  /* Firefox */
  input[type=number] {
    -moz-appearance: textfield;
  }
  

  .text2 {
    color: var(--gray-500, #667085);
    text-align: center;
    font-family: var(--textmd-regular-font-family, "Inter-Regular", sans-serif);
    font-size: var(--textmd-regular-font-size, 16px);
    line-height: var(--textmd-regular-line-height, 24px);
    font-weight: var(--textmd-regular-font-weight, 400);
    position: relative;
    flex: 1;
  }

  .buttons {
    padding: 8px 0 16px 0;
    display: flex;
    gap: 8px;
    justify-content: center;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .primary-button {
    background: var(--golden-grass-300, #fddf47);
    border-radius: 4px;
    width: 89%;
    padding: 8px;
    display: flex;
    flex-direction: row;
    gap: 8px;
    align-items: center;
    justify-content: center;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
    box-shadow: var(--effects-shadow-s-3-box-shadow,
    0px 1px 2px 0px rgba(16, 24, 40, 0.06),
    0px 1px 3px 0px rgba(16, 24, 40, 0.1));
  }

  .text3 {
    color: var(--shark-900, #383a42);
    text-align: center;
    font-family: var(--lato-bold-font-family, "Lato-Bold", sans-serif);
    font-size: var(--lato-bold-font-size, 16px);
    font-weight: var(--lato-bold-font-weight, 700);
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .circle-button2 {
    background: var(--surface-primary, #ffffff);
    border-radius: 1000px;
    padding: 8px;
    display: flex;
    flex-direction: row;
    gap: 0px;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    position: absolute;
    right: 168.5px;
    top: 519px;
  }

  .icn-close2 {
    flex-shrink: 0;
    width: 20px;
    height: 20px;
    position: relative;
    overflow: visible;
  }
`


const StylePopupErrorNumberPhone = styled.div`
  .modal-template,
  .modal-template * {
    box-sizing: border-box;
  }

  .modal-template {
    background: var(--surface-primary, #ffffff);
    width: 90%;
    max-width: 500px;
    margin: auto;
    border-radius: 12px;
    display: flex;
    flex-direction: column;
    gap: 0px;
    align-items: center;
    justify-content: center;
    flex: 1;
    position: relative;
    box-shadow: var(--effects-shadow-s-4-box-shadow,
    0px 2px 4px -2px rgba(16, 24, 40, 0.06),
    0px 4px 8px -2px rgba(16, 24, 40, 0.1));
    overflow: hidden;
  }

  .header {
    padding: 0px 16px 0px 16px;
    display: flex;
    flex-direction: row;
    gap: 0px;
    align-items: center;
    justify-content: center;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .content {
    padding: 8px 0px 8px 0px;
    display: flex;
    flex-direction: column;
    gap: 6px;
    align-items: flex-end;
    justify-content: flex-start;
    flex: 1;
    position: relative;
  }

  .circle-button {
    background: var(--surface-primary, #ffffff);
    border-radius: 1000px;
    padding: 8px;
    display: flex;
    flex-direction: row;
    gap: 0px;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    position: relative;
  }

  .icn-close {
    flex-shrink: 0;
    width: 20px;
    height: 20px;
    position: relative;
    overflow: visible;
  }

  .content-to-swap {
    padding: 0px 0px 16px 0px;
    display: flex;
    flex-direction: column;
    gap: 16px;
    align-items: center;
    justify-content: flex-start;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .frame-1000003393 {
    display: flex;
    flex-direction: column;
    gap: 16px;
    align-items: center;
    justify-content: center;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .image {
    display: flex;
    flex-direction: row;
    gap: 8px;
    align-items: center;
    justify-content: center;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
    overflow: hidden;
  }

  .layer-7 {
    flex-shrink: 0;
    width: 150px;
    height: 177.22px;
    position: relative;
    overflow: visible;
  }

  .content2 {
    padding: 16px 0px 16px 0px;
    display: flex;
    flex-direction: column;
    gap: 16px;
    align-items: center;
    justify-content: flex-start;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .text-and-supporting-text {
    padding: 0px 16px 0px 16px;
    display: flex;
    flex-direction: column;
    gap: 8px;
    align-items: flex-start;
    justify-content: flex-start;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .text {
    color: var(--shark-900, #383a42);
    text-align: center;
    font-family: var(--lato-18-px-bold-font-family, "Lato-Bold", sans-serif);
    font-size: var(--lato-18-px-bold-font-size, 18px);
    font-weight: var(--lato-18-px-bold-font-weight, 700);
    position: relative;
    align-self: stretch;
  }

  .supporting-text {
    color: var(--shark-500, #707887);
    text-align: center;
    font-family: var(--lato-13-px-regular-font-family,
    "Lato-Regular",
    sans-serif);
    font-size: var(--lato-13-px-regular-font-size, 13px);
    font-weight: var(--lato-13-px-regular-font-weight, 400);
    position: relative;
    align-self: stretch;
  }

  .buttons {
    cursor: pointer;
    padding: 8px 8px 16px 8px;
    display: flex;
    flex-direction: column;
    gap: 8px;
    align-items: flex-end;
    justify-content: center;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
  }

  .primary-button {
    background: var(--golden-grass-300, #fddf47);
    border-radius: 4px;
    padding: 8px;
    display: flex;
    flex-direction: row;
    gap: 8px;
    align-items: center;
    justify-content: center;
    align-self: stretch;
    flex-shrink: 0;
    position: relative;
    box-shadow: var(--effects-shadow-s-3-box-shadow,
    0px 1px 2px 0px rgba(16, 24, 40, 0.06),
    0px 1px 3px 0px rgba(16, 24, 40, 0.1));
  }

  .text2 {
    color: var(--shark-900, #383a42);
    text-align: center;
    font-family: var(--lato-bold-font-family, "Lato-Bold", sans-serif);
    font-size: var(--lato-bold-font-size, 16px);
    font-weight: var(--lato-bold-font-weight, 700);
    position: relative;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .circle-button2 {
    background: var(--surface-primary, #ffffff);
    border-radius: 1000px;
    padding: 8px;
    display: flex;
    flex-direction: row;
    gap: 0px;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    position: absolute;
    right: 168.5px;
    top: 436.22px;
  }

  .icn-close2 {
    flex-shrink: 0;
    width: 20px;
    height: 20px;
    position: relative;
    overflow: visible;
  }
`

const InputOTP = styled.input`
  border-radius: unset;
  border-bottom: 2px solid #000000;
  border-width: 0 0 2px 0;
  margin: 0 6px;
  height: 40px;
  width: 15% !important;
  font-size: 32px !important;
  padding: 0 !important;

  ::-webkit-outer-spin-button,
  ::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
  
  :disabled {
    background-color: unset !important;
  }


  :focus-visible {
    outline: unset;
    border-bottom: 2px solid #0d46cc;
  }
  
`

export {Container, PopupNumberPhone, StylePopupErrorNumberPhone, InputOTP};
