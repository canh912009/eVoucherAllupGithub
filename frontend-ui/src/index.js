import React from 'react';
import ReactDOM from 'react-dom/client';
// import './antd.css'
import App from './App';
import reportWebVitals from './reportWebVitals';
import './index.css'
import i18n from './translation/i18n';
import { I18nextProvider } from 'react-i18next';

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
    <I18nextProvider i18n={i18n}>
        <App />
    </I18nextProvider>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
