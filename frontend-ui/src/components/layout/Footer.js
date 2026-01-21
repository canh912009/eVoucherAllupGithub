import React from 'react';
import { Box, Typography } from '@mui/material';
import { useTranslation } from "react-i18next";

const Footer = () => {
    const { t } = useTranslation();
    return (
        <Box
            component="footer"
            sx={{
                width: '100%',
                textAlign: 'center',
                padding: '1rem 0',
                position: 'relative',
                marginTop: '-50px',
            }}
        >
            <Typography variant="body2" color="textSecondary">
                {t('footer.copyright')}
            </Typography>
        </Box>
    );
};

export default Footer;
