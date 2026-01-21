import React from 'react';
export const Dot: React.FC<React.SVGAttributes<{}>> = (props) => {
  return (
    <svg width="250" height="240" viewBox="0 0 25 24" fill="none" xmlns="http://www.w3.org/2000/svg">
      <path d="M12.5 10C11.9696 10 11.4609 10.2107 11.0858 10.5858C10.7107 10.9609 10.5 11.4696 10.5 12C10.5 12.5304 10.7107 13.0391 11.0858 13.4142C11.4609 13.7893 11.9696 14 12.5 14C13.61 14 14.5 13.11 14.5 12C14.5 11.4696 14.2893 10.9609 13.9142 10.5858C13.5391 10.2107 13.0304 10 12.5 10Z" fill="#EEF0F7"/>
    </svg>
  );
};
