import React from 'react';
import styled from 'styled-components';

const IconCopyWrapper = styled.button`
  display: flex;
  align-items: center;
  justify-content: center;
  width: ${({ size = '40px' }) => size};
  height: ${({ size = '40px' }) => size};
  background-color: transparent;
  border: none;
  cursor: pointer;
  padding: 2px;
  border-radius: 4px;

  &:hover {
    background-color: #f0f0f0;
  }

  &:focus {
    outline: none;
  }
`;

const IconCopyImage = ({ size }) => (
    <svg width={size} height={size} viewBox="0 0 11 11" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M3 4.3335C3 3.97983 3.14049 3.64065 3.39057 3.39057C3.64065 3.14049 3.97983 3 4.3335 3H8.6665C8.84162 3 9.01502 3.03449 9.17681 3.10151C9.3386 3.16852 9.4856 3.26675 9.60943 3.39057C9.73325 3.5144 9.83148 3.6614 9.89849 3.82319C9.96551 3.98498 10 4.15838 10 4.3335V8.6665C10 8.84162 9.96551 9.01502 9.89849 9.17681C9.83148 9.3386 9.73325 9.4856 9.60943 9.60943C9.4856 9.73325 9.3386 9.83148 9.17681 9.89849C9.01502 9.96551 8.84162 10 8.6665 10H4.3335C4.15838 10 3.98498 9.96551 3.82319 9.89849C3.6614 9.83148 3.5144 9.73325 3.39057 9.60943C3.26675 9.4856 3.16852 9.3386 3.10151 9.17681C3.03449 9.01502 3 8.84162 3 8.6665V4.3335Z" stroke="black" strokeLinecap="round" strokeLinejoin="round" />
        <path d="M1.506 7.8685C1.3525 7.78129 1.22482 7.65499 1.13595 7.50244C1.04708 7.3499 1.00017 7.17655 1 7V2C1 1.45 1.45 1 2 1H7C7.375 1 7.579 1.1925 7.75 1.5" stroke="black" strokeLinecap="round" strokeLinejoin="round" />
    </svg>
);

const IconCopy = ({ size = 20 }) => (
    <IconCopyWrapper size={size}>
        <IconCopyImage size={size} />
    </IconCopyWrapper>
);

export default IconCopy;
