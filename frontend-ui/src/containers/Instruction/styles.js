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
  width: 100%;
  max-width: 500px;

  .row {
    .header {
      width: 100vw;
      max-width: 500px;
      height: 103px;
      background-color: white;
      position: relative;

      .title {
        font-family: 'Lato';
        font-style: normal;
        width: 280px;
        font-weight: 700;
        font-size: 20px;
        line-height: 24px;
        color: #000000;
        position: absolute;
        right: 50px;
        top: 50%;
        transform: translateY(-50%);
      }

      .icon_back {
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
        left: 20px;
        cursor: pointer;
      }
    }

    .row_list_voucher {
      margin: auto;
      margin-top: 24px;
      width: 342px;

      .line_ngang {
        margin: 24px 0;
        border-top: 2px dashed white;
        width: 100%;
      }

      .tra_loi {
        font-family: 'Inter';
        font-style: normal;
        font-weight: 500;
        font-size: 16px;
        line-height: 19px;
        text-align: justify;
        color: #101820;
        margin-top: 20px;
      }

      .item_voucher {
        position: relative;
        width: 342px;
        height: 110px;
        display: flex;
        background-color: white;
        border-radius: 10px;

        .line_dashed_absolute {
          position: absolute;
          top: 50%;
          transform: translateY(-50%);
          height: 28px;
          width: 28px;
          border-radius: 50%;
          background-color: #fee715;
          box-shadow: none;
        }

        .line_dashed {
          position: relative;
          border-left: 2px dashed #f4f2f5;
        }

        .logo {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          align-content: center;

          img {
            height: 50px;
            padding-left: 14px;
          }
        }

        .detail {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          align-content: center;
          padding: 0 12px;
          font-family: 'Lato';
          font-style: normal;
          font-weight: 400;
          font-size: 20px;
          line-height: 24px;
          color: #000000;
        }
      }
    }
  }
`;

export {Container};
