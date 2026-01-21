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
    .header {
      display: flex;
      width: 100vw;
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
`;

export { Container };
