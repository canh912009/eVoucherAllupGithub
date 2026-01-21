import { useState, useEffect } from 'react';
/**
 * Hook prevents input text to autocomplete so fastly (prevent request API many times)
 * @author: dd.tung
 * @returns
 */
const useInputTimeout = () => {
  const [inputText, setInputText] = useState('');
  const [timeoutId, setTimeoutId] = useState<number | undefined>();

  const onInputChange = (newInputText: string) => {
    clearTimeout(timeoutId);
    const newTimeoutId = window.setTimeout(() => {
      // console.log('End of input key:', newInputText);
      setInputText(newInputText);
    }, 500);
    setTimeoutId(newTimeoutId);
  };

  useEffect(() => {
    return () => {
      clearTimeout(timeoutId);
    };
  }, [timeoutId]);

  return { inputText, onInputChange };
};

export default useInputTimeout;
