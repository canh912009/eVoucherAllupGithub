import React from 'react';
import GoogleMapReact from 'google-map-react';

const K_WIDTH = 16;
const K_HEIGHT = 16;

const greatPlaceStyle = {
    // initially any map object has left top corner at lat lng coordinates
    // it's on you to set object origin to 0,0 coordinates
    position: 'absolute',
    width: K_WIDTH,
    height: K_HEIGHT,
    left: -K_WIDTH / 2,
    top: -K_HEIGHT / 2,

    border: '5px solid #f44336',
    borderRadius: K_HEIGHT,
    backgroundColor: 'white',
    textAlign: 'center',
    color: '#3f51b5',
    fontSize: 12,
    fontWeight: 'bold',
    padding: 4,
};

const AnyReactComponent = ({text}) => (
    <div style={greatPlaceStyle}>{text}</div>
);

export default function SimpleMap({data}) {
    const defaultProps = {
        center: {
            lat: data.lat,
            lng: data.lng,
        },
        zoom: 18,
    };

    return (
        <div style={{height: '100%', width: '100%', borderRadius: '8px'}}>
            <iframe
                style={{width: "100%", height: "100%"}}
                loading="lazy"
                allowFullScreen
                referrerPolicy="no-referrer-when-downgrade"
                src={`https://www.google.com/maps/embed/v1/place?key=AIzaSyA3gRwYzBjuGHpX_50uJJSzBCUr9QEjI6c&q=${data}`}>
            </iframe>
        </div>
    );
}
