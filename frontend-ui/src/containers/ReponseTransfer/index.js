import React from 'react';
import {Container} from './styles';
import LOGO from '../../images/Logo.png';
import IMAGE_POPUP_SUCCESS from "../../images/popupSuccess.png";
import moment from "moment";
import {useTranslation} from "react-i18next";


const ResponseTransfer = () => {
    const {t} = useTranslation()
    document.title = 'Response Transfer';
    return (
        <Container>
            <div className="row">
                <div className="header">
                    <img src={LOGO} alt=""/>
                </div>
                <div className="pop_up_modal">
                    <div className="row_popup">
                        <img src={IMAGE_POPUP_SUCCESS} alt=""/>
                        <h1>{t('voucher.titleResponseTransfer')}</h1>
                        <h2>{t('voucher.descResponseTransfer')} {process.env.REACT_APP_DAY_CONFIRM_TRANSFER} {t('voucher.descResponseTransfer2')}</h2>
                        <div className={"footer"}>{moment().format('LLLL')}</div>
                    </div>
                </div>
            </div>
        </Container>
    );
};

export default ResponseTransfer;
