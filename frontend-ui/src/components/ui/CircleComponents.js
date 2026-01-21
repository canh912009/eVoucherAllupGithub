import React from 'react';
import styled from '@emotion/styled';
import Box from '@mui/material/Box';

// Define the CircleDot component
const CircleDot = styled(Box)(({ position = 'left', size = '24px', top = '10px', backgroundColor = '#fee715', distance }) => {
    distance = distance || `-${parseInt(size, 10) / 2}px`;
    return {
        position: 'absolute',
        top,
        height: size,
        width: size,
        borderRadius: '50%',
        backgroundColor,
        [position]: distance,
        zIndex: 1,
        // border: '1px solid red',
    };
});

// Define the CircleWrap component
const CircleWrap = styled(Box)({
    display: 'flex',
    justifyContent: 'space-between',
    position: 'relative',
    width: '100%',
    // border: '1px solid green',
});

// Combine both components in a single functional component
const CircleComponents = ({ dots }) => {
    return (
        <CircleWrap>
            {dots?.map((dot, index) => (
                <CircleDot key={index} {...dot} />
            ))}
        </CircleWrap>
    );
};

export default CircleComponents;
