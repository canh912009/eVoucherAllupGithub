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
  overflow: hidden;
  padding-bottom: 50px;
  
  @media (max-width: 375px) {
    width: 100%;
  }

  @media (min-width: 500px) {
    width: 100vw;
  }

  .row {
    margin-top: 12px;
    max-width: 90%;
    @media (min-width: 500px) {
      width: max-content;
      padding: 0 40px;
    }
  }

  .detail_voucher {
    margin-top: 30px;
    background-color: white;
    border-radius: 20px;
    padding: 12px 20px;
    position: relative;

    .mask {
      position: absolute;
      left: 0;
      top: 0;
      width: 100%;
      height: 100%;
      border-radius: 20px;
      //background: rgba(128, 128, 128, 0.4);
    }

    .input_amount {
      width: 100%;
      display: flex;
      justify-content: center;
      position: relative;

      .affter_input {
        position: absolute;
        right: 8px;
        top: 10px;
        line-height: 40px;
        padding-left: 4px;
        height: 40px;
        border-left: 1px solid #cccccc;
      }
    }

    .tiker {
      position: absolute;
      right: -32px;
      top: -50px;
      width: 140px;
    }

    .logo {
      display: flex;
      justify-content: center;

      img {
        height: 48px;
      }
    }

    .line_solid {
      margin-top: 20px;
      border: 0.5px solid #ededed;
      width: 100%;
    }

    .time_voucher {
      margin-top: 20px;
      text-align: center;
      font-family: 'Inter';
      font-style: normal;
      font-weight: 400;
      font-size: 12px;
      line-height: 16px;
      height: 16px;
      color: #757575;
      @media (min-width: 500px) {
        font-size: 16px;
      }
    }

    .line_dashed {
      border: 0.5px dashed #e0e0e0;
      order: 4;
      align-self: stretch;
      width: 100%;
      position: relative;

      .left_line {
        position: absolute;
        left: -32px;
        top: -10px;
        height: 24px;
        width: 24px;
        border-radius: 50%;
        background-color: #fee715;
      }

      .right_line {
        position: absolute;
        right: -32px;
        top: -10px;
        height: 24px;
        width: 24px;
        border-radius: 50%;
        background-color: #fee715;
      }
    }

    .title_voucher {
      margin: 6px 0;
      justify-content: center;
      align-content: center;
      align-items: center;
      text-align: center;
      
      img {
        height: 85px;
        @media (min-width: 500px) {
          height: 120px;
        }
        margin: 0 auto;
      }
    }

    .title {
      margin-left: 20px;
      align-items: center;
      justify-content: center;
      text-align: center;

      h1 {
        font-family: 'Lato';
        font-style: normal;
        font-weight: 500;
        font-size: 20px;
        line-height: 31px;
        letter-spacing: 0.02em;
        color: #000000;
      }

      h2 {
        height: 27.77px;
        font-family: 'Lato';
        font-style: normal;
        font-weight: 500;
        font-size: 16px;
        line-height: 19px;
        letter-spacing: 0.02em;
        color: #000000;
      }
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
        }

        .item_right {
          @media (min-width: 500px) {
            margin-left: 50px;
          }
          float: right;
          text-align: right;
        }

        h1 {
          font-family: 'Inter';
          font-style: normal;
          font-weight: 400;
          font-size: 12px;
          line-height: 16px;
          color: #757575;
          @media (min-width: 500px) {
            font-size: 14px;
          }
        }

        h2 {
          font-family: 'Inter';
          font-style: normal;
          font-weight: 500;
          font-size: 16px;
          line-height: 24px;
          letter-spacing: -0.011em;
          color: #404040;
          @media (min-width: 500px) {
            font-size: 18px;
          }
        }
      }
    }

    .show_instruction {
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;

      .text_show_instruction {
        font-family: 'Lato';
        font-style: normal;
        font-weight: 400;
        font-size: 16px;
        text-align: center;
        color: #000000;
        margin-top: 12px;
      }

      .button_show_instruction {
        text-transform: inherit;
        font-weight: 400;
        margin-top: 28px;
        margin-bottom: 12px;
        text-align: center;
        display: flex;
        font-size: 14px;
        justify-content: center;
        align-items: center;
        width: max-content;
        padding: 8px 5px;
        color: #ffffff;
        background: #4c3d3d;
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
        font-family: 'Lato';
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
    margin-bottom: 20px;

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
        border: 12px solid;
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
      padding: 8px 4px;
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

  .qr-scanner {
    position: relative;
    width: 300px;
    margin: 0 auto;
    border: 2px solid #000;
  }

  .qr-code {
    margin-top: 20px;
  }


  .pop_up_modal {
    //position: absolute;
    //height: 100%;
    //width: 100vw;
    //background-color: rgba(0, 0, 0, 0.6);
    //display: flex;
    //justify-content: center;
    //align-items: center;

    .row_popup {
      position: relative;
      width: 369px;
      height: max-content;
      padding-bottom: 34px;
      background: #F6F1E9;
      border-radius: 8px;

      .footer {
        position: absolute;
        width: 369px;
        height: 36px;
        left: 0px;
        bottom: 0;
        background: #FFD93D;
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
        padding-top: 40px;
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
`;

export { Container };
