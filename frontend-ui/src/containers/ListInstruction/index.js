import React from 'react';
import {BsArrowRight} from 'react-icons/bs';
import {Container} from './styles';
import LOGO from '../../images/Logo.png';
import {useNavigate} from "react-router-dom";
import {BiArrowBack} from "react-icons/bi";
import {useTranslation} from "react-i18next";


const ListInstruction = () => {
    const {t} = useTranslation()
    document.title = 'List Instruction';
    const history = useNavigate()
    const LIST_INSTRUCTION = [
        {
            key: 'what_is_e-voucher',
            title: t('voucher.what_is_e-voucher'),
            sub: 'Aqua Retail',
        },
        {
            key: 'what_is_aQua',
            title: t('voucher.what_is_aQua'),
            sub: 'Aqua Retail',
        },
        {
            key: 'what_are_the_precautions',
            title: t('voucher.what_are_the_precautions'),
            sub: 'Aqua Retail',
        },
        {
            key: 'how_to_use_voucher',
            title: t('voucher.how_to_use_voucher'),
            sub: 'Aqua Retail',
        },
        {
            key: 'cannot_convert_to_cash',
            title: t('voucher.cannot_convert_to_cash'),
            sub: 'Aqua Retail',
        },
        {
            key: 'sharing_with_people',
            title: t('voucher.sharing_with_people'),
            sub: 'Aqua Retail',
        },
        {
            key: 'who_is_aqua_retail',
            title: t('voucher.who_is_aqua_retail'),
            sub: 'Aqua Retail',
        },
        {
            key: 'hotline',
            title: t('voucher.question_hotline'),
            sub: 'Aqua Retail',
        },
    ];

    return (
        <Container>
            <div className="row">
                <div className="header">
                    <img src={LOGO} alt=""/>
                    {(window.history.length > 1) && <BiArrowBack color={"#000"} onClick={() => {
                        history(-1)
                    }} size={28} className={"icon_back"}/>}
                </div>

                <div className="row_list_qa">
                    {LIST_INSTRUCTION.map(item => (
                        <div className="item_qa">
                            <div className="title">{item.title}</div>
                            <div className="sub_qa">
                                <div>{item.sub}</div>
                                <div
                                    className="button_more"
                                    onClick={() => {
                                        history(`/instruction/${item.key}`);
                                    }}
                                >
                                    <BsArrowRight style={{marginRight: '8px'}}/>{t('voucher.seeMore')}
                                </div>
                            </div>
                            <div className="line_dashed">
                                <div
                                    className="line_dashed_absolute"
                                    style={{left: '-16px'}}
                                />
                                <div
                                    className="line_dashed_absolute"
                                    style={{right: '-16px'}}
                                />
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </Container>
    );
};

export default ListInstruction;
