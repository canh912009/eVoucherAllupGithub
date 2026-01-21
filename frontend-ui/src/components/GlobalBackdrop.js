import React from 'react';
import {Backdrop} from '@mui/material';
import CircularProgress from '@mui/material/CircularProgress';


const GlobalBackdrop = ({isLoading}) => {

    return (
        <>
            <Backdrop
                sx={{
                    color: '#fff', zIndex: 100,
                }}
                open={isLoading}
            >
                <div style={{width: "100vw", position: "relative"}}>
                    <div style={{position: "absolute", left: "50%", transform: "translateX(-50%)"}}>
                        {/*<PropagateLoader*/}
                        {/*    color="#ecf717"*/}
                        {/*/>*/}
                        <CircularProgress style={{color:"#ecf717"}} disableShrink/>
                    </div>
                </div>
            </Backdrop>
        </>
    );
};

export default GlobalBackdrop;
