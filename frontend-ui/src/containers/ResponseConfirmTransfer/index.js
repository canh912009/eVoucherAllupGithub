import React from 'react';
import {Container} from './styles';
import LOGO from '../../images/logo_white.png';
import IMAGE_SUCCESS from '../../images/voucher_success.gif'
import {useTranslation} from "react-i18next";
import {MdRemoveRedEye} from "react-icons/md";
import {useNavigate} from "react-router-dom";


const ResponseConfirmTransfer = () => {
    const history = useNavigate();
    const {t} = useTranslation()
    document.title = 'Response Transfer';
    return (
        <Container>
            <div className="row">
                <div className="header">
                    <img src={LOGO} alt=""/>
                </div>
                <div style={{display: "flex", justifyContent: "center", flexDirection: "column", alignItems: "center"}}>
                    <img style={{width: "320px"}} src={IMAGE_SUCCESS} alt=""/>
                    <div style={{
                        textAlign: "center", color: "#000",
                        fontSize: "24px",
                        fontFamily: "Roboto",
                        fontWeight: 600
                    }}>{t('voucher.titleResponseConfirmTransfer')}
                    </div>
                    <div style={{
                        textAlign: "center", color: "#000",
                        fontSize: "16px",
                        fontFamily: "Roboto",
                        fontWeight: 500,
                        marginTop: "20px",
                        width: "80%"
                    }}>{t('voucher.descResponseConfirmTransfer')}
                    </div>
                    <div onClick={() => {
                        history(-1)
                    }} style={{
                        marginTop: "30px",
                        width: "120px",
                        cursor: "pointer",
                        height: "120px",
                        borderRadius: "50%",
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                        backgroundColor: "#F6F1E9",
                    }}>
                        <MdRemoveRedEye style={{fontSize: "32px", marginTop: "32px"}}/>
                        <div style={{textAlign: "center"}}>
                            {t('voucher.buttonViewVoucher')}
                        </div>
                    </div>
                </div>
            </div>
        </Container>
    );
};

export default ResponseConfirmTransfer;
