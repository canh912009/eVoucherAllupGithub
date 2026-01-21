import { useState, useEffect, useCallback, useRef } from 'react';

const useCountdown = (initialTime = 60, onComplete = () => { }) => {
    const [time, setTime] = useState(initialTime);
    const [isRunning, setIsRunning] = useState(false);
    const intervalId = useRef(null);
    const currentTime = useRef(initialTime);

    // console.log(`useCountdown: ${time}, isRunning: ${isRunning}, intervalId: ${JSON.stringify(intervalId)}`);

    const start = useCallback(() => {
        if (!isRunning && currentTime.current > 0 && !intervalId.current) {  // <-- Check if intervalId is already set
            setIsRunning(true);
            intervalId.current = setInterval(() => {
                currentTime.current -= 1;
                setTime(currentTime.current);

                if (currentTime.current <= 0) {
                    clearInterval(intervalId.current);
                    intervalId.current = null;  // <-- Set intervalId to null after clearing
                    setIsRunning(false);
                    onComplete();
                }
            }, 1000);
        }
    }, [isRunning, onComplete]);

    const stop = useCallback(() => {
        if (isRunning) {
            console.log(`Clearing interval with id: ${intervalId.current}`);
            clearInterval(intervalId.current);
            intervalId.current = null;
            setIsRunning(false);
        }
    }, [isRunning]);

    const reset = useCallback((newTime = initialTime) => {
        stop();
        currentTime.current = newTime;
        setTime(newTime);
        setIsRunning(false);
    }, [initialTime, stop]);

    const setTimeAndUpdate = useCallback((newTime) => {
        currentTime.current = newTime;
        setTime(newTime);
    }, []);

    useEffect(() => {
        return () => {
            if (intervalId.current) {
                clearInterval(intervalId.current);
            }
        };
    }, []);

    return {
        time,
        isRunning,
        start,
        stop,
        reset,
        setTime: setTimeAndUpdate
    };
};

export default useCountdown;