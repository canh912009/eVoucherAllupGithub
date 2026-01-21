import React from 'react';
import {Container} from "./style";
import IMAGE_404_ERROR from '../../images/404_ERROR.png'
import IMAGE_404 from '../../images/404.png'

const ErrorPage = () => {
    document.title = 'Error Page';
    return (
        <Container>
            <div style={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
            }}>
                <img style={{marginTop: "48px"}} src={IMAGE_404_ERROR} alt=""/>
                <img style={{marginTop: "40px"}} src={IMAGE_404} alt=""/>
                <div style={{fontFamily: 'Inter', fontSize: "24px", color: "#000000", marginTop: "32px"}}>Page Not
                    Found
                </div>
                <div style={{
                    fontFamily: 'Inter',
                    fontSize: "16px",
                    color: "#000000",
                    marginTop: "32px",
                    textAlign: "center",
                    padding: "0 20px"
                }}>Sorry, we
                    could not find the page you are looking for.
                </div>
            </div>
        </Container>
    );
};

export default ErrorPage;