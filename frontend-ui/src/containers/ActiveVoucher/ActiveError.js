import ERROR_IMAGE from "../../images/red-error.png";
import {Button} from "@mui/material";

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
            font-family: "Inter";
            font-style: normal;
            font-weight: 700;
            font-size: 20px;
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
    }
`;

const ActiveError = ({
                         title,
                         message,
                         backWhenError,
                         onErrorBack,
                         backTitle,
                     }) => {
    return (
        <>
            <Container>
                <div className="row_popup">
                    <img src={ERROR_IMAGE} alt=""/>
                    <div className="dash_line" style={{width: "100%"}}/>
                    <h1 style={{textTransform: "uppercase"}}>{title}</h1>
                    <h2>{message}</h2>
                    <div className="dash_line" style={{width: "100%"}}/>
                    {backWhenError && (
                        <Button
                            onClick={() => {
                                onErrorBack();
                            }}
                            sx={{
                                backgroundColor: "#039855",
                                width: "100%",
                                margin: "20px 0",
                            }}
                            variant="contained"
                        >
                            {backTitle}
                        </Button>
                    )}
                </div>
            </Container>
        </>
    );
};

export default ActiveError;
