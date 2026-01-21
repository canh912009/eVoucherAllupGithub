import React from 'react';
import IMAGE_POPUP_ERROR from "../../images/popupError.png";
import {Backdrop, Button} from "@mui/material";
import styled from "styled-components";

const Container = styled.div`
  .pop_up_modal {
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
`

const PopupError = ({title, message, onAction}) => {
    return (
        <Backdrop
            sx={{color: '#fff', zIndex: 10}}
            open={true}
        >
            <Container>
                <div className="pop_up_modal">
                    <div className="row_popup">
                        <img src={IMAGE_POPUP_ERROR} alt=""/>
                        <h1>{title}</h1>
                        <h2>{message}</h2>
                        <div style={{width: "90%", margin: "auto", borderTop: "0.5px dashed #e0e0e0"}}></div>
                        <div style={{display: "flex", justifyContent: "center"}}>
                            <Button
                                onClick={onAction}
                                className={"button_action"} sx={{backgroundColor: '#D1293D'}}
                                variant='contained'>Close</Button>
                        </div>
                        <div className={"footer"}></div>
                    </div>
                </div>
            </Container>
        </Backdrop>
    );
};

export default PopupError;