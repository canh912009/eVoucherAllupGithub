import React from 'react';
import {Backdrop} from "@mui/material";
import {useTranslation} from "react-i18next";
import styled from "styled-components";
import {MdClear} from "react-icons/md";


const Container = styled.div`
  .text_show_instruction {
    font-style: normal;
    font-weight: 700;
    font-size: 18px;
    color: #000000;
  }

  h2 {
    line-height: 28px;
  }

`
const PopupMessage = ({data, onClose}) => {
    const {t} = useTranslation()

    function xuLyLinkAnh(link) {
        const domain = process.env.REACT_APP_IMAGE_URL;

        if (link.startsWith('https://') || link.startsWith('http://')) {
            // Nếu link bắt đầu bằng 'https://' hoặc 'http://', trả về link ban đầu
            return link;
        } else {
            // Nếu không, thêm domain vào trước link và trả về
            return domain + link;
        }
    }

    return (
        <Backdrop
            sx={{color: '#fff', zIndex: 10}}
            open={true}
        >
            <Container>
                <div className="pop_up_modal">
                    <div className="row_popup">
                        <MdClear onClick={onClose} style={{position: "absolute", right: "18px", cursor: "pointer"}}
                                 size={32}/>
                        <h1>{t('voucher.gift_message')}</h1>
                        <h2>{t('voucher.from')}: {data?.senderName}</h2>
                        {(!data?.smsType || data?.smsType !== 'DOWNLOAD') && (
                            <h2>{t('voucher.to')}: {data?.userMobileNumber} ({data?.userName})</h2>
                        )}
                        <h2>{data?.subject}</h2>
                        {(data.contentImagePath || data.contentLink) && <div style={{
                            width: "100%",
                            margin: "auto",
                            borderTop: "0.5px dashed #e0e0e0",
                            marginTop: "12px"
                        }}></div>
                        }
                        <div
                            style={{cursor: "pointer"}}
                            onClick={() => {
                                window.open(data?.contentLink)
                            }}>
                            {data.contentImagePath && <div style={{display: "flex", justifyContent: "center"}}>
                                <img style={{width: "80%", marginTop: "24px"}} src={xuLyLinkAnh(data?.contentImagePath)}
                                     alt=""/>
                            </div>}
                            {data.contentLink && <h2 style={{textAlign: "center", marginTop: "12px"}}>
                                {data?.contentLink}
                            </h2>}
                        </div>
                        <div style={{
                            width: "100%",
                            margin: "auto",
                            borderTop: "0.5px dashed #e0e0e0",
                            marginTop: "12px"
                        }}></div>
                        <div className="text_show_instruction" style={{whiteSpace: "pre-line"}}>
                            <h2>{data?.content || 'NULL'}</h2>
                        </div>


                    </div>
                </div>
            </Container>
        </Backdrop>
    );
};

export default PopupMessage;