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
  width: 100%;
  max-width: 500px;

  .row {
    .header {
      width: 100vw;
      max-width: 500px;
      display: flex;
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
        left: 20px;
        cursor: pointer;
      }
    }

    .row_list_qa {
      margin-top: 12px;

      .item_qa {
        margin: 16px auto;
        width: 351px;
        height: 108px;
        background-color: white;
        border-radius: 10px;

        .title {
          padding-top: 12px;
          padding-left: 20px;
          font-family: 'Lato';
          font-style: normal;
          font-weight: 400;
          font-size: 20px;
          color: #000000;
        }

        .sub_qa {
          padding-top: 12px;
          padding-left: 20px;
          padding-right: 20px;
          font-family: 'Lato';
          font-style: normal;
          font-weight: 400;
          line-height: 19px;
          align-items: center;
          display: flex;
          justify-content: space-between;

          .button_more {
            cursor: pointer;
            display: flex;
            font-size: 12px;
            justify-content: center;
            align-items: center;
            width: max-content;
            padding: 4px 8px;
            color: #ffffff;
            background: #4c3d3d;
            box-shadow: 0px 1px 1px rgba(0, 0, 0, 0.1),
            0px 0px 0px 1px rgba(70, 79, 96, 0.16);
            border-radius: 6px;
            @media (min-width: 500px) {
              font-size: 16px;
              padding: 6px 10px;
            }
          }
        }

        .line_dashed {
          position: relative;
          bottom: -12px;
          width: 100%;
          border: 0.5px dashed #f4f2f5;

          .line_dashed_absolute {
            position: absolute;
            height: 28px;
            width: 28px;
            border-radius: 50%;
            background-color: #fee715;
            box-shadow: none;
            top: 50%;
            transform: translateY(-50%);
          }
        }
      }
    }
  }
`;

export { Container };
