/**
 * ...
 */

import styled from 'styled-components';

const Container = styled.div`
  display: flex;
  justify-content: center;
  margin: auto;
  height: max-content;
  min-height: 100vh;
  background-color: #FEE715;
  padding-bottom: 50px;

  .row {
    .header {
      display: flex;
      width: 100vw;
      justify-content: center;
      align-items: center;
      background-color: #FEE715;
      position: relative;

      img {
        height: 116px;
      }

      .icon_back {
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        left: 20px;
        cursor: pointer;
      }
    }
  }

  .pop_up_modal {
    min-width: 369px;
    margin: 100px 12px;

    .row_popup {
      position: relative;
      min-width: 369px;
      height: max-content;
      padding-bottom: 34px;
      background: #95E7BB;
      border-radius: 8px;

      .footer {
        position: absolute;
        width: 100%;
        height: 36px;
        left: 0px;
        text-align: center;
        line-height: 36px;
        font-family: 'Montserrat';
        font-style: normal;
        font-weight: 600;
        font-size: 10px;
        color: #000000;
        bottom: 0;
        background: #B1EDCD;
        border-bottom-left-radius: 8px;
        border-bottom-right-radius: 8px;
      }

      img {
        position: absolute;
        left: 50%;
        top: -70px;
        transform: translateX(-50%);
      }

      h1 {
        padding-top: 60px;
        font-family: 'Inter';
        font-style: normal;
        font-weight: 700;
        font-size: 24px;
        text-align: center;
        color: #000000;
      }

      h2 {
        font-family: 'Inter';
        font-style: normal;
        font-weight: 400;
        font-size: 12px;
        text-align: center;
        padding-bottom: 40px;
        width: 60%;
        margin: auto;
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
    h3 {
      padding-top: 32px;
      font-family: 'Lato';
      font-style: normal;
      font-weight: 700;
      font-size: 20px;
      display: flex;
      align-items: center;
      color: #000000;
    }
  }

`;

export { Container };
