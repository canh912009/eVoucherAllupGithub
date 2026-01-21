import React from 'react';
import {Container} from './styles';
import LOGO from '../../images/logo_white.png';
import IMAGE_ERROR from '../../images/voucher_error.gif'
import {useTranslation} from "react-i18next";


const ErrorConfirmTransfer = () => {
    const {t} = useTranslation()
    document.title = 'Response Transfer';
    return (
        <Container>
            <div className="row">
                <div className="header">
                    <img src={LOGO} alt=""/>
                </div>
                <div style={{display: "flex", justifyContent: "center", flexDirection: "column", alignItems: "center"}}>
                    <img style={{width: "320px"}} src={IMAGE_ERROR} alt=""/>
                    <div style={{
                        textAlign: "center", color: "#000",
                        fontSize: "24px",
                        fontFamily: "Roboto",
                        fontWeight: 600,
                        marginTop: "30px",
                    }}>{t('voucher.AcceptError')}
                    </div>
                    <div style={{
                        textAlign: "center", color: "#000",
                        fontSize: "16px",
                        fontFamily: "Roboto",
                        fontWeight: 500,
                        marginTop: "20px",
                        width: "80%",
                    }}>{t('voucher.subAcceptError')}
                    </div>
                </div>
            </div>
        </Container>
    );
};

export default ErrorConfirmTransfer;
