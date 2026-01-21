import React from 'react';

const DashedLine = ({ width = '100%', height = '1px', color = 'rgb(224, 224, 224)', dashLength = '5', gapLength = '5' }) => {
    return (
        <svg width={width} height={height}>
            <line
                x1="0"
                y1="0"
                x2="100%"
                y2="0"
                stroke={color}
                strokeWidth={height}
                strokeDasharray={`${dashLength},${gapLength}`}
            />
        </svg>
    );
};

export default DashedLine;
