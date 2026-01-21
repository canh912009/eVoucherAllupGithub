const PREFIX = 'v_';

export const setSessionItem = (key, value) => {
    try {
        const serializedValue = JSON.stringify(value); // Convert to JSON string
        sessionStorage.setItem(PREFIX + key, serializedValue);
    } catch (error) {
        console.error("Error saving to sessionStorage", error);
    }
};

export const getSessionItem = (key) => {
    try {
        const value = sessionStorage.getItem(PREFIX + key);
        return value ? JSON.parse(value) : null; // Parse the JSON string back to JS object
    } catch (error) {
        console.error("Error reading from sessionStorage", error);
        return null;
    }
};

export const removeSessionItem = (key) => {
    try {
        sessionStorage.removeItem(PREFIX + key);
    } catch (error) {
        console.error("Error removing from sessionStorage", error);
    }
};
